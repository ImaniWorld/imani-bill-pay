package com.imani.bill.pay.domain.property.gateway;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.imani.bill.pay.domain.gateway.APIGatewayEvent;
import com.imani.bill.pay.domain.gateway.APIGatewayEventStatusE;
import com.imani.bill.pay.domain.property.Apartment;
import com.imani.bill.pay.domain.property.Bedroom;
import com.imani.bill.pay.domain.property.Floor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author manyce400
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApartmentBuilderEvent extends APIGatewayEvent {


    private Floor floor;

    private Optional<Apartment> builtApartment;

    private List<Bedroom> bedrooms = new ArrayList<>();


    public ApartmentBuilderEvent() {

    }

    public Floor getFloor() {
        return floor;
    }

    public void setFloor(Floor floor) {
        this.floor = floor;
    }

    public Optional<Apartment> getBuiltApartment() {
        return builtApartment;
    }

    public void setBuiltApartment(Optional<Apartment> builtApartment) {
        this.builtApartment = builtApartment;
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
                .append("builtApartment", builtApartment)
                .append("bedrooms", bedrooms)
                .toString();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private ApartmentBuilderEvent apartmentBuilderEvent = new ApartmentBuilderEvent();

        public Builder floor(Floor floor) {
            apartmentBuilderEvent.floor = floor;
            return this;
        }

        public Builder builtApartment(Apartment apartment) {
            Assert.notNull(apartment, "Apartment cannot be null");
            apartmentBuilderEvent.builtApartment = Optional.of(apartment);
            return this;
        }

        public Builder eventTimeNow() {
            apartmentBuilderEvent.addEventTime();
            return this;
        }

        public Builder gatewayEventCommunication(String gatewayEventCommunication) {
            apartmentBuilderEvent.gatewayEventCommunication = gatewayEventCommunication;
            return this;
        }

        public Builder apiGatewayEventStatusE(APIGatewayEventStatusE apiGatewayEventStatusE) {
            apartmentBuilderEvent.apiGatewayEventStatusE = apiGatewayEventStatusE;
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
