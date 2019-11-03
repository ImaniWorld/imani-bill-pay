package com.imani.bill.pay.domain.property.gateway;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.imani.bill.pay.domain.mock.MockObjectMapper;
import com.imani.bill.pay.domain.property.Bedroom;
import com.imani.bill.pay.domain.property.Floor;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * @author manyce400
 */
@RunWith(MockitoJUnitRunner.class)
public class ApartmentBuilderEventTest {


    private Floor floor;

    private Bedroom bedroom1;

    private Bedroom bedroom2;

    private ObjectMapper mapper = new MockObjectMapper();


    @Before
    public void before() {
        floor = Floor.builder()
                .floorNumber(1)
                .codeName("Master Mind")
                .build();

        // Build bedrooms
        bedroom1 = Bedroom.builder()
                .isMasterBedroom(true)
                .squareFootage(500L)
                .build();

        bedroom2 = Bedroom.builder()
                .isMasterBedroom(false)
                .squareFootage(500L)
                .build();
    }

    @Test
    public void testBuildJSON() {
        ApartmentBuilderEvent apartmentBuilderEvent = ApartmentBuilderEvent.builder()
                .bedroom(bedroom1)
                .bedroom(bedroom2)
                .floor(floor)
                .build();

        try {
            String json = mapper.writeValueAsString(apartmentBuilderEvent);
            System.out.println("json = " + json);
        } catch (JsonProcessingException e) {
            Assert.fail("Writing ApartmentBuilderEvent to JSON should not fail");
        }
    }
}
