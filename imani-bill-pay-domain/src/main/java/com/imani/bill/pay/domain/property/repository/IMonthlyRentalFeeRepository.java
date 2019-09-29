package com.imani.bill.pay.domain.property.repository;


import com.imani.bill.pay.domain.property.MonthlyRentalFee;
import com.imani.bill.pay.domain.property.Property;
import com.imani.bill.pay.domain.property.RentalFeeTypeE;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * @author manyce400
 */
@Repository
public interface IMonthlyRentalFeeRepository extends JpaRepository<MonthlyRentalFee, Long> {


    @Query("Select monthlyRentalFee From MonthlyRentalFee monthlyRentalFee Where monthlyRentalFee.property = ?1 and monthlyRentalFee.rentalFeeTypeE = ?2")
    public MonthlyRentalFee findPropertyMonthlyRentalFeeByType(Property property, RentalFeeTypeE rentalFeeTypeE);

}
