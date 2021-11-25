package com.imani.bill.pay.service.billing.compute;

import com.imani.bill.pay.domain.agreement.EmbeddedAgreement;
import com.imani.bill.pay.domain.billing.*;
import com.imani.bill.pay.domain.billing.repository.IBillPayFeeRepository;
import com.imani.bill.pay.domain.utility.EmbeddedUtilityService;
import com.imani.bill.pay.service.billing.IImaniBillService;
import com.imani.bill.pay.service.billing.ImaniBillService;
import com.imani.bill.pay.service.billing.utility.IUtilityBillingDateService;
import com.imani.bill.pay.service.billing.utility.UtilityBillingDateService;
import com.imani.bill.pay.service.util.DateTimeUtil;
import com.imani.bill.pay.service.util.IDateTimeUtil;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Optional;

/**
 * @author manyce400
 */
@Service(BillingFeeComputeService.SPRING_BEAN)
public class BillingFeeComputeService implements IBillingFeeComputeService {


    @Autowired
    private IBillPayFeeRepository iBillPayFeeRepository;

    @Autowired
    @Qualifier(ImaniBillService.SPRING_BEAN)
    private IImaniBillService imaniBillService;

    @Autowired
    @Qualifier(DateTimeUtil.SPRING_BEAN)
    private IDateTimeUtil iDateTimeUtil;

    @Autowired
    @Qualifier(UtilityBillingDateService.SPRING_BEAN)
    private IUtilityBillingDateService iUtilityBillingDateService;



    public static final String SPRING_BEAN = "com.imani.bill.pay.service.billing.compute.BillingFeeComputeService";

    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(BillingFeeComputeService.class);



    @Override
    public void computeUpdateAmountOwedWithSchedFees(List<BillPayFee> billPayFees, ImaniBill imaniBill) {
        Assert.notNull(billPayFees, "BillPay Fees cannot be null");
        Assert.isTrue(billPayFees.size() > 0, "Configured BillPayFee's required");
        Assert.notNull(imaniBill, "ImaniBill cannot be null");

        // Start with the current amount owed, get all scheduled fees appy and compute new amount owed.
        double amountOwed = imaniBill.getAmountOwed();

        // Check to see if scheduled fees already computed and added to this bill, then simpy reuse those.
        List<ImaniBillToFee> imaniBillToFees = imaniBill.getScheduledFees();
        if (!imaniBillToFees.isEmpty()) {
            LOGGER.info("Model 1: Using scheduled fees already applied to ImaniBill...");
            final double[] computedFinalAmt = {amountOwed};
            imaniBillToFees.forEach(imaniBillToFee -> {
                computedFinalAmt[0] = imaniBillToFee.getBillPayFee().calculatePaymentWithFees(computedFinalAmt[0]);
            });

            amountOwed = computedFinalAmt[0];
        } else {
            LOGGER.info("Model 2: Using preconfigured scheduled fees. No scheduled fees already applied to ImaniBill...");
            final double[] computedFinalAmt = {amountOwed};
            billPayFees.forEach(billPayFee -> {
                computedFinalAmt[0] = billPayFee.calculatePaymentWithFees(computedFinalAmt[0]);
                double feeAmount = billPayFee.calculatFeeCharge(computedFinalAmt[0]);
                imaniBill.addImaniBillToFee(billPayFee, feeAmount);
            });

            amountOwed = computedFinalAmt[0];
        }

        imaniBill.setAmountOwed(amountOwed);
        Object[] logArgs = {imaniBill.getId(), imaniBill.getAmountOwed()};
        LOGGER.info("Result of computing scheduled fee charges => ImaniBill[ID: {} | AmountOwed: {}]", logArgs);
    }

