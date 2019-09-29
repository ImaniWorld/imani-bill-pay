package com.imani.bill.pay.domain.property;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.data.solr.core.mapping.Indexed;
import org.springframework.data.solr.core.mapping.SolrDocument;

/**
 * @author manyce400
 */
@SolrDocument(solrCoreName = "PropertyEvictionIndexCore")
public class PropertyEvictionIndex {



    @Indexed(name = "id", type = "long")
    private Long id;


    @Indexed(name = "courtIndexNumber", type = "string")
    private String courtIndexNumber;


    @Indexed(name = "docketNumber", type = "string")
    private String docketNumber;


    @Indexed(name = "evictionAptNumber", type = "string")
    private String evictionAptNumber;


    @Indexed(name = "evictionStatus", type = "string")
    private String evictionStatus;


    @Indexed(name = "marshalFirstName", type = "string")
    private String marshalFirstName;


    @Indexed(name = "marshalLastName", type = "string")
    private String marshalLastName;


    // Index Property details to allow searching evictions using property information
    @Indexed(name = "propertyId", type = "long")
    private Long propertyId;


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


    public PropertyEvictionIndex() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCourtIndexNumber() {
        return courtIndexNumber;
    }

    public void setCourtIndexNumber(String courtIndexNumber) {
        this.courtIndexNumber = courtIndexNumber;
    }

    public String getDocketNumber() {
        return docketNumber;
    }

    public void setDocketNumber(String docketNumber) {
        this.docketNumber = docketNumber;
    }

    public String getEvictionAptNumber() {
        return evictionAptNumber;
    }

    public void setEvictionAptNumber(String evictionAptNumber) {
        this.evictionAptNumber = evictionAptNumber;
    }

    public String getEvictionStatus() {
        return evictionStatus;
    }

    public void setEvictionStatus(String evictionStatus) {
        this.evictionStatus = evictionStatus;
    }

    public String getMarshalFirstName() {
        return marshalFirstName;
    }

    public void setMarshalFirstName(String marshalFirstName) {
        this.marshalFirstName = marshalFirstName;
    }

    public String getMarshalLastName() {
        return marshalLastName;
    }

    public void setMarshalLastName(String marshalLastName) {
        this.marshalLastName = marshalLastName;
    }

    public Long getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(Long propertyId) {
        this.propertyId = propertyId;
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

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("courtIndexNumber", courtIndexNumber)
                .append("docketNumber", docketNumber)
                .append("evictionAptNumber", evictionAptNumber)
                .append("evictionStatus", evictionStatus)
                .append("marshalFirstName", marshalFirstName)
                .append("marshalLastName", marshalLastName)
                .append("propertyId", propertyId)
                .append("propertyNumber", propertyNumber)
                .append("streetName", streetName)
                .append("borough", borough)
                .append("city", city)
                .append("state", state)
                .append("zipCode", zipCode)
                .toString();
    }
}
