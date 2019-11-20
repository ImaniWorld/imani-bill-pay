package com.imani.bill.pay.service.property.builder;

import com.imani.bill.pay.domain.gateway.APIGatewayEvent;
import com.imani.bill.pay.domain.gateway.GenericAPIGatewayResponse;
import com.imani.bill.pay.domain.property.IHasPropertyData;
import com.imani.bill.pay.domain.property.Property;
import com.imani.bill.pay.domain.property.gateway.PropertyBuilderRequest;

import java.util.Optional;

/**
 * @author manyce400
 */
public interface IPropertyBuilderService {


    public Optional<Property> buildAndRecordProperty(IHasPropertyData iHasPropertyData);

    public APIGatewayEvent<PropertyBuilderRequest, GenericAPIGatewayResponse> buildProperty(PropertyBuilderRequest propertyBuilderRequest);

}
