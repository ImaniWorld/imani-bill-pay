package com.imani.bill.pay.service.payment;

import com.imani.bill.pay.domain.payment.RentalPaymentHistory;
import com.imani.bill.pay.domain.property.MonthlyRentalBill;
import com.imani.bill.pay.domain.property.MonthlyRentalBillExplained;
import com.imani.bill.pay.domain.user.UserRecord;

import java.util.List;
import java.util.Optional;

/**
 * @author manyce400
 */
public interface IRentalPaymentHistoryService {


    public void save(RentalPaymentHistory rentalPaymentHistory);

    public void createRentalPaymentHistory(MonthlyRentalBill monthlyRentalBill, MonthlyRentalBillExplained monthlyRentalBillExplained);

    // Finds entire RentalPaymentHistory in the current month for the UserRecord
    public Optional<List<RentalPaymentHistory>> findUserRentalPaymentForCurrentMonth(UserRecord userRecord);

    // Returns true if there are any pending user payments for the current month which haven't posted and settled.
    public boolean hasPendingUserRentalPaymentForCurrentMonth(UserRecord userRecord);

    // Finds and returns all Current Month rental payments which are still pending.
    public Optional<List<RentalPaymentHistory>> findPendingUserRentalPaymentForCurrentMonth(UserRecord userRecord);
}
