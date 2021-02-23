package com.imani.bill.pay.domain.billing.repository;

import com.imani.bill.pay.domain.billing.BillPayFee;
import com.imani.bill.pay.domain.billing.BillScheduleTypeE;
import com.imani.bill.pay.domain.billing.FeeTypeE;
import com.imani.bill.pay.domain.business.Business;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author manyce400
 */
@Repository
public interface IBillPayFeeRepository extends JpaRepository<BillPayFee, Long> {

    @Query("Select billPayFee From BillPayFee billPayFee Where billPayFee.business = ?1 and billPayFee.feeTypeE = ?2")
    public Optional<BillPayFee> findBillPayFeeByFeeType(Business business, FeeTypeE feeTypeE);

    @Query("Select billPayFee From BillPayFee billPayFee Where billPayFee.business = ?1 and billPayFee.feeTypeE = ?2 and billPayFee.billScheduleTypeE = ?3")
    public List<BillPayFee> findBillPayFeeBySchedule(Business business, FeeTypeE feeTypeE, BillScheduleTypeE billScheduleTypeE);

}
