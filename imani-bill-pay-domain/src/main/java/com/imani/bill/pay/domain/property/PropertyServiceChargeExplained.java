package com.imani.bill.pay.domain.property;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.joda.time.DateTime;

/**
 * Domain object that contains detailed explanation of property service charges that were applied to a monthly rental bill
 *
 * @author manyce400
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PropertyServiceChargeExplained {


    private String serviceName;

    private Double serviceMonthlyCost;

    // Date user signed up for this property service
    @JsonFormat(pattern = "yyyy-MM-dd")
    private DateTime signedUpDate;

    public PropertyServiceChargeExplained() {

    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public Double getServiceMonthlyCost() {
        return serviceMonthlyCost;
    }

    public void setServiceMonthlyCost(Double serviceMonthlyCost) {
        this.serviceMonthlyCost = serviceMonthlyCost;
    }

    public DateTime getSignedUpDate() {
        return signedUpDate;
    }

    public void setSignedUpDate(DateTime signedUpDate) {
        this.signedUpDate = signedUpDate;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("serviceName", serviceName)
                .append("serviceMonthlyCost", serviceMonthlyCost)
                .append("signedUpDate", signedUpDate)
                .toString();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private PropertyServiceChargeExplained propertyServiceChargeExplained = new PropertyServiceChargeExplained();

        public Builder serviceName(String serviceName) {
            propertyServiceChargeExplained.serviceName = serviceName;
            return this;
        }

        public Builder serviceMonthlyCost(Double serviceMonthlyCost) {
            propertyServiceChargeExplained.serviceMonthlyCost = serviceMonthlyCost;
            return this;
        }

        public Builder signedUpDate(DateTime signedUpDate) {
            propertyServiceChargeExplained.signedUpDate = signedUpDate;
            return this;
        }

        public PropertyServiceChargeExplained build() {
            return propertyServiceChargeExplained;
        }
    }
}
