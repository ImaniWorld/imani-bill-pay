package com.imani.bill.pay.domain.payment.plaid;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * @author manyce400
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PlaidAccessTokenResponse extends PlaidAPIResponse {


    @JsonProperty("access_token")
    private String accessToken;


    public PlaidAccessTokenResponse() {
        super();
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }


    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("accessToken", accessToken)
                .append("displayMessage", displayMessage)
                .append("errorCode", errorCode)
                .append("errorType", errorType)
                .append("errorMessage", errorMessage)
                .append("requestID", requestID)
                .append("suggestedAction", suggestedAction)
                .toString();
    }
}
