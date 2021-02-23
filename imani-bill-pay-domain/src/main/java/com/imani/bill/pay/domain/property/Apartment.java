package com.imani.bill.pay.domain.property;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.common.collect.ImmutableSet;
import com.imani.bill.pay.domain.AuditableRecord;
import com.imani.bill.pay.domain.user.UserRecord;
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
@Table(name="Apartment")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Apartment extends AuditableRecord {



    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ID", nullable=false)
    private Long id;


    @Column(name="ApartmentNumber", nullable=false, length = 30)
    private String apartmentNumber;


    @Column(name="IsRented", nullable = true, columnDefinition = "TINYINT", length = 1)
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean isRented;


    // Tracks the floor that this apartment is on
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "FloorID", nullable = true)
    private Floor floor;


    // Tracks the User currently renting this appartment
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RentedByUserID", nullable = true)
    private UserRecord rentedByUser;


    // Contains collection of all bedrooms in this apartment
    @OneToMany(cascade=CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "apartment")
    private Set<Bedroom> bedrooms = new HashSet<>();


    public Apartment() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getApartmentNumber() {
        return apartmentNumber;
    }

    public void setApartmentNumber(String apartmentNumber) {
        this.apartmentNumber = apartmentNumber;
    }

    public boolean isRented() {
        return isRented;
    }

    public void setRented(boolean rented) {
        isRented = rented;
    }

    public Floor getFloor() {
        return floor;
    }

    public void setFloor(Floor floor) {
        this.floor = floor;
    }

    public UserRecord getRentedByUser() {
        return rentedByUser;
    }

    public void setRentedByUser(UserRecord rentedByUser) {
        this.rentedByUser = rentedByUser;
    }

    public Set<Bedroom> getBedrooms() {
        return ImmutableSet.copyOf(bedrooms);
    }

    public void addToBedrooms(Bedroom bedroom) {
        Assert.notNull(bedroom, "bedroom cannot be null");
        this.bedrooms.add(bedroom);
    }

    public String getDescriptiveLocation() {
        StringBuffer sb = new StringBuffer(apartmentNumber)
                .append(" - Floor: ").append(floor.getFloorNumber());
//                .append(" - Property: ").append(floor.getProperty().getPrintableAddress());
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Apartment apartment = (Apartment) o;

        return new EqualsBuilder()
                .append(isRented, apartment.isRented)
                .append(id, apartment.id)
                .append(apartmentNumber, apartment.apartmentNumber)
                .append(floor, apartment.floor)
                .append(rentedByUser, apartment.rentedByUser)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .append(apartmentNumber)
                .append(isRented)
                .append(floor)
                .append(rentedByUser)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("apartmentNumber", apartmentNumber)
                .append("isRented", isRented)
                .append("floor", floor)
                .append("rentedByUser", rentedByUser)
                .toString();
    }


    public static Builder builder() {
        return new Builder();
    }


    public static final class Builder {

        private Apartment apartment = new Apartment();

        public Builder apartmentNumber(String apartmentNumber) {
            apartment.apartmentNumber = apartmentNumber;
            return this;
        }

        public Builder isRented(boolean isRented) {
            apartment.isRented = isRented;
            return this;
        }

        public Builder floor(Floor floor) {
            apartment.floor = floor;
            return this;
        }

        public Builder rentedByUser(UserRecord rentedByUser) {
            apartment.rentedByUser = rentedByUser;
            return this;
        }

        public Builder bedroom(Bedroom bedroom) {
            apartment.addToBedrooms(bedroom);
            return this;
        }

        public Apartment build() {
            return apartment;
        }
    }
}
