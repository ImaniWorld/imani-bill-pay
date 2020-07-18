package com.imani.bill.pay.domain.property;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.imani.bill.pay.domain.AuditableRecord;
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
@Entity
@Table(name="LeaseAgreement")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LeaseAgreement extends AuditableRecord {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ID", nullable=false)
    private Long id;


    @Column(name="AgreementDocument", nullable=true, length = 100)
    private String agreementDocument;


    // Captures recorded montly rental cost
    @Column(name="MonthlyRentalCost", nullable=false)
    private Double monthlyRentalCost;


    // Tracks if tenant accepted the rental agreement
    @Column(name="TenantAcceptedAgreement", nullable = true, columnDefinition = "TINYINT", length = 1)
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean tenantAcceptedAgreement;


    @Column(name = "TenantAcceptanceDate", nullable = true)
    @Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @CreatedDate
    private DateTime tenantAcceptanceDate;


    // Tracks if property manager has accepted agreement
    @Column(name="PropertyManagerAcceptedAgreement", nullable = true, columnDefinition = "TINYINT", length = 1)
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean propertyManagerAcceptedAgreement;


    @Column(name = "PropertyManagerAcceptanceDate", nullable = true)
    @Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @CreatedDate
    private DateTime propertyManagerAcceptanceDate;


    // Tracks if property manager has accepted agreement
    @Column(name="PropertyOwnerAcceptedAgreement", nullable = true, columnDefinition = "TINYINT", length = 1)
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean propertyOwnerAcceptedAgreement;


    @Column(name = "PropertyOwnerAcceptanceDate", nullable = true)
    @Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @CreatedDate
    private DateTime propertyOwnerAcceptanceDate;


    @Column(name = "EffectiveDate", nullable = false)
    @Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @CreatedDate
    private DateTime effectiveDate;


    // Tracks date when this agreement gets terminated
    @Column(name = "TerminationDate", nullable = false)
    @Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @CreatedDate
    private DateTime terminationDate;


    // Tracks this LeaseAgreement is in effect, note that a termination date should always be set when this field gets set
    @Column(name="AgreementInEffect", nullable = true, columnDefinition = "TINYINT", length = 1)
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean agreementInEffect;


    // Tracks the User renter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UserRecordID", nullable = false)
    private UserRecord userRecord;


    // Tracks the optional PropertyManager that established the agreement.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PropertyManagerID", nullable = true)
    private PropertyManager propertyManager;


    // Tracks optional PropertyOwner that established the agreement.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PropertyOwnerID", nullable = true)
    private PropertyOwner propertyOwner;


    @Column(name="LeaseAgreementType", nullable=false, length=20)
    @Enumerated(EnumType.STRING)
    private LeaseAgreementTypeE leaseAgreementTypeE;


    // Tracks  Property that this agreement was made on
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PropertyID", nullable = true)
    private Property property;


    // Tracks  Apartment that this agreement was made on
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ApartmentID", nullable = true)
    private Apartment apartment;


    public LeaseAgreement() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAgreementDocument() {
        return agreementDocument;
    }

    public void setAgreementDocument(String agreementDocument) {
        this.agreementDocument = agreementDocument;
    }

    public Double getMonthlyRentalCost() {
        return monthlyRentalCost;
    }

    public void setMonthlyRentalCost(Double monthlyRentalCost) {
        this.monthlyRentalCost = monthlyRentalCost;
    }

    public boolean isTenantAcceptedAgreement() {
        return tenantAcceptedAgreement;
    }

    public void setTenantAcceptedAgreement(boolean tenantAcceptedAgreement) {
        this.tenantAcceptedAgreement = tenantAcceptedAgreement;
    }

    public DateTime getTenantAcceptanceDate() {
        return tenantAcceptanceDate;
    }

    public void setTenantAcceptanceDate(DateTime tenantAcceptanceDate) {
        this.tenantAcceptanceDate = tenantAcceptanceDate;
    }

    public boolean isPropertyManagerAcceptedAgreement() {
        return propertyManagerAcceptedAgreement;
    }

    public void setPropertyManagerAcceptedAgreement(boolean propertyManagerAcceptedAgreement) {
        this.propertyManagerAcceptedAgreement = propertyManagerAcceptedAgreement;
    }

    public DateTime getPropertyManagerAcceptanceDate() {
        return propertyManagerAcceptanceDate;
    }

    public void setPropertyManagerAcceptanceDate(DateTime propertyManagerAcceptanceDate) {
        this.propertyManagerAcceptanceDate = propertyManagerAcceptanceDate;
    }

    public boolean isPropertyOwnerAcceptedAgreement() {
        return propertyOwnerAcceptedAgreement;
    }

    public void setPropertyOwnerAcceptedAgreement(boolean propertyOwnerAcceptedAgreement) {
        this.propertyOwnerAcceptedAgreement = propertyOwnerAcceptedAgreement;
    }

    public DateTime getPropertyOwnerAcceptanceDate() {
        return propertyOwnerAcceptanceDate;
    }

    public void setPropertyOwnerAcceptanceDate(DateTime propertyOwnerAcceptanceDate) {
        this.propertyOwnerAcceptanceDate = propertyOwnerAcceptanceDate;
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

    public boolean isAgreementInEffect() {
        return agreementInEffect;
    }

    public void setAgreementInEffect(boolean agreementInEffect) {
        this.agreementInEffect = agreementInEffect;
    }

    public UserRecord getUserRecord() {
        return userRecord;
    }

    public void setUserRecord(UserRecord userRecord) {
        this.userRecord = userRecord;
    }

    public PropertyManager getPropertyManager() {
        return propertyManager;
    }

    public void setPropertyManager(PropertyManager propertyManager) {
        this.propertyManager = propertyManager;
    }

    public PropertyOwner getPropertyOwner() {
        return propertyOwner;
    }

    public void setPropertyOwner(PropertyOwner propertyOwner) {
        this.propertyOwner = propertyOwner;
    }

    public LeaseAgreementTypeE getLeaseAgreementTypeE() {
        return leaseAgreementTypeE;
    }

    public void setLeaseAgreementTypeE(LeaseAgreementTypeE leaseAgreementTypeE) {
        this.leaseAgreementTypeE = leaseAgreementTypeE;
    }

    public Property getProperty() {
        return property;
    }

    public void setProperty(Property property) {
        this.property = property;
    }

    public Apartment getApartment() {
        return apartment;
    }

    public void setApartment(Apartment apartment) {
        this.apartment = apartment;
    }

    public LeaseAgreementLite toLeaseAgreementLite() {
        LeaseAgreementLite leaseAgreementLite = LeaseAgreementLite.builder()
                .id(id)
                .fixedCost(monthlyRentalCost)
                .effectiveDate(effectiveDate)
                .terminationDate(terminationDate)
                .agreementInForce(agreementInEffect)
                .billScheduleTypeE(BillScheduleTypeE.MONTHLY)
                .build();
        return leaseAgreementLite;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("agreementDocument", agreementDocument)
                .append("monthlyRentalCost", monthlyRentalCost)
                .append("tenantAcceptedAgreement", tenantAcceptedAgreement)
                .append("tenantAcceptanceDate", tenantAcceptanceDate)
                .append("propertyManagerAcceptedAgreement", propertyManagerAcceptedAgreement)
                .append("propertyManagerAcceptanceDate", propertyManagerAcceptanceDate)
                .append("propertyOwnerAcceptedAgreement", propertyOwnerAcceptedAgreement)
                .append("propertyOwnerAcceptanceDate", propertyOwnerAcceptanceDate)
                .append("effectiveDate", effectiveDate)
                .append("terminationDate", terminationDate)
                .append("agreementInEffect", agreementInEffect)
                .append("userRecord", userRecord)
                .append("propertyManager", propertyManager)
                .append("propertyOwner", propertyOwner)
                .append("leaseAgreementTypeE", leaseAgreementTypeE)
                .append("property", property)
                .append("apartment", apartment)
                .toString();
    }

    public static Builder builder() {
        return new Builder();
    }


    public static final class Builder {

        private LeaseAgreement leaseAgreement = new LeaseAgreement();

        public Builder agreementDocument(String agreementDocument) {
            leaseAgreement.agreementDocument = agreementDocument;
            return this;
        }

        public Builder monthlyRentalCost(Double monthlyRentalCost) {
            leaseAgreement.monthlyRentalCost = monthlyRentalCost;
            return this;
        }

        public Builder tenantAcceptedAgreement(boolean tenantAcceptedAgreement) {
            leaseAgreement.tenantAcceptedAgreement = tenantAcceptedAgreement;
            return this;
        }

        public Builder tenantAcceptanceDate(DateTime tenantAcceptanceDate) {
            leaseAgreement.tenantAcceptanceDate = tenantAcceptanceDate;
            return this;
        }

        public Builder propertyManagerAcceptedAgreement(boolean propertyManagerAcceptedAgreement) {
            leaseAgreement.propertyManagerAcceptedAgreement = propertyManagerAcceptedAgreement;
            return this;
        }

        public Builder propertyManagerAcceptanceDate(DateTime propertyManagerAcceptanceDate) {
            leaseAgreement.propertyManagerAcceptanceDate = propertyManagerAcceptanceDate;
            return this;
        }

        public Builder propertyOwnerAcceptedAgreement(boolean propertyOwnerAcceptedAgreement) {
            leaseAgreement.propertyOwnerAcceptedAgreement = propertyOwnerAcceptedAgreement;
            return this;
        }

        public Builder propertyOwnerAcceptanceDate(DateTime propertyOwnerAcceptanceDate) {
            leaseAgreement.propertyOwnerAcceptanceDate = propertyOwnerAcceptanceDate;
            return this;
        }

        public Builder effectiveDate(DateTime effectiveDate) {
            leaseAgreement.effectiveDate = effectiveDate;
            return this;
        }

        public Builder terminationDate(DateTime terminationDate) {
            leaseAgreement.terminationDate = terminationDate;
            return this;
        }

        public Builder agreementInEffect(boolean agreementInEffect) {
            leaseAgreement.agreementInEffect = agreementInEffect;
            return this;
        }

        public Builder userRecord(UserRecord userRecord) {
            leaseAgreement.userRecord = userRecord;
            return this;
        }

        public Builder propertyManager(PropertyManager propertyManager) {
            leaseAgreement.propertyManager = propertyManager;
            return this;
        }

        public Builder propertyOwner(PropertyOwner propertyOwner) {
            leaseAgreement.propertyOwner = propertyOwner;
            return this;
        }

        public Builder leaseAgreementTypeE(LeaseAgreementTypeE leaseAgreementTypeE) {
            leaseAgreement.leaseAgreementTypeE = leaseAgreementTypeE;
            return this;
        }

        public Builder property(Property property) {
            leaseAgreement.property = property;
            return this;
        }

        public Builder apartment(Apartment apartment) {
            leaseAgreement.apartment = apartment;
            return this;
        }

        public LeaseAgreement build() {
            return leaseAgreement;
        }

    }

}