    @Override
    public void computeUpdateAmountOwedWithLateFee(EmbeddedAgreement embeddedAgreement, EmbeddedUtilityService embeddedUtilityService, ImaniBill imaniBill) {
        Assert.notNull(embeddedAgreement, "EmbeddedAgreement cannot be null");
        Assert.notNull(embeddedUtilityService, "EmbeddedUtilityService cannot be null");
        Assert.notNull(imaniBill, "ImaniBill cannot be null");

        // Check to see if Bill is late in order to apply a late fee
        boolean isBillLate = imaniBillService.isBillPaymentLate(imaniBill, embeddedAgreement);

        if (!imaniBill.isPaidInFull() && isBillLate) {
            // Lookup configured late fee by the utility provider on this agreement
            Optional<BillPayFee> lateBillPayFee = iBillPayFeeRepository.findBillPayFeeByFeeType(embeddedUtilityService.getUtilityProviderBusiness(), FeeTypeE.LATE_FEE);
            LOGGER.info("ImaniBill[ID: {}, Owed: {}, Paid: {}] is late attempting to apply late fee...", imaniBill.getId(), imaniBill.getAmountOwed(), imaniBill.getAmountPaid());

            if (lateBillPayFee.isPresent()) {
                // Execute back fill logic.
                // Starting from bill scheduled qtr date check till current qtr date for late fee applied in each qtr
                // IF late fee is missing in a qtr go ahead and apply.
                DateTime end = iDateTimeUtil.getDateTimeAtEndOfCurrentQuarter();

                List<ImmutablePair<DateTime, DateTime>> qtlyBillingDatesTillNow = iUtilityBillingDateService.computeQtlyBillingDatesBetween(imaniBill.getBillScheduleDate(), end, BillScheduleTypeE.QUARTERLY);
                qtlyBillingDatesTillNow.forEach(qtlyBillingDate ->{
                    DateTime qtrStart = qtlyBillingDate.getLeft();
                    DateTime qtrEnd = qtlyBillingDate.getRight();

                    Optional<ImaniBillToFee> lateFeeLeviedInQuarter = imaniBill.getLateFeeBetweenPeriod(qtrStart, qtrEnd);
                    if(!lateFeeLeviedInQuarter.isPresent()) {
                        // time to levy a fee for this current quarter
                        Object[] logArgs = {imaniBill.getId(), imaniBill.getAmountOwed(), qtrStart, qtrEnd};
                        LOGGER.info("Applying late fee against ImaniBill[ID: {} | AmtOwed: {} | QtrStart: {} | QtrEnd: {}]", logArgs);

                        if(qtrStart.equals(imaniBill.getBillScheduleDate())) {
                            DateTime levyDate = qtrStart.plusDays(embeddedAgreement.getNumberOfDaysTillLate());
                            imaniBill.levyLateFee(lateBillPayFee.get(), levyDate);
                        } else {
                            imaniBill.levyLateFee(lateBillPayFee.get(), qtrStart);
                        }
                    }
                });

            } else {
                LOGGER.error("No configured late fee was found for Business[{}]", imaniBill.getId(), embeddedUtilityService.getUtilityProviderBusiness().getId());
            }
        }
    }

    @Override
    public boolean hasLateFeeBeenLeviedInCurrQtr(ImaniBill imaniBill) {
        Assert.notNull(imaniBill, "ImaniBill cannot be null");
        ImmutablePair<DateTime, DateTime> quarterDates = iDateTimeUtil.getCurrQtrStartEndDates();
        DateTime start = quarterDates.getLeft();
        DateTime end = quarterDates.getRight();
        Optional<ImaniBillToFee> lateFeeLeviedInQuarter = imaniBill.getLateFeeBetweenPeriod(start, end);
        return lateFeeLeviedInQuarter.isPresent();
    }

    @Override
    public boolean hasLateFeeBeenLeviedInBillSchedQtr(ImaniBill imaniBill) {
        Assert.notNull(imaniBill, "ImaniBill cannot be null");
        Assert.notNull(imaniBill.getBillScheduleDate(), "ImaniBill scheduled date cannot be null");
        ImmutablePair<DateTime, DateTime> quarterDates = iDateTimeUtil.getQuarterStartEndDates(imaniBill.getBillScheduleDate());
        DateTime start = quarterDates.getLeft();
        DateTime end = quarterDates.getRight();
        Optional<ImaniBillToFee> lateFeeLeviedInSchedQuarter = imaniBill.getLateFeeBetweenPeriod(start, end);
        return lateFeeLeviedInSchedQuarter.isPresent();
    }
}