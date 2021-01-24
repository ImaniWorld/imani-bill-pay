package com.imani.bill.pay.domain.education.repository;

import com.imani.bill.pay.domain.education.TuitionGrade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author manyce400
 */
@Repository
public interface ITuitionGradeRepository extends JpaRepository<TuitionGrade, Long> {


}
