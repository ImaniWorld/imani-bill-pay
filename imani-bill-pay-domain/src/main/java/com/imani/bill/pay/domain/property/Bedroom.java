package com.imani.bill.pay.domain.property;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.imani.bill.pay.domain.AuditableRecord;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.annotations.Type;

import javax.persistence.*;

/**
 * @author manyce400
 */
@Entity
@Table(name="Bedroom")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Bedroom extends AuditableRecord {



    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ID", nullable=false)
    private Long id;


    // We track the possiblity that individual bedrooms can be rented and generate income
    @Column(name="SquareFootage", nullable=true)
    private Long squareFootage;


    // Identifies this ACH Bank Account as the primary account
    @Column(name="IsMasterBedroom", nullable = true, columnDefinition = "TINYINT", length = 1)
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean isMasterBedroom;


    // We track the possiblity that individual bedrooms can be rented and generate income
    @Column(name="MonthlyRent", nullable=true)
    private Double monthlyRent;


    // Tracks
    @Column(name="PhotoLocation", nullable=true, length = 100)
    private String photoLocation;


    // Tracks the Apartment that tis bedroom is in
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ApartmentID", nullable = true)
    private Apartment apartment;


    public Bedroom() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSquareFootage() {
        return squareFootage;
    }

    public void setSquareFootage(Long squareFootage) {
        this.squareFootage = squareFootage;
    }

    public boolean isMasterBedroom() {
        return isMasterBedroom;
    }

    public void setMasterBedroom(boolean masterBedroom) {
        isMasterBedroom = masterBedroom;
    }

    public Double getMonthlyRent() {
        return monthlyRent;
    }

    public void setMonthlyRent(Double monthlyRent) {
        this.monthlyRent = monthlyRent;
    }

    public String getPhotoLocation() {
        return photoLocation;
    }

    public void setPhotoLocation(String photoLocation) {
        this.photoLocation = photoLocation;
    }

    public Apartment getApartment() {
        return apartment;
    }

    public void setApartment(Apartment apartment) {
        this.apartment = apartment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Bedroom bedroom = (Bedroom) o;

        return new EqualsBuilder()
                .append(isMasterBedroom, bedroom.isMasterBedroom)
                .append(id, bedroom.id)
                .append(squareFootage, bedroom.squareFootage)
                .append(monthlyRent, bedroom.monthlyRent)
                .append(photoLocation, bedroom.photoLocation)
                .append(apartment, bedroom.apartment)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .append(squareFootage)
                .append(isMasterBedroom)
                .append(monthlyRent)
                .append(photoLocation)
                .append(apartment)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("squareFootage", squareFootage)
                .append("isMasterBedroom", isMasterBedroom)
                .append("monthlyRent", monthlyRent)
                .append("photoLocation", photoLocation)
                .append("apartment", apartment)
                .toString();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private Bedroom bedroom = new Bedroom();

        public Builder squareFootage(Long squareFootage) {
            bedroom.squareFootage = squareFootage;
            return this;
        }

        public Builder isMasterBedroom(boolean isMasterBedroom) {
            bedroom.isMasterBedroom = isMasterBedroom;
            return this;
        }

        public Builder photoLocation(String photoLocation) {
            bedroom.photoLocation = photoLocation;
            return this;
        }

        public Builder apartment(Apartment apartment) {
            bedroom.apartment = apartment;
            return this;
        }

        public Bedroom build() {
            return bedroom;
        }
    }
}
