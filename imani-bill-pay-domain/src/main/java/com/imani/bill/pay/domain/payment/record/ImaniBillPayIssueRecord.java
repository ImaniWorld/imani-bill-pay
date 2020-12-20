package com.imani.bill.pay.domain.payment.record;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.imani.bill.pay.domain.payment.PaymentProcessingIssueTypeE;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;

/**
 * Tracks a record of any Platform issues that occur as part of attempting to process an
 * ImaniBill Payment.
 *
 * @author manyce400
 */
@Entity
@Table(name="ImaniBillPayIssueRecord")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ImaniBillPayIssueRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ID", nullable=false)
    private Long id;

    @Column(name="PaymentProcessingIssueType", nullable=false, length=20)
    @Enumerated(EnumType.STRING)
    private PaymentProcessingIssueTypeE paymentProcessingIssueTypeE;

    @Column(name="IssueMessage", length = 100)
    protected String issueMessage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ImaniBillPayRecordID", nullable = false)
    private ImaniBillPayRecord imaniBillPayRecord;


    public ImaniBillPayIssueRecord() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PaymentProcessingIssueTypeE getPaymentProcessingIssueTypeE() {
        return paymentProcessingIssueTypeE;
    }

    public void setPaymentProcessingIssueTypeE(PaymentProcessingIssueTypeE paymentProcessingIssueTypeE) {
        this.paymentProcessingIssueTypeE = paymentProcessingIssueTypeE;
    }

    public String getIssueMessage() {
        return issueMessage;
    }

    public void setIssueMessage(String issueMessage) {
        this.issueMessage = issueMessage;
    }

    public ImaniBillPayRecord getImaniBillPayRecord() {
        return imaniBillPayRecord;
    }

    public void setImaniBillPayRecord(ImaniBillPayRecord imaniBillPayRecord) {
        this.imaniBillPayRecord = imaniBillPayRecord;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("paymentProcessingIssueTypeE", paymentProcessingIssueTypeE)
                .append("issueMessage", issueMessage)
                .append("imaniBillPayRecord", imaniBillPayRecord)
                .toString();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private ImaniBillPayIssueRecord imaniBillPayIssueRecord = new ImaniBillPayIssueRecord();

        public Builder paymentProcessingIssueTypeE(PaymentProcessingIssueTypeE paymentProcessingIssueTypeE) {
            imaniBillPayIssueRecord.paymentProcessingIssueTypeE = paymentProcessingIssueTypeE;
            return this;
        }

        public Builder issueMessage(String issueMessage) {
            imaniBillPayIssueRecord.issueMessage = issueMessage;
            return this;
        }

        public Builder imaniBillPayRecord(ImaniBillPayRecord imaniBillPayRecord) {
            imaniBillPayIssueRecord.imaniBillPayRecord = imaniBillPayRecord;
            return this;
        }

        public ImaniBillPayIssueRecord build() {
            return imaniBillPayIssueRecord;
        }

    }

}
