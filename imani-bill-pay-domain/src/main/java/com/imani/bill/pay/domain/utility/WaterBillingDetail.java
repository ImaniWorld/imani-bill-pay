package com.imani.bill.pay.domain.utility;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

/**
 * @author manyce400
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WaterBillingDetail {



    private WaterUtilizationChargeLite waterUtilizationChargeLite;

    private List<WaterUtilizationLite> waterUtilizationLites = new ArrayList<>();

    public WaterBillingDetail() {

    }

    public WaterUtilizationChargeLite getWaterUtilizationChargeLite() {
        return waterUtilizationChargeLite;
    }

    public void setWaterUtilizationChargeLite(WaterUtilizationChargeLite waterUtilizationChargeLite) {
        this.waterUtilizationChargeLite = waterUtilizationChargeLite;
    }

    public List<WaterUtilizationLite> getWaterUtilizationLites() {
        return waterUtilizationLites;
    }

    public void addWaterUtilizationLite(WaterUtilizationLite waterUtilizationLite) {
        Assert.notNull(waterUtilizationLite, "WaterUtilizationLite cannot be null");
        waterUtilizationLites.add(waterUtilizationLite);
    }

    public void addWaterUtilizationLites(List<WaterUtilizationLite> waterUtilizationLites) {
        Assert.notNull(waterUtilizationLites, "WaterUtilizationLite list cannot be null");
        this.waterUtilizationLites.addAll(waterUtilizationLites);
    }


    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private final WaterBillingDetail waterBillingDetail = new WaterBillingDetail();

        public Builder waterUtilizationChargeLite(WaterUtilizationChargeLite waterUtilizationChargeLite) {
            waterBillingDetail.waterUtilizationChargeLite = waterUtilizationChargeLite;
            return this;
        }

        public Builder waterUtilizationLite(WaterUtilizationLite waterUtilizationLite) {
            waterBillingDetail.addWaterUtilizationLite(waterUtilizationLite);
            return this;
        }

        public Builder waterUtilizationLites(List<WaterUtilizationLite> waterUtilizationLites) {
            waterBillingDetail.addWaterUtilizationLites(waterUtilizationLites);
            return this;
        }

        public WaterBillingDetail build() {
            return waterBillingDetail;
        }
    }
}
