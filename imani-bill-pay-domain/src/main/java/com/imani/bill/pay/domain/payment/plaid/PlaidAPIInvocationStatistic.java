package com.imani.bill.pay.domain.payment.plaid;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.imani.bill.pay.domain.payment.ACHPaymentInfo;
import com.imani.bill.pay.domain.property.PropertyManager;
import com.imani.bill.pay.domain.user.UserRecord;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;

/**
 * @author manyce400
 */
@Entity
@Table(name="PlaidAPIInvocationStatistic")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PlaidAPIInvocationStatistic {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ID", nullable=false)
    private Long id;


    @Column(name="PlaidProduct", length=20)
    @Enumerated(EnumType.STRING)
    private PlaidProductE plaidProductE;


    @Column(name="PlaidAPIInvocation", length=20)
    @Enumerated(EnumType.STRING)
    private PlaidAPIInvocationE plaidAPIInvocationE;


    // UserRecord that this Payment information belongs to
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UserRecordID", nullable = true)
    private UserRecord userRecord;


    // PropertyManager that the API Invocation was made on behalf of
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PropertyManagerID", nullable = true)
    private PropertyManager propertyManager;


    // Tracks the ACHPaymentInfo that Plaid API call was made against
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ACHPaymentInfoID")
    private ACHPaymentInfo achPaymentInfo;


    @Embedded
    private PlaidAPIRequest plaidAPIRequest;

    @Embedded
    private PlaidAPIResponse plaidAPIResponse;


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


    public PlaidAPIInvocationStatistic() {

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

    public PlaidAPIInvocationE getPlaidAPIInvocationE() {
        return plaidAPIInvocationE;
    }

    public void setPlaidAPIInvocationE(PlaidAPIInvocationE plaidAPIInvocationE) {
        this.plaidAPIInvocationE = plaidAPIInvocationE;
    }

    public UserRecord getUserRecord() {
        return userRecord;
    }

    public void setUserRecord(UserRecord userRecord) {
        this.userRecord = userRecord;
    }

    public PropertyManager getPropertyManager() {
        return propertyManager;
    }

    public void setPropertyManager(PropertyManager propertyManager) {
        this.propertyManager = propertyManager;
    }

    public ACHPaymentInfo getAchPaymentInfo() {
        return achPaymentInfo;
    }

    public void setAchPaymentInfo(ACHPaymentInfo achPaymentInfo) {
        this.achPaymentInfo = achPaymentInfo;
    }

    public PlaidAPIRequest getPlaidAPIRequest() {
        return plaidAPIRequest;
    }

    public void setPlaidAPIRequest(PlaidAPIRequest plaidAPIRequest) {
        this.plaidAPIRequest = plaidAPIRequest;
    }

    public PlaidAPIResponse getPlaidAPIResponse() {
        return plaidAPIResponse;
    }

    public void setPlaidAPIResponse(PlaidAPIResponse plaidAPIResponse) {
        this.plaidAPIResponse = plaidAPIResponse;
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

    public void startAPIInvocation() {
        setApiInvocationStartDate(DateTime.now());
    }

    public void endAPIInvocation() {
        setApiInvocationEndDate(DateTime.now());
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("plaidProductE", plaidProductE)
                .append("plaidAPIInvocationE", plaidAPIInvocationE)
                .append("userRecord", userRecord)
                .append("propertyManager", propertyManager)
                .append("achPaymentInfo", achPaymentInfo)
                .append("plaidAPIRequest", plaidAPIRequest)
                .append("plaidAPIResponse", plaidAPIResponse)
                .append("apiInvocationStartDate", apiInvocationStartDate)
                .append("apiInvocationEndDate", apiInvocationEndDate)
                .toString();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private final PlaidAPIInvocationStatistic plaidAPIInvocationStatistic = new PlaidAPIInvocationStatistic();


        public Builder plaidProductE(PlaidProductE plaidProductE) {
            plaidAPIInvocationStatistic.plaidProductE = plaidProductE;
            return this;
        }

        public Builder plaidAPIInvocationE(PlaidAPIInvocationE plaidAPIInvocationE) {
            plaidAPIInvocationStatistic.plaidAPIInvocationE = plaidAPIInvocationE;
            return this;
        }

        public Builder userRecord(UserRecord userRecord) {
            plaidAPIInvocationStatistic.userRecord = userRecord;
            return this;
        }

        public Builder propertyManager(PropertyManager propertyManager) {
            plaidAPIInvocationStatistic.propertyManager = propertyManager;
            return this;
        }

        public Builder achPaymentInfo(ACHPaymentInfo achPaymentInfo) {
            plaidAPIInvocationStatistic.achPaymentInfo = achPaymentInfo;
            return this;
        }

        public Builder plaidAPIRequest(PlaidAPIRequest plaidAPIRequest) {
            plaidAPIInvocationStatistic.plaidAPIRequest = plaidAPIRequest;
            return this;
        }

        public Builder plaidAPIResponse(PlaidAPIResponse plaidAPIResponse) {
            plaidAPIInvocationStatistic.plaidAPIResponse = plaidAPIResponse;
            return this;
        }

        public Builder apiInvocationStartDate(DateTime apiInvocationStartDate) {
            plaidAPIInvocationStatistic.apiInvocationStartDate = apiInvocationStartDate;
            return this;
        }

        public Builder apiInvocationEndDate(DateTime apiInvocationEndDate) {
            plaidAPIInvocationStatistic.apiInvocationStartDate = apiInvocationEndDate;
            return this;
        }

        public PlaidAPIInvocationStatistic build() {
            return plaidAPIInvocationStatistic;
        }
    }
}
