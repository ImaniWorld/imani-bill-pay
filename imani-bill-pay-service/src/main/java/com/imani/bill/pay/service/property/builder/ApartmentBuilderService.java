package com.imani.bill.pay.service.property.builder;

import com.imani.bill.pay.domain.gateway.APIGatewayEventStatusE;
import com.imani.bill.pay.domain.property.Apartment;
import com.imani.bill.pay.domain.property.gateway.ApartmentBuilderEvent;
import com.imani.bill.pay.domain.property.Bedroom;
import com.imani.bill.pay.domain.property.Floor;
import com.imani.bill.pay.domain.property.repository.IApartmentRepository;
import com.imani.bill.pay.domain.property.repository.IFloorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

/**
 * @author manyce400
 */
@Service(ApartmentBuilderService.SPRING_BEAN)
public class ApartmentBuilderService implements IApartmentBuilderService {


    @Autowired
    private IFloorRepository iFloorRepository;

    @Autowired
    private IApartmentRepository iApartmentRepository;

    public static final String SPRING_BEAN = "com.imani.bill.pay.service.property.builder.ApartmentBuilderService";

    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(ApartmentBuilderService.class);


    @Transactional
    @Override
    public ApartmentBuilderEvent buildApartment(ApartmentBuilderEvent apartmentBuilderEvent) {
        Assert.notNull(apartmentBuilderEvent, "ApartmentBuilderEvent cannot be null");
        LOGGER.debug("Building Apartment from apartmentBuilderEvent:=> {}", apartmentBuilderEvent);

        // Refresh Floor data
        Optional<Floor> floor = iFloorRepository.findById(apartmentBuilderEvent.getFloor().getId());

        if(floor.isPresent()
                && floor.get().getFloorNumber().equals(apartmentBuilderEvent.getFloor().getFloorNumber())) {
            Optional<Apartment> apartment = null;

            if(apartmentBuilderEvent.getBedrooms().size() > 0) {
                apartment = buildApartment(floor.get(), apartmentBuilderEvent.getBedrooms());
            } else {
                apartment = buildApartment(floor.get());
            }

            ApartmentBuilderEvent apartmentBuilderEventResult = ApartmentBuilderEvent.builder()
                    .builtApartment(apartment.get())
                    .eventTimeNow()
                    .build();
            return apartmentBuilderEventResult;
        }

        LOGGER.warn("No valid floor found in the system for floor:=> {}", apartmentBuilderEvent.getFloor());
        ApartmentBuilderEvent apartmentBuilderEventResult = ApartmentBuilderEvent.builder()
                .eventTimeNow()
                .gatewayEventCommunication("Floor passed in request not found in property.")
                .apiGatewayEventStatusE(APIGatewayEventStatusE.InvalidRequest)
                .build();
        return apartmentBuilderEventResult;
    }

    @Transactional
    @Override
    public Optional<Apartment> buildApartment(Floor floor) {
        Assert.notNull(floor, "Floor cannot be null");
        LOGGER.debug("Building apartment on Property floor:=> {}", floor);

        Apartment apartment = Apartment.builder()
                .floor(floor)
                .build();
        iApartmentRepository.save(apartment);
        floor.addToApartments(apartment);
        iFloorRepository.save(floor);
        return Optional.of(apartment);
    }

    @Transactional
    @Override
    public Optional<Apartment> buildApartment(Floor floor, List<Bedroom> bedrooms) {
        Assert.notNull(floor, "Floor cannot be null");
        Assert.notNull(bedrooms, "Bedrooms cannot be null");
        Assert.isTrue(bedrooms.size() > 0, "Bedrooms list cannot be empty");

        LOGGER.debug("Building apartment with bedrooms on Property floor:=> {}", floor);

        // Build empty apartment
        Apartment apartment = new Apartment();
        bedrooms.forEach(bedroom -> {
            apartment.addToBedrooms(bedroom);
        });

        iApartmentRepository.save(apartment);
        floor.addToApartments(apartment);
        iFloorRepository.save(floor);
        return Optional.of(apartment);
    }
}
