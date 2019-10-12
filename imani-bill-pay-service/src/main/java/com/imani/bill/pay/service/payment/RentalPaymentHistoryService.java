package com.imani.bill.pay.service.payment;

import com.imani.bill.pay.domain.payment.ACHPaymentInfo;
import com.imani.bill.pay.domain.payment.EmbeddedPayment;
import com.imani.bill.pay.domain.payment.PaymentStatusE;
import com.imani.bill.pay.domain.payment.RentalPaymentHistory;
import com.imani.bill.pay.domain.payment.repository.IACHPaymentInfoRepository;
import com.imani.bill.pay.domain.payment.repository.IRentalPaymentHistoryRepository;
import com.imani.bill.pay.domain.property.MonthlyRentalBill;
import com.imani.bill.pay.domain.property.MonthlyRentalBillExplained;
import com.imani.bill.pay.domain.user.UserRecord;
import com.imani.bill.pay.service.util.DateTimeUtil;
import com.imani.bill.pay.service.util.IDateTimeUtil;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

/**
 * @author manyce400
 */
@Service(RentalPaymentHistoryService.SPRING_BEAN)
public class RentalPaymentHistoryService implements IRentalPaymentHistoryService {


    @Autowired
    private IRentalPaymentHistoryRepository iRentalPaymentHistoryRepository;

    @Autowired
    private IACHPaymentInfoRepository iachPaymentInfoRepository;

    @Autowired
    @Qualifier(DateTimeUtil.SPRING_BEAN)
    private IDateTimeUtil iDateTimeUtil;

    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(RentalPaymentHistoryService.class);

    public static final String SPRING_BEAN = "com.imani.bill.pay.service.payment.RentalPaymentHistoryService";


    @Transactional
    @Override
    public void save(RentalPaymentHistory rentalPaymentHistory) {
        Assert.notNull(rentalPaymentHistory, "rentalPaymentHistory cannot be null");
        LOGGER.debug("Saving rentalPaymentHistory: {}", rentalPaymentHistory);
        iRentalPaymentHistoryRepository.save(rentalPaymentHistory);
    }


    @Transactional
    @Override
    public void createRentalPaymentHistory(MonthlyRentalBill monthlyRentalBill, MonthlyRentalBillExplained monthlyRentalBillExplained) {
        Assert.notNull(monthlyRentalBillExplained, "monthlyRentalBillExplained cannot be null");
        Assert.notNull(monthlyRentalBill, "monthlyRentalBill cannot be null");

        LOGGER.debug("Creating RentalPayment history for payment on monthlyRentalBillExplained:=> {}", monthlyRentalBillExplained);

        ACHPaymentInfo achPaymentInfo = iachPaymentInfoRepository.findUserACHPaymentInfo(monthlyRentalBillExplained.getUserResidence().getUserRecord());

        // Create embedded payment information
        EmbeddedPayment embeddedPayment = EmbeddedPayment.builder()
                .paymentAmount(monthlyRentalBillExplained.getAmtBeingPaid())
                .paymentDate(DateTime.now())
                .paymentStatusE(PaymentStatusE.Pending)
                .currency("USD")
                .build();

        // Create RentalPaymentHistory
        RentalPaymentHistory rentalPaymentHistory = RentalPaymentHistory.builder()
                .achPaymentInfo(achPaymentInfo)
                .embeddedPayment(embeddedPayment)
                .monthlyRentalBill(monthlyRentalBill)
                .userRecord(monthlyRentalBillExplained.getUserResidence().getUserRecord())
                .build();

        // Save the RentalPaymentHistory
        save(rentalPaymentHistory);
    }

    @Override
    public Optional<List<RentalPaymentHistory>> findUserRentalPaymentForCurrentMonth(UserRecord userRecord) {
        Assert.notNull(userRecord, "UserRecord cannot be empty");

        // Current Month DateTime should start at the start of the month.  All bills are due 1'st day of month
        DateTime now = DateTime.now();
        DateTime dateTimeAtStartOfMonth = iDateTimeUtil.getDateTimeAtStartOfMonth(now);
        DateTime dateTimeAtEndOfMonth = iDateTimeUtil.getDateTimeAtEndOfMonth(now);

        LOGGER.info("Finding all rental payments made for user:=> {} dateTimeAtStartOfMonth:=> {}  dateTimeAtEndOfMonth:=> {}", userRecord.getEmbeddedContactInfo().getEmail(), dateTimeAtStartOfMonth, dateTimeAtEndOfMonth);

        List<RentalPaymentHistory> rentalPaymentHistoryList = iRentalPaymentHistoryRepository.findUserRentalPaymentHistoryByDateRange(userRecord, dateTimeAtStartOfMonth, dateTimeAtEndOfMonth);

        if(rentalPaymentHistoryList.size() > 0) {
            return Optional.of(rentalPaymentHistoryList);
        }

        return Optional.empty();
    }

    @Override
    public boolean hasPendingUserRentalPaymentForCurrentMonth(UserRecord userRecord) {
        Optional<List<RentalPaymentHistory>> pendingRentalPaymentHistoryList = findPendingUserRentalPaymentForCurrentMonth(userRecord);
        return pendingRentalPaymentHistoryList.isPresent();
    }

    @Override
    public Optional<List<RentalPaymentHistory>> findPendingUserRentalPaymentForCurrentMonth(UserRecord userRecord) {
        Assert.notNull(userRecord, "UserRecord cannot be empty");

        // Current Month DateTime should start at the start of the month.  All bills are due 1'st day of month
        DateTime now = DateTime.now();
        DateTime dateTimeAtStartOfMonth = iDateTimeUtil.getDateTimeAtStartOfMonth(now);
        DateTime dateTimeAtEndOfMonth = iDateTimeUtil.getDateTimeAtEndOfMonth(now);

        LOGGER.info("Finding all pending rental payments made for user:=> {} dateTimeAtStartOfMonth:=> {}  dateTimeAtEndOfMonth:=> {}", userRecord.getEmbeddedContactInfo().getEmail(), dateTimeAtStartOfMonth, dateTimeAtEndOfMonth);

        List<RentalPaymentHistory> rentalPaymentHistoryList = iRentalPaymentHistoryRepository.findUserRentalPaymentHistoryByStatusAndDateRange(userRecord, PaymentStatusE.Pending, dateTimeAtStartOfMonth, dateTimeAtEndOfMonth);

        if(rentalPaymentHistoryList.size() > 0) {
            return Optional.of(rentalPaymentHistoryList);
        }

        return Optional.empty();
    }
}
