package com.imani.bill.pay.domain.payment.repository;

import com.imani.bill.pay.domain.payment.PaymentStatusE;
import com.imani.bill.pay.domain.payment.RentalPaymentHistory;
import com.imani.bill.pay.domain.user.UserRecord;
import org.joda.time.DateTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author manyce400
 */
@Repository
public interface IRentalPaymentHistoryRepository extends JpaRepository<RentalPaymentHistory, Long> {

    @Query("Select  rentalPaymentHistory From RentalPaymentHistory rentalPaymentHistory Where rentalPaymentHistory.userRecord =?1 and rentalPaymentHistory.embeddedPayment.paymentDate =?2")
    public List<RentalPaymentHistory> findUserRentalPaymentHistoryByDate(UserRecord userRecord, DateTime paymentDate);

    @Query("Select  rentalPaymentHistory From RentalPaymentHistory rentalPaymentHistory Where rentalPaymentHistory.userRecord =?1 and rentalPaymentHistory.embeddedPayment.paymentDate >=?2  and rentalPaymentHistory.embeddedPayment.paymentDate <=?3")
    public List<RentalPaymentHistory> findUserRentalPaymentHistoryByDateRange(UserRecord userRecord, DateTime startDate, DateTime endDate);

    @Query("Select  rentalPaymentHistory From RentalPaymentHistory rentalPaymentHistory Where rentalPaymentHistory.userRecord =?1 and rentalPaymentHistory.embeddedPayment.paymentStatusE =?2 and rentalPaymentHistory.embeddedPayment.paymentDate =?3")
    public List<RentalPaymentHistory> findUserRentalPaymentHistoryByStatusAndDate(UserRecord userRecord, PaymentStatusE paymentStatusE, DateTime paymentDate);

    @Query("Select  rentalPaymentHistory From RentalPaymentHistory rentalPaymentHistory Where rentalPaymentHistory.userRecord =?1 and rentalPaymentHistory.embeddedPayment.paymentStatusE =?2 and rentalPaymentHistory.embeddedPayment.paymentDate >=?3 and rentalPaymentHistory.embeddedPayment.paymentDate <=?4")
    public List<RentalPaymentHistory> findUserRentalPaymentHistoryByStatusAndDateRange(UserRecord userRecord, PaymentStatusE paymentStatusE, DateTime startDate, DateTime endDate);

}
