package com.imani.bill.pay.domain.payment;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;

/**
 * Captures metric on invoking Plaid API's.
 *
 * @author manyce400
 */
@Entity
@Table(name="PlaidAPIStatistic")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PlaidAPIStatistic {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ID", nullable=false)
    private Long id;


    @Column(name="PlaidProduct", nullable=false, length=20)
    @Enumerated(EnumType.STRING)
    private PlaidProductE plaidProductE;


    // Tracks the result of executing the Plaid API
    @Column(name="PaymentAPIExecResult", nullable=false, length=20)
    @Enumerated(EnumType.STRING)
    private PaymentAPIExecResultE paymentAPIExecResultE;

    // Captures any exec errors that were encountered during API call
    @Column(name="ApiExecError", nullable=true, length=300)
    private String apiExecError;


    // Tracks the ACHPaymentInfo that Plaid API call was made against
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ACHPaymentInfoID", nullable = true)
    private ACHPaymentInfo achPaymentInfo;


    // DateTime that the Plaid API invocation was made.
    @Column(name = "ApiInvocationStartDate", nullable = false)
    @Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @CreatedDate
    private DateTime apiInvocationStartDate;


    // DateTime that the Plaid API invocation result was returned.  This will be very useful in identifying any latency.
    @Column(name = "ApiInvocationEndDate", nullable = true)
    @Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @CreatedDate
    private DateTime apiInvocationEndDate;


    public PlaidAPIStatistic() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PlaidProductE getPlaidProductE() {
        return plaidProductE;
    }

    public void setPlaidProductE(PlaidProductE plaidProductE) {
        this.plaidProductE = plaidProductE;
    }

    public PaymentAPIExecResultE getPaymentAPIExecResultE() {
        return paymentAPIExecResultE;
    }

    public void setPaymentAPIExecResultE(PaymentAPIExecResultE paymentAPIExecResultE) {
        this.paymentAPIExecResultE = paymentAPIExecResultE;
    }

    public String getApiExecError() {
        return apiExecError;
    }

    public void setApiExecError(String apiExecError) {
        this.apiExecError = apiExecError;
    }

    public ACHPaymentInfo getAchPaymentInfo() {
        return achPaymentInfo;
    }

    public void setAchPaymentInfo(ACHPaymentInfo achPaymentInfo) {
        this.achPaymentInfo = achPaymentInfo;
    }

    public DateTime getApiInvocationStartDate() {
        return apiInvocationStartDate;
    }

    public void setApiInvocationStartDate(DateTime apiInvocationStartDate) {
        this.apiInvocationStartDate = apiInvocationStartDate;
    }

    public DateTime getApiInvocationEndDate() {
        return apiInvocationEndDate;
    }

    public void setApiInvocationEndDate(DateTime apiInvocationEndDate) {
        this.apiInvocationEndDate = apiInvocationEndDate;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("plaidProductE", plaidProductE)
                .append("paymentAPIExecResultE", paymentAPIExecResultE)
                .append("apiExecError", apiExecError)
                .append("achPaymentInfo", achPaymentInfo)
                .append("apiInvocationStartDate", apiInvocationStartDate)
                .append("apiInvocationEndDate", apiInvocationEndDate)
                .toString();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private PlaidAPIStatistic plaidAPIStatistic = new PlaidAPIStatistic();

        public Builder plaidProductE(PlaidProductE plaidProductE) {
            plaidAPIStatistic.plaidProductE = plaidProductE;
            return this;
        }

        public Builder paymentAPIExecResultE(PaymentAPIExecResultE paymentAPIExecResultE) {
            plaidAPIStatistic.paymentAPIExecResultE = paymentAPIExecResultE;
            return this;
        }

        public Builder apiExecError(String apiExecError) {
            plaidAPIStatistic.apiExecError = apiExecError;
            return this;
        }

        public Builder achPaymentInfo(ACHPaymentInfo achPaymentInfo) {
            plaidAPIStatistic.achPaymentInfo = achPaymentInfo;
            return this;
        }

        public Builder apiInvocationStartDate(DateTime apiInvocationStartDate) {
            plaidAPIStatistic.apiInvocationStartDate = apiInvocationStartDate;
            return this;
        }

        public Builder apiInvocationEndDate(DateTime apiInvocationEndDate) {
            plaidAPIStatistic.apiInvocationEndDate = apiInvocationEndDate;
            return this;
        }

        public PlaidAPIStatistic build() {
            return plaidAPIStatistic;
        }
    }
}
