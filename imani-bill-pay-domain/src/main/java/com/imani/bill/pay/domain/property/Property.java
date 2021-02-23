package com.imani.bill.pay.domain.property;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.common.collect.ImmutableSet;
import com.imani.bill.pay.domain.AuditableRecord;
import com.imani.bill.pay.domain.contact.Address;
import com.imani.bill.pay.domain.geographical.Community;
import com.imani.bill.pay.domain.user.UserRecord;
import com.imani.bill.pay.domain.utility.UtilityServiceArea;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.util.Assert;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Models all data to uniquely identify a property by attributes captured at the municipality level.
 *
 * TODO:  Currently this model is designed to support US Based properties.  In future refactor to support international addresses.
 *
 * @author manyce400
 */
@Entity
@Table(name="Property")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Property extends AuditableRecord {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ID", nullable=false)
    private Long id;

    // Tax Block for the building
    @Column(name="Block", nullable=true, length = 30)
    private String block;

    // Tax lot for the building.
    @Column(name="Lot", nullable=true, length = 30)
    private String lot;

    // Buliding Identification Number is a unique number used to identify the building by City
    @Column(name="BIN", nullable=true, length=30)
    private String buildingIdentificationNumber;

    @Column(name="Latitude", nullable=true)
    private Double latitude;

    @Column(name="Longitude", nullable=true)
    private Double longitude;

    @Column(name="PropertyTypeE", nullable=false, length=20)
    @Enumerated(EnumType.STRING)
    private PropertyTypeE propertyTypeE;

    // Determines how many days a monthly rental payment can be late for till before late fee's are applied.
    @Column(name="MthlyNumberOfDaysPaymentLate", nullable=true)
    private Integer mthlyNumberOfDaysPaymentLate;

    // Based on the location of the property, this is an optional field. Setup mainly for NYC properties
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "AddressID", nullable = false)
    private Address address;

    // Represents optional Community that this property is part of
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "CommunityID")
    private Community community;

    // Maps to optional PropertyOwner individual that actually owns the property.
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "OwnerID", nullable = true)
    private UserRecord owner;

    // Contains collection of all Floors that are in this property
    @OneToMany(cascade=CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "property")
    private Set<Floor> floors = new HashSet<>();

    @OneToMany(cascade=CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "property")
    private Set<UtilityServiceArea> utilityServiceAreas = new HashSet<>();


    public Property() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBlock() {
        return block;
    }

    public void setBlock(String block) {
        this.block = block;
    }

    public String getLot() {
        return lot;
    }

    public void setLot(String lot) {
        this.lot = lot;
    }

    public String getBuildingIdentificationNumber() {
        return buildingIdentificationNumber;
    }

    public void setBuildingIdentificationNumber(String buildingIdentificationNumber) {
        this.buildingIdentificationNumber = buildingIdentificationNumber;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public PropertyTypeE getPropertyTypeE() {
        return propertyTypeE;
    }

    public void setPropertyTypeE(PropertyTypeE propertyTypeE) {
        this.propertyTypeE = propertyTypeE;
    }

    public Integer getMthlyNumberOfDaysPaymentLate() {
        return mthlyNumberOfDaysPaymentLate;
    }

    public void setMthlyNumberOfDaysPaymentLate(Integer mthlyNumberOfDaysPaymentLate) {
        this.mthlyNumberOfDaysPaymentLate = mthlyNumberOfDaysPaymentLate;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Community getCommunity() {
        return community;
    }

    public void setCommunity(Community community) {
        this.community = community;
    }

    public UserRecord getOwner() {
        return owner;
    }

    public void setOwner(UserRecord owner) {
        this.owner = owner;
    }

    public Set<Floor> getFloors() {
        return ImmutableSet.copyOf(floors);
    }

    public void addToFloors(Floor floor) {
        Assert.notNull(floor, "floor cannot be null");
        floors.add(floor);
    }

    public void addUtilityServiceArea(UtilityServiceArea utilityServiceArea) {
        Assert.notNull(utilityServiceArea, "UtilityServiceArea cannot be null");
        utilityServiceAreas.add(utilityServiceArea);
    }

    public Set<UtilityServiceArea> getUtilityServiceAreas() {
        return ImmutableSet.copyOf(utilityServiceAreas);
    }


//    public PropertyIndex toPropertyIndex() {
//        PropertyIndex propertyIndex = PropertyIndex.builder()
//                .id(this.id)
//                .propertyNumber(this.propertyNumber)
//                .streetName(this.streetName)
//                .borough(this.borough.getName())
//                .city(this.borough.getCity().getName())
//                .state(this.borough.getCity().getState().getName())
//                .zipCode(this.zipCode)
//                .build();
//
//        // set PropertyOwner, PropertyManager and total number of floors
//        if(this.propertyOwner != null) {
//            propertyIndex.setPropertyOwner(this.propertyOwner.getBusinessName());
//        }
//
//        if(this.propertyManager != null) {
//            propertyIndex.setPropertyManager(propertyManager.getName());
//        }
//
//        // set total number of floors
//        propertyIndex.setTotalFloors(this.floors.size());
//        return propertyIndex;
//    }


    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("block", block)
                .append("lot", lot)
                .append("buildingIdentificationNumber", buildingIdentificationNumber)
                .append("latitude", latitude)
                .append("longitude", longitude)
                .append("propertyTypeE", propertyTypeE)
                .append("mthlyNumberOfDaysPaymentLate", mthlyNumberOfDaysPaymentLate)
                .append("address", address)
                .append("community", community)
                .append("owner", owner)
                .toString();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private Property property = new Property();

        public Builder block(String block) {
            property.block = block;
            return this;
        }

        public Builder lot(String lot) {
            property.lot = lot;
            return this;
        }

        public Builder buildingIdentificationNumber(String buildingIdentificationNumber) {
            property.buildingIdentificationNumber = buildingIdentificationNumber;
            return this;
        }

        public Builder latitude(Double latitude) {
            property.latitude = latitude;
            return this;
        }

        public Builder longitude(Double longitude) {
            property.longitude = longitude;
            return this;
        }

        public Builder propertyTypeE(PropertyTypeE propertyTypeE) {
            property.propertyTypeE = propertyTypeE;
            return this;
        }

        public Builder mthlyNumberOfDaysPaymentLate(Integer mthlyNumberOfDaysPaymentLate) {
            property.mthlyNumberOfDaysPaymentLate = mthlyNumberOfDaysPaymentLate;
            return this;
        }

        public Builder address(Address address) {
            property.address = address;
            return this;
        }

        public Builder community(Community community) {
            property.community = community;
            return this;
        }

        public Builder owner(UserRecord owner) {
            property.owner = owner;
            return this;
        }

        public Property build() {
            return property;
        }
    }
}
