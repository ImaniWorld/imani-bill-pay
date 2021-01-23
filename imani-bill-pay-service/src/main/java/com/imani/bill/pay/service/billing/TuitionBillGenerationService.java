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
import org.springframework.util.Assert;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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


    @Transactional
    @Override
    public boolean generateImaniBill(UserRecord userRecord) {
        Assert.notNull(userRecord, "UserRecord cannot be null");

        LOGGER.info("Attempting to generate and apply fees to Tuition Agreement bills for Parent/Guardian({})", userRecord.getEmbeddedContactInfo().getEmail());

        final DateTime dateTimeAtStartOfMonth = iDateTimeUtil.getDateTimeAtStartOfMonth(DateTime.now());

        // Find all current tuition agreements currently in force for a parent/guardian user who will be responsible for making payments
        List<TuitionAgreement> tuitionAgreements = iTuitionAgreementRepository.findParentTuitionAgreements(userRecord);

        tuitionAgreements.forEach(tuitionAgreement -> {
            LOGGER.info(tuitionAgreement.describeAgreement());

            // First try to create a new Monthly/Weekly bill for this agreement if one doesn't exist already
            Optional<ImaniBill> imaniBill = imaniBillService.findCurrentMonthBillForTuitionAgreement(userRecord, tuitionAgreement);

            if(!imaniBill.isPresent()) {
                genImaniBill(userRecord, tuitionAgreement, dateTimeAtStartOfMonth);
            } else {
                LOGGER.info("Existing ImaniBill already created for current month, skipping creation of new bill");
            }

            // Find all open and unpaid YTD Imani bills for current tuitionAgreement and apply late fees where applicable
            Set<ImaniBill> unPaidTuitionBills = imaniBillService.findYTDUnPaidImaniBillsForUser(userRecord, tuitionAgreement);
            LOGGER.info("System has detected that there are currently {} unpaid Imani Bills on this agreement", unPaidTuitionBills.size());

            unPaidTuitionBills.forEach(unPaidImaniBill -> {
                iBillPayFeeGenerationService.addImaniBillFees(userRecord, tuitionAgreement, imaniBill.get());
                imaniBillService.save(unPaidImaniBill);
            });
        });

        return true;
    }


    void genImaniBill(UserRecord userRecord, TuitionAgreement tuitionAgreement, DateTime dateTimeAtStartOfMonth) {
        LOGGER.info("Generating a new TuitionAgreement ImaniBill for Parent/Guardian({}) with dateTimeAtStartOfMonth:=> {}", userRecord.getEmbeddedContactInfo().getEmail(), dateTimeAtStartOfMonth);
        ImaniBill imaniBill = ImaniBill.builder()
                .billScheduleDate(dateTimeAtStartOfMonth)
                .billScheduleTypeE(tuitionAgreement.getEmbeddedAgreement().getBillScheduleTypeE())
                .billServiceRenderedTypeE(BillServiceRenderedTypeE.Tuition)
                .billedUser(userRecord)
                .amountOwed(tuitionAgreement.getEmbeddedAgreement().getFixedCost())
                .tuitionAgreement(tuitionAgreement)
                .amountPaid(0.0)
                .build();
        imaniBillService.save(imaniBill);
    }

}