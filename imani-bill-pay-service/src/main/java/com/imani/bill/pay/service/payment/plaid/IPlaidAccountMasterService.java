package com.imani.bill.pay.service.payment.plaid;

import com.imani.bill.pay.domain.payment.plaid.PlaidAccessTokenResponse;
import com.imani.bill.pay.domain.payment.plaid.StripeBankAccountResponse;
import com.imani.bill.pay.domain.user.UserRecord;

import java.util.Optional;

/**
 * @author manyce400
 */
public interface IPlaidAccountMasterService {

    // Post Account setup through Plaid Link API's use the given public token and account id to fully configure Plaid Bank Account
    public Optional<UserRecord> linkPlaidBankAcct(String plaidPublicToken, String plaidAccountID, UserRecord userRecord);

    // Create a Linked Stripe Bank Account for this given Plaid account.  Implementation needs to exchange public token for access token
    public Optional<StripeBankAccountResponse> createStripeAccount(String plaidPublicToken, String plaidAccountID);

    // Create a Linked Stripe Bank Account for this given Plaid account.
    public Optional<StripeBankAccountResponse> createStripeAccount(PlaidAccessTokenResponse plaidAccessTokenResponse, String plaidAccountID);

}
