package com.imani.bill.pay.domain.payment.plaid;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * @author manyce400
 */
public class StripeBankAccountResponse extends PlaidAPIResponse {


    @JsonProperty("stripe_bank_account_token")
    private String stripeBankAcctToken;


    public StripeBankAccountResponse() {
        super();
    }

    public String getStripeBankAcctToken() {
        return stripeBankAcctToken;
    }

    public void setStripeBankAcctToken(String stripeBankAcctToken) {
        this.stripeBankAcctToken = stripeBankAcctToken;
    }

    @Override
    public String toString() {
        String sstring = super.toString();
        return new ToStringBuilder(this)
                .append(sstring)
                .append("stripeBankAcctToken", stripeBankAcctToken)
                .toString();
    }
}
