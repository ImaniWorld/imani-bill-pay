package com.imani.bill.pay.domain.utility;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.joda.time.DateTime;

/**
 * @author manyce400
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WaterUtilizationChargeLite {



    private Long id;

    private Double charge;

    private Long totalGallonsUsed;

    @JsonFormat(pattern = "yyyy-MM-dd")
    protected DateTime utilizationStart;

    @JsonFormat(pattern = "yyyy-MM-dd")
    protected DateTime utilizationEnd;


    public WaterUtilizationChargeLite() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getCharge() {
        return charge;
    }

    public void setCharge(Double charge) {
        this.charge = charge;
    }

    public Long getTotalGallonsUsed() {
        return totalGallonsUsed;
    }

    public void setTotalGallonsUsed(Long totalGallonsUsed) {
        this.totalGallonsUsed = totalGallonsUsed;
    }

    public DateTime getUtilizationStart() {
        return utilizationStart;
    }

    public void setUtilizationStart(DateTime utilizationStart) {
        this.utilizationStart = utilizationStart;
    }

    public DateTime getUtilizationEnd() {
        return utilizationEnd;
    }

    public void setUtilizationEnd(DateTime utilizationEnd) {
        this.utilizationEnd = utilizationEnd;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("charge", charge)
                .append("totalGallonsUsed", totalGallonsUsed)
                .append("utilizationStart", utilizationStart)
                .append("utilizationEnd", utilizationEnd)
                .toString();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private final WaterUtilizationChargeLite waterUtilizationChargeLite = new WaterUtilizationChargeLite();

        public Builder id(Long id) {
            waterUtilizationChargeLite.id = id;
            return this;
        }

        public Builder charge(Double charge) {
            waterUtilizationChargeLite.charge = charge;
            return this;
        }

        public Builder totalGallonsUsed(Long totalGallonsUsed) {
            waterUtilizationChargeLite.totalGallonsUsed = totalGallonsUsed;
            return this;
        }

        public Builder utilizationStart(DateTime utilizationStart) {
            waterUtilizationChargeLite.utilizationStart = utilizationStart;
            return this;
        }

        public Builder utilizationEnd(DateTime utilizationEnd) {
            waterUtilizationChargeLite.utilizationEnd = utilizationEnd;
            return this;
        }

        public WaterUtilizationChargeLite build() {
            return waterUtilizationChargeLite;
        }
    }
}
