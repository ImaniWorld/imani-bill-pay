package com.imani.bill.pay.domain.payment.plaid.gateway;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.imani.bill.pay.domain.gateway.GenericAPIGatewayRequest;
import com.imani.bill.pay.domain.user.UserRecord;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * @author manyce400
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PlaidAcctLinkRequest extends GenericAPIGatewayRequest {


    private String plaidPublicToken;

    private String plaidAccountID;

    public PlaidAcctLinkRequest() {

    }

    public PlaidAcctLinkRequest(UserRecord execUserRecord, String plaidPublicToken, String plaidAccountID) {
        super(execUserRecord);
        this.plaidPublicToken = plaidPublicToken;
        this.plaidAccountID = plaidAccountID;
    }

    public String getPlaidPublicToken() {
        return plaidPublicToken;
    }

    public void setPlaidPublicToken(String plaidPublicToken) {
        this.plaidPublicToken = plaidPublicToken;
    }

    public String getPlaidAccountID() {
        return plaidAccountID;
    }

    public void setPlaidAccountID(String plaidAccountID) {
        this.plaidAccountID = plaidAccountID;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("plaidPublicToken", plaidPublicToken)
                .append("plaidAccountID", plaidAccountID)
                .toString();
    }
}
