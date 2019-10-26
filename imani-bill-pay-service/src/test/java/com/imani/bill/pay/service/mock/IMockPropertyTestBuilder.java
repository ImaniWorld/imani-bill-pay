package com.imani.bill.pay.service.mock;

import com.imani.bill.pay.domain.property.Property;
import com.imani.bill.pay.domain.property.PropertyTypeE;

/**
 * @author manyce400
 */
public interface IMockPropertyTestBuilder {

    public static final Integer PROPERTY_NUM_DAYS_PAYMENT_LATE = 10;

    public default Property buildMultiFamilyProperty() {
        Property property = Property.builder()
                .mthlyNumberOfDaysPaymentLate(PROPERTY_NUM_DAYS_PAYMENT_LATE)
                .propertyTypeE(PropertyTypeE.MultiFamily)
                .build();
        return property;
    }

    public default Property buildCondoProperty() {
        Property property = Property.builder()
                .mthlyNumberOfDaysPaymentLate(PROPERTY_NUM_DAYS_PAYMENT_LATE)
                .propertyTypeE(PropertyTypeE.Condo)
                .build();
        return property;
    }

}
