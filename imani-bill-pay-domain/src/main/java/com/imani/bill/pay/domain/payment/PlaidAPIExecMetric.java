package com.imani.bill.pay.domain.payment;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.imani.bill.pay.domain.user.UserRecord;
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
@Table(name="PlaidAPIExecMetric")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PlaidAPIExecMetric {


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


    // Tracks the UserRecord that PlaidMetric was invoked on behalf of
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UserRecordID", nullable = true)
    private UserRecord userRecord;


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


    public PlaidAPIExecMetric() {

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

    public UserRecord getUserRecord() {
        return userRecord;
    }

    public void setUserRecord(UserRecord userRecord) {
        this.userRecord = userRecord;
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
                .append("userRecord", userRecord)
                .append("apiInvocationStartDate", apiInvocationStartDate)
                .append("apiInvocationEndDate", apiInvocationEndDate)
                .toString();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private PlaidAPIExecMetric plaidAPIExecMetric = new PlaidAPIExecMetric();

        public Builder plaidProductE(PlaidProductE plaidProductE) {
            plaidAPIExecMetric.plaidProductE = plaidProductE;
            return this;
        }

        public Builder paymentAPIExecResultE(PaymentAPIExecResultE paymentAPIExecResultE) {
            plaidAPIExecMetric.paymentAPIExecResultE = paymentAPIExecResultE;
            return this;
        }

        public Builder apiExecError(String apiExecError) {
            plaidAPIExecMetric.apiExecError = apiExecError;
            return this;
        }

        public Builder userRecord(UserRecord userRecord) {
            plaidAPIExecMetric.userRecord = userRecord;
            return this;
        }

        public Builder apiInvocationStartDate(DateTime apiInvocationStartDate) {
            plaidAPIExecMetric.apiInvocationStartDate = apiInvocationStartDate;
            return this;
        }

        public Builder apiInvocationEndDate(DateTime apiInvocationEndDate) {
            plaidAPIExecMetric.apiInvocationEndDate = apiInvocationEndDate;
            return this;
        }

        public PlaidAPIExecMetric build() {
            return plaidAPIExecMetric;
        }
    }
}
