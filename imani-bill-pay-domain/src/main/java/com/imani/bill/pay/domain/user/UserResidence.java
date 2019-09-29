package com.imani.bill.pay.domain.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.common.collect.ImmutableSet;
import com.imani.bill.pay.domain.AuditableRecord;
import com.imani.bill.pay.domain.property.Apartment;
import com.imani.bill.pay.domain.property.Property;
import com.imani.bill.pay.domain.property.PropertyService;
import com.imani.bill.pay.domain.property.RentalAgreement;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.annotations.Type;
import org.springframework.util.Assert;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * @author manyce400
 */
@Entity
@Table(name="UserResidence")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserResidence extends AuditableRecord {



    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ID", nullable=false)
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UserRecordID", nullable = false)
    private UserRecord userRecord;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PropertyID", nullable = true)
    private Property property;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ApartmentID", nullable = true)
    private Apartment apartment;


    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RentalAgreementID", nullable = true)
    private RentalAgreement rentalAgreement;


    @Column(name="IsPrimaryResidence", nullable = true, columnDefinition = "TINYINT", length = 1)
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean primaryResidence;

    // Tracks additional Property Services that this user has signed up for.
    @JsonIgnore
    @OneToMany(cascade=CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "userResidence")
    private Set<UserResidencePropertyService> userResidencePropertyServices = new HashSet<>();


    public UserResidence() {

    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserRecord getUserRecord() {
        return userRecord;
    }

    public void setUserRecord(UserRecord userRecord) {
        this.userRecord = userRecord;
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

    public RentalAgreement getRentalAgreement() {
        return rentalAgreement;
    }

    public void setRentalAgreement(RentalAgreement rentalAgreement) {
        this.rentalAgreement = rentalAgreement;
    }

    public boolean isPrimaryResidence() {
        return primaryResidence;
    }

    public void setPrimaryResidence(boolean primaryResidence) {
        this.primaryResidence = primaryResidence;
    }

    public Set<UserResidencePropertyService> getUserResidencePropertyServices() {
        return ImmutableSet.copyOf(userResidencePropertyServices);
    }

    public void addPropertyService(PropertyService propertyService) {
        Assert.notNull(propertyService, "propertyService cannot be null");
        UserResidencePropertyService userResidencePropertyService = UserResidencePropertyService.builder()
                .active(true)
                .propertyService(propertyService)
                .userResidence(this)
                .build();
        userResidencePropertyServices.add(userResidencePropertyService);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        UserResidence that = (UserResidence) o;

        return new EqualsBuilder()
                .append(id, that.id)
                .append(userRecord, that.userRecord)
                .append(property, that.property)
                .append(apartment, that.apartment)
                .append(rentalAgreement, that.rentalAgreement)
                .append(primaryResidence, that.primaryResidence)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .append(userRecord)
                .append(property)
                .append(apartment)
                .append(rentalAgreement)
                .append(primaryResidence)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("userRecord", userRecord)
                .append("property", property)
                .append("apartment", apartment)
                .append("rentalAgreement", rentalAgreement)
                .append("primaryResidence", primaryResidence)
                .toString();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private final UserResidence userResidence = new UserResidence();

        public Builder userRecord(UserRecord userRecord) {
            userResidence.userRecord = userRecord;
            return this;
        }

        public Builder property(Property property) {
            userResidence.property = property;
            return this;
        }

        public Builder apartment(Apartment apartment) {
            userResidence.apartment = apartment;
            return this;
        }

        public Builder primaryResidence(boolean primaryResidence) {
            userResidence.primaryResidence = primaryResidence;
            return this;
        }

        public Builder rentalAgreement(RentalAgreement rentalAgreement) {
            userResidence.rentalAgreement = rentalAgreement;
            return this;
        }

        public Builder propertyService(PropertyService propertyService) {
            userResidence.addPropertyService(propertyService);
            return this;
        }

        public UserResidence build() {
            return userResidence;
        }

    }
}
