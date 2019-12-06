package com.imani.bill.pay.domain.payment.plaid;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.util.StringUtils;

/**
 * @author manyce400
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PlaidAPIResponse {


    @JsonProperty("request_id")
    private String requestID;

    @JsonProperty("item_id")
    private String itemID;

    @JsonProperty("error_type")
    private String errorType;

    @JsonProperty("error_code")
    private String errorCode;

    @JsonProperty("error_message")
    private String errorMessage;

    @JsonProperty("error_message")
    private String displayMessage;


    public PlaidAPIResponse() {

    }

    public String getRequestID() {
        return requestID;
    }

    public void setRequestID(String requestID) {
        this.requestID = requestID;
    }

    public String getItemID() {
        return itemID;
    }

    public void setItemID(String itemID) {
        this.itemID = itemID;
    }

    public String getErrorType() {
        return errorType;
    }

    public void setErrorType(String errorType) {
        this.errorType = errorType;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getDisplayMessage() {
        return displayMessage;
    }

    public void setDisplayMessage(String displayMessage) {
        this.displayMessage = displayMessage;
    }

    public boolean hasError() {
        if(!StringUtils.isEmpty(errorType)
                || !StringUtils.isEmpty(errorCode)
                || !StringUtils.isEmpty(errorMessage)
                || !StringUtils.isEmpty(displayMessage)) {
            return true;
        }

        return false;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("requestID", requestID)
                .append("itemID", itemID)
                .append("errorType", errorType)
                .append("errorCode", errorCode)
                .append("errorMessage", errorMessage)
                .append("displayMessage", displayMessage)
                .toString();
    }
}
