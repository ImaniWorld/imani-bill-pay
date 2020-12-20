package com.imani.bill.pay.domain.billing;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.joda.time.DateTime;

/**
 * Domain object that contains a detailed explanation of fees that were applied to a monthly rental.
 *
 * @author manyce400
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BillPayFeeExplained {


    private String feeName;

    private Double feeCharge;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private DateTime feeAppliedDate;

    public BillPayFeeExplained() {

    }

    public String getFeeName() {
        return feeName;
    }

    public void setFeeName(String feeName) {
        this.feeName = feeName;
    }

    public Double getFeeCharge() {
        return feeCharge;
    }

    public void setFeeCharge(Double feeCharge) {
        this.feeCharge = feeCharge;
    }

    public DateTime getFeeAppliedDate() {
        return feeAppliedDate;
    }

    public void setFeeAppliedDate(DateTime feeAppliedDate) {
        this.feeAppliedDate = feeAppliedDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        BillPayFeeExplained that = (BillPayFeeExplained) o;

        return new EqualsBuilder()
                .append(feeName, that.feeName)
                .append(feeCharge, that.feeCharge)
                .append(feeAppliedDate, that.feeAppliedDate)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(feeName)
                .append(feeCharge)
                .append(feeAppliedDate)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("feeName", feeName)
                .append("feeCharge", feeCharge)
                .append("feeAppliedDate", feeAppliedDate)
                .toString();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private BillPayFeeExplained monthlyRentalFeeExplained = new BillPayFeeExplained();

        public Builder feeName(String feeName) {
            monthlyRentalFeeExplained.feeName = feeName;
            return this;
        }

        public Builder feeCharge(Double feeCharge) {
            monthlyRentalFeeExplained.feeCharge = feeCharge;
            return this;
        }

        public Builder feeAppliedDate(DateTime feeAppliedDate) {
            monthlyRentalFeeExplained.feeAppliedDate = feeAppliedDate;
            return this;
        }

        public BillPayFeeExplained build() {
            return monthlyRentalFeeExplained;
        }
    }
}
