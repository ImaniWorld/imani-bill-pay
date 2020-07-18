package com.imani.bill.pay.domain.billing;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.common.collect.ImmutableSet;
import com.imani.bill.pay.domain.user.UserRecordLite;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.joda.time.DateTime;
import org.springframework.util.Assert;

import java.util.HashSet;
import java.util.Set;

/**
 * @author manyce400
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ImaniBillExplained {


    private Long imaniBillID;

    private Double amountOwed;

    private Double amountPaid;

    // Passed only when a client is making a payment.  Should never exceed the total amount due
    private Double amtBeingPaid;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private DateTime billScheduleDate;

    private BillScheduleTypeE billScheduleTypeE;

    private BillServiceRenderedTypeE billServiceRenderedTypeE;

    private UserRecordLite userRecordLite;

    // Collection of additional fees applied against this rental bill
    private Set<BillPayFeeExplained> billPayFeeExplainedSet = new HashSet<>();


    public ImaniBillExplained() {

    }

    public Long getImaniBillID() {
        return imaniBillID;
    }

    public void setImaniBillID(Long imaniBillID) {
        this.imaniBillID = imaniBillID;
    }

    public Double getAmountOwed() {
        return amountOwed;
    }

    public void setAmountOwed(Double amountOwed) {
        this.amountOwed = amountOwed;
    }

    public Double getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(Double amountPaid) {
        this.amountPaid = amountPaid;
    }

    public Double getAmtBeingPaid() {
        return amtBeingPaid;
    }

    public void setAmtBeingPaid(Double amtBeingPaid) {
        this.amtBeingPaid = amtBeingPaid;
    }

    public DateTime getBillScheduleDate() {
        return billScheduleDate;
    }

    public void setBillScheduleDate(DateTime billScheduleDate) {
        this.billScheduleDate = billScheduleDate;
    }

    public BillScheduleTypeE getBillScheduleTypeE() {
        return billScheduleTypeE;
    }

    public void setBillScheduleTypeE(BillScheduleTypeE billScheduleTypeE) {
        this.billScheduleTypeE = billScheduleTypeE;
    }

    public BillServiceRenderedTypeE getBillServiceRenderedTypeE() {
        return billServiceRenderedTypeE;
    }

    public void setBillServiceRenderedTypeE(BillServiceRenderedTypeE billServiceRenderedTypeE) {
        this.billServiceRenderedTypeE = billServiceRenderedTypeE;
    }

    public UserRecordLite getUserRecordLite() {
        return userRecordLite;
    }

    public void setUserRecordLite(UserRecordLite userRecordLite) {
        this.userRecordLite = userRecordLite;
    }

    public Set<BillPayFeeExplained> getBillPayFeeExplained() {
        return ImmutableSet.copyOf(billPayFeeExplainedSet);
    }

    public void addBillPayFeeExplained(BillPayFeeExplained billPayFeeExplained) {
        Assert.notNull(billPayFeeExplained, "BillPayFeeExplained cannot be null");
        billPayFeeExplainedSet.add(billPayFeeExplained);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("imaniBillID", imaniBillID)
                .append("amountOwed", amountOwed)
                .append("amountPaid", amountPaid)
                .append("amtBeingPaid", amtBeingPaid)
                .append("billScheduleDate", billScheduleDate)
                .append("billScheduleTypeE", billScheduleTypeE)
                .append("billServiceRenderedTypeE", billServiceRenderedTypeE)
                .append("userRecordLite", userRecordLite)
                .toString();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private ImaniBillExplained monthlyRentalBillExplained = new ImaniBillExplained();

        public Builder imaniBillID(Long imaniBillID) {
            monthlyRentalBillExplained.imaniBillID = imaniBillID;
            return this;
        }

        public Builder amountOwed(Double amountOwed) {
            monthlyRentalBillExplained.amountOwed = amountOwed;
            return this;
        }

        public Builder amountPaid(Double amountPaid) {
            monthlyRentalBillExplained.amountPaid = amountPaid;
            return this;
        }

        public Builder amtBeingPaid(Double amtBeingPaid) {
            monthlyRentalBillExplained.amtBeingPaid = amtBeingPaid;
            return this;
        }

        public Builder billScheduleDate(DateTime billScheduleDate) {
            monthlyRentalBillExplained.billScheduleDate = billScheduleDate;
            return this;
        }

        public Builder billScheduleTypeE(BillScheduleTypeE billScheduleTypeE) {
            monthlyRentalBillExplained.billScheduleTypeE = billScheduleTypeE;
            return this;
        }

        public Builder billServiceRenderedTypeE(BillServiceRenderedTypeE billServiceRenderedTypeE) {
            monthlyRentalBillExplained.billServiceRenderedTypeE = billServiceRenderedTypeE;
            return this;
        }

        public Builder userRecordLite(UserRecordLite userRecordLite) {
            monthlyRentalBillExplained.userRecordLite = userRecordLite;
            return this;
        }

        public ImaniBillExplained build() {
            return monthlyRentalBillExplained;
        }
    }

}
