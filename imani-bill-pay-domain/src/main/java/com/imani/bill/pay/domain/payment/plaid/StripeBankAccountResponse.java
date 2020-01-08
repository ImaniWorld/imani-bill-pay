package com.imani.bill.pay.domain.payment.plaid;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * @author manyce400
 */
public class StripeBankAccountResponse extends PlaidAPIResponse {


    @JsonProperty("stripe_bank_account_token")
    private String stripeBankAcctToken;

    // Access Token is critical for accessing details about this Plaid Stripe Bank account and should be persisted
    @JsonIgnore
    private String plaidAccessToken;


    public StripeBankAccountResponse() {
        super();
    }

    public String getStripeBankAcctToken() {
        return stripeBankAcctToken;
    }

    public void setStripeBankAcctToken(String stripeBankAcctToken) {
        this.stripeBankAcctToken = stripeBankAcctToken;
    }

    public String getPlaidAccessToken() {
        return plaidAccessToken;
    }

    public void setPlaidAccessToken(String plaidAccessToken) {
        this.plaidAccessToken = plaidAccessToken;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("stripeBankAcctToken", stripeBankAcctToken)
                .append("plaidAccessToken", plaidAccessToken)
                .toString();
    }
}
