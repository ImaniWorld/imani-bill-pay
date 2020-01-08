package com.imani.bill.pay.service.payment.plaid;

import com.imani.bill.pay.domain.payment.plaid.PlaidAccessTokenResponse;
import com.imani.bill.pay.domain.payment.plaid.PlaidAPIRequest;
import com.imani.bill.pay.domain.payment.plaid.PlaidItemAccountsResponse;
import com.imani.bill.pay.domain.payment.plaid.StripeBankAccountResponse;

import java.util.Optional;

/**
 * @author manyce400
 */
public interface IPlaidAPIService {

    public Optional<StripeBankAccountResponse> createStripeBankAccount(PlaidAPIRequest plaidAPIRequest);

    public Optional<PlaidItemAccountsResponse> getPlaidItemAccounts(PlaidAPIRequest plaidAPIRequest);

    public Optional<PlaidAccessTokenResponse> exchangePublicTokenForAccess(String plaidPublicToken);

    public Optional<PlaidAccessTokenResponse> exchangePublicTokenForAccess(PlaidAPIRequest plaidAPIRequest);

}
