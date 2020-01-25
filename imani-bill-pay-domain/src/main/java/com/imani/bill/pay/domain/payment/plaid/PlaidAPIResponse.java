package com.imani.bill.pay.domain.payment.plaid;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * @author manyce400
 */
@Embeddable
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PlaidAPIResponse {


    @JsonProperty("request_id")
    @Column(name="RequestID", length = 300)
    protected String requestID;


    @JsonProperty("item_id")
    @Column(name="ItemID", length = 300)
    protected String itemID;


    @JsonProperty("error_type")
    @Column(name="ErrorType", length = 300)
    protected String errorType;


    @JsonProperty("error_code")
    @Column(name="ErrorCode", length = 300)
    protected String errorCode;


    @JsonProperty("error_message")
    @Column(name="ErrorMessage", length = 300)
    protected String errorMessage;


    @JsonProperty("display_message")
    @Column(name="DisplayMessage", length = 300)
    protected String displayMessage;


    @JsonProperty("suggested_action")
    @Column(name="SuggestedAction", length = 300)
    protected String suggestedAction;


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

    public String getSuggestedAction() {
        return suggestedAction;
    }

    public void setSuggestedAction(String suggestedAction) {
        this.suggestedAction = suggestedAction;
    }

    public void from(PlaidAPIResponse plaidAPIResponse) {
        Assert.notNull(plaidAPIResponse, "PlaidAPIResponse cannot be null");
        setDisplayMessage(plaidAPIResponse.getDisplayMessage());
        setErrorCode(plaidAPIResponse.getErrorCode());
        setErrorMessage(plaidAPIResponse.getErrorMessage());
        setErrorType(plaidAPIResponse.getErrorType());
        setRequestID(plaidAPIResponse.getRequestID());
        setSuggestedAction(plaidAPIResponse.getSuggestedAction());
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
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("requestID", requestID)
                .append("itemID", itemID)
                .append("errorType", errorType)
                .append("errorCode", errorCode)
                .append("errorMessage", errorMessage)
                .append("displayMessage", displayMessage)
                .append("suggestedAction", suggestedAction)
                .toString();
    }
}
