package com.imani.bill.pay.service.payment.plaid;

import com.imani.bill.pay.domain.payment.plaid.AccessTokenResponse;
import com.imani.bill.pay.domain.payment.plaid.PlaidAPIRequest;
import com.imani.bill.pay.domain.payment.plaid.StripeBankAccountResponse;

import java.util.Optional;

/**
 * @author manyce400
 */
interface IPlaidAPIService {

    public Optional<AccessTokenResponse> exchangePublicTokenForAccess(PlaidAPIRequest plaidAPIRequest);

    public Optional<StripeBankAccountResponse> createStripeBankAccount(PlaidAPIRequest plaidAPIRequest);

}
