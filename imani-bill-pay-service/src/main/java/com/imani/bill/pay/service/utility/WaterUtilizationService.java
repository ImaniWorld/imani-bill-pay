package com.imani.bill.pay.service.utility;


import com.imani.bill.pay.domain.billing.BillPayFee;
import com.imani.bill.pay.domain.billing.BillScheduleTypeE;
import com.imani.bill.pay.domain.billing.FeeTypeE;
import com.imani.bill.pay.domain.billing.ImaniBill;
import com.imani.bill.pay.domain.utility.WaterServiceAgreement;
import com.imani.bill.pay.domain.utility.WaterUtilization;
import com.imani.bill.pay.domain.utility.WaterUtilizationCharge;
import com.imani.bill.pay.domain.utility.WaterUtilizationLite;
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
        DateTime end = iDateTimeUtil.getDateTimeAtEndOfCurrentQuarter();

        // Get existing WaterUtilizationCharge or create new and compute + update charges based on current utilization
        WaterUtilizationCharge waterUtilizationCharge = getDefaultWaterUtilizationCharge(imaniBill, start, end);
        computeAndUpdateWaterUtilizationCharge(imaniBill.getWaterServiceAgreement(), waterUtilizationCharge);

        return waterUtilizationCharge;
    }

    @Override
    public WaterUtilizationCharge computeUpdateWithUtilizationCharge(ImaniBill imaniBill) {
        Assert.notNull(imaniBill, "ImaniBill cannot be null");

        // Execute call to compute utilization based on total gallons of water used
        WaterUtilizationCharge waterUtilizationCharge = computeUtilizationCharge(imaniBill);
        double amtOwedWithUtilization = waterUtilizationCharge.getCharge().doubleValue() + imaniBill.getAmountOwed();

        Object[] logArgs = {imaniBill.getId(), waterUtilizationCharge.getCharge().doubleValue(), amtOwedWithUtilization};
        LOGGER.info("Computed water utilization details ImaniBill[ID: {} | UtilizationCharge: {} | AmountOwed: {}]", logArgs);

        imaniBill.setAmountOwed(amtOwedWithUtilization);
        return waterUtilizationCharge;
    }

    @Transactional
    @Override
    public double computeUtilizationChargeWithSchdFees(ImaniBill imaniBill) {
        Assert.notNull(imaniBill, "ImaniBill cannot be null");
        Assert.notNull(imaniBill.getWaterServiceAgreement(), "WaterServiceAgreement on bill cannot be null");

        WaterServiceAgreement waterServiceAgreement = imaniBill.getWaterServiceAgreement();

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
            List<BillPayFee> billPayFees = waterServiceAgreement.getScheduledBillPayFees();
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
    public Optional<WaterUtilization> logWaterUtilization(WaterUtilizationLite waterUtilizationLite) {
        Assert.notNull(waterUtilizationLite, "WaterUtilizationLite cannot be null");

        // Load the agreement associated with this logging event
        Optional<WaterServiceAgreement> waterServiceAgreement = iWaterServiceAgreementRepository.findById(waterUtilizationLite.getWaterServiceAgreementLite().getId());
        if (waterServiceAgreement.isPresent()) {

            // Check to see if utilization has already been logged for the utilization day.  IF it has treat this as an update
            Optional<WaterUtilization> existingUtilization = iWaterUtilizationRepository.findUtilizationByDate(waterServiceAgreement.get(), waterUtilizationLite.getUtilizationDate());
            if (!existingUtilization.isPresent()) {
                LOGGER.info("Processing and logging new water utilization on=> [AgreementID: {} | UtilizationDate: {}]", waterUtilizationLite.getWaterServiceAgreementLite().getId(), waterUtilizationLite.getUtilizationDate());
                WaterUtilization newUtilization = WaterUtilization.builder()
                        .waterServiceAgreement(waterServiceAgreement.get())
                        .description(waterUtilizationLite.getDescription())
                        .numberOfGallonsUsed(waterUtilizationLite.getNumberOfGallonsUsed())
                        .utilizationDate(waterUtilizationLite.getUtilizationDate())
                        .build();
                iWaterUtilizationRepository.save(newUtilization);
                return Optional.of(newUtilization);
            } else {
                LOGGER.info("Processing an update to water utilization on=> [AgreementID: {} | UtilizationDate: {}]", waterUtilizationLite.getWaterServiceAgreementLite().getId(), waterUtilizationLite.getUtilizationDate());
                existingUtilization.get().setDescription(waterUtilizationLite.getDescription());
                existingUtilization.get().setNumberOfGallonsUsed(waterUtilizationLite.getNumberOfGallonsUsed());
                existingUtilization.get().setUtilizationDate(waterUtilizationLite.getUtilizationDate());
                iWaterUtilizationRepository.save(existingUtilization.get());
            }
        }

        return Optional.empty();
    }

    void computeAndUpdateWaterUtilizationCharge(WaterServiceAgreement waterServiceAgreement, WaterUtilizationCharge waterUtilizationCharge) {
        // TODO note that this currently only supports a quarterly utilization computation.  Make this more flexible
        DateTime start = iDateTimeUtil.getDateTimeAStartOfCurrentQuarter();
        DateTime end = iDateTimeUtil.getDateTimeAtEndOfCurrentQuarter();

        LOGGER.info("Attempting to compute current water utilization charge with DateRange[{} - {}] on agreement", start, end);

        long totalGallonsUsed = 0;
        List<WaterUtilization> waterUtilizations = iWaterUtilizationRepository.findUtilizationInPeriod(waterServiceAgreement, start, end);

        LOGGER.info("Found a total of {} utilizations", waterUtilizations.size());

        for(WaterUtilization waterUtilization : waterUtilizations) {
            totalGallonsUsed = totalGallonsUsed + waterUtilization.getNumberOfGallonsUsed();
        }

        if(totalGallonsUsed > 0) {
            double fixCostPer1000Galls = waterServiceAgreement.getEmbeddedAgreement().getFixedCost(); // Per agreement this will be the (fixed cost/1000 gallons)
            LOGGER.info("Total current water utilization computed to [{} gallons], figuring out chare on FixCostPer1000Galls[{}]", totalGallonsUsed, fixCostPer1000Galls);
            double finalChargeOnUtilization = (totalGallonsUsed * fixCostPer1000Galls) / 1000;
            waterUtilizationCharge.setTotalGallonsUsed(totalGallonsUsed);
            waterUtilizationCharge.setCharge(finalChargeOnUtilization);
        } else {
            waterUtilizationCharge.setTotalGallonsUsed(0L);
            waterUtilizationCharge.setCharge(0d);
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
                Object[] logArgs = {optionalWaterUtilizationCharge.get().getId(), optionalWaterUtilizationCharge.get().getCharge(),optionalWaterUtilizationCharge.get().getUtilizationStart(), optionalWaterUtilizationCharge.get().getUtilizationEnd()};
                LOGGER.info("Found an existing water WaterUtilizationCharge[ID: {} | Charge: {} Start: {} | End: {}] ", logArgs);
                waterUtilizationCharge = optionalWaterUtilizationCharge.get();
            }
        }

        return waterUtilizationCharge;
    }

}
