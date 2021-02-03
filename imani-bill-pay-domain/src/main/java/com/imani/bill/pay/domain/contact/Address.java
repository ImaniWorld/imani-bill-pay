package com.imani.bill.pay.domain.contact;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.imani.bill.pay.domain.AuditableRecord;
import com.imani.bill.pay.domain.geographical.City;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;

@Entity
@Table(name="Address")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Address extends AuditableRecord {



    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ID", nullable=false)
    private Long id;

    @Column(name="StreetAddress", length = 400)
    private String streetAddress;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "CityID", nullable = false)
    private City city;

    @Column(name="ZipCode", nullable=false, length=50)
    private String zipCode;

    @Column(name="POBoxNumber", length=50)
    private String postOfficeBoxNumber;


    public Address() {

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

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
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
                .append("city", city)
                .append("zipCode", zipCode)
                .append("postOfficeBoxNumber", postOfficeBoxNumber)
                .toString();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Address address = new Address();

        public Builder streetAddress(String streetAddress) {
            address.streetAddress = streetAddress;
            return this;
        }

        public Builder city(City city) {
            address.city = city;
            return this;
        }

        public Builder zipCode(String zipCode) {
            address.zipCode = zipCode;
            return this;
        }

        public Builder postOfficeBoxNumber(String postOfficeBoxNumber) {
            address.postOfficeBoxNumber = postOfficeBoxNumber;
            return this;
        }

        public Address build() {
            return address;
        }

    }
}