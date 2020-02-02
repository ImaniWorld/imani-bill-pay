package com.imani.bill.pay.service.payment.plaid;

import com.imani.bill.pay.domain.payment.ACHPaymentInfo;
import com.imani.bill.pay.domain.user.UserRecord;

import java.util.Optional;

/**
 * @author manyce400
 */
public interface IPlaidAccountMasterService {

    // Post Account setup through Plaid Link API's use the given public token and account id to fully configure Plaid Bank Account
    public Optional<UserRecord> linkPlaidBankAcct(String plaidPublicToken, String plaidAccountID, UserRecord userRecord);

    // Create a Linked Stripe Bank Account for this given Plaid account.  Plaid account should have already been linked with available access token
    public Optional<ACHPaymentInfo> createStripeAcctForPrimaryPlaidAcct(UserRecord userRecord);

}
