package com.imani.bill.pay.domain.payment.plaid;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * @author manyce400
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
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
        return new ToStringBuilder(this)
                .append("stripeBankAcctToken", stripeBankAcctToken)
                .append("requestID", requestID)
                .append("itemID", itemID)
                .append("errorType", errorType)
                .append("errorCode", errorCode)
                .append("errorMessage", errorMessage)
                .append("displayMessage", displayMessage)
                .append("suggestedAction", suggestedAction)
                .toString();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private StripeBankAccountResponse stripeBankAccountResponse = new StripeBankAccountResponse();

        public Builder stripeBankAcctToken(String stripeBankAcctToken) {
            stripeBankAccountResponse.stripeBankAcctToken = stripeBankAcctToken;
            return this;
        }

        public StripeBankAccountResponse build() {
            return stripeBankAccountResponse;
        }
    }

}
