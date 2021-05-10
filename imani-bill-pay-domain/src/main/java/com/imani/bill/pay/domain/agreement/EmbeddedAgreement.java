package com.imani.bill.pay.domain.agreement;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.imani.bill.pay.domain.billing.BillScheduleTypeE;
import com.imani.bill.pay.domain.business.Business;
import com.imani.bill.pay.domain.geographical.Community;
import com.imani.bill.pay.domain.property.Property;
import com.imani.bill.pay.domain.user.UserRecord;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.*;

/**
 * @author manyce400
 */
@Embeddable
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EmbeddedAgreement {


    // Captures cost per bill schedule to be applied excluding any fees
    @Column(name="FixedCost")
    private Double fixedCost;


    // Determines the billing schedule/cycle for this agreement
    @Column(name="BillScheduleTypeE", nullable=false, length=20)
    @Enumerated(EnumType.STRING)
    private BillScheduleTypeE billScheduleTypeE;


    // Identifies if this agreement is still in force.  Imani BillPay will auto generate bills if in force
    @Column(name="AgreementInForce", nullable = true, columnDefinition = "TINYINT", length = 1)
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean agreementInForce;


    // Specifies the number of days past a payment due date for a payment to be considered late
    // This can be overridden if set to null using any other stored relevant data or logic based on implementation
    @Column(name="NumberOfDaysTillLate", nullable=true)
    private Integer numberOfDaysTillLate;


    // Tracks the effective date of the agreement not created date
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "EffectiveDate", nullable = false)
    @Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime effectiveDate;


    // Tracks date when this agreement gets terminated
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "TerminationDate")
    @Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime terminationDate;


    // Optional agreement document if available that stipulates all aspects of agreement
    @Column(name="AgreementDocument", nullable=true, length = 100)
    private String agreementDocument;

    // Optional UserRecord for agreement.  User will be responsible for payments.
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "AgreementUserRecordID")
    private UserRecord agreementUserRecord;

    // Optional Community for agreement.  Community will be responsible for payments.
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "AgreementPropertyID")
    private Property agreementProperty;

    // Optional Business for agreement.  Business will be responsible for payments.
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "AgreementBusinessID")
    private Business agreementBusiness;

    // Optional Community for agreement.  Community will be responsible for payments.
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "AgreementCommunityID")
    private Community agreementCommunity;


    public EmbeddedAgreement() {

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

    public UserRecord getAgreementUserRecord() {
        return agreementUserRecord;
    }

    public void setAgreementUserRecord(UserRecord agreementUserRecord) {
        this.agreementUserRecord = agreementUserRecord;
    }

    public Property getAgreementProperty() {
        return agreementProperty;
    }

    public void setAgreementProperty(Property agreementProperty) {
        this.agreementProperty = agreementProperty;
    }

    public Business getAgreementBusiness() {
        return agreementBusiness;
    }

    public void setAgreementBusiness(Business agreementBusiness) {
        this.agreementBusiness = agreementBusiness;
    }

    public Community getAgreementCommunity() {
        return agreementCommunity;
    }

    public void setAgreementCommunity(Community agreementCommunity) {
        this.agreementCommunity = agreementCommunity;
    }

    public boolean isBilledEntityUser() {
        return agreementUserRecord != null;
    }

    public boolean isBilledEntityBusiness() {
        return agreementBusiness != null;
    }

    public boolean isBilledEntityCommunity() {
        return agreementCommunity != null;
    }

    public String getBilledEntity() {
        StringBuffer sb = new StringBuffer("");

        if(isBilledEntityUser()) {
            sb.append("User(").append(agreementUserRecord.getEmbeddedContactInfo().getEmail()).append(")");
        } else if(isBilledEntityBusiness()) {
            sb.append("Business(").append(agreementBusiness.getName()).append(")");
        } else if(isBilledEntityCommunity()) {
            sb.append("Community(").append(agreementCommunity.getCommunityName()).append(")");
        }

        return sb.toString();
    }

    public EmbeddedAgreementLite toEmbeddedLite() {
        EmbeddedAgreementLite embeddedAgreementLite = EmbeddedAgreementLite.builder()
                .fixedCost(fixedCost)
                .billScheduleTypeE(billScheduleTypeE)
                .agreementInForce(agreementInForce)
                .effectiveDate(effectiveDate)
                .terminationDate(terminationDate)
                .numberOfDaysTillLate(numberOfDaysTillLate)
                .agreementDocument(agreementDocument)
                .agreementUserRecord(agreementUserRecord.toUserRecordLite())
                .agreementProperty(agreementProperty.toPropertyLite())
                .build();
        return embeddedAgreementLite;
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
                .append("agreementBusiness", agreementBusiness)
                .append("agreementCommunity", agreementCommunity)
                .toString();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final EmbeddedAgreement embeddedAgreement = new EmbeddedAgreement();

        public Builder fixedCost(Double fixedCost) {
            embeddedAgreement.fixedCost = fixedCost;
            return this;
        }

        public Builder billScheduleTypeE(BillScheduleTypeE billScheduleTypeE) {
            embeddedAgreement.billScheduleTypeE = billScheduleTypeE;
            return this;
        }

        public Builder agreementInForce(boolean agreementInForce) {
            embeddedAgreement.agreementInForce = agreementInForce;
            return this;
        }

        public Builder numberOfDaysTillLate(Integer numberOfDaysTillLate) {
            embeddedAgreement.numberOfDaysTillLate = numberOfDaysTillLate;
            return this;
        }

        public Builder effectiveDate(DateTime effectiveDate) {
            embeddedAgreement.effectiveDate = effectiveDate;
            return this;
        }

        public Builder terminationDate(DateTime terminationDate) {
            embeddedAgreement.terminationDate = terminationDate;
            return this;
        }

        public Builder agreementDocument(String agreementDocument) {
            embeddedAgreement.agreementDocument = agreementDocument;
            return this;
        }

        public Builder agreementUserRecord(UserRecord agreementUserRecord) {
            embeddedAgreement.agreementUserRecord = agreementUserRecord;
            return this;
        }

        public Builder agreementProperty(Property agreementProperty) {
            embeddedAgreement.agreementProperty = agreementProperty;
            return this;
        }

        public Builder agreementBusiness(Business agreementBusiness) {
            embeddedAgreement.agreementBusiness = agreementBusiness;
            return this;
        }

        public Builder agreementCommunity(Community agreementCommunity) {
            embeddedAgreement.agreementCommunity = agreementCommunity;
            return this;
        }

        public EmbeddedAgreement build() {
            return embeddedAgreement;
        }

    }

}
