package com.imani.bill.pay.domain.agreement;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.imani.bill.pay.domain.billing.BillScheduleTypeE;
import com.imani.bill.pay.domain.property.PropertyLite;
import com.imani.bill.pay.domain.user.UserRecord;
import com.imani.bill.pay.domain.user.UserRecordLite;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.joda.time.DateTime;

/**
 * @author manyce400
 */
public class EmbeddedAgreementLite {


    private Double fixedCost;

    private BillScheduleTypeE billScheduleTypeE;

    private boolean agreementInForce;

    private Integer numberOfDaysTillLate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private DateTime effectiveDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private DateTime terminationDate;

    private String agreementDocument;

    private UserRecordLite agreementUserRecord;

    private PropertyLite agreementProperty;


    public EmbeddedAgreementLite() {

    }

    public Double getFixedCost() {
        return fixedCost;
    }

    public void setFixedCost(Double fixedCost) {
        this.fixedCost = fixedCost;
    }

    public BillScheduleTypeE getBillScheduleTypeE() {
        return billScheduleTypeE;
    }

    public void setBillScheduleTypeE(BillScheduleTypeE billScheduleTypeE) {
        this.billScheduleTypeE = billScheduleTypeE;
    }

    public boolean isAgreementInForce() {
        return agreementInForce;
    }

    public void setAgreementInForce(boolean agreementInForce) {
        this.agreementInForce = agreementInForce;
    }

    public Integer getNumberOfDaysTillLate() {
        return numberOfDaysTillLate;
    }

    public void setNumberOfDaysTillLate(Integer numberOfDaysTillLate) {
        this.numberOfDaysTillLate = numberOfDaysTillLate;
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

    public String getAgreementDocument() {
        return agreementDocument;
    }

    public void setAgreementDocument(String agreementDocument) {
        this.agreementDocument = agreementDocument;
    }

    public UserRecordLite getAgreementUserRecord() {
        return agreementUserRecord;
    }

    public void setAgreementUserRecord(UserRecordLite agreementUserRecord) {
        this.agreementUserRecord = agreementUserRecord;
    }

    public PropertyLite getAgreementProperty() {
        return agreementProperty;
    }

    public void setAgreementProperty(PropertyLite agreementProperty) {
        this.agreementProperty = agreementProperty;
    }

    public EmbeddedAgreement toEmbeddedAgreement(UserRecord billedUser) {
        EmbeddedAgreement embeddedAgreement = EmbeddedAgreement.builder()
                .fixedCost(fixedCost)
                .billScheduleTypeE(billScheduleTypeE)
                .agreementInForce(agreementInForce)
                .numberOfDaysTillLate(numberOfDaysTillLate)
                .effectiveDate(effectiveDate)
                .terminationDate(terminationDate)
                .agreementDocument(agreementDocument)
                .agreementUserRecord(billedUser)
                .build();
        return embeddedAgreement;
    }

    public static Builder builder() {
        return new Builder();
    }
    
    public static class Builder {
        
        private EmbeddedAgreementLite embeddedAgreementLite = new EmbeddedAgreementLite();
        
        public Builder fixedCost(Double fixedCost) {
            embeddedAgreementLite.fixedCost = fixedCost;
            return this;
        }

        public Builder billScheduleTypeE(BillScheduleTypeE billScheduleTypeE) {
            embeddedAgreementLite.billScheduleTypeE = billScheduleTypeE;
            return this;
        }

        public Builder agreementInForce(boolean agreementInForce) {
            embeddedAgreementLite.agreementInForce = agreementInForce;
            return this;
        }

        public Builder numberOfDaysTillLate(Integer numberOfDaysTillLate) {
            embeddedAgreementLite.numberOfDaysTillLate = numberOfDaysTillLate;
            return this;
        }

        public Builder effectiveDate(DateTime effectiveDate) {
            embeddedAgreementLite.effectiveDate = effectiveDate;
            return this;
        }

        public Builder terminationDate(DateTime terminationDate) {
            embeddedAgreementLite.terminationDate = terminationDate;
            return this;
        }

        public Builder agreementDocument(String agreementDocument) {
            embeddedAgreementLite.agreementDocument = agreementDocument;
            return this;
        }

        public Builder agreementUserRecord(UserRecordLite agreementUserRecord) {
            embeddedAgreementLite.agreementUserRecord = agreementUserRecord;
            return this;
        }

        public Builder agreementProperty(PropertyLite agreementProperty) {
            embeddedAgreementLite.agreementProperty = agreementProperty;
            return this;
        }

        public EmbeddedAgreementLite build() {
            return embeddedAgreementLite;
        }
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("fixedCost", fixedCost)
                .append("billScheduleTypeE", billScheduleTypeE)
                .append("agreementInForce", agreementInForce)
                .append("numberOfDaysTillLate", numberOfDaysTillLate)
                .append("effectiveDate", effectiveDate)
                .append("terminationDate", terminationDate)
                .append("agreementDocument", agreementDocument)
                .append("agreementUserRecord", agreementUserRecord)
                .append("agreementProperty", agreementProperty)
                .toString();
    }

}
