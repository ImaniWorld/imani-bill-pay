package com.imani.bill.pay.service.property.builder;

import com.imani.bill.pay.domain.geographical.repository.IBoroughRepository;
import com.imani.bill.pay.domain.property.IHasPropertyData;
import com.imani.bill.pay.domain.property.Property;
import com.imani.bill.pay.domain.property.PropertyTypeE;
import com.imani.bill.pay.domain.property.repository.IPropertyRepository;
import com.imani.bill.pay.service.property.AbstractMockPropertyBuilderTest;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * TODO complete entire test coverage
 * @author manyce400
 */
@RunWith(MockitoJUnitRunner.class)
public class PropertyBuilderServiceTest extends AbstractMockPropertyBuilderTest {


    @Mock
    private IBoroughRepository iBoroughRepository;

    @Mock
    private IPropertyRepository iPropertyRepository;

    @InjectMocks
    private PropertyBuilderService propertyBuilderService;


    @Test
    public void testInitializePropertyFloors() {
        IHasPropertyData iHasPropertyData = buildIHasPropertyData(4, PropertyTypeE.MultiFamily);
        Property property = buildProperty();
        propertyBuilderService.initializePropertyFloors(property, iHasPropertyData);
        Assert.assertEquals(4, property.getFloors().size());
    }

}
