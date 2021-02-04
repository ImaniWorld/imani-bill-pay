package com.imani.bill.pay.domain.agreement;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.imani.bill.pay.domain.billing.BillScheduleTypeE;
import com.imani.bill.pay.domain.user.UserRecord;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;
import org.springframework.data.annotation.CreatedDate;

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
    @CreatedDate
    private DateTime effectiveDate;


    // Tracks date when this agreement gets terminated
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "TerminationDate")
    @Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @CreatedDate
    private DateTime terminationDate;


    // Optional agreement document if available that stipulates all aspects of agreement
    @Column(name="AgreementDocument", nullable=true, length = 100)
    private String agreementDocument;


    // Tracks ImaniBillPay user for which agreement is created.  This user will be responsible for making payments
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "UserRecordID", nullable = false)
    private UserRecord userRecord;


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

    public UserRecord getUserRecord() {
        return userRecord;
    }

    public void setUserRecord(UserRecord userRecord) {
        this.userRecord = userRecord;
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
                .append("userRecord", userRecord)
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

        public Builder userRecord(UserRecord userRecord) {
            embeddedAgreement.userRecord = userRecord;
            return this;
        }

        public EmbeddedAgreement build() {
            return embeddedAgreement;
        }

    }

}
