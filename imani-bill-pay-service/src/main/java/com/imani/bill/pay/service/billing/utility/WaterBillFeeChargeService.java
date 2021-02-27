package com.imani.bill.pay.service.billing.utility;

import com.imani.bill.pay.domain.billing.*;
import com.imani.bill.pay.domain.billing.repository.IBillPayFeeRepository;
import com.imani.bill.pay.domain.billing.repository.IImaniBillWaterSvcAgreementRepository;
import com.imani.bill.pay.domain.utility.WaterServiceAgreement;
import com.imani.bill.pay.domain.utility.WaterUtilizationCharge;
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

import java.util.List;
import java.util.Optional;
import java.util.Set;

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
    private IImaniBillWaterSvcAgreementRepository imaniBillWaterSvcAgreementRepository;


    public static final String SPRING_BEAN = "com.imani.bill.pay.service.billing.utility.WaterBillFeeChargeService";

    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(WaterBillFeeChargeService.class);


    @Override
    public void chargeWaterBillLateFees(WaterServiceAgreement waterServiceAgreement) {
        Assert.notNull(waterServiceAgreement, "WaterServiceAgreement cannot be null");

        LOGGER.info("Attempting to apply late fees on ImaniBill's for agreemet {}", waterServiceAgreement.getId());

        List<ImaniBill> imaniBills = imaniBillWaterSvcAgreementRepository.findAllAgreementUnPaidBills(waterServiceAgreement.getId());
        imaniBills.forEach(imaniBill -> {
            boolean feeCharged = chargeWaterBillLateFee(imaniBill);
            if (feeCharged) {
                imaniBillService.save(imaniBill);
            }
        });
    }

    boolean chargeWaterBillLateFee(ImaniBill imaniBill) {
        Assert.notNull(imaniBill, "ImaniBill cannot be null");
        Assert.notNull(imaniBill.getWaterServiceAgreement(), "WaterServiceAgreement cannot be null");

        if (!imaniBill.isPaidInFull()) { // Only apply late fee's if bill is not paid in full
            WaterServiceAgreement waterServiceAgreement = imaniBill.getWaterServiceAgreement();

            LOGGER.info("Attempting to charge and apply late fee on ImaniBill[ID: {}]", imaniBill.getId());

            // Check to see if Bill is late in order to apply a late fee
            boolean isBillLate = imaniBillService.isBillPaymentLate(imaniBill, waterServiceAgreement.getEmbeddedAgreement());
            if(isBillLate) {
                // Load up the late fee for this business
                Optional<BillPayFee> billPayFee = iBillPayFeeRepository.findBillPayFeeByFeeType(waterServiceAgreement.getEmbeddedUtilityService().getUtilityProviderBusiness(), FeeTypeE.LATE_FEE);

                if(billPayFee.isPresent()) {
                    BillScheduleTypeE billScheduleTypeE = waterServiceAgreement.getEmbeddedAgreement().getBillScheduleTypeE();

                    if(billScheduleTypeE == BillScheduleTypeE.QUARTERLY) { // TODO we only currently support quarterly billing for water, make more flexible
                        // Get the current water charge based on current utilization
                        WaterUtilizationCharge waterUtilizationCharge = iWaterUtilizationService.computeWaterUtilizationChargeWithScheduledFees(imaniBill);
                        boolean feeLevied = applyQuarterlyLateFee(waterUtilizationCharge.getCharge(), imaniBill, billPayFee.get());
                        return feeLevied;
                    }
                } else {
                    LOGGER.warn("ImaniBill[ID: {}] is late however no configured late fee was found for Business[{}]", imaniBill.getId(), waterServiceAgreement.getEmbeddedUtilityService().getUtilityProviderBusiness().getId());
                }
            }
        }

        return false;
    }

    boolean applyQuarterlyLateFee(Double actualBillCharge, ImaniBill imaniBill, BillPayFee billPayFee) {
        // Get all fee's that have been applied to see if there is already a fee in the quarter. IF not we want to apply a new quarterly fee
        Set<ImaniBillToFee> imaniBillToFees = imaniBill.getBillPayFeesByFeeTypeE(FeeTypeE.LATE_FEE);
        boolean chargeFee = true;
        for(ImaniBillToFee imaniBillToFee : imaniBillToFees) {
            DateTime feeChargedDate = imaniBillToFee.getCreateDate();
            boolean isDateTimeInCurrentQuarter = iDateTimeUtil.isDateTimeInCurrentQuarter(feeChargedDate);

            if((isDateTimeInCurrentQuarter)) {
                LOGGER.info("Detected that a late fee already applied to bill in current quarter on Date[{}]", feeChargedDate);
                chargeFee = false;
                break;
            }
        }

        if(chargeFee) {
            // Very critical, we compute the fee amount off the original actual bill charge not the amount owed since that will keep changing
            Double feeAmount = billPayFee.calculatFeeCharge(actualBillCharge);
            double newAmountOwed = actualBillCharge + feeAmount;
            imaniBill.setAmountOwed(newAmountOwed);
            imaniBill.addImaniBillToFee(billPayFee, feeAmount);
        }

        return chargeFee;
    }

}