package com.imani.bill.pay.service.property;

import com.imani.bill.pay.domain.property.IHasPropertyData;
import com.imani.bill.pay.domain.property.Property;

import java.util.Optional;

/**
 * @author manyce400
 */
public interface IPropertyBuilderService {


    public Optional<Property> buildAndRecordProperty(IHasPropertyData iHasPropertyData);

}
