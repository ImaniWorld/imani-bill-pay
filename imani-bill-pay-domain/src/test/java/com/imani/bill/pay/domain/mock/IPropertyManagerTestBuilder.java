package com.imani.bill.pay.domain.mock;

import com.imani.bill.pay.domain.contact.EmbeddedContactInfo;
import com.imani.bill.pay.domain.property.PropertyManager;

/**
 * @author manyce400
 */
public interface IPropertyManagerTestBuilder {

    public default PropertyManager buildPropertyManager() {
        EmbeddedContactInfo embeddedContactInfo = EmbeddedContactInfo.builder()
                .email("test.property.manager@imani.com")
                .build();
        PropertyManager propertyManager = PropertyManager.builder()
                .name("AB & C Property Management")
                .embeddedContactInfo(embeddedContactInfo)
                .build();
        return propertyManager;
    }
}
