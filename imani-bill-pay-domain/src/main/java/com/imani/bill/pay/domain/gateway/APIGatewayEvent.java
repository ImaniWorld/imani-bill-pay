package com.imani.bill.pay.domain.gateway;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.imani.bill.pay.domain.user.UserRecord;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.joda.time.DateTime;

import java.util.Optional;

/**
 * @author manyce400
 * @param <I> Gateway event request and input object.
 * @param <O> Gateway event response and output object must extend GenericAPIGatewayResponse
 * @see GenericAPIGatewayResponse
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class APIGatewayEvent<I extends GenericAPIGatewayRequest, O extends GenericAPIGatewayResponse> {



    // Tracks the time that Gateway event got triggered, useful for debugging
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    protected DateTime eventTime;

    protected Optional<I> requestBody;

    protected Optional<O> responseBody;

    // UserRecord that initiated the gateway event
    protected Optional<UserRecord> eventUserRecord;


    public APIGatewayEvent() {
        eventTime = DateTime.now();
    }

    public DateTime getEventTime() {
        return eventTime;
    }

    public Optional<I> getRequestBody() {
        return requestBody;
    }

    public void setRequestBody(Optional<I> requestBody) {
        this.requestBody = requestBody;
    }

    public Optional<O> getResponseBody() {
        return responseBody;
    }

    public void setResponseBody(Optional<O> responseBody) {
        this.responseBody = responseBody;
    }

    public Optional<UserRecord> getEventUserRecord() {
        return eventUserRecord;
    }

    public void setEventUserRecord(Optional<UserRecord> eventUserRecord) {
        this.eventUserRecord = eventUserRecord;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("eventTime", eventTime)
                .append("requestBody", requestBody)
                .append("responseBody", responseBody)
                .append("eventUserRecord", eventUserRecord)
                .toString();
    }

    public static <I extends GenericAPIGatewayRequest, O extends GenericAPIGatewayResponse> APIGatewayEvent getSuccessGenericAPIGatewayResponse(UserRecord userRecord) {
        GenericAPIGatewayResponse genericAPIGatewayResponse = GenericAPIGatewayResponse.getSuccessGenericAPIGatewayResponse();
        APIGatewayEvent<I, O> apiGatewayEvent = APIGatewayEvent.builder()
                .responseBody(genericAPIGatewayResponse)
                .eventUserRecord(userRecord)
                .build();
        return apiGatewayEvent;
    }

    public static <I extends GenericAPIGatewayRequest, O extends GenericAPIGatewayResponse> APIGatewayEvent getSuccessGenericAPIGatewayResponse(O genericAPIGatewayResponse, UserRecord userRecord) {
        APIGatewayEvent<I, O> apiGatewayEvent = APIGatewayEvent.builder()
                .responseBody(genericAPIGatewayResponse)
                .eventUserRecord(userRecord)
                .build();
        return apiGatewayEvent;
    }

    public static <I extends GenericAPIGatewayRequest, O extends GenericAPIGatewayResponse> APIGatewayEvent getFailedGenericAPIGatewayResponse(String communication, UserRecord userRecord) {
        GenericAPIGatewayResponse genericAPIGatewayResponse = GenericAPIGatewayResponse.getFailureGenericAPIGatewayResponse(communication);
        APIGatewayEvent<I, O> apiGatewayEvent = APIGatewayEvent.builder()
                .responseBody(genericAPIGatewayResponse)
                .eventUserRecord(userRecord)
                .build();
        return apiGatewayEvent;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder<I extends GenericAPIGatewayRequest, O extends GenericAPIGatewayResponse> {

        private APIGatewayEvent<I, O> apiGatewayEvent = new APIGatewayEvent();

        public Builder requestBody(I requestBody) {
            apiGatewayEvent.requestBody = Optional.of(requestBody);
            return this;
        }

        public Builder responseBody(O responseBody) {
            apiGatewayEvent.responseBody = Optional.of(responseBody);
            return this;
        }

        public Builder eventUserRecord(UserRecord eventUserRecord) {
            apiGatewayEvent.eventUserRecord = Optional.of(eventUserRecord);
            return this;
        }

        public APIGatewayEvent build() {
            return apiGatewayEvent;
        }
    }

}
