package com.imani.bill.pay.domain.payment.record.repository;

import com.imani.bill.pay.domain.billing.ImaniBill;
import com.imani.bill.pay.domain.payment.record.ImaniBillPayRecord;
import com.imani.bill.pay.domain.payment.PaymentStatusE;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author manyce400
 */
@Repository
public interface ImaniBillPayRecordRepository extends JpaRepository<ImaniBillPayRecord, Long> {

    @Query("Select  imaniBillPayRecord From ImaniBillPayRecord imaniBillPayRecord Where imaniBillPayRecord.imaniBill =?1")
    public List<ImaniBillPayRecord> findPaymentHistoryOnBill(ImaniBill imaniBill);

    @Query("Select  imaniBillPayRecord From ImaniBillPayRecord imaniBillPayRecord Where imaniBillPayRecord.imaniBill =?1 and imaniBillPayRecord.embeddedPayment.paymentStatusE =?2")
    public List<ImaniBillPayRecord> findPaymentHistoryOnBillByStatus(ImaniBill imaniBill, PaymentStatusE paymentStatusE);

//    @Query("Select  imaniBillPayHistory From ImaniBillPayHistory imaniBillPayHistory Where imaniBillPayHistory.userRecord =?1 and imaniBillPayHistory.embeddedPayment.paymentDate =?2")
//    public List<ImaniBillPayHistory> findPaymentHistoryByDate(UserRecord userRecord, DateTime paymentDate);
//
//    @Query("Select  imaniBillPayHistory From ImaniBillPayHistory imaniBillPayHistory Where imaniBillPayHistory.userRecord =?1 and imaniBillPayHistory.embeddedPayment.paymentDate >=?2  and imaniBillPayHistory.embeddedPayment.paymentDate <=?3")
//    public List<ImaniBillPayHistory> findPaymentHistoryByDateRange(UserRecord userRecord, DateTime startDate, DateTime endDate);
//
//    @Query("Select  imaniBillPayHistory From ImaniBillPayHistory imaniBillPayHistory Where imaniBillPayHistory.userRecord =?1 and imaniBillPayHistory.embeddedPayment.paymentStatusE =?2 and imaniBillPayHistory.embeddedPayment.paymentDate =?3")
//    public List<ImaniBillPayHistory> findPaymentHistoryByStatusAndDate(UserRecord userRecord, PaymentStatusE paymentStatusE, DateTime paymentDate);
//
//    @Query("Select  imaniBillPayHistory From ImaniBillPayHistory imaniBillPayHistory Where imaniBillPayHistory.userRecord =?1 and imaniBillPayHistory.embeddedPayment.paymentStatusE =?2 and imaniBillPayHistory.embeddedPayment.paymentDate >=?3 and imaniBillPayHistory.embeddedPayment.paymentDate <=?4")
//    public List<ImaniBillPayHistory> findPaymentHistoryByStatusAndDateRange(UserRecord userRecord, PaymentStatusE paymentStatusE, DateTime startDate, DateTime endDate);

}
