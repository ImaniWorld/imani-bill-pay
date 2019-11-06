package com.imani.bill.pay.domain.gateway;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * @author manyce400
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GenericAPIGatewayResponse {


    protected String communication;

    protected APIGatewayEventStatusE apiGatewayEventStatusE;

    public GenericAPIGatewayResponse() {

    }

    public GenericAPIGatewayResponse(APIGatewayEventStatusE apiGatewayEventStatusE) {
        this.apiGatewayEventStatusE = apiGatewayEventStatusE;
    }

    public GenericAPIGatewayResponse(String communication, APIGatewayEventStatusE apiGatewayEventStatusE) {
        this.communication = communication;
        this.apiGatewayEventStatusE = apiGatewayEventStatusE;
    }

    public String getCommunication() {
        return communication;
    }

    public void setCommunication(String communication) {
        this.communication = communication;
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
                .append("communication", communication)
                .append("apiGatewayEventStatusE", apiGatewayEventStatusE)
                .toString();
    }

    public static GenericAPIGatewayResponse getSuccessGenericAPIGatewayResponse() {
        return new GenericAPIGatewayResponse(APIGatewayEventStatusE.Success);
    }

    public static GenericAPIGatewayResponse getInvalidRequestGenericAPIGatewayResponse(String communication) {
        return new GenericAPIGatewayResponse(communication, APIGatewayEventStatusE.InvalidRequest);
    }

    public static GenericAPIGatewayResponse getFailureGenericAPIGatewayResponse(String communication) {
        return new GenericAPIGatewayResponse(communication, APIGatewayEventStatusE.Success);
    }
}
