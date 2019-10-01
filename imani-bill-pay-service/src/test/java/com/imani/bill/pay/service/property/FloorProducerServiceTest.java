package com.imani.bill.pay.service.property;

import com.imani.bill.pay.domain.property.Floor;
import com.imani.bill.pay.domain.property.repository.IFloorRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * @author manyce400
 */
@RunWith(MockitoJUnitRunner.class)
public class FloorProducerServiceTest extends AbstractMockPropertyBuilderTest {


    @Mock
    private IFloorRepository iFloorRepository;

    @InjectMocks
    private FloorProducerService floorProducerService;


    @Test
    public void testCreateFloorApartments() {
        Floor floor = buildFloorWithProperty(5);
        floorProducerService.createFloorApartments(floor, 8);
        Assert.assertEquals(8, floor.getApartments().size());
        Mockito.verify(iFloorRepository, Mockito.times(1)).save(Mockito.any()); // verify floor save is called once to save floor information.
    }

}
