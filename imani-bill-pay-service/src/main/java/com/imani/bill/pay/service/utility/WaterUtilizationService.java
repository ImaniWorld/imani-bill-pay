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
    private IBillPayFeeRepository iBillPayFeeRepository;

    @Autowired
    private IWaterUtilizationChargeRepository iWaterUtilizationChargeRepository;

    @Autowired
    private IWaterUtilizationRepository iWaterUtilizationRepository;

    @Autowired
    private IWaterServiceAgreementRepository iWaterServiceAgreementRepository;

    @Autowired
    @Qualifier(DateTimeUtil.SPRING_BEAN)
    private IDateTimeUtil iDateTimeUtil;

    public static final String SPRING_BEAN = "com.imani.bill.pay.service.utility.WaterUtilizationService";

    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(WaterUtilizationService.class);


    @Override
    public WaterUtilizationCharge computeWaterUtilizationCharge(WaterServiceAgreement waterServiceAgreement) {
        Assert.notNull(waterServiceAgreement, "WaterServiceAgreement cannot be null");
        WaterUtilizationCharge waterUtilizationCharge = computeChargeOnUtilization(waterServiceAgreement, Optional.empty());
        LOGGER.info("Computed water utilization charge of: {}", waterUtilizationCharge);
        return waterUtilizationCharge;
    }

    @Override
    public WaterUtilizationCharge computeWaterUtilizationChargeWithScheduledFees(ImaniBill imaniBill) {
        Assert.notNull(imaniBill, "ImaniBill cannot be null");
        WaterUtilizationCharge waterUtilizationCharge = computeChargeOnUtilizationWithSchdFees(imaniBill.getWaterServiceAgreement(), Optional.of(imaniBill));
        return waterUtilizationCharge;
    }

    @Override
    public double computeUtilizationChargeWithFees(WaterServiceAgreement waterServiceAgreement, ImaniBill imaniBill, double waterChargeOnUtilization) {
        Assert.notNull(imaniBill, "ImaniBill cannot be null");
        LOGGER.info("Applying all scheduled fees to current waterChargeOnUtilization=> {}", waterChargeOnUtilization);

        BillScheduleTypeE billScheduleTypeE = waterServiceAgreement.getEmbeddedAgreement().getBillScheduleTypeE();

        // Look up only the scheduled fees here. Late fees are applied seperately
        List<BillPayFee> billPayFees = iBillPayFeeRepository.findBillPayFeeBySchedule(waterServiceAgreement.getEmbeddedUtilityService().getUtilityProviderBusiness(), FeeTypeE.Scheduled_Fee, billScheduleTypeE);
        double utilizationChargePlusFees = computeTotalPaymentWithFees(billPayFees, waterChargeOnUtilization, imaniBill);
        return utilizationChargePlusFees;
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

    WaterUtilizationCharge computeChargeOnUtilization(WaterServiceAgreement waterServiceAgreement, Optional<ImaniBill> imaniBill) {
        // TODO note that this currently only supports a quarterly utilization computation.  Make this more flexible
        DateTime start = iDateTimeUtil.getDateTimeAStartOfCurrentQuarter();
        DateTime end = iDateTimeUtil.getDateTimeAEndOfCurrentQuarter();
        WaterUtilizationCharge waterUtilizationCharge = getDefaultWaterUtilizationCharge(imaniBill, start, end);

        LOGGER.info("Attempting to compute water utilization charges with DateRange[{} - {}]", start, end);

        long totalGallonsUsed = 0;

        List<WaterUtilization> waterUtilizations = iWaterUtilizationRepository.findUtilizationInPeriod(waterServiceAgreement, start, end);
        for(WaterUtilization waterUtilization : waterUtilizations) {
            totalGallonsUsed = totalGallonsUsed + waterUtilization.getNumberOfGallonsUsed();
        }

        double fixCostPer1000Galls = waterServiceAgreement.getEmbeddedAgreement().getFixedCost(); // Per agreement this will be the (fixed cost/1000 gallons)
        LOGGER.info("Total current water utilization computed to [{} gallons], figuring out chare on FixCostPer1000Galls[{}]", totalGallonsUsed, fixCostPer1000Galls);

        if(totalGallonsUsed > 0) {
            double waterChargeOnUtilization = (totalGallonsUsed * fixCostPer1000Galls) / 1000;
            waterUtilizationCharge.setTotalGallonsUsed(totalGallonsUsed);
            waterUtilizationCharge.setCharge(waterChargeOnUtilization);
        } else {
            // Since we computed 0 total utilization it means there was no utilization at all
            waterUtilizationCharge.setCharge(0d);
        }

        return waterUtilizationCharge;
    }

    WaterUtilizationCharge computeChargeOnUtilizationWithSchdFees(WaterServiceAgreement waterServiceAgreement, Optional<ImaniBill> imaniBill) {
        WaterUtilizationCharge waterUtilizationCharge = computeChargeOnUtilization(waterServiceAgreement, imaniBill);
        double waterChargeOnUtilization = waterUtilizationCharge.getCharge();

        // Look up only the scheduled fees here. Late fees are applied seperately
        BillScheduleTypeE billScheduleTypeE = waterServiceAgreement.getEmbeddedAgreement().getBillScheduleTypeE();
        List<BillPayFee> billPayFees = iBillPayFeeRepository.findBillPayFeeBySchedule(waterServiceAgreement.getEmbeddedUtilityService().getUtilityProviderBusiness(), FeeTypeE.Scheduled_Fee, billScheduleTypeE);

        for(BillPayFee billPayFee : billPayFees) {
            waterChargeOnUtilization = billPayFee.calculatePaymentWithFees(waterChargeOnUtilization);
        }

        waterUtilizationCharge.setCharge(waterChargeOnUtilization);
        return waterUtilizationCharge;
    }


    double computeTotalPaymentWithFees(List<BillPayFee> billPayFees, double waterChargeOnUtilization, ImaniBill imaniBill) {
        for(BillPayFee billPayFee : billPayFees) {
            waterChargeOnUtilization = billPayFee.calculatePaymentWithFees(waterChargeOnUtilization);
            double feeAmount = billPayFee.calculatFeeCharge(waterChargeOnUtilization);
            imaniBill.addImaniBillToFee(billPayFee, feeAmount);
        }

        return waterChargeOnUtilization;
    }

    WaterUtilizationCharge getDefaultWaterUtilizationCharge(Optional<ImaniBill> imaniBill, DateTime start, DateTime end) {
        WaterUtilizationCharge waterUtilizationCharge = WaterUtilizationCharge.builder()
                .utilizationStart(start)
                .utilizationEnd(end)
                .build();

        if (imaniBill.isPresent()) {
            Optional<WaterUtilizationCharge> optionalWaterUtilizationCharge = iWaterUtilizationChargeRepository.findByImaniBillInQtr(imaniBill.get(), start, end);

            if(optionalWaterUtilizationCharge.isPresent()) {
                // A water charge already exists for this bll so use that
                LOGGER.info("Found an already existing utilization charge => {} ", optionalWaterUtilizationCharge.get());
                waterUtilizationCharge = optionalWaterUtilizationCharge.get();
            }
        }

        return waterUtilizationCharge;
    }

}
