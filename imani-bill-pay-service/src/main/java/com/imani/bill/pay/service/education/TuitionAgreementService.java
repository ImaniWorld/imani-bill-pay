package com.imani.bill.pay.service.education;

import com.imani.bill.pay.domain.business.repository.IBusinessRepository;
import com.imani.bill.pay.domain.education.SchoolToTuitionGrade;
import com.imani.bill.pay.domain.education.TuitionAgreement;
import com.imani.bill.pay.domain.education.repository.ISchoolToTuitionGradeRepository;
import com.imani.bill.pay.domain.education.repository.ITuitionAgreementRepository;
import com.imani.bill.pay.service.util.DateTimeUtil;
import com.imani.bill.pay.service.util.IDateTimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

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


    //    @Transactional
//    @Override
//    public void newTuitionAgreement(TuitionAgreementLite tuitionAgreementLite) {
//        Assert.notNull(tuitionAgreementLite, "tuitionAgreementLite cannot be null");
//        LOGGER.debug("Building new tuition agreement from: {}", tuitionAgreementLite);
//
//        Business business = iBusinessRepository.getOne(tuitionAgreementLite.getBusinessID());
//
//        TuitionAgreement tuitionAgreement = TuitionAgreement.builder()
////                .studentFirstName(tuitionAgreementLite.getStudentFirstName())
////                .studentLastName(tuitionAgreementLite.getStudentLastName())
//                .embeddedAgreement(tuitionAgreementLite.getEmbeddedAgreement())
//                .business(business)
//                .build();
//
//        iTuitionAgreementRepository.save(tuitionAgreement);
//    }

}
