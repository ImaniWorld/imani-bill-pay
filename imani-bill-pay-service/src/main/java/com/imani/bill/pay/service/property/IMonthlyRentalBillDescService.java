package com.imani.bill.pay.service.property;

import com.imani.bill.pay.domain.property.MonthlyRentalBill;
import com.imani.bill.pay.domain.property.MonthlyRentalBillExplained;
import com.imani.bill.pay.domain.user.UserRecord;
import com.imani.bill.pay.domain.user.UserResidencePropertyService;

import java.util.Optional;
import java.util.Set;

/**
 * @author manyce400
 */
public interface IMonthlyRentalBillDescService {

    public Optional<MonthlyRentalBillExplained> getCurrentMonthRentalBill(UserRecord userRecord);

    public Optional<MonthlyRentalBillExplained> explainCurrentMonthRentalBill(UserRecord userRecord);

    public Double calculateTotalAmountDue(MonthlyRentalBill monthlyRentalBill, Set<UserResidencePropertyService> userResidencePropertyServices);

}
