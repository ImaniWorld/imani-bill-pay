package com.imani.bill.pay.service.billing.utility;

import com.imani.bill.pay.domain.billing.*;
import com.imani.bill.pay.domain.billing.repository.IBillPayFeeRepository;
import com.imani.bill.pay.domain.billing.repository.IImaniBillToFeeRepository;
import com.imani.bill.pay.domain.billing.repository.IImaniBillWaterSvcAgreementRepository;
import com.imani.bill.pay.domain.utility.WaterServiceAgreement;
import com.imani.bill.pay.service.billing.IImaniBillService;
import com.imani.bill.pay.service.billing.ImaniBillService;
import com.imani.bill.pay.service.billing.fee.IBillFeeChargeService;
import com.imani.bill.pay.service.util.DateTimeUtil;
import com.imani.bill.pay.service.util.IDateTimeUtil;
import com.imani.bill.pay.service.utility.IWaterSvcAgreementService;
import com.imani.bill.pay.service.utility.IWaterUtilizationService;
import com.imani.bill.pay.service.utility.WaterSvcAgreementService;
import com.imani.bill.pay.service.utility.WaterUtilizationService;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

/**
 * Default implementation for applying late fees to all Imani BillPay agreement types.
 *
 * <b>Note!!!</b> that this implementation will apply only one late fee during the due cycle.
 * Example if a bill is due monthly fee will only be applied once in that month.  Same logic applies for quarterly due fees
 *
 * @author manyce400
 */
@Service(WaterBillFeeChargeService.SPRING_BEAN)
public class WaterBillFeeChargeService implements IBillFeeChargeService<WaterServiceAgreement> {


    @Autowired
    @Qualifier(DateTimeUtil.SPRING_BEAN)
    private IDateTimeUtil iDateTimeUtil;

    @Autowired
    @Qualifier(WaterSvcAgreementService.SPRING_BEAN)
    private IWaterSvcAgreementService iWaterSvcAgreementService;

    @Autowired
    @Qualifier(ImaniBillService.SPRING_BEAN)
    private IImaniBillService imaniBillService;

    @Autowired
    @Qualifier(WaterUtilizationService.SPRING_BEAN)
    private IWaterUtilizationService iWaterUtilizationService;

    @Autowired
    private IBillPayFeeRepository iBillPayFeeRepository;

    @Autowired
    private IImaniBillToFeeRepository iImaniBillToFeeRepository;

    @Autowired
    private IImaniBillWaterSvcAgreementRepository imaniBillWaterSvcAgreementRepository;


    public static final String SPRING_BEAN = "com.imani.bill.pay.service.billing.utility.WaterBillFeeChargeService";

    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(WaterBillFeeChargeService.class);


    @Transactional
    @Override
    public void chargeWaterBillLateFees(WaterServiceAgreement waterServiceAgreement) {
        Assert.notNull(waterServiceAgreement, "WaterServiceAgreement cannot be null");

        LOGGER.info("Attempting to apply late fees on ImaniBill's on ", waterServiceAgreement.describeAgreement());

        final DateTime start = iDateTimeUtil.getDateTimeAStartOfCurrentQuarter();
        final DateTime end = iDateTimeUtil.getDateTimeAEndOfCurrentQuarter();

        // Find all unpaid bills on the WaterServiceAgreement and try to apply late fee where applicable
        List<ImaniBill> imaniBills = imaniBillWaterSvcAgreementRepository.findAllAgreementUnPaidBills(waterServiceAgreement.getId());

        imaniBills.forEach(imaniBill -> {
            // This will recompute utilization with scheduled fees again in case new utilizations have been added before attempting to add late fee
            iWaterUtilizationService.computeUtilizationChargeWithSchdFees(imaniBill);

            // Lookup configured late fee by the utility provider on this agreement
            Optional<BillPayFee> lateBillPayFee = iBillPayFeeRepository.findBillPayFeeByFeeType(waterServiceAgreement.getEmbeddedUtilityService().getUtilityProviderBusiness(), FeeTypeE.LATE_FEE);

            if (lateBillPayFee.isPresent()) {
                // IF late fee already applied in this quarter then skip
                Optional<ImaniBillToFee> imaniBillToLateFee = iImaniBillToFeeRepository.findLateFeeInQtr(imaniBill, lateBillPayFee.get(), start, end);

                if (!imaniBillToLateFee.isPresent()) { // This ensures that we only apply 1 late fee in the quarter
                    chargeWaterBillLateFee(imaniBill, lateBillPayFee.get());
                    imaniBillService.save(imaniBill);
                } else {
                    LOGGER.info("Detected that a late fee already applied to the bill in current quarter");
                }
            } else {
                LOGGER.warn("ImaniBill[ID: {}] is late however no configured late fee was found for Business[{}]", imaniBill.getId(), waterServiceAgreement.getEmbeddedUtilityService().getUtilityProviderBusiness().getId());
            }
        });
    }

    void chargeWaterBillLateFee(ImaniBill imaniBill, BillPayFee lateBillPayFee) {
        Assert.notNull(imaniBill, "ImaniBill cannot be null");
        Assert.notNull(imaniBill.getWaterServiceAgreement(), "WaterServiceAgreement cannot be null");

        if (!imaniBill.isPaidInFull()) { // Only apply late fee's if bill is not paid in full
            WaterServiceAgreement waterServiceAgreement = imaniBill.getWaterServiceAgreement();

            LOGGER.info("Attempting to charge and apply late fee on ImaniBill[ID: {}]", imaniBill.getId());

            // Check to see if Bill is late in order to apply a late fee
            boolean isBillLate = imaniBillService.isBillPaymentLate(imaniBill, waterServiceAgreement.getEmbeddedAgreement());
            if(isBillLate) {
                BillScheduleTypeE billScheduleTypeE = waterServiceAgreement.getEmbeddedAgreement().getBillScheduleTypeE();

                if(billScheduleTypeE == BillScheduleTypeE.QUARTERLY) { // TODO we only currently support quarterly billing for water, make more flexible
                    applyQuarterlyLateFee(imaniBill, lateBillPayFee);
                }
            }
        }
    }

    void applyQuarterlyLateFee(ImaniBill imaniBill, BillPayFee billPayFee) {
        // Very critical, we compute the fee amount off the original actual bill charge not the amount owed since that will keep changing
        Double feeAmount = billPayFee.calculatFeeCharge(imaniBill.getAmountOwed());
        double newAmountOwed = imaniBill.getAmountOwed() + feeAmount;
        imaniBill.setAmountOwed(newAmountOwed);
        imaniBill.addImaniBillToFee(billPayFee, feeAmount);
    }

}