package com.imani.bill.pay.service.payment;

import com.imani.bill.pay.domain.property.MonthlyRentalBillExplained;
import com.imani.bill.pay.domain.property.RentalBillPayResult;

/**
 * @author manyce400
 */
public interface IMonthlyRentalBillPayService {

    // Execute monthly rental payment
    public RentalBillPayResult payMonthlyRental(MonthlyRentalBillExplained monthlyRentalBillExplained);

}
