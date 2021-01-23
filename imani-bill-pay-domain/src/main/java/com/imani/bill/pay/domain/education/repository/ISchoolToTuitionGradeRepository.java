package com.imani.bill.pay.domain.education.repository;

import com.imani.bill.pay.domain.business.Business;
import com.imani.bill.pay.domain.education.SchoolToTuitionGrade;
import com.imani.bill.pay.domain.education.TuitionGrade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author manyce400
 */
@Repository
public interface ISchoolToTuitionGradeRepository extends JpaRepository<SchoolToTuitionGrade, Long> {

    @Query("Select schoolToTuitionGrade From SchoolToTuitionGrade schoolToTuitionGrade Where schoolToTuitionGrade.school = ?1 and schoolToTuitionGrade.tuitionGrade = ?2")
    public Optional<SchoolToTuitionGrade> findBySchoolAndTuitionGrade(Business school, TuitionGrade tuitionGrade);

}
