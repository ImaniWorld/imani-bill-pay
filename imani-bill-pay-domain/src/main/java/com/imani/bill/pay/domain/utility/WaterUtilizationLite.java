package com.imani.bill.pay.domain.utility;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.joda.time.DateTime;

/**
 * @author manyce400
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WaterUtilizationLite {


    private Long id;

    private Long numberOfGallonsUsed;

    private String description;

    private WaterServiceAgreementLite waterServiceAgreementLite;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private DateTime utilizationDate;


    public WaterUtilizationLite() {

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

    public WaterServiceAgreementLite getWaterServiceAgreementLite() {
        return waterServiceAgreementLite;
    }

    public void setWaterServiceAgreementLite(WaterServiceAgreementLite waterServiceAgreementLite) {
        this.waterServiceAgreementLite = waterServiceAgreementLite;
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
                .append("waterServiceAgreementLite", waterServiceAgreementLite)
                .append("utilizationDate", utilizationDate)
                .toString();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private final WaterUtilizationLite waterUtilizationLite = new WaterUtilizationLite();

        public Builder id(Long id) {
            waterUtilizationLite.id = id;
            return this;
        }

        public Builder numberOfGallonsUsed(Long numberOfGallonsUsed) {
            waterUtilizationLite.numberOfGallonsUsed = numberOfGallonsUsed;
            return this;
        }

        public Builder description(String description) {
            waterUtilizationLite.description = description;
            return this;
        }

        public Builder waterServiceAgreementLite(WaterServiceAgreementLite waterServiceAgreementLite) {
            waterUtilizationLite.waterServiceAgreementLite = waterServiceAgreementLite;
            return this;
        }

        public Builder utilizationDate(DateTime utilizationDate) {
            waterUtilizationLite.utilizationDate = utilizationDate;
            return this;
        }

        public WaterUtilizationLite build() {
            return waterUtilizationLite;
        }

    }

}