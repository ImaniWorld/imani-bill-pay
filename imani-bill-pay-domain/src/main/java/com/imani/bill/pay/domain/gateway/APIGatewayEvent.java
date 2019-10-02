package com.imani.bill.pay.domain.gateway;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.joda.time.DateTime;

/**
 * @author manyce400
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class APIGatewayEvent {



    // Tracks the time that Gateway event got triggered, useful for debugging
    protected DateTime eventTime;

    protected APIGatewayEventStatusE apiGatewayEventStatusE;


    public APIGatewayEvent() {

    }

    public DateTime getEventTime() {
        return eventTime;
    }

    public void setEventTime(DateTime eventTime) {
        this.eventTime = eventTime;
    }

    public void addEventTime() {
        this.eventTime = DateTime.now();
    }

    public APIGatewayEventStatusE getApiGatewayEventStatusE() {
        return apiGatewayEventStatusE;
    }

    public void setApiGatewayEventStatusE(APIGatewayEventStatusE apiGatewayEventStatusE) {
        this.apiGatewayEventStatusE = apiGatewayEventStatusE;
    }

}
