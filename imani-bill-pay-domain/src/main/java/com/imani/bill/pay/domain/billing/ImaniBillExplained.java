package com.imani.bill.pay.domain.billing;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.common.collect.ImmutableSet;
import com.imani.bill.pay.domain.payment.EmbeddedPayment;
import com.imani.bill.pay.domain.payment.PaymentStatusE;
import com.imani.bill.pay.domain.user.UserRecordLite;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.util.Assert;

import java.util.HashSet;
import java.util.Set;

/**
 * @author manyce400
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ImaniBillExplained<O> {

    private Long imaniBillID;

    private Double amountOwed;

    private Double amountPaid;

    // Passed only when a client is making a payment.
    // Should never exceed the total amount due
    private Double amtBeingPaid;

    private O billingDetail;

    private BillPurposeExplained billPurposeExplained;

    private UserRecordLite userBilled;

    // Collection of additional fees applied against this rental bill
    private Set<BillPayFeeExplained> billPayFeeExplainedSet = new HashSet<>();

    private Set<EmbeddedPayment> embeddedPayments = new HashSet<>();


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

    public Double getAmtBeingPaid() {
        return amtBeingPaid;
    }

    public void setAmtBeingPaid(Double amtBeingPaid) {
        this.amtBeingPaid = amtBeingPaid;
    }

    public O getBillingDetail() {
        return billingDetail;
    }

    public void setBillingDetail(O billingDetail) {
        this.billingDetail = billingDetail;
    }

    public BillPurposeExplained getBillPurposeExplained() {
        return billPurposeExplained;
    }

    public void setBillPurposeExplained(BillPurposeExplained billPurposeExplained) {
        this.billPurposeExplained = billPurposeExplained;
    }

    public UserRecordLite getUserBilled() {
        return userBilled;
    }

    public void setUserBilled(UserRecordLite userBilled) {
        this.userBilled = userBilled;
    }

    public Set<BillPayFeeExplained> getBillPayFeeExplained() {
        return ImmutableSet.copyOf(billPayFeeExplainedSet);
    }

    public void addBillPayFeeExplained(BillPayFeeExplained billPayFeeExplained) {
        Assert.notNull(billPayFeeExplained, "BillPayFeeExplained cannot be null");
        billPayFeeExplainedSet.add(billPayFeeExplained);
    }

    public Set<EmbeddedPayment> getEmbeddedPayments() {
        return ImmutableSet.copyOf(embeddedPayments);
    }

    public void addEmbeddedPayment(EmbeddedPayment embeddedPayment) {
        Assert.notNull(embeddedPayment, "EmbeddedPayment cannot be null");
        embeddedPayments.add(embeddedPayment);
        computeProcessedPayments();
    }

    private void computeProcessedPayments() {
        // Update amounts paid
        double computedPayments = 0;
        for(EmbeddedPayment embeddedPayment : embeddedPayments) {
            if(embeddedPayment.getPaymentStatusE() == PaymentStatusE.Success) {
                computedPayments = computedPayments +embeddedPayment.getPaymentAmount().doubleValue();
            }
        }

        amountPaid = computedPayments;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("imaniBillID", imaniBillID)
                .append("amountOwed", amountOwed)
                .append("amountPaid", amountPaid)
                .append("amtBeingPaid", amtBeingPaid)
                .append("billingDetail", billingDetail)
                .append("billPurposeExplained", billPurposeExplained)
                .append("userBilled", userBilled)
                .toString();
    }

    public static <O> Builder<O> builder() {
        return new Builder<O>();
    }

    public static class Builder<O> {

        private ImaniBillExplained<O> imaniBillExplained = new ImaniBillExplained<>();

        public Builder<O> imaniBillID(Long imaniBillID) {
            imaniBillExplained.imaniBillID = imaniBillID;
            return this;
        }

        public Builder<O> amountOwed(Double amountOwed) {
            imaniBillExplained.amountOwed = amountOwed;
            return this;
        }

        public Builder<O> amountPaid(Double amountPaid) {
            imaniBillExplained.amountPaid = amountPaid;
            return this;
        }

        public Builder<O> amtBeingPaid(Double amtBeingPaid) {
            imaniBillExplained.amtBeingPaid = amtBeingPaid;
            return this;
        }

        public Builder<O> billingDetail(O billingDetail) {
            imaniBillExplained.billingDetail = billingDetail;
            return this;
        }

        public Builder<O> billPurposeExplained(BillPurposeExplained billPurposeExplained) {
            imaniBillExplained.billPurposeExplained = billPurposeExplained;
            return this;
        }

        public Builder<O> userBilled(UserRecordLite userBilled) {
            imaniBillExplained.userBilled = userBilled;
            return this;
        }

        public ImaniBillExplained<O> build() {
            return imaniBillExplained;
        }
    }

}
