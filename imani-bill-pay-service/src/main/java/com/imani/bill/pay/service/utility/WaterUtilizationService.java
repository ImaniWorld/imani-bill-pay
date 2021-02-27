package com.imani.bill.pay.service.utility;


import com.imani.bill.pay.domain.billing.BillPayFee;
import com.imani.bill.pay.domain.billing.BillScheduleTypeE;
import com.imani.bill.pay.domain.billing.FeeTypeE;
import com.imani.bill.pay.domain.billing.ImaniBill;
import com.imani.bill.pay.domain.billing.repository.IBillPayFeeRepository;
import com.imani.bill.pay.domain.execution.ExecutionResult;
import com.imani.bill.pay.domain.execution.ValidationAdvice;
import com.imani.bill.pay.domain.utility.WaterServiceAgreement;
import com.imani.bill.pay.domain.utility.WaterUtilization;
import com.imani.bill.pay.domain.utility.WaterUtilizationCharge;
import com.imani.bill.pay.domain.utility.repository.IWaterServiceAgreementRepository;
import com.imani.bill.pay.domain.utility.repository.IWaterUtilizationChargeRepository;
import com.imani.bill.pay.domain.utility.repository.IWaterUtilizationRepository;
import com.imani.bill.pay.service.billing.IImaniBillService;
import com.imani.bill.pay.service.billing.ImaniBillService;
import com.imani.bill.pay.service.util.DateTimeUtil;
import com.imani.bill.pay.service.util.IDateTimeUtil;
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
@Service(WaterUtilizationService.SPRING_BEAN)
public class WaterUtilizationService implements IWaterUtilizationService {


    @Autowired
    @Qualifier(DateTimeUtil.SPRING_BEAN)
    private IDateTimeUtil iDateTimeUtil;

    @Autowired
    @Qualifier(ImaniBillService.SPRING_BEAN)
    private IImaniBillService imaniBillService;


    @Autowired
    private IBillPayFeeRepository iBillPayFeeRepository;

    @Autowired
    private IWaterUtilizationChargeRepository iWaterUtilizationChargeRepository;

    @Autowired
    private IWaterUtilizationRepository iWaterUtilizationRepository;

    @Autowired
    private IWaterServiceAgreementRepository iWaterServiceAgreementRepository;


    public static final String SPRING_BEAN = "com.imani.bill.pay.service.utility.WaterUtilizationService";

    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(WaterUtilizationService.class);



    @Transactional
    @Override
    public WaterUtilizationCharge computeUtilizationCharge(ImaniBill imaniBill) {
        Assert.notNull(imaniBill, "ImaniBill cannot be null");
        Assert.notNull(imaniBill.getWaterServiceAgreement(), "WaterServiceAgreement on bill cannot be null");

        // TODO note that this currently only supports a quarterly utilization computation.  Make this more flexible
        DateTime start = iDateTimeUtil.getDateTimeAStartOfCurrentQuarter();
        DateTime end = iDateTimeUtil.getDateTimeAEndOfCurrentQuarter();

        // Get existing WaterUtilizationCharge or create new and compute + update charges based on current utilization
        WaterUtilizationCharge waterUtilizationCharge = getDefaultWaterUtilizationCharge(imaniBill, start, end);
        computeAndUpdateWaterUtilizationCharge(imaniBill.getWaterServiceAgreement(), waterUtilizationCharge);

        return waterUtilizationCharge;
    }

    @Transactional
    @Override
    public double computeUtilizationChargeWithSchdFees(ImaniBill imaniBill) {
        Assert.notNull(imaniBill, "ImaniBill cannot be null");
        Assert.notNull(imaniBill.getWaterServiceAgreement(), "WaterServiceAgreement on bill cannot be null");

        // Get and update the current water utilization charge
        WaterUtilizationCharge waterUtilizationCharge = computeUtilizationCharge(imaniBill);

        // Compute the total amount of scheduled fees applied already to check if we need to apply new fees
        double chargeWithScheduledFees = 0;
        double totalFeesLeviedAmount = imaniBill.computeTotalFeeAmountByFeeTypeE(FeeTypeE.Scheduled_Fee);

        if(totalFeesLeviedAmount > 0) {
            // Fees have already been applied on the ImaniBill so add it to newly computed utlization charge
            LOGGER.info("Total scheduled fees in amount => {} have already been levied against this bill", totalFeesLeviedAmount);
            chargeWithScheduledFees = waterUtilizationCharge.getCharge().doubleValue() + totalFeesLeviedAmount;
            imaniBill.setAmountOwed(chargeWithScheduledFees);
        } else {
            // Compute and apply scheduled fees. Look up only the scheduled fees here. Late fees are applied seperately
            BillScheduleTypeE billScheduleTypeE = imaniBill.getWaterServiceAgreement().getEmbeddedAgreement().getBillScheduleTypeE();
            List<BillPayFee> billPayFees = iBillPayFeeRepository.findBillPayFeeBySchedule(imaniBill.getWaterServiceAgreement().getEmbeddedUtilityService().getUtilityProviderBusiness(), FeeTypeE.Scheduled_Fee, billScheduleTypeE);
            chargeWithScheduledFees = computeTotalChargeWithFees(billPayFees, waterUtilizationCharge, imaniBill);
        }

        LOGGER.info("Computed final chargeWithScheduledFees=> {}", chargeWithScheduledFees);

        // Save or update the ImaniBill as well as the WaterUtilizationCharge
        imaniBillService.save(imaniBill);
        iWaterUtilizationChargeRepository.save(waterUtilizationCharge);
        return chargeWithScheduledFees;
    }

