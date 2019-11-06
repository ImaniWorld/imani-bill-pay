package com.imani.bill.pay.domain.mock;

import com.imani.bill.pay.domain.property.Apartment;
import com.imani.bill.pay.domain.property.Floor;

/**
 * @author manyce400
 */
public interface IMockApartmentTestBuilder {

    public default Apartment buildApartment() {
        Apartment apartment = Apartment.builder()
                .apartmentNumber("235 F")
                .floor(buildFloor())
                .build();
        return apartment;
    }

    public default Floor buildFloor() {
        Floor floor = Floor.builder()
                .floorNumber(1)
                .build();
        return floor;
    }

    public default Floor buildFloorWithProperty(int floorNumber) {
        Floor floor = Floor.builder()
                .floorNumber(floorNumber)
                .build();
        return floor;
    }

}