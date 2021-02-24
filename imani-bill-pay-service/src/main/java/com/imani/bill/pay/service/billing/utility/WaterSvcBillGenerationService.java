package com.imani.bill.pay.service.billing.utility;

import com.imani.bill.pay.domain.billing.BillScheduleTypeE;
import com.imani.bill.pay.domain.billing.BillServiceRenderedTypeE;
import com.imani.bill.pay.domain.billing.ImaniBill;
import com.imani.bill.pay.domain.billing.repository.IImaniBillWaterSvcAgreementRepository;
import com.imani.bill.pay.domain.user.UserRecord;
import com.imani.bill.pay.domain.utility.WaterServiceAgreement;
import com.imani.bill.pay.domain.utility.WaterUtilizationCharge;
import com.imani.bill.pay.domain.utility.repository.IWaterUtilizationChargeRepository;
import com.imani.bill.pay.service.billing.IBillGenerationService;
import com.imani.bill.pay.service.billing.IImaniBillService;
import com.imani.bill.pay.service.billing.ImaniBillService;
import com.imani.bill.pay.service.util.DateTimeUtil;
import com.imani.bill.pay.service.util.IDateTimeUtil;
import com.imani.bill.pay.service.utility.IWaterUtilizationService;
import com.imani.bill.pay.service.utility.WaterUtilizationService;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.transaction.Transactional;
import java.util.Optional;

/**
 * @author manyce400
 */
@Service(WaterSvcBillGenerationService.SPRING_BEAN)
public class WaterSvcBillGenerationService  implements IBillGenerationService<WaterServiceAgreement> {


    @Autowired
    @Qualifier(DateTimeUtil.SPRING_BEAN)
    private IDateTimeUtil iDateTimeUtil;

    @Autowired
    @Qualifier(ImaniBillService.SPRING_BEAN)
    private IImaniBillService imaniBillService;

    @Autowired
    @Qualifier(WaterUtilizationService.SPRING_BEAN)
    private IWaterUtilizationService iWaterUtilizationService;

    @Autowired
    private IWaterUtilizationChargeRepository iWaterUtilizationChargeRepository;

    @Autowired
    private IImaniBillWaterSvcAgreementRepository imaniBillWaterSvcAgreementRepository;


    public static final String SPRING_BEAN = "com.imani.bill.pay.service.billing.utility.WaterSvcBillGenerationService";

    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(WaterSvcBillGenerationService.class);

    @Transactional
    @Override
    public boolean generateImaniBill(UserRecord userRecord) {
        return false;
    }

    @Transactional
    @Override
    public boolean generateImaniBill(WaterServiceAgreement waterServiceAgreement) {
        Assert.notNull(waterServiceAgreement, "WaterServiceAgreement cannot be null");
        LOGGER.info("Attempting to generate a quarterly ImaniBill on {}", waterServiceAgreement.describeAgreement());

        if(waterServiceAgreement.getEmbeddedAgreement().getBillScheduleTypeE() == BillScheduleTypeE.QUARTERLY) {
            // Check to see IF a bill has already been created for the start of the quarter on this agreement
            DateTime dateAtStartOfQtr = iDateTimeUtil.getDateTimeAStartOfCurrentQuarter();
            Optional<ImaniBill> imaniBill = imaniBillWaterSvcAgreementRepository.getImaniBillForAgreement(waterServiceAgreement.getEmbeddedAgreement().getAgreementUserRecord(), waterServiceAgreement, dateAtStartOfQtr);

            if(!imaniBill.isPresent()) {
                LOGGER.info("Generating new ImaniBill for quarter start Date[{}] ", dateAtStartOfQtr);
                ImaniBill persistedBill = generateImaniBill(waterServiceAgreement.getEmbeddedAgreement().getAgreementUserRecord(), waterServiceAgreement, dateAtStartOfQtr);
                WaterUtilizationCharge waterUtilizationCharge = iWaterUtilizationService.computeWaterUtilizationCharge(waterServiceAgreement);

                // Compute charges with fees and persist bill.
                double amountOwed = iWaterUtilizationService.computeUtilizationChargeWithFees(waterServiceAgreement, persistedBill, waterUtilizationCharge.getCharge());
                persistedBill.setAmountOwed(amountOwed);
                imaniBillService.save(persistedBill);

                if (waterUtilizationCharge.getCharge().doubleValue() > 0) {
                    LOGGER.info("Persisting utilization details...");
                    waterUtilizationCharge.setImaniBill(persistedBill);
                    iWaterUtilizationChargeRepository.save(waterUtilizationCharge);
                }
            }
        }

        return false;
    }

    private ImaniBill generateImaniBill(UserRecord userRecord, WaterServiceAgreement waterServiceAgreement, DateTime dateAtStartOfQtr) {
        LOGGER.info("Generating a new WaterServiceAgreement ImaniBill for User({}) with dateAtStartOfQtr:=> {}", userRecord.getEmbeddedContactInfo().getEmail(), dateAtStartOfQtr);
        ImaniBill imaniBill = ImaniBill.builder()
                .billScheduleDate(dateAtStartOfQtr)
                .billScheduleTypeE(waterServiceAgreement.getEmbeddedAgreement().getBillScheduleTypeE())
                .billServiceRenderedTypeE(BillServiceRenderedTypeE.Utility)
                .billedUser(userRecord)
                .waterServiceAgreement(waterServiceAgreement)
                .build();
        return imaniBill;
    }

}
