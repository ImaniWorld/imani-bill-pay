package com.imani.bill.pay.service.property.builder;

import com.imani.bill.pay.domain.gateway.APIGatewayEvent;
import com.imani.bill.pay.domain.gateway.GenericAPIGatewayResponse;
import com.imani.bill.pay.domain.property.Apartment;
import com.imani.bill.pay.domain.property.Bedroom;
import com.imani.bill.pay.domain.property.Floor;
import com.imani.bill.pay.domain.property.gateway.ApartmentBuilderRequest;

import java.util.List;
import java.util.Optional;

/**
 * @author manyce400
 */
public interface IApartmentBuilderService {

    public APIGatewayEvent<ApartmentBuilderRequest, GenericAPIGatewayResponse> buildApartment(ApartmentBuilderRequest apartmentBuilderRequest);

    public Optional<Apartment> buildApartment(Floor floor, String apartmentNumber);

    public Optional<Apartment> buildApartment(Floor floor, String apartmentNumber, List<Bedroom> bedrooms);
}
