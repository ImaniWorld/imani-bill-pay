package com.imani.bill.pay.domain.gateway;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.ImmutableList;
import com.imani.bill.pay.domain.billing.BillPayFee;
import com.imani.bill.pay.domain.user.UserRecord;
import com.imani.bill.pay.domain.user.UserRecordLite;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

/**
 * Super class of all Imani BillPay API Gateway request implementations
 *
 * @author manyce400
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class APIGatewayRequest<O> {

    protected O requestObject;

    protected UserRecord onBehalfOf;

    protected UserRecordLite userRecordLite;

    private List<BillPayFee> billPayFees = new ArrayList<>();


    public APIGatewayRequest() {

    }

    // Full blown constructor.  Customized for object instantiation through JacksonMapper
    @JsonCreator
    public APIGatewayRequest(@JsonProperty("requestObject") O requestObject, @JsonProperty("onBehalfOf") UserRecord onBehalfOf) {
        this.requestObject = requestObject;
        this.onBehalfOf = onBehalfOf;
    }

    public O getRequestObject() {
        return requestObject;
    }

    public void setRequestObject(O requestObject) {
        this.requestObject = requestObject;
    }

    public UserRecord getOnBehalfOf() {
        return onBehalfOf;
    }

    public void setOnBehalfOf(UserRecord onBehalfOf) {
        this.onBehalfOf = onBehalfOf;
    }

    public UserRecordLite getUserRecordLite() {
        return userRecordLite;
    }

    public void setUserRecordLite(UserRecordLite userRecordLite) {
        this.userRecordLite = userRecordLite;
    }

    public List<BillPayFee> getBillPayFees() {
        return ImmutableList.copyOf(billPayFees);
    }

    public void addBillPayFee(BillPayFee billPayFee) {
        Assert.notNull(billPayFee, "BillPayFee cannot be null");
        billPayFees.add(billPayFee);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("requestObject", requestObject)
                .append("onBehalfOf", onBehalfOf)
                .append("userRecordLite", userRecordLite)
                .toString();
    }
    
}
