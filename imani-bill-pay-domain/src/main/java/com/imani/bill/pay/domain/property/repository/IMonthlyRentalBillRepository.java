package com.imani.bill.pay.domain.property.repository;

import com.imani.bill.pay.domain.property.MonthlyRentalBill;
import com.imani.bill.pay.domain.user.UserRecord;
import org.joda.time.DateTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author manyce400
 */
public interface IMonthlyRentalBillRepository extends JpaRepository<MonthlyRentalBill, Long> {


    @Query("Select monthlyRentalBill From MonthlyRentalBill monthlyRentalBill Where monthlyRentalBill.userResidence.userRecord = ?1")
    public List<MonthlyRentalBill> getAllUserMonthlyRentalBill(UserRecord userRecord);

    @Query("Select monthlyRentalBill From MonthlyRentalBill monthlyRentalBill Where monthlyRentalBill.userResidence.userRecord = ?1 and monthlyRentalBill.rentalMonth = ?2")
    public MonthlyRentalBill getUserMonthlyRentalBill(UserRecord userRecord, DateTime dateTime);

}
