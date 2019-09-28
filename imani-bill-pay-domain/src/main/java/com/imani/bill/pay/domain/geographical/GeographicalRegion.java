package com.imani.bill.pay.domain.geographical;

import com.imani.bill.pay.domain.AuditableRecord;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;

/**
 * Represents a Geographical Region that Imani Cash and Payment Managem
 * @author manyce400
 */
@Entity
@Table(name="GeographicalRegion")
public class GeographicalRegion extends AuditableRecord {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ID", nullable=false)
    private Long id;


    @Column(name="RegionName", nullable=false, length=100, unique=true)
    private String regionName;


    @Column(name="RegionCode", nullable=false, length=4)
    private String regionCode;


    public GeographicalRegion() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public String getRegionCode() {
        return regionCode;
    }

    public void setRegionCode(String regionCode) {
        this.regionCode = regionCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        GeographicalRegion that = (GeographicalRegion) o;

        return new EqualsBuilder()
                .append(id, that.id)
                .append(regionName, that.regionName)
                .append(regionCode, that.regionCode)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .append(regionName)
                .append(regionCode)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("regionName", regionName)
                .append("regionCode", regionCode)
                .toString();
    }

}
