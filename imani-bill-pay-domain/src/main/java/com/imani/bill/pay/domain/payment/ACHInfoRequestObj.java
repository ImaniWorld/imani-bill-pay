package com.imani.bill.pay.domain.payment;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * @author manyce400
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ACHInfoRequestObj {


    @JsonProperty("client_id")
    private String clientID;

    private String secret;

    @JsonProperty("access_token")
    private String accessToken;

//    @JsonProperty("options")
//    private ACHInfoAcctOptions achInfoAcctOptions = new ACHInfoAcctOptions();


    public ACHInfoRequestObj() {

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

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

//    public ACHInfoAcctOptions getAchInfoAcctOptions() {
//        return achInfoAcctOptions;
//    }


    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("clientID", clientID)
                .append("secret", secret)
                .append("accessToken", accessToken)
//                .append("achInfoAcctOptions", achInfoAcctOptions)
                .toString();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private ACHInfoRequestObj achInfoRequestObj = new ACHInfoRequestObj();

        public Builder clientID(String clientID) {
            achInfoRequestObj.clientID = clientID;
            return this;
        }

        public Builder secret(String secret) {
            achInfoRequestObj.secret = secret;
            return this;
        }

        public Builder accessToken(String accessToken) {
            achInfoRequestObj.accessToken = accessToken;
            return this;
        }

//        public Builder accountID(String accountID) {
//            achInfoRequestObj.achInfoAcctOptions.addAccountID(accountID);
//            return this;
//        }

        public ACHInfoRequestObj build() {
            return achInfoRequestObj;
        }
    }

}