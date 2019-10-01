package com.imani.bill.pay.service.payment;

import com.imani.bill.pay.domain.payment.ACHPaymentInfo;
import com.imani.bill.pay.domain.payment.Balance;
import com.imani.bill.pay.domain.user.UserRecord;

import java.util.Optional;

/**
 * @author manyce400
 */
public interface IPlaidAccountBalanceService {

    public Optional<Balance> getACHPaymentInfoBalances(UserRecord userRecord);

    public Optional<Balance> getACHPaymentInfoBalances(ACHPaymentInfo achPaymentInfo);

    public boolean availableBalanceCoversPayment(UserRecord userRecord, Double paymentAmnt);

    public boolean availableBalanceCoversPayment(ACHPaymentInfo achPaymentInfo, Double paymentAmnt);

}
