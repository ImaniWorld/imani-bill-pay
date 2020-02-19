package com.imani.bill.pay.domain.gateway;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.imani.bill.pay.domain.contact.ContactTypeE;
import com.imani.bill.pay.domain.contact.EmbeddedContactInfo;
import com.imani.bill.pay.domain.property.PropertyManager;
import com.imani.bill.pay.domain.user.UserRecord;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Super class of all Imani BillPay API Gateway request implementations
 *
 * @author manyce400
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class APIGatewayRequest<O> {



    protected O requestObject;

    // User for which this request is being executed.
    protected UserRecord onBehalfOf;

    // This is an optional field to track the user requesting this execution.
    // IF its the same as the onBehalfOf then this should not be set.
    protected UserRecord requestedBy;


    public APIGatewayRequest() {

    }

    // Full blown constructor.  Customized for object instantiation through JacksonMapper
    @JsonCreator
    public APIGatewayRequest(@JsonProperty("requestObject") O requestObject, @JsonProperty("onBehalfOf") UserRecord onBehalfOf, @JsonProperty("requestedBy") UserRecord requestedBy) {
        this.requestObject = requestObject;
        this.onBehalfOf = onBehalfOf;
        this.requestedBy = requestedBy;
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

    public UserRecord getRequestedBy() {
        return requestedBy;
    }

    public void setRequestedBy(UserRecord requestedBy) {
        this.requestedBy = requestedBy;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("requestObject", requestObject)
                .append("onBehalfOf", onBehalfOf)
                .append("requestedBy", requestedBy)
                .toString();
    }


    public static void main(String[] args) {
        ObjectMapper objectMapper = new ObjectMapper();
        APIGatewayRequest<PropertyManager> propertyRequest = new APIGatewayRequest<PropertyManager>();

        EmbeddedContactInfo embeddedContactInfo = EmbeddedContactInfo.builder()
                .email("sales@tencentproperties.com")
                .mobilePhone(2123458987L)
                .phone(2123458987L)
                .preferredContactType(ContactTypeE.Email)
                .build();

        PropertyManager propertyManager = PropertyManager.builder()
                .name("Tencent Property Holdings")
                .embeddedContactInfo(embeddedContactInfo)
                .build();
        propertyRequest.setRequestObject(propertyManager);

        try {
            String s = objectMapper.writeValueAsString(propertyRequest);
            System.out.println("JSON String = " + s);
            
            // Convert back to Object
            APIGatewayRequest<PropertyManager> newObj =  objectMapper.readValue(s, APIGatewayRequest.class);
            //System.out.println("newObj = " + newObj);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}
