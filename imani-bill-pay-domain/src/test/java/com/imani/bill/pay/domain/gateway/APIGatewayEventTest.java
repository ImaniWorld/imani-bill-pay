package com.imani.bill.pay.domain.gateway;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.imani.bill.pay.domain.mock.IMockUserRecordTestBuilder;
import com.imani.bill.pay.domain.mock.MockObjectMapper;
import com.imani.bill.pay.domain.user.UserRecord;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * @author manyce400
 */
@RunWith(MockitoJUnitRunner.class)
public class APIGatewayEventTest implements IMockUserRecordTestBuilder {



    private UserRecord eventUserRecord;

    private APIGatewayEvent<GenericAPIGatewayRequest, GenericAPIGatewayResponse> apiGatewayEvent;

    private ObjectMapper mapper = new MockObjectMapper();

    @Before
    public void before() {
        eventUserRecord = buildUserRecord();
    }


    @Test
    public void testGenericRequest() {
        GenericAPIGatewayRequest genericAPIGatewayRequest = GenericAPIGatewayRequest.build(eventUserRecord);
        APIGatewayEvent<GenericAPIGatewayRequest, GenericAPIGatewayResponse> apiGatewayEvent = APIGatewayEvent.builder()
                .requestBody(genericAPIGatewayRequest)
                .build();

        try {
            // Verify that we can write ApartmentBuilderRequest to JSON
            String json = mapper.writeValueAsString(apiGatewayEvent);
            System.out.println("json = " + json);
        } catch (JsonProcessingException e) {
            Assert.fail("Failed to write UserRecordRequest to JSON.");
        }
    }
}
