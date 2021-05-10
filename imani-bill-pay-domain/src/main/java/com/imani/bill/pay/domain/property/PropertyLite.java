package com.imani.bill.pay.domain.property;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.imani.bill.pay.domain.contact.AddressLite;
import com.imani.bill.pay.domain.geographical.CommunityLite;
import com.imani.bill.pay.domain.user.UserRecordLite;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * @author manyce400
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PropertyLite {

    private Long id;

    private String block;

    private String lot;

    private String buildingIdentificationNumber;

    private Double latitude;

    private Double longitude;

    private PropertyTypeE propertyTypeE;

    private Integer mthlyNumberOfDaysPaymentLate;

    private AddressLite addressLite;

    private CommunityLite communityLite;

    private UserRecordLite owner;

    public PropertyLite() {

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

    public AddressLite getAddressLite() {
        return addressLite;
    }

    public void setAddressLite(AddressLite addressLite) {
        this.addressLite = addressLite;
    }

    public CommunityLite getCommunityLite() {
        return communityLite;
    }

    public void setCommunityLite(CommunityLite communityLite) {
        this.communityLite = communityLite;
    }

    public UserRecordLite getOwner() {
        return owner;
    }

    public void setOwner(UserRecordLite owner) {
        this.owner = owner;
    }

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
                .append("addressLite", addressLite)
                .append("communityLite", communityLite)
                .append("owner", owner)
                .toString();
    }

    public static Builder builder() {
        return new Builder();
    }


    public static class Builder {

        private final PropertyLite propertyLite = new PropertyLite();

        public Builder id(Long id) {
            propertyLite.id = id;
            return this;
        }

        public Builder block(String block) {
            propertyLite.block = block;
            return this;
        }

        public Builder lot(String lot) {
            propertyLite.lot = lot;
            return this;
        }

        public Builder buildingIdentificationNumber(String buildingIdentificationNumber) {
            propertyLite.buildingIdentificationNumber = buildingIdentificationNumber;
            return this;
        }

        public Builder latitude(Double latitude) {
            propertyLite.latitude = latitude;
            return this;
        }

        public Builder longitude(Double longitude) {
            propertyLite.longitude = longitude;
            return this;
        }

        public Builder propertyTypeE(PropertyTypeE propertyTypeE) {
            propertyLite.propertyTypeE = propertyTypeE;
            return this;
        }

        public Builder mthlyNumberOfDaysPaymentLate(Integer mthlyNumberOfDaysPaymentLate) {
            propertyLite.mthlyNumberOfDaysPaymentLate = mthlyNumberOfDaysPaymentLate;
            return this;
        }

        public Builder addressLite(AddressLite addressLite) {
            propertyLite.addressLite = addressLite;
            return this;
        }

        public Builder communityLite(CommunityLite communityLite) {
            propertyLite.communityLite = communityLite;
            return this;
        }

        public Builder owner(UserRecordLite owner) {
            propertyLite.owner = owner;
            return this;
        }

        public PropertyLite build() {
            return propertyLite;
        }

    }
}
