package com.imani.bill.pay.domain.payment.plaid;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * @author manyce400
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PlaidItemDetail {


    @JsonProperty("item")
    private PlaidItem plaidItem;

    @JsonProperty("request_id")
    protected String requestID;

    @JsonProperty("status")
    public Status status;

    public PlaidItemDetail() {

    }

    public PlaidItem getPlaidItem() {
        return plaidItem;
    }

    public void setPlaidItem(PlaidItem plaidItem) {
        this.plaidItem = plaidItem;
    }

    public String getRequestID() {
        return requestID;
    }

    public void setRequestID(String requestID) {
        this.requestID = requestID;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("plaidItemInfo", plaidItem)
                .append("requestID", requestID)
                .append("status", status)
                .toString();
    }

    public static class Status {

        private String lastWebhook;

        public Status() {

        }

        public String getLastWebhook() {
            return lastWebhook;
        }

        public void setLastWebhook(String lastWebhook) {
            this.lastWebhook = lastWebhook;
        }
    }

}