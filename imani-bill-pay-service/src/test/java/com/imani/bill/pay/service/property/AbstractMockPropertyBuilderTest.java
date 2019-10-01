package com.imani.bill.pay.service.property;

import com.imani.bill.pay.domain.property.Floor;
import com.imani.bill.pay.domain.property.Property;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * @author manyce400
 */
@RunWith(MockitoJUnitRunner.class)
public abstract class AbstractMockPropertyBuilderTest {


    public static final Integer PROPERTY_NUM_DAYS_PAYMENT_LATE = 10;

    protected Property buildProperty() {
        Property property = Property.builder()
                .mthlyNumberOfDaysPaymentLate(PROPERTY_NUM_DAYS_PAYMENT_LATE)
                .build();
        return property;
    }

    protected Floor buildFloorWithProperty(int floorNumber) {
        Property property = buildProperty();
        Floor floor = Floor.builder()
                .floorNumber(floorNumber)
                .property(property)
                .build();
        return floor;
    }

}
