package com.imani.bill.pay.domain.property;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.imani.bill.pay.domain.gateway.APIGatewayEvent;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * @author manyce400
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApartmentBuilderEvent extends APIGatewayEvent {


    private Floor floor;

    private List<Bedroom> bedrooms = new ArrayList<>();


    public ApartmentBuilderEvent() {

    }

    public Floor getFloor() {
        return floor;
    }

    public void setFloor(Floor floor) {
        this.floor = floor;
    }

    public List<Bedroom> getBedrooms() {
        return bedrooms;
    }

    public void setBedrooms(List<Bedroom> bedrooms) {
        this.bedrooms = bedrooms;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("floor", floor)
                .append("bedrooms", bedrooms)
                .toString();
    }


    public static class Builder {

        private ApartmentBuilderEvent apartmentBuilderEvent = new ApartmentBuilderEvent();

        public Builder floor(Floor floor) {
            apartmentBuilderEvent.floor = floor;
            return this;
        }

        public Builder bedroom(Bedroom bedroom) {
            apartmentBuilderEvent.bedrooms.add(bedroom);
            return this;
        }

        public ApartmentBuilderEvent build() {
            return apartmentBuilderEvent;
        }
    }
}
