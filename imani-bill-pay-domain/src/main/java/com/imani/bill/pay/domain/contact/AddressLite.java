package com.imani.bill.pay.domain.contact;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * @author manyce400
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AddressLite {


    private Long id;

    private String streetAddress;

    private String zipCode;

    private String postOfficeBoxNumber;

    public AddressLite() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getPostOfficeBoxNumber() {
        return postOfficeBoxNumber;
    }

    public void setPostOfficeBoxNumber(String postOfficeBoxNumber) {
        this.postOfficeBoxNumber = postOfficeBoxNumber;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("streetAddress", streetAddress)
                .append("zipCode", zipCode)
                .append("postOfficeBoxNumber", postOfficeBoxNumber)
                .toString();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private final AddressLite addressLite = new AddressLite();

        public Builder id(Long id) {
            addressLite.id = id;
            return this;
        }

        public Builder streetAddress(String streetAddress) {
            addressLite.streetAddress = streetAddress;
            return this;
        }

        public Builder zipCode(String zipCode) {
            addressLite.zipCode = zipCode;
            return this;
        }

        public Builder postOfficeBoxNumber(String postOfficeBoxNumber) {
            addressLite.postOfficeBoxNumber = postOfficeBoxNumber;
            return this;
        }

        public AddressLite build() {
            return addressLite;
        }
    }
}
