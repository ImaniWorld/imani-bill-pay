package com.imani.bill.pay.domain.payment;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.imani.bill.pay.domain.AuditableRecord;
import com.imani.bill.pay.domain.business.Business;
import com.imani.bill.pay.domain.payment.plaid.PlaidBankAcct;
import com.imani.bill.pay.domain.payment.stripe.StripeBankAcct;
import com.imani.bill.pay.domain.user.UserRecord;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.annotations.Type;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import javax.persistence.*;

/**
 * @author manyce400
 */
@Entity
@Table(name="ACHPaymentInfo")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ACHPaymentInfo extends AuditableRecord {



    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ID", nullable=false)
    private Long id;

    @Embedded
    private StripeBankAcct stripeBankAcct;

    @Embedded
    private PlaidBankAcct plaidBankAcct;


    // Identifies this ACH Bank Account as the primary account
    @Column(name="IsPrimary", nullable = true, columnDefinition = "TINYINT", length = 1)
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean isPrimary;


    // UserRecord that this Payment information belongs to
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "UserRecordID", nullable = true)
    private UserRecord userRecord;


    // PropertyManager that this Payment information belongs to
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "BusinessID", nullable = true)
    private Business business;


    public ACHPaymentInfo() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public StripeBankAcct getStripeBankAcct() {
        return stripeBankAcct;
    }

    public void setStripeBankAcct(StripeBankAcct stripeBankAcct) {
        this.stripeBankAcct = stripeBankAcct;
    }

    public PlaidBankAcct getPlaidBankAcct() {
        return plaidBankAcct;
    }

    public void setPlaidBankAcct(PlaidBankAcct plaidBankAcct) {
        this.plaidBankAcct = plaidBankAcct;
    }

    public boolean isPrimary() {
        return isPrimary;
    }

    public void setPrimary(boolean primary) {
        isPrimary = primary;
    }

    public UserRecord getUserRecord() {
        return userRecord;
    }

    public void setUserRecord(UserRecord userRecord) {
        this.userRecord = userRecord;
    }

    public Business getBusiness() {
        return business;
    }

    public void setBusiness(Business business) {
        this.business = business;
    }

    public void updateStripeBankAcctID(String acctID) {
        Assert.notNull(acctID, "Stripe Bank Account Id cannot be null");
        if(stripeBankAcct == null) {
            stripeBankAcct = new StripeBankAcct();
        }

        stripeBankAcct.setId(acctID);
    }

    public void updateStripeBankAcctToken(String bankAcctToken) {
        Assert.notNull(bankAcctToken, "Stripe Bank Account token cannot be null");
        if(stripeBankAcct == null) {
            stripeBankAcct = new StripeBankAcct();
        }

        stripeBankAcct.setBankAcctToken(bankAcctToken);
    }

    public boolean hasStripeBankAcct() {
        return stripeBankAcct != null && !StringUtils.isEmpty(stripeBankAcct.getBankAcctToken());
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("stripeBankAcct", stripeBankAcct)
                .append("plaidBankAcct", plaidBankAcct)
                .append("isPrimary", isPrimary)
                .append("userRecord", userRecord)
                .append("business", business)
                .toString();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private ACHPaymentInfo achPaymentInfo = new ACHPaymentInfo();


        public Builder stripeBankAcct(StripeBankAcct stripeBankAcct) {
            achPaymentInfo.stripeBankAcct = stripeBankAcct;
            return this;
        }

        public Builder plaidBankAcct(PlaidBankAcct plaidBankAcct) {
            achPaymentInfo.plaidBankAcct = plaidBankAcct;
            return this;
        }

        public Builder isPrimary(boolean isPrimary) {
            achPaymentInfo.isPrimary = isPrimary;
            return this;
        }

        public Builder userRecord(UserRecord userRecord) {
            achPaymentInfo.userRecord = userRecord;
            return this;
        }

        public Builder business(Business business) {
            achPaymentInfo.business = business;
            return this;
        }

        public ACHPaymentInfo build() {
            return achPaymentInfo;
        }

    }

}
