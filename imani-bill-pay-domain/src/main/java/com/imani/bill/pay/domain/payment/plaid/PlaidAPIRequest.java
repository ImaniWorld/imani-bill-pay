package com.imani.bill.pay.domain.payment.plaid;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Transient;

/**
 * @author manyce400
 */
@Embeddable
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PlaidAPIRequest {


    @JsonProperty("client_id")
    @Transient
    private String clientID;


    @JsonProperty("secret")
    @Transient
    private String secret;


    @JsonProperty("public_token")
    @Column(name="PublicToken", length = 300)
    private String publicToken;


    @JsonProperty("access_token")
    @Transient
    private String accessToken;


    @JsonProperty("account_id")
    @Column(name="AccountID", length = 300)
    private String accountID;


    public PlaidAPIRequest() {

    }

    public String getClientID() {
        return clientID;
    }

    public void setClientID(String clientID) {
        this.clientID = clientID;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getPublicToken() {
        return publicToken;
    }

    public void setPublicToken(String publicToken) {
        this.publicToken = publicToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccountID() {
        return accountID;
    }

    public void setAccountID(String accountID) {
        this.accountID = accountID;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("clientID", clientID)
                .append("secret", secret)
                .append("publicToken", publicToken)
                .append("accessToken", accessToken)
                .append("accountID", accountID)
                .toString();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private PlaidAPIRequest plaidAPIRequest = new PlaidAPIRequest();

        public Builder clientID(String clientID) {
            plaidAPIRequest.clientID = clientID;
            return this;
        }

        public Builder secret(String secret) {
            plaidAPIRequest.secret = secret;
            return this;
        }

        public Builder publicToken(String publicToken) {
            plaidAPIRequest.publicToken = publicToken;
            return this;
        }

        public Builder accessToken(String accessToken) {
            plaidAPIRequest.accessToken = accessToken;
            return this;
        }

        public Builder accountID(String accountID) {
            plaidAPIRequest.accountID = accountID;
            return this;
        }

        public PlaidAPIRequest build() {
            return plaidAPIRequest;
        }
    }
}
