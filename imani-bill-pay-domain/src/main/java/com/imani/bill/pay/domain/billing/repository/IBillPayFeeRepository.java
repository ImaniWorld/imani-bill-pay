package com.imani.bill.pay.domain.billing.repository;

import com.imani.bill.pay.domain.billing.BillPayFee;
import com.imani.bill.pay.domain.billing.FeeTypeE;
import com.imani.bill.pay.domain.property.Property;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * @author manyce400
 */
@Repository
public interface IBillPayFeeRepository extends JpaRepository<BillPayFee, Long> {

    @Query("Select billPayFee From BillPayFee billPayFee Where billPayFee.property = ?1 and billPayFee.feeTypeE = ?2")
    public BillPayFee findBillPayFeeByFeeType(Property property, FeeTypeE feeTypeE);

}
