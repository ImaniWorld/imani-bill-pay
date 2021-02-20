package com.imani.bill.pay.service.billing;

import com.imani.bill.pay.domain.billing.BillScheduleTypeE;
import com.imani.bill.pay.domain.billing.BillServiceRenderedTypeE;
import com.imani.bill.pay.domain.billing.ImaniBill;
import com.imani.bill.pay.domain.billing.repository.IImaniBillWaterSvcAgreementRepository;
import com.imani.bill.pay.domain.user.UserRecord;
import com.imani.bill.pay.domain.utility.WaterServiceAgreement;
import com.imani.bill.pay.domain.utility.repository.IWaterServiceAgreementRepository;
import com.imani.bill.pay.service.billing.fee.BillFeeChargeService;
import com.imani.bill.pay.service.billing.fee.IBillFeeChargeService;
import com.imani.bill.pay.service.util.DateTimeUtil;
import com.imani.bill.pay.service.util.IDateTimeUtil;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.util.Assert;

import javax.transaction.Transactional;
import java.util.Optional;

/**
 * @author manyce400
 */
public class WaterSvcBillGenerationService  implements IBillGenerationService {


    @Autowired
    private IImaniBillWaterSvcAgreementRepository imaniBillWaterSvcAgreementRepository;

    @Autowired
    private IWaterServiceAgreementRepository iWaterServiceAgreementRepository;

    @Autowired
    @Qualifier(DateTimeUtil.SPRING_BEAN)
    private IDateTimeUtil iDateTimeUtil;

    @Autowired
    @Qualifier(ImaniBillService.SPRING_BEAN)
    private IImaniBillService imaniBillService;

    @Autowired
    @Qualifier(BillFeeChargeService.SPRING_BEAN)
    private IBillFeeChargeService iBillFeeChargeService;


    public static final String SPRING_BEAN = "com.imani.bill.pay.service.billing.WaterSvcBillGenerationService";

    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(WaterSvcBillGenerationService.class);

    @Transactional
    @Override
    public boolean generateImaniBill(UserRecord userRecord) {
        Assert.notNull(userRecord, "UserRecord cannot be null");

        LOGGER.info("Attempting to generate and apply fees to Water Svc Agreement bills for User({})", userRecord.getEmbeddedContactInfo().getEmail());
        Optional<WaterServiceAgreement> waterServiceAgreement = iWaterServiceAgreementRepository.findWaterServiceAgreement(userRecord);
        if(waterServiceAgreement.isPresent()
                && waterServiceAgreement.get().getEmbeddedAgreement().getBillScheduleTypeE() == BillScheduleTypeE.QUARTERLY) {
            // Check to see IF a bill has already been created for the start of the quarter on this agreement
            DateTime dateAtStartOfQtr = iDateTimeUtil.getDateTimeAStartOfCurrentQuarter();
            Optional<ImaniBill> imaniBill = imaniBillWaterSvcAgreementRepository.getImaniBillForAgreement(userRecord, waterServiceAgreement.get(), dateAtStartOfQtr);

            if(!imaniBill.isPresent()) {
                LOGGER.info("Generating new ImaniBill on: {} for quarterly date[{}] ", waterServiceAgreement.get().describeAgreement(), dateAtStartOfQtr);
                newImaniBill(userRecord, waterServiceAgreement.get(), dateAtStartOfQtr);
            } else {
                iBillFeeChargeService.chargeWaterBillLateFees(waterServiceAgreement.get());
            }
        }

        return false;
    }

    private void newImaniBill(UserRecord userRecord, WaterServiceAgreement waterServiceAgreement, DateTime dateAtStartOfQtr) {
        LOGGER.info("Generating a new WaterServiceAgreement ImaniBill for User({}) with dateAtStartOfQtr:=> {}", userRecord.getEmbeddedContactInfo().getEmail(), dateAtStartOfQtr);
        ImaniBill imaniBill = ImaniBill.builder()
                .billScheduleDate(dateAtStartOfQtr)
                .billScheduleTypeE(waterServiceAgreement.getEmbeddedAgreement().getBillScheduleTypeE())
                .billServiceRenderedTypeE(BillServiceRenderedTypeE.Tuition)
                .billedUser(userRecord)
                .waterServiceAgreement(waterServiceAgreement)
                .build();
        imaniBillService.save(imaniBill);
    }

}
