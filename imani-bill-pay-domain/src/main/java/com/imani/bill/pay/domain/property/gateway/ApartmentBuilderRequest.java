package com.imani.bill.pay.domain.property.gateway;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.imani.bill.pay.domain.gateway.GenericAPIGatewayRequest;
import com.imani.bill.pay.domain.property.Bedroom;
import com.imani.bill.pay.domain.property.Floor;
import com.imani.bill.pay.domain.user.UserRecord;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author manyce400
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApartmentBuilderRequest extends GenericAPIGatewayRequest {


    private String apartmentNumber;

    private Floor floor;

    private List<Bedroom> bedrooms = new ArrayList<>();


    public ApartmentBuilderRequest() {

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

        private ApartmentBuilderRequest apartmentBuilderRequest = new ApartmentBuilderRequest();

        public Builder apartmentNumber(String apartmentNumber) {
            apartmentBuilderRequest.apartmentNumber = apartmentNumber;
            return this;
        }

        public Builder floor(Floor floor) {
            apartmentBuilderRequest.floor = floor;
            return this;
        }

        public Builder bedroom(Bedroom bedroom) {
            apartmentBuilderRequest.bedrooms.add(bedroom);
            return this;
        }

        public Builder execUserRecord(UserRecord execUserRecord) {
            apartmentBuilderRequest.execUserRecord = Optional.of(execUserRecord);
            return this;
        }

        public ApartmentBuilderRequest build() {
            return apartmentBuilderRequest;
        }
    }
}
