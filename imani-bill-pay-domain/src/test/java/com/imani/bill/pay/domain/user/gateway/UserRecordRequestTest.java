package com.imani.bill.pay.domain.user.gateway;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.imani.bill.pay.domain.gateway.APIGatewayEvent;
import com.imani.bill.pay.domain.gateway.GenericAPIGatewayResponse;
import com.imani.bill.pay.domain.mock.IMockUserLoginStatistic;
import com.imani.bill.pay.domain.mock.IMockUserRecordTestBuilder;
import com.imani.bill.pay.domain.mock.MockObjectMapper;
import com.imani.bill.pay.domain.user.UserLoginStatistic;
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
public class UserRecordRequestTest implements IMockUserRecordTestBuilder, IMockUserLoginStatistic {


    private UserRecord eventUserRecord;

    private ObjectMapper mapper = new MockObjectMapper();

    @Before
    public void beforeTest() {
        eventUserRecord = buildUserRecord();
    }

    @Test
    public void testWriteUserRecordRequestRequestJSON() {
        UserLoginStatistic userLoginStatistic = buildUserLoginStatistic(eventUserRecord);
        UserRecordRequest userRecordRequest = UserRecordRequest.builder()
                .execUserRecord(eventUserRecord)
                .userLoginStatistic(userLoginStatistic)
                .build();

        APIGatewayEvent<UserRecordRequest, GenericAPIGatewayResponse> apiGatewayEvent = APIGatewayEvent.builder()
                .requestBody(userRecordRequest)
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
