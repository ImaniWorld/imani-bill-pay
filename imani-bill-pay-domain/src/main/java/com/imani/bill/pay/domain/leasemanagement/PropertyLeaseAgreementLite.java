package com.imani.bill.pay.domain.leasemanagement;

import com.imani.bill.pay.domain.billing.BillScheduleTypeE;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.joda.time.DateTime;

/**
 * @author manyce400 
 */
public class PropertyLeaseAgreementLite {

    private Long id;

    private Double fixedCost;

    private boolean agreementInForce;

    private DateTime effectiveDate;

    private DateTime terminationDate;

    private BillScheduleTypeE billScheduleTypeE;

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

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("fixedCost", fixedCost)
                .append("agreementInForce", agreementInForce)
                .append("effectiveDate", effectiveDate)
                .append("terminationDate", terminationDate)
                .append("billScheduleTypeE", billScheduleTypeE)
                .toString();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final PropertyLeaseAgreementLite propertyLeaseAgreementLite = new PropertyLeaseAgreementLite();

        public Builder id(Long id) {
            id= id;
            return this;
        }

        public Builder fixedCost(Double fixedCost) {
            fixedCost= fixedCost;
            return this;
        }

        public Builder agreementInForce(boolean agreementInForce) {
            agreementInForce= agreementInForce;
            return this;
        }

        public Builder effectiveDate(DateTime effectiveDate) {
            effectiveDate= effectiveDate;
            return this;
        }

        public Builder terminationDate(DateTime terminationDate) {
            terminationDate= terminationDate;
            return this;
        }

        public Builder billScheduleTypeE(BillScheduleTypeE billScheduleTypeE) {
            billScheduleTypeE= billScheduleTypeE;
            return this;
        }

        public PropertyLeaseAgreementLite build() {
            return PropertyLeaseAgreementLite.builder().build();
        }
    }
}
