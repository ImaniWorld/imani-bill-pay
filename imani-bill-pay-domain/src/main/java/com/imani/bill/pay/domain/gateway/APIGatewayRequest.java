package com.imani.bill.pay.domain.gateway;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.imani.bill.pay.domain.user.UserRecord;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Super class of all Imani BillPay API Gateway request implementations
 *
 * @author manyce400
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class APIGatewayRequest {


    protected UserRecord onBehalfOf;

    protected UserRecord executingUser;

    public APIGatewayRequest() {

    }

    public UserRecord getOnBehalfOf() {
        return onBehalfOf;
    }

    public void setOnBehalfOf(UserRecord onBehalfOf) {
        this.onBehalfOf = onBehalfOf;
    }

    public UserRecord getExecutingUser() {
        return executingUser;
    }

    public void setExecutingUser(UserRecord executingUser) {
        this.executingUser = executingUser;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("onBehalfOf", onBehalfOf)
                .toString();
    }
}
