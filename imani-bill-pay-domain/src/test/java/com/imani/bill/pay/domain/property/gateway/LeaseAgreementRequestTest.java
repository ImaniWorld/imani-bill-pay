package com.imani.bill.pay.domain.property.gateway;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.imani.bill.pay.domain.gateway.APIGatewayEvent;
import com.imani.bill.pay.domain.gateway.GenericAPIGatewayResponse;
import com.imani.bill.pay.domain.mock.IMockApartmentTestBuilder;
import com.imani.bill.pay.domain.mock.IMockUserRecordTestBuilder;
import com.imani.bill.pay.domain.mock.IPropertyManagerTestBuilder;
import com.imani.bill.pay.domain.mock.MockObjectMapper;
import com.imani.bill.pay.domain.property.Apartment;
import com.imani.bill.pay.domain.property.LeaseAgreementTypeE;
import com.imani.bill.pay.domain.property.PropertyManager;
import com.imani.bill.pay.domain.user.UserRecord;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.util.Optional;

/**
 * @author manyce400
 */
@RunWith(MockitoJUnitRunner.class)
public class LeaseAgreementRequestTest implements IMockUserRecordTestBuilder, IMockApartmentTestBuilder, IPropertyManagerTestBuilder {


    private Double monthlyRentalCost;

    private UserRecord userRecord;

    private Apartment apartment;

    private PropertyManager propertyManager;

    private LeaseAgreementTypeE leaseAgreementTypeE;

    private ObjectMapper mapper = new MockObjectMapper();
    
    
    @Before
    public void beforeTest() {
        monthlyRentalCost = 1500.00;
        userRecord = buildUserRecord();
        apartment = buildApartmentNoFloor();
        propertyManager = buildPropertyManager();
    }

    @Test
    public void testWriteLeaseAgreementRequestJSON() {
        APIGatewayEvent<LeaseAgreementRequest, GenericAPIGatewayResponse> apiGatewayEvent = new APIGatewayEvent<>();

        LeaseAgreementRequest requestBody = new LeaseAgreementRequest();
        requestBody.setLeaseAgreementTypeE(LeaseAgreementTypeE.Monthly);
        requestBody.setUserRecord(userRecord);
        requestBody.setApartment(apartment);
        requestBody.setPropertyManager(propertyManager);
        requestBody.setMonthlyRentalCost(monthlyRentalCost);

        apiGatewayEvent.setRequestBody(Optional.of(requestBody));

        try {
            // Verify that we can write LeaseAgreementRequest to JSON.
            String json = mapper.writeValueAsString(apiGatewayEvent);
            System.out.println("json = " + json);
        } catch (JsonProcessingException e) {
            Assert.fail("Failed to write LeaseAgreementRequest to JSON.");
        }
    }

    @Test
    public void testReadLeaseAgreementRequestJSON() {
        String json = "{\"eventTime\":\"2019-11-05 15:08:43\",\"requestBody\":{\"monthlyRentalCost\":1500.0,\"userRecord\":{\"firstName\":\"Test\",\"lastName\":\"User\",\"embeddedContactInfo\":{\"email\":\"test.user@imani.com\"},\"loggedIn\":false,\"resetPassword\":false,\"accountLocked\":false,\"acceptedTermsAndConditions\":false},\"apartment\":{\"apartmentNumber\":\"235 F\",\"floor\":{\"floorNumber\":1,\"apartments\":[]},\"bedrooms\":[],\"rented\":false},\"propertyManager\":{\"name\":\"AB & C Property Management\",\"embeddedContactInfo\":{\"email\":\"test.property.manager@imani.com\"},\"portfolio\":[]},\"leaseAgreementTypeE\":\"Monthly\"}}";
        try {
            APIGatewayEvent<LeaseAgreementRequest, GenericAPIGatewayResponse> apiGatewayEvent = mapper.readValue(json, APIGatewayEvent.class);
            System.out.println("apiGatewayEvent = " + apiGatewayEvent);
        } catch (IOException e) {
            e.printStackTrace();
            Assert.fail("Failed to read LeaseAgreementRequest from JSON.");
        }
    }


}