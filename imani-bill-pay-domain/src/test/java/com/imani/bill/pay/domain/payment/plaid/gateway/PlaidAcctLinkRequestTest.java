package com.imani.bill.pay.domain.payment.plaid.gateway;

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

import java.io.IOException;
import java.util.Optional;

/**
 * @author manyce400
 */
@RunWith(MockitoJUnitRunner.class)
public class PlaidAcctLinkRequestTest implements IMockUserRecordTestBuilder {


    private UserRecord eventUserRecord;

    private ObjectMapper mapper = new MockObjectMapper();
    
    private static final String expected = "{\"execUserRecord\":{\"firstName\":\"Erin\",\"lastName\":\"Addy Lamptey\",\"embeddedContactInfo\":{\"email\":\"erin.addy@gmail.com\"},\"loggedIn\":false,\"resetPassword\":false,\"accountLocked\":false,\"acceptedTermsAndConditions\":false},\"plaidPublicToken\":\"public-development-7c8a40c5-cbe1-49c6-93de-3c4bdbe2994d\",\"plaidAccountID\":\"Me7MpWB1jvueZ6kMxgVBUGjxoMdBAmi9lRwDy\"}";


    @Before
    public void before() {
        eventUserRecord = buildUserRecord("erin.addy@gmail.com", "Erin", "Addy Lamptey");
    }

    @Test
    public void testWritePlaidAcctLinkRequest() {
        PlaidAcctLinkRequest plaidAcctLinkRequest = new PlaidAcctLinkRequest();
        plaidAcctLinkRequest.setExecUserRecord(Optional.of(eventUserRecord));
        plaidAcctLinkRequest.setPlaidAccountID("Me7MpWB1jvueZ6kMxgVBUGjxoMdBAmi9lRwDy");
        plaidAcctLinkRequest.setPlaidPublicToken("public-development-7c8a40c5-cbe1-49c6-93de-3c4bdbe2994d");

        try {
            // Verify that we can write ApartmentBuilderRequest to JSON
            String json = mapper.writeValueAsString(plaidAcctLinkRequest);
            Assert.assertEquals(expected, json);
            System.out.println("json = " + json);
        } catch (JsonProcessingException e) {
            Assert.fail("Failed to write PlaidAcctLinkRequest to JSON.");
        }
    }
    
    
    @Test
    public void testReadPlaidAcctLinkRequest() {
        try {
            PlaidAcctLinkRequest plaidAcctLinkRequest = mapper.readValue(expected, PlaidAcctLinkRequest.class);
            System.out.println("plaidAcctLinkRequest.getExecUserRecord().get() = " + plaidAcctLinkRequest.getExecUserRecord().get());
        } catch (IOException e) {
            Assert.fail("Failed to read PlaidAcctLinkRequest from JSON.");
        }
    }
}
