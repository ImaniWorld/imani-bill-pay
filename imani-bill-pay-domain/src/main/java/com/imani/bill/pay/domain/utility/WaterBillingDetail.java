package com.imani.bill.pay.domain.utility;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.common.collect.ImmutableList;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

/**
 * @author manyce400
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WaterBillingDetail {



    private WaterUtilizationCharge waterUtilizationCharge;

    private List<WaterUtilization> waterUtilizations = new ArrayList<>();

    public WaterBillingDetail() {

    }

    public WaterUtilizationCharge getWaterUtilizationCharge() {
        return waterUtilizationCharge;
    }

    public void setWaterUtilizationCharge(WaterUtilizationCharge waterUtilizationCharge) {
        this.waterUtilizationCharge = waterUtilizationCharge;
    }

    public List<WaterUtilization> getWaterUtilizations() {
        return ImmutableList.copyOf(waterUtilizations);
    }

    public void addWaterUtilizations(WaterUtilization waterUtilization) {
        Assert.notNull(waterUtilization, "WaterUtilization cannot be null");
        this.waterUtilizations = waterUtilizations;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("waterUtilizationCharge", waterUtilizationCharge)
                .append("waterUtilizations", waterUtilizations)
                .toString();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private final WaterBillingDetail waterBillingDetail = new WaterBillingDetail();

        public Builder waterUtilizationCharge(WaterUtilizationCharge waterUtilizationCharge) {
            waterBillingDetail.waterUtilizationCharge = waterUtilizationCharge;
            return this;
        }

        public Builder waterUtilization(WaterUtilization waterUtilization) {
            waterBillingDetail.addWaterUtilizations(waterUtilization);
            return this;
        }

        public Builder waterUtilizations(List<WaterUtilization> waterUtilizations) {
            waterBillingDetail.waterUtilizations.addAll(waterUtilizations);
            return this;
        }

        public WaterBillingDetail build() {
            return waterBillingDetail;
        }
    }
}
