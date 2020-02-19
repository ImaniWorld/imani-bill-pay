package com.imani.bill.pay.domain.property;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.common.collect.ImmutableSet;
import com.imani.bill.pay.domain.UserAuditableRecord;
import com.imani.bill.pay.domain.contact.EmbeddedContactInfo;
import com.imani.bill.pay.domain.payment.ACHPaymentInfo;
import com.imani.bill.pay.domain.user.UserRecord;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.util.Assert;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * @author manyce400
 */
@Entity
@Table(name="PropertyManager")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PropertyManager extends UserAuditableRecord {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ID", nullable=false)
    private Long id;


    @Column(name="Name", nullable=false, length = 50)
    private String name;


    @Embedded
    private EmbeddedContactInfo embeddedContactInfo;


    // Represents Stripe Account ID for Institutions - PropertyManager, PropertyOwner etc
    @Column(name="StripeAcctID", length=100)
    public String stripeAcctID;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ACHPaymentInfoID", nullable = true)
    private ACHPaymentInfo achPaymentInfo;


    // Tracks the Business Property Address.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PropertyInfoID", nullable = true)
    private Property businessAddressInfo;


    // Contains the portfolio of properties managed by the property management firm
    @OneToMany(cascade=CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "propertyManager")
    private Set<Property> portfolio = new HashSet<>();


    public PropertyManager() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public EmbeddedContactInfo getEmbeddedContactInfo() {
        return embeddedContactInfo;
    }

    public void setEmbeddedContactInfo(EmbeddedContactInfo embeddedContactInfo) {
        this.embeddedContactInfo = embeddedContactInfo;
    }

    public String getStripeAcctID() {
        return stripeAcctID;
    }

    public void setStripeAcctID(String stripeAcctID) {
        this.stripeAcctID = stripeAcctID;
    }

    public ACHPaymentInfo getAchPaymentInfo() {
        return achPaymentInfo;
    }

    public void setAchPaymentInfo(ACHPaymentInfo achPaymentInfo) {
        this.achPaymentInfo = achPaymentInfo;
    }

    public Property getBusinessAddressInfo() {
        return businessAddressInfo;
    }

    public void setBusinessAddressInfo(Property businessAddressInfo) {
        this.businessAddressInfo = businessAddressInfo;
    }

    public Set<Property> getPortfolio() {
        return ImmutableSet.copyOf(portfolio);
    }

    public void addToPortfolio(Property property) {
        Assert.notNull(property, "property cannot be null");
        this.portfolio.add(property);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("name", name)
                .append("embeddedContactInfo", embeddedContactInfo)
                .append("stripeAcctID", stripeAcctID)
                .append("achPaymentInfo", achPaymentInfo)
                .append("businessAddressInfo", businessAddressInfo)
                .append("portfolio", portfolio)
                .append("createdBy", createdBy)
                .append("createDate", createDate)
                .append("modifyDate", modifyDate)
                .toString();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private PropertyManager propertyManager = new PropertyManager();

        public Builder name(String name) {
            propertyManager.name = name;
            return this;
        }

        public Builder embeddedContactInfo(EmbeddedContactInfo embeddedContactInfo) {
            propertyManager.embeddedContactInfo = embeddedContactInfo;
            return this;
        }

        public Builder stripeAcctID(String stripeAcctID) {
            propertyManager.stripeAcctID = stripeAcctID;
            return this;
        }

        public Builder achPaymentInfo(ACHPaymentInfo achPaymentInfo) {
            propertyManager.achPaymentInfo = achPaymentInfo;
            return this;
        }

        public Builder businessAddressInfo(Property businessAddressInfo) {
            propertyManager.businessAddressInfo = businessAddressInfo;
            return this;
        }

        public Builder createdBy(UserRecord createdBy) {
            propertyManager.createdBy = createdBy;
            return this;
        }

        public PropertyManager build() {
            return propertyManager;
        }
    }
    
}