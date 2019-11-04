package com.imani.bill.pay.domain.property.gateway;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.imani.bill.pay.domain.gateway.APIGatewayEvent;
import com.imani.bill.pay.domain.gateway.APIGatewayEventStatusE;
import com.imani.bill.pay.domain.property.Bedroom;
import com.imani.bill.pay.domain.property.Floor;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * @author manyce400
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApartmentBuilderEvent extends APIGatewayEvent {


    private String apartmentNumber;

    private Floor floor;

    private List<Bedroom> bedrooms = new ArrayList<>();


    public ApartmentBuilderEvent() {

    }

    public String getApartmentNumber() {
        return apartmentNumber;
    }

    public void setApartmentNumber(String apartmentNumber) {
        this.apartmentNumber = apartmentNumber;
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
                .append("apartmentNumber", apartmentNumber)
                .append("floor", floor)
                .append("bedrooms", bedrooms)
                .toString();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private ApartmentBuilderEvent apartmentBuilderEvent = new ApartmentBuilderEvent();

        public Builder apartmentNumber(String apartmentNumber) {
            apartmentBuilderEvent.apartmentNumber = apartmentNumber;
            return this;
        }

        public Builder floor(Floor floor) {
            apartmentBuilderEvent.floor = floor;
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
