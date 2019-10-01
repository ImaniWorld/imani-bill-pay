package com.imani.bill.pay.service.property;

import com.imani.bill.pay.domain.payment.Balance;
import com.imani.bill.pay.domain.payment.PaymentStatusE;
import com.imani.bill.pay.domain.payment.repository.IACHPaymentInfoRepository;
import com.imani.bill.pay.domain.property.MonthlyRentalBill;
import com.imani.bill.pay.domain.property.MonthlyRentalBillExplained;
import com.imani.bill.pay.domain.property.RentalBillPayResult;
import com.imani.bill.pay.domain.property.repository.IMonthlyRentalBillRepository;
import com.imani.bill.pay.domain.user.UserRecord;
import com.imani.bill.pay.domain.user.UserResidence;
import com.imani.bill.pay.domain.user.repository.IUserRecordRepository;
import com.imani.bill.pay.service.payment.IRentalPaymentHistoryService;
import com.imani.bill.pay.service.payment.RentalPaymentHistoryService;
import org.apache.commons.math3.stat.descriptive.summary.Sum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.transaction.Transactional;
import java.util.Optional;

/**
 * @author manyce400
 */
@Service(MonthlyRentalBillPayService.SPRING_BEAN)
public class MonthlyRentalBillPayService implements IMonthlyRentalBillPayService {



    @Autowired
    private IUserRecordRepository iUserRecordRepository;

    @Autowired
    private IMonthlyRentalBillRepository iMonthlyRentalBillRepository;

    @Autowired
    private IACHPaymentInfoRepository iachPaymentInfoRepository;

    @Autowired
    @Qualifier(MonthlyRentalBillDescService.SPRING_BEAN)
    private IMonthlyRentalBillDescService iMonthlyRentalBillDescService;

    @Autowired
    @Qualifier(RentalPaymentHistoryService.SPRING_BEAN)
    private IRentalPaymentHistoryService iRentalPaymentHistoryService;

    @Autowired
    @Qualifier(PlaidAccountBalanceService.SPRING_BEAN)
    private IPlaidAccountBalanceService iPlaidAccountBalanceService;


    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(MonthlyRentalBillPayService.class);

    public static final String SPRING_BEAN = "com.imani.cash.domain.service.property.billing.MonthlyRentalBillPayService";

    @Transactional
    @Override
    public RentalBillPayResult payMonthlyRental(MonthlyRentalBillExplained monthlyRentalBillExplained) {
        Assert.notNull(monthlyRentalBillExplained, "monthlyRentalBillExplained cannot be null");

        UserResidence userResidence = monthlyRentalBillExplained.getUserResidence();
        UserRecord userRecord = userResidence.getUserRecord();

        // Find the current user's monthly rental bill
        MonthlyRentalBill jpaMonthlyRentalBill = iMonthlyRentalBillRepository.getUserMonthlyRentalBill(userRecord, monthlyRentalBillExplained.getRentalMonth());

        // IF there are any pending payments that have not yet posted for this current month then dont allow the user to submit a new payment
        if(iRentalPaymentHistoryService.hasPendingUserRentalPaymentForCurrentMonth(userRecord)) {
            LOGGER.warn("User: {} already has pending payment for current month.  Payment should post before new payments can be submitted", userRecord.getEmbeddedContactInfo().getEmail());
            return getRentalBillPayResultOnPendingPayments(jpaMonthlyRentalBill, monthlyRentalBillExplained);
        }


        if (jpaMonthlyRentalBill != null && !jpaMonthlyRentalBill.isBillClosed()) {
            // Refresh all the user details from persistent store
            UserRecord jpaUserRecord = iUserRecordRepository.findByUserEmail(userRecord.getEmbeddedContactInfo().getEmail());
            LOGGER.info("Executing rental payment request for user: {}", jpaUserRecord.getEmbeddedContactInfo().getEmail());

            // Prevent double payment, make sure that this bill has not been fully paid or is currently closed.
            Double computedTotalAmtDue = iMonthlyRentalBillDescService.calculateTotalAmountDue(jpaMonthlyRentalBill, userResidence.getUserResidencePropertyServices());
            boolean canMakePaymentOnBill = canMakePaymentOnBill(jpaMonthlyRentalBill, computedTotalAmtDue, monthlyRentalBillExplained.getAmtBeingPaid());

            if(canMakePaymentOnBill) {
                // Now validate real-time using Plaid that this user has enough funds in their available account balance to make this payment and execute.
                Optional<Balance> balance = iPlaidAccountBalanceService.getACHPaymentInfoBalances(userRecord);

                if(balance.isPresent() && !balance.get().hasAvailableBalanceForPayment(monthlyRentalBillExplained.getAmtBeingPaid())) {
                    return getRentalBillPayResultOnInsufficientFunds(balance, monthlyRentalBillExplained);
                } else {
                    // TODO in this scenario, if we cant find balance information we process payment.  Evaluate if this is the best step to proceed.
                    // We can now safely post this payment to user's bank account since balance has been verified and return a completed payment result
                    LOGGER.info("Submitting ACH bill payment for User:=> {} and Rental Month:=> {}", userRecord.getEmbeddedContactInfo().getEmail(), monthlyRentalBillExplained.getRentalMonth());
                }
            }
        }

        return getRentalBillPayResultOnIncorrectBill(jpaMonthlyRentalBill, monthlyRentalBillExplained);
    }


