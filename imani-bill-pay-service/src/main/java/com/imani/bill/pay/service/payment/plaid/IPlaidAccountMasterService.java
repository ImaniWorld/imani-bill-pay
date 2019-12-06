package com.imani.bill.pay.service.payment.plaid;

import com.imani.bill.pay.domain.payment.plaid.StripeBankAccountResponse;

import java.util.Optional;

/**
 * @author manyce400
 */
public interface IPlaidAccountMasterService {

    public Optional<StripeBankAccountResponse> createStripeAccount(String plaidPublicToken, String plaidAccountID);

}
