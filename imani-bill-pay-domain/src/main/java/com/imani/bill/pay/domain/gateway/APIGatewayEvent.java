package com.imani.bill.pay.domain.gateway;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.joda.time.DateTime;

/**
 * @author manyce400
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class APIGatewayEvent {



    // Tracks the time that Gateway event got triggered, useful for debugging
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    protected DateTime eventTime;

    // Optional communication message for this gateway event
    protected String gatewayEventCommunication;

    protected APIGatewayEventStatusE apiGatewayEventStatusE;


    public APIGatewayEvent() {

    }

    public DateTime getEventTime() {
        return eventTime;
    }

    public void setEventTime(DateTime eventTime) {
        this.eventTime = eventTime;
    }

    public String getGatewayEventCommunication() {
        return gatewayEventCommunication;
    }

    public void setGatewayEventCommunication(String gatewayEventCommunication) {
        this.gatewayEventCommunication = gatewayEventCommunication;
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

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("eventTime", eventTime)
                .append("gatewayEventCommunication", gatewayEventCommunication)
                .append("apiGatewayEventStatusE", apiGatewayEventStatusE)
                .toString();
    }

    //    public static Builder builder() {
//        return new Builder();
//    }
//
//    public static class Builder {
//
//        private APIGatewayEvent apiGatewayEvent = new APIGatewayEvent();
//
//        public Builder eventTime(DateTime eventTime) {
//            apiGatewayEvent.eventTime = eventTime;
//            return this;
//        }
//
//        public Builder apiGatewayEventStatusE(APIGatewayEventStatusE apiGatewayEventStatusE) {
//            apiGatewayEvent.apiGatewayEventStatusE = apiGatewayEventStatusE;
//            return this;
//        }
//
//        public APIGatewayEvent build() {
//            return apiGatewayEvent;
//        }
//    }

}
