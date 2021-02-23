package com.imani.bill.pay.service.billing.fee;

import com.imani.bill.pay.domain.agreement.EmbeddedAgreement;
import com.imani.bill.pay.domain.billing.*;
import com.imani.bill.pay.domain.billing.repository.IBillPayFeeRepository;
import com.imani.bill.pay.domain.billing.repository.IImaniBillWaterSvcAgreementRepository;
import com.imani.bill.pay.domain.utility.WaterServiceAgreement;
import com.imani.bill.pay.service.billing.IImaniBillService;
import com.imani.bill.pay.service.billing.ImaniBillService;
import com.imani.bill.pay.service.util.DateTimeUtil;
import com.imani.bill.pay.service.util.IDateTimeUtil;
import com.imani.bill.pay.service.utility.IWaterSvcAgreementService;
import com.imani.bill.pay.service.utility.WaterSvcAgreementService;
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
@Service(BillFeeChargeService.SPRING_BEAN)
public class BillFeeChargeService implements IBillFeeChargeService {


    @Autowired
    @Qualifier(DateTimeUtil.SPRING_BEAN)
    private IDateTimeUtil iDateTimeUtil;

    @Autowired
    private IBillPayFeeRepository iBillPayFeeRepository;

    @Autowired
    private IImaniBillWaterSvcAgreementRepository imaniBillWaterSvcAgreementRepository;

    @Autowired
    @Qualifier(WaterSvcAgreementService.SPRING_BEAN)
    private IWaterSvcAgreementService iWaterSvcAgreementService;

    @Autowired
    @Qualifier(ImaniBillService.SPRING_BEAN)
    private IImaniBillService imaniBillService;

    public static final String SPRING_BEAN = "com.imani.bill.pay.service.billing.fee.BillFeeChargeService";

    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(BillFeeChargeService.class);


    @Override
    public void chargeWaterBillLateFees(WaterServiceAgreement waterServiceAgreement) {
        Assert.notNull(waterServiceAgreement, "WaterServiceAgreement cannot be null");
        List<ImaniBill> imaniBills = imaniBillWaterSvcAgreementRepository.findAllAgreementUnPaidBills(waterServiceAgreement);
        imaniBills.forEach(imaniBill -> {
            boolean feeCharged = chargeWaterBillLateFee(imaniBill);
            if (feeCharged) {
                imaniBillService.save(imaniBill);
            }
        });
    }


    private boolean chargeWaterBillLateFee(ImaniBill imaniBill) {
        Assert.notNull(imaniBill, "ImaniBill cannot be null");
        Assert.notNull(imaniBill.getWaterServiceAgreement(), "WaterServiceAgreement cannot be null");
        Assert.isTrue(!imaniBill.isPaidInFull(), "ImaniBill already paid in full");

        WaterServiceAgreement waterServiceAgreement = imaniBill.getWaterServiceAgreement();

        // Get the actual water charge based on user water utilization
        double quarterlyCharge = 0;//iWaterSvcAgreementService.computeWaterChargeOnQuarterlyUtilization(waterServiceAgreement);

        LOGGER.info("Attempting to charge and apply late fee on ImaniBill[{}] on: ", imaniBill.getId(), waterServiceAgreement.describeAgreement());

        // Check to see if Bill is late
        boolean isBillLate = isImaniBillPaymentLate(imaniBill, waterServiceAgreement.getEmbeddedAgreement());
        if(isBillLate) {
            // Load up the late fee for this business
            Optional<BillPayFee> billPayFee = iBillPayFeeRepository.findBillPayFeeByFeeType(waterServiceAgreement.getEmbeddedUtilityService().getUtilityProviderBusiness(), FeeTypeE.LATE_FEE);
            if(billPayFee.isPresent()) {
                BillScheduleTypeE billScheduleTypeE = waterServiceAgreement.getEmbeddedAgreement().getBillScheduleTypeE();

                if(billScheduleTypeE == BillScheduleTypeE.QUARTERLY) {
                    applyQuarterlyLateFee(null, imaniBill, billPayFee.get());
                    return true;
                }

            } else {
                LOGGER.warn("ImaniBill[{}] is late however no configured late fee was found for Business[{}]", imaniBill.getId(), waterServiceAgreement.getEmbeddedUtilityService().getUtilityProviderBusiness().getId());
            }
        }

        return false;
    }

    boolean isImaniBillPaymentLate(ImaniBill imaniBill, EmbeddedAgreement embeddedAgreement) {
        DateTime billScheduleDate = imaniBill.getBillScheduleDate();
        Integer maxDaysTillLate = embeddedAgreement.getNumberOfDaysTillLate();
        Integer daysBetweenDueAndNow = iDateTimeUtil.getDaysBetweenDates(billScheduleDate, DateTime.now());
        LOGGER.info("Late check. maxDaysTillLate => {} daysBetweenDueAndNow => {}", maxDaysTillLate, daysBetweenDueAndNow);
        return daysBetweenDueAndNow > maxDaysTillLate;
    }

    void applyQuarterlyLateFee(Double actualBillCharge, ImaniBill imaniBill, BillPayFee billPayFee) {
        // Get all fee's that have been applied to see if there is already a fee in the quarter. IF not we want to apply a new quarterly fee
        Set<ImaniBillToFee> imaniBillToFees = imaniBill.getBillPayFeesByFeeTypeE(FeeTypeE.LATE_FEE);
        boolean chargeFee = true;
        for(ImaniBillToFee imaniBillToFee : imaniBillToFees) {
            DateTime feeChargedDate = imaniBillToFee.getCreateDate();
            boolean isDateTimeInCurrentQuarter = iDateTimeUtil.isDateTimeInCurrentQuarter(feeChargedDate);
            if((isDateTimeInCurrentQuarter)) {
                LOGGER.info("Detected that a late fee already applied in current quarter on Date[{}]", feeChargedDate);
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
    }

}
