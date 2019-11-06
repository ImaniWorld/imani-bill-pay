package com.imani.bill.pay.domain.property.gateway;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.imani.bill.pay.domain.gateway.GenericAPIGatewayRequest;
import com.imani.bill.pay.domain.property.Apartment;
import com.imani.bill.pay.domain.property.LeaseAgreementTypeE;
import com.imani.bill.pay.domain.property.PropertyManager;
import com.imani.bill.pay.domain.user.UserRecord;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Optional;

/**
 * @author manyce400
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LeaseAgreementRequest extends GenericAPIGatewayRequest {


    private Double monthlyRentalCost;

    private UserRecord userRecord;

    private Apartment apartment;

    private PropertyManager propertyManager;

    private LeaseAgreementTypeE leaseAgreementTypeE;


    public Double getMonthlyRentalCost() {
        return monthlyRentalCost;
    }

    public void setMonthlyRentalCost(Double monthlyRentalCost) {
        this.monthlyRentalCost = monthlyRentalCost;
    }

    public UserRecord getUserRecord() {
        return userRecord;
    }

    public void setUserRecord(UserRecord userRecord) {
        this.userRecord = userRecord;
    }

    public Apartment getApartment() {
        return apartment;
    }

    public void setApartment(Apartment apartment) {
        this.apartment = apartment;
    }

    public PropertyManager getPropertyManager() {
        return propertyManager;
    }

    public void setPropertyManager(PropertyManager propertyManager) {
        this.propertyManager = propertyManager;
    }

    public LeaseAgreementTypeE getLeaseAgreementTypeE() {
        return leaseAgreementTypeE;
    }

    public void setLeaseAgreementTypeE(LeaseAgreementTypeE leaseAgreementTypeE) {
        this.leaseAgreementTypeE = leaseAgreementTypeE;
    }


    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("monthlyRentalCost", monthlyRentalCost)
                .append("userRecord", userRecord)
                .append("apartment", apartment)
                .append("propertyManager", propertyManager)
                .append("leaseAgreementTypeE", leaseAgreementTypeE)
                .toString();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private LeaseAgreementRequest leaseAgreementRequest = new LeaseAgreementRequest();

        public Builder monthlyRentalCost(Double monthlyRentalCost) {
            leaseAgreementRequest.monthlyRentalCost = monthlyRentalCost;
            return this;
        }

        public Builder userRecord(UserRecord userRecord) {
            leaseAgreementRequest.userRecord = userRecord;
            return this;
        }

        public Builder apartment(Apartment apartment) {
            leaseAgreementRequest.apartment = apartment;
            return this;
        }

        public Builder propertyManager(PropertyManager propertyManager) {
            leaseAgreementRequest.propertyManager = propertyManager;
            return this;
        }

        public Builder leaseAgreementTypeE(LeaseAgreementTypeE leaseAgreementTypeE) {
            leaseAgreementRequest.leaseAgreementTypeE = leaseAgreementTypeE;
            return this;
        }

        public Builder execUserRecord(UserRecord execUserRecord) {
            leaseAgreementRequest.execUserRecord = Optional.of(execUserRecord);
            return this;
        }

        public LeaseAgreementRequest build() {
            return leaseAgreementRequest;
        }
    }
}
