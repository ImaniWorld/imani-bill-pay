package com.imani.bill.pay.service.property;

import com.imani.bill.pay.domain.property.Apartment;
import com.imani.bill.pay.domain.property.Floor;
import com.imani.bill.pay.domain.property.repository.IFloorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.transaction.Transactional;

/**
 * @author manyce400
 */
@Service(FloorProducerService.SPRING_BEAN)
public class FloorProducerService implements IFloorProducerService {


    @Autowired
    private IFloorRepository iFloorRepository;

    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(FloorProducerService.class);

    public static final String SPRING_BEAN = "com.imani.cash.domain.service.property.rental.FloorProducerService";


    @Transactional
    @Override
    public void createFloorApartments(Floor floor, Integer numberOfApts) {
        Assert.notNull(floor, "Floor cannot be null");
        Assert.notNull(floor.getProperty(), "Floor Property cannot be null");
        Assert.notNull(numberOfApts, "numberOfApts cannot be null");
        Assert.isTrue(numberOfApts.intValue() > 0, "numberOfApts cannot be 0");

        LOGGER.info("Generating -> {} # of Apartments on property floor:=> {}", numberOfApts, floor);

        for (int i = 1; i<= numberOfApts; i++) {
            String apartmentNumber = "Floor #" + floor.getFloorNumber() + " - Apt #" + i;  // We generate a temp apartment number =>  FloorNumber + - + i
            Apartment apartment = Apartment.builder()
                    .floor(floor)
                    .isRented(false)
                    .apartmentNumber(apartmentNumber)
                    .build();
            floor.addToApartments(apartment);
        }

        // Save the floor with all the new apartments added.
        iFloorRepository.save(floor);
    }
}
