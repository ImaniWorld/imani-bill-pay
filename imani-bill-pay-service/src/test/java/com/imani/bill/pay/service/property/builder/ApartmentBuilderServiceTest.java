package com.imani.bill.pay.service.property.builder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.imani.bill.pay.domain.property.repository.IApartmentRepository;
import com.imani.bill.pay.domain.property.repository.IFloorRepository;
import com.imani.bill.pay.service.mock.MockObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * @author manyce400
 */
@RunWith(MockitoJUnitRunner.class)
public class ApartmentBuilderServiceTest {


    @Mock
    private IFloorRepository iFloorRepository;

    @Mock
    private IApartmentRepository iApartmentRepository;

    @InjectMocks
    private ApartmentBuilderService apartmentBuilderService;


    private ObjectMapper mapper = new MockObjectMapper();



    @Test
    public void test() {
        //TODO complete
    }

}
