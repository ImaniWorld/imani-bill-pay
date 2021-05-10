package com.imani.bill.pay.service.education;

import com.imani.bill.pay.domain.business.Business;
import com.imani.bill.pay.domain.business.repository.IBusinessRepository;
import com.imani.bill.pay.domain.education.SchoolToTuitionGrade;
import com.imani.bill.pay.domain.education.TuitionAgreement;
import com.imani.bill.pay.domain.education.TuitionAgreementLite;
import com.imani.bill.pay.domain.education.TuitionGrade;
import com.imani.bill.pay.domain.education.repository.ISchoolToTuitionGradeRepository;
import com.imani.bill.pay.domain.education.repository.ITuitionAgreementRepository;
import com.imani.bill.pay.domain.education.repository.ITuitionGradeRepository;
import com.imani.bill.pay.domain.user.UserRecord;
import com.imani.bill.pay.domain.user.repository.IUserRecordRepository;
import com.imani.bill.pay.service.util.DateTimeUtil;
import com.imani.bill.pay.service.util.IDateTimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.transaction.Transactional;
import java.util.Optional;

/**
 * @author manyce400
 */
@Service(TuitionAgreementService.SPRING_BEAN)
public class TuitionAgreementService implements ITuitionAgreementService {


    @Autowired
    @Qualifier(DateTimeUtil.SPRING_BEAN)
    private IDateTimeUtil iDateTimeUtil;

    @Autowired
    private IBusinessRepository iBusinessRepository;

    @Autowired
    private IUserRecordRepository iUserRecordRepository;

    @Autowired
    private ITuitionGradeRepository iTuitionGradeRepository;

    @Autowired
    private ISchoolToTuitionGradeRepository iSchoolToTuitionGradeRepository;

    @Autowired
    private ITuitionAgreementRepository iTuitionAgreementRepository;

    public static final String SPRING_BEAN = "com.imani.bill.pay.service.education.TuitionAgreementService";

    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(TuitionAgreementService.class);


    @Override
    public Integer findNumberOfDaysTillPaymentLate(TuitionAgreement tuitionAgreement) {
        Assert.notNull(tuitionAgreement, "TuitionAgreement cannot be null");
        LOGGER.info("Finding number of days till a monthly payment is considered late on tuitionAgreement: {}", tuitionAgreement);

        Integer overrideNumberOfDaysTillLate = tuitionAgreement.getEmbeddedAgreement().getNumberOfDaysTillLate();
        Optional<SchoolToTuitionGrade> schoolToTuitionGrade = iSchoolToTuitionGradeRepository.findBySchoolAndTuitionGrade(tuitionAgreement.getBusiness(), tuitionAgreement.getTuitionGrade());

        if(overrideNumberOfDaysTillLate != null) {
            LOGGER.info("Returning override configured NumberOfDaysTillLate => {}", overrideNumberOfDaysTillLate);
            return overrideNumberOfDaysTillLate;

        }

        LOGGER.info("Returning default configured NumberOfDaysTillLate => {}", schoolToTuitionGrade.get().getNumberOfDaysTillLate());
        return schoolToTuitionGrade.get().getNumberOfDaysTillLate();
    }


    @Transactional
    @Override
    public void newTuitionAgreement(TuitionAgreementLite tuitionAgreementLite) {
        Assert.notNull(tuitionAgreementLite, "tuitionAgreementLite cannot be null");
        LOGGER.debug("Building new tuition agreement from: {}", tuitionAgreementLite);

        Business business = iBusinessRepository.getOne(tuitionAgreementLite.getSchoolID());
        TuitionGrade tuitionGrade = iTuitionGradeRepository.getOne(tuitionAgreementLite.getTuitionGradeID());
        UserRecord student = iUserRecordRepository.findByUserEmail(tuitionAgreementLite.getStudent().getEmail());
        UserRecord billedUser = iUserRecordRepository.findByUserEmail(tuitionAgreementLite.getEmbeddedAgreementLite().getAgreementUserRecord().getEmail());

        TuitionAgreement tuitionAgreement = TuitionAgreement.builder()
                .student(student)
                .tuitionGrade(tuitionGrade)
                .business(business)
                .embeddedAgreement(tuitionAgreementLite.getEmbeddedAgreementLite().toEmbeddedAgreement(billedUser))

                .build();

        iTuitionAgreementRepository.save(tuitionAgreement);
    }

}
