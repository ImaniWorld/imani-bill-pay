package com.imani.bill.pay.domain.billing.repository;

import com.imani.bill.pay.domain.billing.BillPayFee;
import com.imani.bill.pay.domain.billing.ImaniBill;
import com.imani.bill.pay.domain.billing.ImaniBillToFee;
import org.joda.time.DateTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

/**
 * @author manyce400
 */
public interface IImaniBillToFeeRepository extends JpaRepository<ImaniBillToFee, Long> {

    @Query("Select imaniBillToFee From ImaniBillToFee imaniBillToFee Where imaniBillToFee.imaniBill = ?1 and imaniBillToFee.billPayFee = ?2 and imaniBillToFee.createDate >= ?3 and imaniBillToFee.createDate <= ?4")
    public Optional<ImaniBillToFee> findLateFeeInQtr(ImaniBill imaniBill, BillPayFee billPayFee, DateTime qtrStart, DateTime qtrEnd);

}
