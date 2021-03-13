package com.imani.bill.pay.domain.utility;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.imani.bill.pay.domain.AuditableRecord;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.*;

/**
 * @author manyce400
 */
@Entity
@Table(name="WaterUtilization")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WaterUtilization extends AuditableRecord {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ID", nullable=false)
    private Long id;

    @Column(name="NumberOfGallonsUsed")
    private Long numberOfGallonsUsed;

    @Column(name="Description", length = 200)
    private String description;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "WaterServiceAgreementID", nullable = false)
    private WaterServiceAgreement waterServiceAgreement;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "UtilizationDate", nullable = false)
    @Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime utilizationDate;

    public WaterUtilization() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getNumberOfGallonsUsed() {
        return numberOfGallonsUsed;
    }

    public void setNumberOfGallonsUsed(Long numberOfGallonsUsed) {
        this.numberOfGallonsUsed = numberOfGallonsUsed;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public WaterServiceAgreement getWaterServiceAgreement() {
        return waterServiceAgreement;
    }

    public void setWaterServiceAgreement(WaterServiceAgreement waterServiceAgreement) {
        this.waterServiceAgreement = waterServiceAgreement;
    }

    public DateTime getUtilizationDate() {
        return utilizationDate;
    }

    public void setUtilizationDate(DateTime utilizationDate) {
        this.utilizationDate = utilizationDate;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("numberOfGallonsUsed", numberOfGallonsUsed)
                .append("description", description)
                .append("waterServiceAgreement", waterServiceAgreement)
                .append("utilizationDate", utilizationDate)
                .toString();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private WaterUtilization waterUtilization = new WaterUtilization();

        public Builder numberOfGallonsUsed(Long numberOfGallonsUsed) {
            waterUtilization.numberOfGallonsUsed = numberOfGallonsUsed;
            return this;
        }

        public Builder description(String description) {
            waterUtilization.description = description;
            return this;
        }

        public Builder waterServiceAgreement(WaterServiceAgreement waterServiceAgreement) {
            waterUtilization.waterServiceAgreement = waterServiceAgreement;
            return this;
        }

        public Builder utilizationDate(DateTime utilizationDate) {
            waterUtilization.utilizationDate = utilizationDate;
            return this;
        }

        public WaterUtilization build() {
            return waterUtilization;
        }

    }

}