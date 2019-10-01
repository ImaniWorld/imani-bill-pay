package com.imani.bill.pay.service.property;

import com.imani.bill.pay.domain.property.MonthlyRentalBill;
import com.imani.bill.pay.domain.property.PropertyServiceChargeExplained;
import com.imani.bill.pay.domain.user.UserResidence;

import java.util.List;
import java.util.Optional;

/**
 * @author manyce400
 */
public interface IMonthlyPropertySvcChargeService {

    public Optional<List<PropertyServiceChargeExplained>> applyMonthlyPropertyServiceCharge(UserResidence userResidence, MonthlyRentalBill monthlyRentalBill);

}
