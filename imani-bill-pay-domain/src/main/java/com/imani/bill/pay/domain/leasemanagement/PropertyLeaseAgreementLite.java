package com.imani.bill.pay.domain.leasemanagement;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.imani.bill.pay.domain.billing.BillScheduleTypeE;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.joda.time.DateTime;

/**
 * @author manyce400 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PropertyLeaseAgreementLite {

    private Long id;

    private Double fixedCost;

    private boolean agreementInForce;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private DateTime effectiveDate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private DateTime terminationDate;

    private BillScheduleTypeE billScheduleTypeE;

    private Long leasedApartmentID;

    private Long leasedPropertyID;

    public PropertyLeaseAgreementLite() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getFixedCost() {
        return fixedCost;
    }

    public void setFixedCost(Double fixedCost) {
        this.fixedCost = fixedCost;
    }

    public boolean isAgreementInForce() {
        return agreementInForce;
    }

    public void setAgreementInForce(boolean agreementInForce) {
        this.agreementInForce = agreementInForce;
    }

    public DateTime getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(DateTime effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public DateTime getTerminationDate() {
        return terminationDate;
    }

    public void setTerminationDate(DateTime terminationDate) {
        this.terminationDate = terminationDate;
    }

    public BillScheduleTypeE getBillScheduleTypeE() {
        return billScheduleTypeE;
    }

    public void setBillScheduleTypeE(BillScheduleTypeE billScheduleTypeE) {
        this.billScheduleTypeE = billScheduleTypeE;
    }

    public Long getLeasedApartmentID() {
        return leasedApartmentID;
    }

    public void setLeasedApartmentID(Long leasedApartmentID) {
        this.leasedApartmentID = leasedApartmentID;
    }

    public Long getLeasedPropertyID() {
        return leasedPropertyID;
    }

    public void setLeasedPropertyID(Long leasedPropertyID) {
        this.leasedPropertyID = leasedPropertyID;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("fixedCost", fixedCost)
                .append("agreementInForce", agreementInForce)
                .append("effectiveDate", effectiveDate)
                .append("terminationDate", terminationDate)
                .append("billScheduleTypeE", billScheduleTypeE)
                .append("leasedApartmentID", leasedApartmentID)
                .append("leasedPropertyID", leasedPropertyID)
                .toString();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final PropertyLeaseAgreementLite propertyLeaseAgreementLite = new PropertyLeaseAgreementLite();

        public Builder id(Long id) {
            propertyLeaseAgreementLite.id= id;
            return this;
        }

        public Builder fixedCost(Double fixedCost) {
            propertyLeaseAgreementLite.fixedCost= fixedCost;
            return this;
        }

        public Builder agreementInForce(boolean agreementInForce) {
            propertyLeaseAgreementLite.agreementInForce= agreementInForce;
            return this;
        }

        public Builder effectiveDate(DateTime effectiveDate) {
            propertyLeaseAgreementLite.effectiveDate= effectiveDate;
            return this;
        }

        public Builder terminationDate(DateTime terminationDate) {
            propertyLeaseAgreementLite.terminationDate= terminationDate;
            return this;
        }

        public Builder billScheduleTypeE(BillScheduleTypeE billScheduleTypeE) {
            propertyLeaseAgreementLite.billScheduleTypeE= billScheduleTypeE;
            return this;
        }

        public Builder leasedApartmentID(Long leasedApartmentID) {
            propertyLeaseAgreementLite.leasedApartmentID = leasedApartmentID;
            return this;
        }

        public Builder leasedPropertyID(Long leasedPropertyID) {
            propertyLeaseAgreementLite.leasedPropertyID = leasedPropertyID;
            return this;
        }

        public PropertyLeaseAgreementLite build() {
            return propertyLeaseAgreementLite;
        }
    }
}
