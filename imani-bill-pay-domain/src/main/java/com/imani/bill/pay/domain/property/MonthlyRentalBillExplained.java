package com.imani.bill.pay.domain.property;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.common.collect.ImmutableSet;
import com.imani.bill.pay.domain.user.UserResidence;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.joda.time.DateTime;
import org.springframework.util.Assert;

import java.util.HashSet;
import java.util.Set;

/**
 * @author manyce400
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MonthlyRentalBillExplained {


    private Double monthlyRentalCost;

    private Double totalAmtDue;

    private Double amtPaid;

    // Passed only when a client is making a payment.  Should never exceed the total amount due
    private Double amtBeingPaid;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private DateTime rentalMonth;

    private UserResidence userResidence;

    // Collection of additional fees applied against this rental bill
    private Set<MonthlyRentalFeeExplained> monthlyRentalFeeExplainedSet = new HashSet<>();

    // Collection of additional property services which this user has signed up for and applied to total amount due
    private Set<PropertyServiceChargeExplained> propertyServiceChargeExplainedSet = new HashSet<>();


    public MonthlyRentalBillExplained() {

    }

    public Double getMonthlyRentalCost() {
        return monthlyRentalCost;
    }

    public void setMonthlyRentalCost(Double monthlyRentalCost) {
        this.monthlyRentalCost = monthlyRentalCost;
    }

    public Double getTotalAmtDue() {
        return totalAmtDue;
    }

    public void setTotalAmtDue(Double totalAmtDue) {
        this.totalAmtDue = totalAmtDue;
    }

    public Double getAmtPaid() {
        return amtPaid;
    }

    public void setAmtPaid(Double amtPaid) {
        this.amtPaid = amtPaid;
    }

    public Double getAmtBeingPaid() {
        return amtBeingPaid;
    }

    public void setAmtBeingPaid(Double amtBeingPaid) {
        this.amtBeingPaid = amtBeingPaid;
    }

    public DateTime getRentalMonth() {
        return rentalMonth;
    }

    public void setRentalMonth(DateTime rentalMonth) {
        this.rentalMonth = rentalMonth;
    }

    public UserResidence getUserResidence() {
        return userResidence;
    }

    public void setUserResidence(UserResidence userResidence) {
        this.userResidence = userResidence;
    }

    public Set<MonthlyRentalFeeExplained> getMonthlyRentalFeeExplainedSet() {
        return ImmutableSet.copyOf(monthlyRentalFeeExplainedSet);
    }

    public void addMonthlyRentalFeeExplained(MonthlyRentalFeeExplained monthlyRentalFeeExplained) {
        Assert.notNull(monthlyRentalFeeExplained, "MonthlyRentalFeeExplained cannot be null");
        monthlyRentalFeeExplainedSet.add(monthlyRentalFeeExplained);
    }

    public Set<PropertyServiceChargeExplained> getPropertyServiceChargeExplainedSet() {
        return ImmutableSet.copyOf(propertyServiceChargeExplainedSet);
    }

    public void addPropertyServiceChargeExplained(PropertyServiceChargeExplained propertyServiceChargeExplained) {
        Assert.notNull(propertyServiceChargeExplained, "PropertyServiceChargeExplained cannot be null");
        propertyServiceChargeExplainedSet.add(propertyServiceChargeExplained);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("monthlyRentalCost", monthlyRentalCost)
                .append("totalAmtDue", totalAmtDue)
                .append("amtPaid", amtPaid)
                .append("amtBeingPaid", amtBeingPaid)
                .append("rentalMonth", rentalMonth)
                .append("userResidence", userResidence)
                .append("monthlyRentalFeeExplainedSet", monthlyRentalFeeExplainedSet)
                .append("propertyServiceChargeExplainedSet", propertyServiceChargeExplainedSet)
                .toString();
    }


    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private MonthlyRentalBillExplained monthlyRentalBillExplained = new MonthlyRentalBillExplained();

        public Builder monthlyRentalCost(Double monthlyRentalCost) {
            monthlyRentalBillExplained.monthlyRentalCost = monthlyRentalCost;
            return this;
        }

        public Builder totalAmtDue(Double totalAmtDue) {
            monthlyRentalBillExplained.totalAmtDue = totalAmtDue;
            return this;
        }

        public Builder amtPaid(Double amtPaid) {
            monthlyRentalBillExplained.amtPaid = amtPaid;
            return this;
        }

        public Builder amtBeingPaid(Double amtBeingPaid) {
            monthlyRentalBillExplained.amtBeingPaid = amtBeingPaid;
            return this;
        }

        public Builder rentalMonth(DateTime rentalMonth) {
            monthlyRentalBillExplained.rentalMonth = rentalMonth;
            return this;
        }

        public Builder userResidence(UserResidence userResidence) {
            monthlyRentalBillExplained.userResidence = userResidence;
            return this;
        }

        public Builder monthlyRentalFeeExplained(MonthlyRentalFeeExplained monthlyRentalFeeExplained) {
            monthlyRentalBillExplained.addMonthlyRentalFeeExplained(monthlyRentalFeeExplained);
            return this;
        }

        public Builder propertyServiceChargeExplained(PropertyServiceChargeExplained propertyServiceChargeExplained) {
            monthlyRentalBillExplained.addPropertyServiceChargeExplained(propertyServiceChargeExplained);
            return this;
        }

        public MonthlyRentalBillExplained build() {
            return monthlyRentalBillExplained;
        }
    }

}