    @Transactional
    @Override
    public void logWaterUtilization(WaterUtilization waterUtilization, ExecutionResult executionResult) {
        Assert.notNull(waterUtilization, "WaterUtilization cannot be null");
        Assert.notNull(executionResult, "ExecutionResult cannot be null");
        Assert.isNull(waterUtilization.getId(), "WaterUtilization is not a new non persisted instance");

        Optional<WaterServiceAgreement> waterServiceAgreement = iWaterServiceAgreementRepository.findById(waterUtilization.getWaterServiceAgreement().getId());

        if (waterServiceAgreement.isPresent()) {
            DateTime start = iDateTimeUtil.getDateTimeAtStartOfCurrentDay();
            DateTime end = iDateTimeUtil.getDateTimeAtEndOfCurrentDay();

            // Check to see if utilization has already been logged for the day.  We only allow one utilization log per day.
            List<WaterUtilization> waterUtilizations = iWaterUtilizationRepository.findUtilizationInPeriod(waterServiceAgreement.get(), start, end);

            if (waterUtilizations.isEmpty()) {
                WaterUtilization persistedUtilization = WaterUtilization.builder()
                        .waterServiceAgreement(waterServiceAgreement.get())
                        .description(waterUtilization.getDescription())
                        .numberOfGallonsUsed(waterUtilization.getNumberOfGallonsUsed())
                        .build();
                iWaterUtilizationRepository.save(persistedUtilization);
            } else {
                executionResult.addValidationAdvice(ValidationAdvice.newInstance("Water utilization already logged for day on this agreement."));
            }
        } else {
            executionResult.addValidationAdvice(ValidationAdvice.newInstance("No existing water service agreement found"));
        }
    }


    void computeAndUpdateWaterUtilizationCharge(WaterServiceAgreement waterServiceAgreement, WaterUtilizationCharge waterUtilizationCharge) {
        // TODO note that this currently only supports a quarterly utilization computation.  Make this more flexible
        DateTime start = iDateTimeUtil.getDateTimeAStartOfCurrentQuarter();
        DateTime end = iDateTimeUtil.getDateTimeAEndOfCurrentQuarter();

        LOGGER.info("Attempting to compute current water utilization charge with DateRange[{} - {}] on agreement", start, end);

        long totalGallonsUsed = 0;
        List<WaterUtilization> waterUtilizations = iWaterUtilizationRepository.findUtilizationInPeriod(waterServiceAgreement, start, end);

        for(WaterUtilization waterUtilization : waterUtilizations) {
            totalGallonsUsed = totalGallonsUsed + waterUtilization.getNumberOfGallonsUsed();
        }

        if(totalGallonsUsed > 0) {
            double fixCostPer1000Galls = waterServiceAgreement.getEmbeddedAgreement().getFixedCost(); // Per agreement this will be the (fixed cost/1000 gallons)
            LOGGER.info("Total current water utilization computed to [{} gallons], figuring out chare on FixCostPer1000Galls[{}]", totalGallonsUsed, fixCostPer1000Galls);
            double finalChargeOnUtilization = (totalGallonsUsed * fixCostPer1000Galls) / 1000;
            waterUtilizationCharge.setTotalGallonsUsed(totalGallonsUsed);
            waterUtilizationCharge.setCharge(finalChargeOnUtilization);
        }
    }




    double computeTotalChargeWithFees(List<BillPayFee> billPayFees, WaterUtilizationCharge waterUtilizationCharge, ImaniBill imaniBill) {
        double chargeWithFees = waterUtilizationCharge.getCharge();

        for(BillPayFee billPayFee : billPayFees) {
            chargeWithFees = billPayFee.calculatePaymentWithFees(chargeWithFees);
            double feeAmount = billPayFee.calculatFeeCharge(chargeWithFees);
            imaniBill.addImaniBillToFee(billPayFee, feeAmount);
        }

        imaniBill.setAmountOwed(chargeWithFees);
        return chargeWithFees;
    }

    WaterUtilizationCharge getDefaultWaterUtilizationCharge(ImaniBill imaniBill, DateTime start, DateTime end) {
        WaterUtilizationCharge waterUtilizationCharge = WaterUtilizationCharge.builder()
                .utilizationStart(start)
                .utilizationEnd(end)
                .imaniBill(imaniBill)
                .build();

        if (imaniBill.getId() != null) { // Make sure object is persisted before this check
            Optional<WaterUtilizationCharge> optionalWaterUtilizationCharge = iWaterUtilizationChargeRepository.findByImaniBillInQtr(imaniBill, start, end);

            if(optionalWaterUtilizationCharge.isPresent()) {
                LOGGER.info("Found an already existing utilization charge => {} ", optionalWaterUtilizationCharge.get());
                waterUtilizationCharge = optionalWaterUtilizationCharge.get();
            }
        }

        return waterUtilizationCharge;
    }

}
