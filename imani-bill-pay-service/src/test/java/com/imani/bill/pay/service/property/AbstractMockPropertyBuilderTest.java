package com.imani.bill.pay.service.property;

import com.imani.bill.pay.domain.property.Floor;
import com.imani.bill.pay.domain.property.IHasPropertyData;
import com.imani.bill.pay.domain.property.Property;
import com.imani.bill.pay.domain.property.PropertyTypeE;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * @author manyce400
 */
@RunWith(MockitoJUnitRunner.class)
public abstract class AbstractMockPropertyBuilderTest {


    public static final Integer PROPERTY_NUM_DAYS_PAYMENT_LATE = 10;


    protected IHasPropertyData buildIHasPropertyData(final Integer legalStories,  final PropertyTypeE propertyTypeE) {
        MockIHasPropertyData mockIHasPropertyData = new MockIHasPropertyData() {
            @Override
            public Integer getLegalStories() {
                return legalStories;
            }

            @Override
            public PropertyTypeE getPropertyTypeE() {
                return propertyTypeE;
            }
        };

        return mockIHasPropertyData;
    }

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


    private class MockIHasPropertyData implements IHasPropertyData {

        @Override
        public Long getBoroID() {
            return null;
        }

        @Override
        public Long getCityID() {
            return null;
        }

        @Override
        public String getPropertyNumber() {
            return null;
        }

        @Override
        public String getStreetName() {
            return null;
        }

        @Override
        public Integer getLegalStories() {
            return null;
        }

        @Override
        public String getZipCode() {
            return null;
        }

        @Override
        public String getBlock() {
            return null;
        }

        @Override
        public String getLot() {
            return null;
        }

        @Override
        public String getBin() {
            return null;
        }

        @Override
        public Boolean isPublicHousing() {
            return null;
        }

        @Override
        public PropertyTypeE getPropertyTypeE() {
            return null;
        }
    }

}
