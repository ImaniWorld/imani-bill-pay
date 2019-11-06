package com.imani.bill.pay.service.property.builder;

import com.imani.bill.pay.domain.gateway.APIGatewayEvent;
import com.imani.bill.pay.domain.gateway.GenericAPIGatewayResponse;
import com.imani.bill.pay.domain.property.Apartment;
import com.imani.bill.pay.domain.property.Bedroom;
import com.imani.bill.pay.domain.property.Floor;
import com.imani.bill.pay.domain.property.gateway.ApartmentBuilderRequest;
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
    public APIGatewayEvent<ApartmentBuilderRequest, GenericAPIGatewayResponse> buildApartment(ApartmentBuilderRequest apartmentBuilderRequest) {
        Assert.notNull(apartmentBuilderRequest, "ApartmentBuilderRequest cannot be null");
        LOGGER.debug("Building Apartment from apartmentBuilderRequest:=> {}", apartmentBuilderRequest);

        // Refresh Floor data
        Optional<Floor> floor = iFloorRepository.findById(apartmentBuilderRequest.getFloor().getId());

        if(floor.isPresent()
                && floor.get().getProperty().getId().equals(apartmentBuilderRequest.getFloor().getProperty().getId()) // make sure that this floor belongs to expected property
                && floor.get().getFloorNumber().equals(apartmentBuilderRequest.getFloor().getFloorNumber())) { // validate that this is the correct floor number
            Optional<Apartment> apartment = null;

            if(apartmentBuilderRequest.getBedrooms().size() > 0) {
                apartment = buildApartment(floor.get(), apartmentBuilderRequest.getApartmentNumber(), apartmentBuilderRequest.getBedrooms());
            } else {
                apartment = buildApartment(floor.get(), apartmentBuilderRequest.getApartmentNumber());
            }

            GenericAPIGatewayResponse genericAPIGatewayResponse = GenericAPIGatewayResponse.getSuccessGenericAPIGatewayResponse();
            return APIGatewayEvent.builder()
                    .responseBody(genericAPIGatewayResponse)
                    //.eventUserRecord(apartmentBuilderRequest.getExecUserRecord().get())
                    .build();
        }

        LOGGER.warn("No valid floor found in the system for floor:=> {}", apartmentBuilderRequest.getFloor());
        GenericAPIGatewayResponse genericAPIGatewayResponse = GenericAPIGatewayResponse.getInvalidRequestGenericAPIGatewayResponse("Invalid floor details provided");
        return APIGatewayEvent.builder()
                .responseBody(genericAPIGatewayResponse)
                //.eventUserRecord(apartmentBuilderRequest.getExecUserRecord().get())
                .build();
    }

    @Transactional
    @Override
    public Optional<Apartment> buildApartment(Floor floor, String apartmentNumber) {
        Assert.notNull(floor, "Floor cannot be null");
        LOGGER.debug("Building apartment on Property floor:=> {}", floor);

        Apartment apartment = Apartment.builder()
                .floor(floor)
                .apartmentNumber(apartmentNumber)
                .build();
        iApartmentRepository.save(apartment);
        floor.addToApartments(apartment);
        iFloorRepository.save(floor);
        return Optional.of(apartment);
    }

    @Transactional
    @Override
    public Optional<Apartment> buildApartment(Floor floor, String apartmentNumber, List<Bedroom> bedrooms) {
        Assert.notNull(floor, "Floor cannot be null");
        Assert.notNull(bedrooms, "Bedrooms cannot be null");
        Assert.isTrue(bedrooms.size() > 0, "Bedrooms list cannot be empty");

        LOGGER.debug("Building apartment with bedrooms on Property floor:=> {}", floor);

        // Build empty apartment
        Apartment apartment = Apartment.builder()
                .floor(floor)
                .apartmentNumber(apartmentNumber)
                .build();

        bedrooms.forEach(bedroom -> {
            bedroom.setApartment(apartment);
            apartment.addToBedrooms(bedroom);
        });

        iApartmentRepository.save(apartment);
        floor.addToApartments(apartment);
        iFloorRepository.save(floor);
        return Optional.of(apartment);
    }
}
