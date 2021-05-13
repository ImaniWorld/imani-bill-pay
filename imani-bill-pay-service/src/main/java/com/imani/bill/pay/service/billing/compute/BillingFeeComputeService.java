package com.imani.bill.pay.service.billing.compute;

import com.imani.bill.pay.domain.billing.BillPayFee;
import com.imani.bill.pay.domain.billing.FeeTypeE;
import com.imani.bill.pay.domain.billing.ImaniBill;
import com.imani.bill.pay.domain.billing.ImaniBillToFee;
import com.imani.bill.pay.domain.billing.repository.IBillPayFeeRepository;
import com.imani.bill.pay.domain.utility.WaterServiceAgreement;
import com.imani.bill.pay.service.billing.IImaniBillService;
import com.imani.bill.pay.service.billing.ImaniBillService;
import com.imani.bill.pay.service.util.DateTimeUtil;
import com.imani.bill.pay.service.util.IDateTimeUtil;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.transaction.Transactional;
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



    public static final String SPRING_BEAN = "com.imani.bill.pay.service.billing.compute.BillingFeeComputeService";

    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(BillingFeeComputeService.class);

    @Override
    public void computeUpdateAmountOwedWithSchedFees(WaterServiceAgreement waterServiceAgreement, ImaniBill imaniBill) {
        Assert.notNull(waterServiceAgreement, "WaterServiceAgreement cannot be null");
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
            List<BillPayFee> billPayFees = waterServiceAgreement.getScheduledBillPayFees();
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

    @Transactional
    @Override
    public void computeUpdateAmountOwedWithLateFee(WaterServiceAgreement waterServiceAgreement, ImaniBill imaniBill) {
        Assert.notNull(waterServiceAgreement, "WaterServiceAgreement cannot be null");
        Assert.notNull(imaniBill, "ImaniBill cannot be null");

        // Check to see if Bill is late in order to apply a late fee
        boolean isBillLate = imaniBillService.isBillPaymentLate(imaniBill, waterServiceAgreement.getEmbeddedAgreement());

        if (!imaniBill.isPaidInFull() && isBillLate) {
            // Lookup configured late fee by the utility provider on this agreement
            LOGGER.info("Computing and updating amount owed on ImaniBill[ID: {} | AmountOwed: {}] based on configured late fee", imaniBill.getId(), imaniBill.getAmountOwed());
            Optional<BillPayFee> lateBillPayFee = iBillPayFeeRepository.findBillPayFeeByFeeType(waterServiceAgreement.getEmbeddedUtilityService().getUtilityProviderBusiness(), FeeTypeE.LATE_FEE);

            if (lateBillPayFee.isPresent()) {
                // Compute the fee amount and update by adding it to the current amount owed on the bill
                Double feeAmount = lateBillPayFee.get().calculatFeeCharge(imaniBill.getAmountOwed());
                double newAmountOwed = imaniBill.getAmountOwed() + feeAmount;
                imaniBill.setAmountOwed(newAmountOwed);

                // Critical: Logic to make sure that only 1 late fee will be applied each quarter.  We don't levy more than 1 late fee in
                ImmutablePair<DateTime, DateTime> quarterDates = iDateTimeUtil.getQuarterStartEndDates(imaniBill.getBillScheduleDate());
                DateTime start = quarterDates.getLeft();
                DateTime end = quarterDates.getRight();
                Optional<ImaniBillToFee> lateFeeLeviedInQuarter = imaniBill.getLateFeeBetweenPeriod(start, end);

                if(!lateFeeLeviedInQuarter.isPresent()) {
                    LOGGER.info("No late fee has been levied in Qtr[Start: {} | End: {}] on ImaniBill", start, end);
                    imaniBill.addImaniBillToFee(lateBillPayFee.get(), feeAmount);
                } else {
                    LOGGER.info("Detected a late fee levied in Qtr[Start: {} | End: {} | Fee: {}]", start, end, lateFeeLeviedInQuarter.get().getFeeAmount());
                    lateFeeLeviedInQuarter.get().setFeeAmount(feeAmount);
                }

            } else {
                LOGGER.error("No configured late fee was found for Business[{}]", imaniBill.getId(), waterServiceAgreement.getEmbeddedUtilityService().getUtilityProviderBusiness().getId());
            }
        }
    }

}