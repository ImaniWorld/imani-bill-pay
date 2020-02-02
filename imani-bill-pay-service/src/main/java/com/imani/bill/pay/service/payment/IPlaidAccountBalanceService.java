package com.imani.bill.pay.service.payment;

import com.imani.bill.pay.domain.payment.ACHPaymentInfo;
import com.imani.bill.pay.domain.payment.plaid.PlaidBankAcctBalance;
import com.imani.bill.pay.domain.user.UserRecord;

import java.util.Optional;

/**
 * @author manyce400
 */
public interface IPlaidAccountBalanceService {

    public Optional<PlaidBankAcctBalance> getACHPaymentInfoBalances(UserRecord userRecord);

    public Optional<PlaidBankAcctBalance> getACHPaymentInfoBalances(ACHPaymentInfo achPaymentInfo);

    public boolean availableBalanceCoversPayment(UserRecord userRecord, Double paymentAmnt);

    public boolean availableBalanceCoversPayment(ACHPaymentInfo achPaymentInfo, Double paymentAmnt);

}
