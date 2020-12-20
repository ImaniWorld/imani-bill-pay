package com.imani.bill.pay.domain.payment.record;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.common.collect.ImmutableSet;
import com.imani.bill.pay.domain.billing.ImaniBill;
import com.imani.bill.pay.domain.payment.ACHPaymentInfo;
import com.imani.bill.pay.domain.payment.EmbeddedPayment;
import com.imani.bill.pay.domain.payment.plaid.PlaidAPIError;
import com.imani.bill.pay.domain.payment.plaid.PlaidErrorCodeE;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.util.Assert;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * @author manyce400
 */
@Entity
@Table(name="ImaniBillPayRecord")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ImaniBillPayRecord {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ID", nullable=false)
    private Long id;

    // Information about the payment made.
    @Embedded
    private EmbeddedPayment embeddedPayment;

    // Tracks the Bank Account that the payment was made from.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ACHPaymentInfoID", nullable = true)
    private ACHPaymentInfo achPaymentInfo;

    // Tracks the MonthlyRentalBill for which payment was made.
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ImaniBillID", nullable = true)
    private ImaniBill imaniBill;

    // Tracks all Plaid API call results that were made as part of making payments on this bill
    @OneToMany(cascade=CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "imaniBillPayRecord")
    private Set<ImaniBillPayPlaidRecord> imaniBillPayPlaidRecords = new HashSet<>();

    // Tracks all platform issues that occured as part of making payments on this bill
    @OneToMany(cascade=CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "imaniBillPayRecord")
    private Set<ImaniBillPayIssueRecord> imaniBillPayIssueRecords = new HashSet<>();


    public ImaniBillPayRecord() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EmbeddedPayment getEmbeddedPayment() {
        return embeddedPayment;
    }

    public void setEmbeddedPayment(EmbeddedPayment embeddedPayment) {
        this.embeddedPayment = embeddedPayment;
    }

    public ACHPaymentInfo getAchPaymentInfo() {
        return achPaymentInfo;
    }

    public void setAchPaymentInfo(ACHPaymentInfo achPaymentInfo) {
        this.achPaymentInfo = achPaymentInfo;
    }

    public ImaniBill getImaniBill() {
        return imaniBill;
    }

    public void setImaniBill(ImaniBill imaniBill) {
        this.imaniBill = imaniBill;
    }

    public void addBillPayPlaidRecord(ImaniBillPayPlaidRecord imaniBillPayPlaidRecord) {
        Assert.notNull(imaniBillPayPlaidRecord, "BillPayPlaidRecord cannot be null");
        imaniBillPayPlaidRecords.add(imaniBillPayPlaidRecord);
    }

    public Set<ImaniBillPayPlaidRecord> getImaniBillPayPlaidRecords() {
        return ImmutableSet.copyOf(imaniBillPayPlaidRecords);
    }

    public void addImaniBillPayIssueRecord(ImaniBillPayIssueRecord imaniBillPayIssueRecord) {
        Assert.notNull(imaniBillPayIssueRecord, "ImaniBillPayIssueRecord cannot be null");
        imaniBillPayIssueRecords.add(imaniBillPayIssueRecord);
    }

    public Set<ImaniBillPayIssueRecord> getImaniBillPayIssueRecords() {
        return ImmutableSet.copyOf(imaniBillPayIssueRecords);
    }

    public boolean hasPlaidItemLoginError() {
        boolean itemLoginErrorFound = false;
        for(ImaniBillPayPlaidRecord imaniBillPayPlaidRecord : imaniBillPayPlaidRecords) {
            PlaidAPIError plaidAPIError = imaniBillPayPlaidRecord.getPlaidAPIError();
            if(plaidAPIError.getErrorCode() == PlaidErrorCodeE.ITEM_LOGIN_REQUIRED) {
                itemLoginErrorFound = true;
                break;
            }
        }

        return itemLoginErrorFound;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("embeddedPayment", embeddedPayment)
                .append("achPaymentInfo", achPaymentInfo)
                .append("imaniBill", imaniBill)
                .toString();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private ImaniBillPayRecord imaniBillPayRecord = new ImaniBillPayRecord();

        public Builder embeddedPayment(EmbeddedPayment embeddedPayment) {
            imaniBillPayRecord.embeddedPayment = embeddedPayment;
            return this;
        }

        public Builder achPaymentInfo(ACHPaymentInfo achPaymentInfo) {
            imaniBillPayRecord.achPaymentInfo = achPaymentInfo;
            return this;
        }

        public Builder imaniBill(ImaniBill imaniBill) {
            imaniBillPayRecord.imaniBill = imaniBill;
            return this;
        }

        public Builder withBillPayPlaidRecord(ImaniBillPayPlaidRecord imaniBillPayPlaidRecord) {
            imaniBillPayRecord.addBillPayPlaidRecord(imaniBillPayPlaidRecord);
            return this;
        }

        public Builder withBillPayIssueRecord(ImaniBillPayIssueRecord imaniBillPayIssueRecord) {
            imaniBillPayRecord.addImaniBillPayIssueRecord(imaniBillPayIssueRecord);
            return this;
        }

        public ImaniBillPayRecord build() {
            return imaniBillPayRecord;
        }

    }

}
