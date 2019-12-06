package com.imani.bill.pay.domain.payment;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.imani.bill.pay.domain.AuditableRecord;
import com.imani.bill.pay.domain.property.PropertyManager;
import com.imani.bill.pay.domain.property.PropertyOwner;
import com.imani.bill.pay.domain.user.UserRecord;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.annotations.Type;

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


    // Represents Stripe Account ID for Institutions - PropertyManager, PropertyOwner etc
    @Column(name="StripeAcctID", nullable=false, length=100)
    public String stripeAcctID;

    // Represents a Stripe Customer ID if this entry is for an End-User Customer(UserRecord)
    @Column(name="StripeCustomerID", nullable=false, length=100)
    public String stripeCustomerID;


    // Stripe Bank Acct Token that can be used to generate ACH payments
    @Column(name="StripeBankAcctToken", nullable=false, length=100)
    public String stripeBankAcctToken;


    // Bank AcctID tracked in Plaid post verification and authorization using Link
    @Column(name="PlaidAcctID", nullable=false, length=100)
    public String plaidAcctID;


    // Plaid Public Access token associated to the Item to which account is linked, required for all Plaid product integrations
    @Column(name="PlaidAccessToken", nullable=false, length=300)
    public String plaidAccessToken;


    @Column(name="acctName", nullable=true, length=100)
    public String acctName;


    @Column(name="OfficialAcctName", nullable=true, length=250)
    public String officialAcctName;

    @Column(name="AcctSubType", nullable=true, length=50)
    public String acctSubType;


    @Column(name="AcctType", nullable=true, length=50)
    public String acctType;


    @Column(name="FinancialInstitution", nullable=true, length=100)
    private String financialInstitution;

    // Identifies this ACH Bank Account as the primary account
    @Column(name="IsPrimary", nullable = true, columnDefinition = "TINYINT", length = 1)
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean isPrimary;


    // Flag to indicate if this account has been verified.  Payments can only be made to verified accounts
    @Column(name="IsVerified", nullable = true, columnDefinition = "TINYINT", length = 1)
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean isVerified;


    // UserRecord that this Payment information belongs to
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UserRecordID", nullable = true)
    private UserRecord userRecord;


    // PropertyManager that this Payment information belongs to
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PropertyManagerID", nullable = true)
    private PropertyManager propertyManager;


    // PropertyOwner that this Payment information belongs to
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PropertyOwnerID", nullable = true)
    private PropertyOwner propertyOwner;



    public ACHPaymentInfo() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStripeBankAcctToken() {
        return stripeBankAcctToken;
    }

    public void setStripeBankAcctToken(String stripeBankAcctToken) {
        this.stripeBankAcctToken = stripeBankAcctToken;
    }

    public String getStripeAcctID() {
        return stripeAcctID;
    }

    public void setStripeAcctID(String stripeAcctID) {
        this.stripeAcctID = stripeAcctID;
    }

    public String getStripeCustomerID() {
        return stripeCustomerID;
    }

    public void setStripeCustomerID(String stripeCustomerID) {
        this.stripeCustomerID = stripeCustomerID;
    }

    public String getPlaidAcctID() {
        return plaidAcctID;
    }

    public void setPlaidAcctID(String plaidAcctID) {
        this.plaidAcctID = plaidAcctID;
    }

    public String getPlaidAccessToken() {
        return plaidAccessToken;
    }

    public void setPlaidAccessToken(String plaidAccessToken) {
        this.plaidAccessToken = plaidAccessToken;
    }

    public String getAcctName() {
        return acctName;
    }

    public void setAcctName(String acctName) {
        this.acctName = acctName;
    }

    public String getOfficialAcctName() {
        return officialAcctName;
    }

    public void setOfficialAcctName(String officialAcctName) {
        this.officialAcctName = officialAcctName;
    }

    public String getAcctSubType() {
        return acctSubType;
    }

    public void setAcctSubType(String acctSubType) {
        this.acctSubType = acctSubType;
    }

    public String getAcctType() {
        return acctType;
    }

    public void setAcctType(String acctType) {
        this.acctType = acctType;
    }

    public String getFinancialInstitution() {
        return financialInstitution;
    }

    public void setFinancialInstitution(String financialInstitution) {
        this.financialInstitution = financialInstitution;
    }

    public boolean isPrimary() {
        return isPrimary;
    }

    public void setPrimary(boolean primary) {
        isPrimary = primary;
    }

    public boolean isVerified() {
        return isVerified;
    }

    public void setVerified(boolean verified) {
        isVerified = verified;
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

    public PropertyOwner getPropertyOwner() {
        return propertyOwner;
    }

    public void setPropertyOwner(PropertyOwner propertyOwner) {
        this.propertyOwner = propertyOwner;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("stripeAcctID", stripeAcctID)
                .append("stripeCustomerID", stripeCustomerID)
                .append("stripeBankAcctToken", stripeBankAcctToken)
                .append("plaidAcctID", plaidAcctID)
                .append("plaidAccessToken", plaidAccessToken)
                .append("acctName", acctName)
                .append("officialAcctName", officialAcctName)
                .append("acctSubType", acctSubType)
                .append("acctType", acctType)
                .append("financialInstitution", financialInstitution)
                .append("isPrimary", isPrimary)
                .append("isVerified", isVerified)
                .append("userRecord", userRecord)
                .append("propertyManager", propertyManager)
                .append("propertyOwner", propertyOwner)
                .toString();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private ACHPaymentInfo achPaymentInfo = new ACHPaymentInfo();

        public Builder stripeAcctID(String stripeAcctID) {
            achPaymentInfo.stripeAcctID = stripeAcctID;
            return this;
        }

        public Builder stripeCustomerID(String stripeCustomerID) {
            achPaymentInfo.stripeCustomerID = stripeCustomerID;
            return this;
        }

        public Builder stripeBankAcctToken(String stripeBankAcctToken) {
            achPaymentInfo.stripeBankAcctToken = stripeBankAcctToken;
            return this;
        }

        public Builder plaidAcctID(String plaidAcctID) {
            achPaymentInfo.plaidAcctID = plaidAcctID;
            return this;
        }

        public Builder plaidAccessToken(String plaidAccessToken) {
            achPaymentInfo.plaidAccessToken = plaidAccessToken;
            return this;
        }

        public Builder acctName(String acctName) {
            achPaymentInfo.acctName = acctName;
            return this;
        }

        public Builder officialAcctName(String officialAcctName) {
            achPaymentInfo.officialAcctName = officialAcctName;
            return this;
        }

        public Builder acctSubType(String acctSubType) {
            achPaymentInfo.acctSubType = acctSubType;
            return this;
        }

        public Builder acctType(String acctType) {
            achPaymentInfo.acctType = acctType;
            return this;
        }

        public Builder financialInstitution(String financialInstitution) {
            achPaymentInfo.financialInstitution = financialInstitution;
            return this;
        }

        public Builder isPrimary(boolean isPrimary) {
            achPaymentInfo.isPrimary = isPrimary;
            return this;
        }

        public Builder isVerified(boolean isVerified) {
            achPaymentInfo.isVerified = isVerified;
            return this;
        }

        public Builder userRecord(UserRecord userRecord) {
            achPaymentInfo.userRecord = userRecord;
            return this;
        }

        public Builder propertyOwner(PropertyOwner propertyOwner) {
            achPaymentInfo.propertyOwner = propertyOwner;
            return this;
        }

        public Builder propertyManager(PropertyManager propertyManager) {
            achPaymentInfo.propertyManager = propertyManager;
            return this;
        }

        public ACHPaymentInfo build() {
            return achPaymentInfo;
        }
    }

}
