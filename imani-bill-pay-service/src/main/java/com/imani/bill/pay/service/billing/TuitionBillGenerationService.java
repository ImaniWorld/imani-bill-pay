package com.imani.bill.pay.service.billing;

import com.imani.bill.pay.domain.billing.BillServiceRenderedTypeE;
import com.imani.bill.pay.domain.billing.ImaniBill;
import com.imani.bill.pay.domain.education.TuitionAgreement;
import com.imani.bill.pay.domain.education.repository.ITuitionAgreementRepository;
import com.imani.bill.pay.domain.user.UserRecord;
import com.imani.bill.pay.service.util.DateTimeUtil;
import com.imani.bill.pay.service.util.IDateTimeUtil;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * @author manyce400
 */
@Service(TuitionBillGenerationService.SPRING_BEAN)
public class TuitionBillGenerationService implements IBillGenerationService {



    @Autowired
    private ITuitionAgreementRepository iTuitionAgreementRepository;

    @Autowired
    @Qualifier(DateTimeUtil.SPRING_BEAN)
    private IDateTimeUtil iDateTimeUtil;

    @Autowired
    @Qualifier(ImaniBillService.SPRING_BEAN)
    private IImaniBillService imaniBillService;

    @Autowired
    @Qualifier(TuitionBillPayFeeGenerationService.SPRING_BEAN)
    private IBillPayFeeGenerationService iBillPayFeeGenerationService;



    public static final String SPRING_BEAN = "com.imani.bill.pay.service.billing.TuitionBillGenerationService";

    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(TuitionBillGenerationService.class);


    @Override
    public boolean generateImaniBill(Object generationObject) {
        return false;
    }

    void genImaniBill(UserRecord userRecord, TuitionAgreement tuitionAgreement, DateTime dateTimeAtStartOfMonth) {
        LOGGER.info("Generating a new TuitionAgreement ImaniBill for Parent/Guardian({}) with dateTimeAtStartOfMonth:=> {}", userRecord.getEmbeddedContactInfo().getEmail(), dateTimeAtStartOfMonth);
        ImaniBill imaniBill = ImaniBill.builder()
                .billScheduleDate(dateTimeAtStartOfMonth)
                .billScheduleTypeE(tuitionAgreement.getEmbeddedAgreement().getBillScheduleTypeE())
                .billServiceRenderedTypeE(BillServiceRenderedTypeE.Tuition)
                .amountOwed(tuitionAgreement.getEmbeddedAgreement().getFixedCost())
                .tuitionAgreement(tuitionAgreement)
                .amountPaid(0.0)
                .build();
        imaniBillService.save(imaniBill);
    }

}