    boolean canMakePaymentOnBill(MonthlyRentalBill jpaMonthlyRentalBill,  Double computedTotalAmtDue, Double amtBeingPaid) {
        if(!jpaMonthlyRentalBill.isBillClosed()) {
            // Make sure that the amount currently paid wont end up exceeding the total amount to be paid considering what has been paid already
            double totalPaidPostPayment = calculateTotalAmountPaidAfterNewPaymentIsComplete(jpaMonthlyRentalBill, amtBeingPaid);

            if(totalPaidPostPayment > computedTotalAmtDue.doubleValue()) {
                LOGGER.warn("New payment being made in amount:=> {} will cause the total payment to exceed monthly charge, denying this payment request", amtBeingPaid);
                return false;
            } else {
                LOGGER.info("New payment being made on bill in amount:=> {} passes amount validation", amtBeingPaid);
                return true;
            }
        }

        LOGGER.warn("Received a monthly rental bill which is already closed out: {}", jpaMonthlyRentalBill);
        return false;
    }

    double calculateTotalAmountPaidAfterNewPaymentIsComplete(MonthlyRentalBill jpaMonthlyRentalBill, Double amtBeingPaid) {
       Sum sum = new Sum();
       double currentAmtAlreadyPaid = jpaMonthlyRentalBill.getAmountPaid();
       sum.increment(amtBeingPaid);
       sum.increment(currentAmtAlreadyPaid);
       return sum.getResult();
    }

    RentalBillPayResult getRentalBillPayResultOnPendingPayments(MonthlyRentalBill jpaMonthlyRentalBill, MonthlyRentalBillExplained monthlyRentalBillExplained) {
        UserRecord userRecord = monthlyRentalBillExplained.getUserResidence().getUserRecord();
        LOGGER.warn("Cannot process payment. Pending payments found for User:=> {} and RentalMonth:=> {}", userRecord.getEmbeddedContactInfo().getEmail(), monthlyRentalBillExplained.getRentalMonth());
        RentalBillPayResult rentalBillPayResult = RentalBillPayResult.builder()
                .paymentStatusE(PaymentStatusE.CannotProcess)
                .paymentMessage("Cannot process payment.  You currently have a pending payment which has not yet posted.  Payments can be made after pending payments have cleared.")
                .monthlyRentalBillExplained(monthlyRentalBillExplained)
                .build();

        return rentalBillPayResult;
    }

    RentalBillPayResult getRentalBillPayResultOnIncorrectBill(MonthlyRentalBill jpaMonthlyRentalBill, MonthlyRentalBillExplained monthlyRentalBillExplained) {
        UserRecord userRecord = monthlyRentalBillExplained.getUserResidence().getUserRecord();
        RentalBillPayResult rentalBillPayResult = null;

        if(jpaMonthlyRentalBill == null) {
            // Log this as a warning, user tried to make payment for a rental month which we haven't already created a bill for.
            LOGGER.warn("Cannot process payment. No MonthlyRentalBill was found User:=> {} and RentalMonth:=> {}", userRecord.getEmbeddedContactInfo().getEmail(), monthlyRentalBillExplained.getRentalMonth());
            rentalBillPayResult = RentalBillPayResult.builder()
                    .paymentStatusE(PaymentStatusE.CannotProcess)
                    .paymentMessage("Your payment could not be processed, please reach out to Customer Support.")
                    .monthlyRentalBillExplained(monthlyRentalBillExplained)
                    .build();
        } else if(jpaMonthlyRentalBill.isBillClosed()) {
            // Rental bill is already closed and fully paid, log this
            LOGGER.warn("Cannot process payment. MonthlyRentalBill for User:=> {} and RentalMonth:=> {} is already currently closed off", userRecord.getEmbeddedContactInfo().getEmail(), monthlyRentalBillExplained.getRentalMonth());
            rentalBillPayResult = RentalBillPayResult.builder()
                    .paymentStatusE(PaymentStatusE.CannotProcess)
                    .paymentMessage("Your payment could not be processed, please reach out to Customer Support.")
                    .monthlyRentalBillExplained(monthlyRentalBillExplained)
                    .build();
        }

        return rentalBillPayResult;
    }


    RentalBillPayResult getRentalBillPayResultOnInsufficientFunds(Optional<Balance> balance, MonthlyRentalBillExplained monthlyRentalBillExplained) {
        UserRecord userRecord = monthlyRentalBillExplained.getUserResidence().getUserRecord();

        LOGGER.warn("Cannot process payment. Insufficient funds found User:=> {} and RentalMonth:=> {}", userRecord.getEmbeddedContactInfo().getEmail(), monthlyRentalBillExplained.getRentalMonth());

        StringBuffer sb = new StringBuffer()
                .append("Insufficient Available Funds:  Your available Bank Acct Balance => ").append(balance.get().getAvailable())
                .append(" cannot cover payment amount => ").append(monthlyRentalBillExplained.getAmtBeingPaid());

        RentalBillPayResult rentalBillPayResult = RentalBillPayResult.builder()
                .paymentStatusE(PaymentStatusE.InsufficientFunds)
                .paymentMessage(sb.toString())
                .monthlyRentalBillExplained(monthlyRentalBillExplained)
                .build();
        return rentalBillPayResult;

    }

}
