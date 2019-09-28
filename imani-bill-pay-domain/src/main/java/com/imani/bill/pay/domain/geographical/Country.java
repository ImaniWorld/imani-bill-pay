package com.imani.bill.pay.domain.geographical;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;

/**
 * @author manyce400
 */
@Entity
@Table(name="Country")
public class Country {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ID", nullable=false)
    private Long id;


    @Column(name="CountryName", nullable=false, length=100, unique=true)
    private String countryName;


    @Column(name="CountryCode", nullable=false, length=5)
    private String countryCode;


    @Column(name="CountryCurrency", nullable=false, length=10)
    private String countryCurrency;


    // Geographical region area that Country is mapped to
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "GeographicalRegionID", nullable = false)
    private GeographicalRegion geographicalRegion;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCountryCurrency() {
        return countryCurrency;
    }

    public void setCountryCurrency(String countryCurrency) {
        this.countryCurrency = countryCurrency;
    }

    public GeographicalRegion getGeographicalRegion() {
        return geographicalRegion;
    }

    public void setGeographicalRegion(GeographicalRegion geographicalRegion) {
        this.geographicalRegion = geographicalRegion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Country country = (Country) o;

        return new EqualsBuilder()
                .append(id, country.id)
                .append(countryName, country.countryName)
                .append(countryCode, country.countryCode)
                .append(countryCurrency, country.countryCurrency)
                .append(geographicalRegion, country.geographicalRegion)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .append(countryName)
                .append(countryCode)
                .append(countryCurrency)
                .append(geographicalRegion)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("countryName", countryName)
                .append("countryCode", countryCode)
                .append("countryCurrency", countryCurrency)
                .append("geographicalRegion", geographicalRegion)
                .toString();
    }
}
