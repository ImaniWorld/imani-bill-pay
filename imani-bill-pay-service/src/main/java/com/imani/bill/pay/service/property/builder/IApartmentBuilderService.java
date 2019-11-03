package com.imani.bill.pay.service.property.builder;

import com.imani.bill.pay.domain.property.Apartment;
import com.imani.bill.pay.domain.property.gateway.ApartmentBuilderEvent;
import com.imani.bill.pay.domain.property.Bedroom;
import com.imani.bill.pay.domain.property.Floor;

import java.util.List;
import java.util.Optional;

/**
 * @author manyce400
 */
public interface IApartmentBuilderService {

    public ApartmentBuilderEvent buildApartment(ApartmentBuilderEvent apartmentBuilderEvent);

    public Optional<Apartment> buildApartment(Floor floor);

    public Optional<Apartment> buildApartment(Floor floor, List<Bedroom> bedrooms);
}
