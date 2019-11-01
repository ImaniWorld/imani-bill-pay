package com.imani.bill.pay.service.contact;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.imani.bill.pay.domain.contact.BillPayInquiry;
import com.imani.bill.pay.domain.contact.repository.IBillPayInquiryRepository;
import com.imani.bill.pay.service.mock.MockObjectMapper;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * @author manyce400
 */
@RunWith(MockitoJUnitRunner.class)
public class BillPayInquiryServiceTest {


    @Mock
    private IBillPayInquiryRepository iBillPayInquiryRepository;

    @InjectMocks
    private BillPayInquiryService billPayInquiryService;

    private ObjectMapper mapper = new MockObjectMapper();


    @Test
    public void testSave() {
        BillPayInquiry billPayInquiry = BillPayInquiry.builder()
                .businessType("Property Management")
                .contactName("Joe Longo")
                .contactEmail("joe.longo@forever.com")
                .contactPhone(2123456789L)
                .numberOfUsers(20)
                .contactQuestion("I want more detailed information on how this works")
                .build();

        try {
            String stringValue = mapper.writeValueAsString(billPayInquiry);
            System.out.println("stringValue = " + stringValue);
        } catch (JsonProcessingException e) {
            Assert.fail("BillPayInquiry should be writeable to JSON.");
        }
    }
}
