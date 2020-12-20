package com.imani.bill.pay.domain.gateway;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.imani.bill.pay.domain.execution.ExecutionResult;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.util.Assert;

/**
 * Super class of all Imani BillPay API Gateway response implementations
 *
 * @author manyce400
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class APIGatewayResponse {

    protected Object executionResult;

    protected HttpStatus httpStatus;

    protected String message;

    public APIGatewayResponse() {

    }

    public Object getExecutionResult() {
        return executionResult;
    }

    public void setExecutionResult(Object executionResult) {
        this.executionResult = executionResult;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("httpStatus", httpStatus)
                .append("message", message)
                .toString();
    }

    public static APIGatewayResponse fromExecutionResult(ExecutionResult executionResult) {
        Assert.notNull(executionResult, "ExecutionResult cannot be null");
        APIGatewayResponse apiGatewayResponse = new APIGatewayResponse();
        apiGatewayResponse.executionResult = executionResult;

        if(executionResult.isExecutionSuccessful()) {
            apiGatewayResponse.httpStatus = HttpStatus.OK;
        } else {
            if(executionResult.hasValidationAdvice()) {
                // We have validation advice, this will signify that there is something wrong with client request
                apiGatewayResponse.httpStatus = HttpStatus.BAD_REQUEST;
            } else if(executionResult.hasExecutionError()) {
                // We have validation errors, this will signify that there is something wrong with client request
                apiGatewayResponse.httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
                apiGatewayResponse.message = "A problem occurred while executing request. Contact Imani BillPay Support";
            }
        }

        return apiGatewayResponse;
    }
}
