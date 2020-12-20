package com.imani.bill.pay.domain.payment;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

/**
 * @author manyce400
 */
@Embeddable
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EmbeddedPayment {



    @Column(name="PaymentAmount", nullable=false)
    private Double paymentAmount;


    @Column(name="Currency", nullable=true, length=3)
    private String currency;


    @Column(name="PaymentStatusE", nullable=false, length=20)
    @Enumerated(EnumType.STRING)
    private PaymentStatusE paymentStatusE;


    // Date the payment was made on
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    @Column(name = "PaymentDate", nullable = false, updatable = false)
    @Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime paymentDate;


    // Date the payment actually posted. Useful for settling payments against example Bank accounts which might not post immidiately
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    @Column(name = "PaymentPostDate", nullable = true, updatable = false)
    @Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime paymentPostDate;


    public EmbeddedPayment() {

    }

    public Double getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(Double paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public PaymentStatusE getPaymentStatusE() {
        return paymentStatusE;
    }

    public void setPaymentStatusE(PaymentStatusE paymentStatusE) {
        this.paymentStatusE = paymentStatusE;
    }

    public DateTime getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(DateTime paymentDate) {
        this.paymentDate = paymentDate;
    }

    public DateTime getPaymentPostDate() {
        return paymentPostDate;
    }

    public void setPaymentPostDate(DateTime paymentPostDate) {
        this.paymentPostDate = paymentPostDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        EmbeddedPayment that = (EmbeddedPayment) o;

        return new EqualsBuilder()
                .append(paymentAmount, that.paymentAmount)
                .append(currency, that.currency)
                .append(paymentStatusE, that.paymentStatusE)
                .append(paymentDate, that.paymentDate)
                .append(paymentPostDate, that.paymentPostDate)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(paymentAmount)
                .append(currency)
                .append(paymentStatusE)
                .append(paymentDate)
                .append(paymentPostDate)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("paymentAmount", paymentAmount)
                .append("currency", currency)
                .append("paymentStatusE", paymentStatusE)
                .append("paymentDate", paymentDate)
                .append("paymentPostDate", paymentPostDate)
                .toString();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private EmbeddedPayment embeddedPayment = new EmbeddedPayment();

        public Builder paymentAmount(Double paymentAmount) {
            embeddedPayment.paymentAmount = paymentAmount;
            return this;
        }

        public Builder currency(String currency) {
            embeddedPayment.currency = currency;
            return this;
        }

        public Builder paymentStatusE(PaymentStatusE paymentStatusE) {
            embeddedPayment.paymentStatusE = paymentStatusE;
            return this;
        }

        public Builder paymentDate(DateTime paymentDate) {
            embeddedPayment.paymentDate = paymentDate;
            return this;
        }

        public Builder paymentPostDate(DateTime paymentPostDate) {
            embeddedPayment.paymentPostDate = paymentPostDate;
            return this;
        }

        public EmbeddedPayment build() {
            return embeddedPayment;
        }

    }
}
