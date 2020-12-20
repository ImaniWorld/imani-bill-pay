package com.imani.bill.pay.domain.payment.plaid;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.util.StringUtils;

import javax.persistence.*;

/**
 * @author manyce400
 */
@Embeddable
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PlaidAPIError {


    @JsonProperty("error_type")
    @Enumerated(EnumType.STRING)
    @Column(name="PlaidErrorType", length = 300)
    protected PlaidErrorTypeE errorType;

    @JsonProperty("error_code")
    @Enumerated(EnumType.STRING)
    @Column(name="PlaidErrorCode", length = 300)
    protected PlaidErrorCodeE errorCode;

    @JsonProperty("error_message")
    @Column(name="PlaidErrorMessage", length = 500)
    protected String errorMessage;

    @Transient
    @JsonProperty("display_message")
    protected String displayMessage;



    public PlaidAPIError() {

    }

    public PlaidErrorTypeE getErrorType() {
        return errorType;
    }

    public void setErrorType(PlaidErrorTypeE errorType) {
        this.errorType = errorType;
    }

    public PlaidErrorCodeE getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(PlaidErrorCodeE errorCode) {
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
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("errorType", errorType)
                .append("errorCode", errorCode)
                .append("errorMessage", errorMessage)
                .append("displayMessage", displayMessage)
                .toString();
    }

}
