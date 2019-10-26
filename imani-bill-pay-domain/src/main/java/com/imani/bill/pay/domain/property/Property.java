package com.imani.bill.pay.domain.property;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.common.collect.ImmutableSet;
import com.imani.bill.pay.domain.AuditableRecord;
import com.imani.bill.pay.domain.geographical.Borough;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.util.Assert;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Models all data to uniquely identify a property by attributes captured at the municipality level.
 * For example in NYC all below attributes will help to uniquely identify a property and find all information
 * stored by the City on the property.
 *
 * TODO:  Currently this model is designed to support US Based properties.  In future refactor to support international addresses.
 *
 * @author manyce400
 */
@Entity
@Table(name="Property", indexes = {@Index(name = "Property_Finder_IDX", columnList = "PropertyNumber, StreetName, ZipCode, BoroughID")})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Property extends AuditableRecord {



    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ID", nullable=false)
    private Long id;


    @Column(name="PropertyNumber", nullable=false, length=50)
    private String propertyNumber;


    @Column(name="StreetName", nullable=false, length = 300)
    private String streetName;


    // Tax Block for the building
    @Column(name="Block", nullable=true, length = 30)
    private String block;


    // Tax lot for the building.
    @Column(name="Lot", nullable=true, length = 30)
    private String lot;


    // Buliding Identification Number is a unique number used to identify the building by City
    @Column(name="BIN", nullable=true, length=30)
    private String buildingIdentificationNumber;


    @Column(name="ZipCode", nullable=false, length=30)
    private String zipCode;

    @Column(name="Latitude", nullable=true)
    private Double latitude;

    @Column(name="Longitude", nullable=true)
    private Double longitude;


    @Column(name="PropertyTypeE", nullable=false, length=20)
    @Enumerated(EnumType.STRING)
    private PropertyTypeE propertyTypeE;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "BoroughID", nullable = false)
    private Borough borough;


    // Maps to optional PropertyManagement firm responsible for managing the property
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "PropertyManagerID", nullable = true)
    private PropertyManager propertyManager;


    // Maps to optional PropertyOwner individual that actually owns the property.
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "PropertyOwnerID", nullable = true)
    private PropertyOwner propertyOwner;


    // Determines how many days a monthly rental payment can be late for till before late fee's are applied.
    @Column(name="MthlyNumberOfDaysPaymentLate", nullable=true)
    private Integer mthlyNumberOfDaysPaymentLate;


    // Contains collection of all Floors that are in this property
    @OneToMany(cascade=CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "property")
    private Set<Floor> floors = new HashSet<>();


    public Property() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPropertyNumber() {
        return propertyNumber;
    }

    public void setPropertyNumber(String propertyNumber) {
        this.propertyNumber = propertyNumber;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
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

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
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

    public Borough getBorough() {
        return borough;
    }

    public void setBorough(Borough borough) {
        this.borough = borough;
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

    public Integer getMthlyNumberOfDaysPaymentLate() {
        return mthlyNumberOfDaysPaymentLate;
    }

    public void setMthlyNumberOfDaysPaymentLate(Integer mthlyNumberOfDaysPaymentLate) {
        this.mthlyNumberOfDaysPaymentLate = mthlyNumberOfDaysPaymentLate;
    }

    public Set<Floor> getFloors() {
        return ImmutableSet.copyOf(floors);
    }

    public void addToFloors(Floor floor) {
        Assert.notNull(floor, "floor cannot be null");
        floors.add(floor);
    }

    @JsonIgnore
    public String getPrintableAddress() {
        StringBuilder sb = new StringBuilder()
                .append(propertyNumber).append(" ")
                .append(streetName).append(" ")
                .append(borough.getName()).append(" ")
                .append(borough.getCity().getName()).append(" ")
                .append(borough.getCity().getState().getName()).append("")
                .append(zipCode);
        return sb.toString();
    }

    public PropertyIndex toPropertyIndex() {
        PropertyIndex propertyIndex = PropertyIndex.builder()
                .id(this.id)
                .propertyNumber(this.propertyNumber)
                .streetName(this.streetName)
                .borough(this.borough.getName())
                .city(this.borough.getCity().getName())
                .state(this.borough.getCity().getState().getName())
                .zipCode(this.zipCode)
                .build();

        // set PropertyOwner, PropertyManager and total number of floors
        if(this.propertyOwner != null) {
            propertyIndex.setPropertyOwner(this.propertyOwner.getBusinessName());
        }

        if(this.propertyManager != null) {
            propertyIndex.setPropertyManager(propertyManager.getName());
        }

        // set total number of floors
        propertyIndex.setTotalFloors(this.floors.size());
        return propertyIndex;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Property property = (Property) o;

        return new EqualsBuilder()
                .append(id, property.id)
                .append(propertyNumber, property.propertyNumber)
                .append(streetName, property.streetName)
                .append(block, property.block)
                .append(lot, property.lot)
                .append(buildingIdentificationNumber, property.buildingIdentificationNumber)
                .append(zipCode, property.zipCode)
                .append(latitude, property.latitude)
                .append(longitude, property.longitude)
                .append(propertyTypeE, property.propertyTypeE)
                .append(borough, property.borough)
                .append(propertyManager, property.propertyManager)
                .append(propertyOwner, property.propertyOwner)
                .append(mthlyNumberOfDaysPaymentLate, property.mthlyNumberOfDaysPaymentLate)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .append(propertyNumber)
                .append(streetName)
                .append(block)
                .append(lot)
                .append(buildingIdentificationNumber)
                .append(zipCode)
                .append(latitude)
                .append(longitude)
                .append(propertyTypeE)
                .append(borough)
                .append(propertyManager)
                .append(propertyOwner)
                .append(mthlyNumberOfDaysPaymentLate)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("propertyNumber", propertyNumber)
                .append("streetName", streetName)
                .append("block", block)
                .append("lot", lot)
                .append("buildingIdentificationNumber", buildingIdentificationNumber)
                .append("zipCode", zipCode)
                .append("latitude", latitude)
                .append("longitude", longitude)
                .append("propertyTypeE", propertyTypeE)
                .append("borough", borough)
                .append("propertyManager", propertyManager)
                .append("propertyOwner", propertyOwner)
                .append("mthlyNumberOfDaysPaymentLate", mthlyNumberOfDaysPaymentLate)
                .append("floors", floors)
                .toString();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private Property property = new Property();

        public Builder propertyNumber(String propertyNumber) {
            property.propertyNumber = propertyNumber;
            return this;
        }

        public Builder streetName(String streetName) {
            property.streetName = streetName;
            return this;
        }

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

        public Builder zipCode(String zipCode) {
            property.zipCode = zipCode;
            return this;
        }

        public Builder propertyTypeE(PropertyTypeE propertyTypeE) {
            property.propertyTypeE = propertyTypeE;
            return this;
        }

        public Builder borough(Borough borough) {
            property.borough = borough;
            return this;
        }

        public Builder propertyManager(PropertyManager propertyManager) {
            property.propertyManager = propertyManager;
            return this;
        }

        public Builder propertyOwner(PropertyOwner propertyOwner) {
            property.propertyOwner = propertyOwner;
            return this;
        }

        public Builder mthlyNumberOfDaysPaymentLate(Integer mthlyNumberOfDaysPaymentLate) {
            property.mthlyNumberOfDaysPaymentLate = mthlyNumberOfDaysPaymentLate;
            return this;
        }


        public Property build() {
            return property;
        }
    }
}
