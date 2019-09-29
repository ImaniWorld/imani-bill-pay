package com.imani.bill.pay.domain.property;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.data.solr.core.mapping.Indexed;
import org.springframework.data.solr.core.mapping.SolrDocument;

/**
 * @author manyce400
 */
@SolrDocument(solrCoreName = "PropertyIndexCore")
public class PropertyIndex {




    @Indexed(name = "id", type = "long")
    private Long id;


    @Indexed(name = "propertyNumber", type = "string")
    private String propertyNumber;


    @Indexed(name = "streetName", type = "string")
    private String streetName;


    @Indexed(name = "borough", type = "string")
    private String borough;


    @Indexed(name = "city", type = "string")
    private String city;


    @Indexed(name = "state", type = "string")
    private String state;


    @Indexed(name = "zipCode", type = "string")
    private String zipCode;


    @Indexed(name = "propertyManager", type = "string")
    private String propertyManager;


    @Indexed(name = "propertyOwner", type = "string")
    private String propertyOwner;


    @Indexed(name = "totalFloors", type = "integer")
    private Integer totalFloors;



    public PropertyIndex() {

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

    public String getBorough() {
        return borough;
    }

    public void setBorough(String borough) {
        this.borough = borough;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getPropertyManager() {
        return propertyManager;
    }

    public void setPropertyManager(String propertyManager) {
        this.propertyManager = propertyManager;
    }

    public String getPropertyOwner() {
        return propertyOwner;
    }

    public void setPropertyOwner(String propertyOwner) {
        this.propertyOwner = propertyOwner;
    }

    public Integer getTotalFloors() {
        return totalFloors;
    }

    public void setTotalFloors(Integer totalFloors) {
        this.totalFloors = totalFloors;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("propertyNumber", propertyNumber)
                .append("streetName", streetName)
                .append("borough", borough)
                .append("city", city)
                .append("state", state)
                .append("zipCode", zipCode)
                .append("propertyManager", propertyManager)
                .append("propertyOwner", propertyOwner)
                .append("totalFloors", totalFloors)
                .toString();
    }

    public static Builder builder() {
        return new Builder();
    }


    public static final class Builder {

        private PropertyIndex propertyIndex = new PropertyIndex();

        public Builder id(Long id) {
            propertyIndex.id = id;
            return this;
        }

        public Builder propertyNumber(String propertyNumber) {
            propertyIndex.propertyNumber = propertyNumber;
            return this;
        }

        public Builder streetName(String streetName) {
            propertyIndex.streetName = streetName;
            return this;
        }

        public Builder propertyManager(String propertyManager) {
            propertyIndex.propertyManager = propertyManager;
            return this;
        }

        public Builder borough(String borough) {
            propertyIndex.borough = borough;
            return this;
        }

        public Builder city(String city) {
            propertyIndex.city = city;
            return this;
        }

        public Builder state(String state) {
            propertyIndex.state = state;
            return this;
        }

        public Builder zipCode(String zipCode) {
            propertyIndex.zipCode = zipCode;
            return this;
        }

        public Builder propertyOwner(String propertyOwner) {
            propertyIndex.propertyOwner = propertyOwner;
            return this;
        }

        public Builder totalFloors(Integer totalFloors) {
            propertyIndex.totalFloors = totalFloors;
            return this;
        }

        public PropertyIndex build() {
            return propertyIndex;
        }

    }

}
