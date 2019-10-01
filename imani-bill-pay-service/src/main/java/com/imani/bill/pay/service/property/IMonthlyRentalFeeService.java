package com.imani.bill.pay.service.property;

import com.imani.bill.pay.domain.property.MonthlyRentalBill;
import com.imani.bill.pay.domain.property.MonthlyRentalFeeExplained;
import com.imani.bill.pay.domain.user.UserResidence;

import java.util.List;
import java.util.Optional;

/**
 * @author manyce400
 */
public interface IMonthlyRentalFeeService {

    // Applies rental fee's where applicable to a MonthlyRentalBill
    public Optional<List<MonthlyRentalFeeExplained>> applyMonthlyRentalFees(UserResidence userResidence, MonthlyRentalBill monthlyRentalBill);

}
