package com.imani.bill.pay.domain.property.gateway;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.imani.bill.pay.domain.gateway.APIGatewayEvent;
import com.imani.bill.pay.domain.gateway.GenericAPIGatewayResponse;
import com.imani.bill.pay.domain.mock.IMockUserRecordTestBuilder;
import com.imani.bill.pay.domain.mock.MockObjectMapper;
import com.imani.bill.pay.domain.property.Bedroom;
import com.imani.bill.pay.domain.property.Floor;
import com.imani.bill.pay.domain.property.Property;
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
public class ApartmentBuilderRequestTest implements IMockUserRecordTestBuilder {


    private Floor floor;

    private Bedroom bedroom1;

    private Bedroom bedroom2;

    private Property property;

    private UserRecord eventUserRecord;

    private ObjectMapper mapper = new MockObjectMapper();


    @Before
    public void before() {
        property = Property.builder()
                .propertyNumber("14509")
                .build();
        property.setId(1L);
        floor = Floor.builder()
                .floorNumber(1)
                .build();
        floor.setId(1L);
        floor.setProperty(property);

        // Build bedrooms
        bedroom1 = Bedroom.builder()
                .isMasterBedroom(true)
                .squareFootage(500L)
                .build();

        bedroom2 = Bedroom.builder()
                .isMasterBedroom(false)
                .squareFootage(500L)
                .build();

        eventUserRecord = buildUserRecord("admin@patriot.com", "John", "Doe");
    }

    @Test
    public void testWriteApartmentBuilderRequestJSON() {
        ApartmentBuilderRequest apartmentBuilderRequest = ApartmentBuilderRequest.builder()
                .apartmentNumber("1100")
                .bedroom(bedroom1)
                .bedroom(bedroom2)
                .floor(floor)
                .build();

        APIGatewayEvent<ApartmentBuilderRequest, GenericAPIGatewayResponse> apiGatewayEvent = new APIGatewayEvent<>();
        apiGatewayEvent.setRequestBody(Optional.of(apartmentBuilderRequest));
        apiGatewayEvent.setEventUserRecord(Optional.of(eventUserRecord));

        try {
            // Verify that we can write ApartmentBuilderRequest to JSON
            String json = mapper.writeValueAsString(apiGatewayEvent);
            System.out.println("json = " + json);
        } catch (JsonProcessingException e) {
            Assert.fail("Failed to write ApartmentBuilderRequest to JSON.");
        }
    }

    @Test
    public void testReadApartmentBuilderRequestJSON() {
        String json = "{\"eventTime\":\"2019-11-05 16:20:16\",\"requestBody\":{\"floor\":{\"id\":1,\"floorNumber\":1,\"property\":{\"id\":1,\"propertyNumber\":\"14509\",\"floors\":[]},\"apartments\":[]},\"bedrooms\":[{\"squareFootage\":500,\"masterBedroom\":true},{\"squareFootage\":500,\"masterBedroom\":false}]},\"eventUserRecord\":{\"firstName\":\"Test\",\"lastName\":\"User\",\"embeddedContactInfo\":{\"email\":\"test.user@imani.com\"},\"loggedIn\":false,\"resetPassword\":false,\"accountLocked\":false,\"acceptedTermsAndConditions\":false}}";
        try {
            APIGatewayEvent<ApartmentBuilderRequest, GenericAPIGatewayResponse> apiGatewayEvent = mapper.readValue(json, APIGatewayEvent.class);
            System.out.println("apiGatewayEvent = " + apiGatewayEvent);
        } catch (IOException e) {
            Assert.fail("Failed to read ApartmentBuilderRequest from JSON.");
        }
    }
    
}