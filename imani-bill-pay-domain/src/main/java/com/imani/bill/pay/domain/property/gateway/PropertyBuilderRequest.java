package com.imani.bill.pay.domain.property.gateway;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.imani.bill.pay.domain.gateway.GenericAPIGatewayRequest;
import com.imani.bill.pay.domain.property.IHasPropertyData;
import com.imani.bill.pay.domain.property.PropertyTypeE;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * @author manyce400
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PropertyBuilderRequest extends GenericAPIGatewayRequest implements IHasPropertyData {



    private Long boroID;

    private Long cityID;

    private String propertyNumber;

    private String streetName;

    private Integer legalStories;

    private String zipCode;

    private String block;

    private String lot;

    private String bin;

    private Boolean publicHousing;

    private PropertyTypeE propertyTypeE;


    public PropertyBuilderRequest() {

    }

    @Override
    public Long getBoroID() {
        return boroID;
    }

    public void setBoroID(Long boroID) {
        this.boroID = boroID;
    }

    @Override
    public Long getCityID() {
        return cityID;
    }

    public void setCityID(Long cityID) {
        this.cityID = cityID;
    }

    @Override
    public String getPropertyNumber() {
        return propertyNumber;
    }

    public void setPropertyNumber(String propertyNumber) {
        this.propertyNumber = propertyNumber;
    }

    @Override
    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    @Override
    public Integer getLegalStories() {
        return legalStories;
    }

    public void setLegalStories(Integer legalStories) {
        this.legalStories = legalStories;
    }

    @Override
    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    @Override
    public String getBlock() {
        return block;
    }

    public void setBlock(String block) {
        this.block = block;
    }

    @Override
    public String getLot() {
        return lot;
    }

    public void setLot(String lot) {
        this.lot = lot;
    }

    @Override
    public String getBin() {
        return bin;
    }

    public void setBin(String bin) {
        this.bin = bin;
    }

    public Boolean isPublicHousing() {
        return publicHousing;
    }

    public void setPublicHousing(Boolean publicHousing) {
        this.publicHousing = publicHousing;
    }

    @Override
    public PropertyTypeE getPropertyTypeE() {
        return propertyTypeE;
    }

    public void setPropertyTypeE(PropertyTypeE propertyTypeE) {
        this.propertyTypeE = propertyTypeE;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("boroID", boroID)
                .append("cityID", cityID)
                .append("propertyNumber", propertyNumber)
                .append("streetName", streetName)
                .append("legalStories", legalStories)
                .append("zipCode", zipCode)
                .append("block", block)
                .append("lot", lot)
                .append("bin", bin)
                .append("publicHousing", publicHousing)
                .append("propertyTypeE", propertyTypeE)
                .toString();
    }

    public static class Builder {

        private PropertyBuilderRequest propertyBuilderRequest = new PropertyBuilderRequest();

        public Builder boroID(Long boroID) {
            propertyBuilderRequest.boroID = boroID;
            return this;
        }

        public PropertyBuilderRequest build() {
            return propertyBuilderRequest;
        }
    }
}
