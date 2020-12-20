package com.imani.bill.pay.domain.payment.record;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.imani.bill.pay.domain.APICallTimeRecord;
import com.imani.bill.pay.domain.payment.plaid.PlaidAPIError;
import com.imani.bill.pay.domain.payment.plaid.PlaidAPIInvocationE;
import com.imani.bill.pay.domain.payment.plaid.PlaidProductE;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;

/**
 * This captures the results of all Plaid API invocation calls made as part of processing
 *  * a specific ImaniBill payment.  Errors associated with the API call are also recorded.
 *
 * @author manyce400
 */
@Entity
@Table(name="ImaniBillPayPlaidRecord")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ImaniBillPayPlaidRecord {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ID", nullable=false)
    private Long id;

    @Column(name="PlaidProduct", nullable=true, length=20)
    @Enumerated(EnumType.STRING)
    private PlaidProductE plaidProductE;

    // Determines the API method call
    @Column(name="PlaidAPIInvocation", nullable=false, length=20)
    @Enumerated(EnumType.STRING)
    private PlaidAPIInvocationE plaidAPIInvocationE;

    @Embedded
    PlaidAPIError plaidAPIError;

    @Embedded
    private APICallTimeRecord apiCallTimeRecord;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ImaniBillPayRecordID", nullable = false)
    private ImaniBillPayRecord imaniBillPayRecord;

    public ImaniBillPayPlaidRecord() {

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

    public PlaidAPIError getPlaidAPIError() {
        return plaidAPIError;
    }

    public void setPlaidAPIError(PlaidAPIError plaidAPIError) {
        this.plaidAPIError = plaidAPIError;
    }

    public APICallTimeRecord getApiCallTimeRecord() {
        return apiCallTimeRecord;
    }

    public void setApiCallTimeRecord(APICallTimeRecord apiCallTimeRecord) {
        this.apiCallTimeRecord = apiCallTimeRecord;
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
                .append("plaidProductE", plaidProductE)
                .append("plaidAPIInvocationE", plaidAPIInvocationE)
                .append("plaidAPIError", plaidAPIError)
                .append("apiCallTimeRecord", apiCallTimeRecord)
                .append("imaniBillPayRecord", imaniBillPayRecord)
                .toString();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private ImaniBillPayPlaidRecord imaniBillPayPlaidRecord = new ImaniBillPayPlaidRecord();

        public Builder plaidProductE(PlaidProductE plaidProductE) {
            imaniBillPayPlaidRecord.plaidProductE = plaidProductE;
            return this;
        }

        public Builder plaidAPIInvocationE(PlaidAPIInvocationE plaidAPIInvocationE) {
            imaniBillPayPlaidRecord.plaidAPIInvocationE = plaidAPIInvocationE;
            return this;
        }

        public Builder plaidAPIError(PlaidAPIError plaidAPIError) {
            imaniBillPayPlaidRecord.plaidAPIError = plaidAPIError;
            return this;
        }

        public Builder withAPICallStart() {
            imaniBillPayPlaidRecord.apiCallTimeRecord = APICallTimeRecord.start();
            return this;
        }

        public Builder withAPICallTimeRecord(APICallTimeRecord apiCallTimeRecord) {
            imaniBillPayPlaidRecord.apiCallTimeRecord = apiCallTimeRecord;
            return this;
        }

        public Builder imaniBillPayRecord(ImaniBillPayRecord imaniBillPayRecord) {
            imaniBillPayPlaidRecord.imaniBillPayRecord = imaniBillPayRecord;
            return this;
        }

        public ImaniBillPayPlaidRecord build() {
            return imaniBillPayPlaidRecord;
        }

    }
}
