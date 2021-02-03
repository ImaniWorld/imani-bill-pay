package com.imani.bill.pay.domain.utility;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.common.collect.ImmutableSet;
import com.imani.bill.pay.domain.AuditableRecord;
import com.imani.bill.pay.domain.agreement.EmbeddedAgreement;
import com.imani.bill.pay.domain.agreement.IHasBillingAgreement;
import com.imani.bill.pay.domain.business.Business;
import com.imani.bill.pay.domain.property.Property;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.util.Assert;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * @author manyce400
 */
@Entity
@Table(name="WaterServiceAgreement")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WaterServiceAgreement extends AuditableRecord implements IHasBillingAgreement {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ID", nullable=false)
    private Long id;

    // Unique Business AccountID that the business uses to track this customer agreement
    @Column(name="BusinessCustomerAcctID", length = 100)
    private String businessCustomerAcctID;


    // Tracks the usage rate according to number of gallons billed per each fixed cost/
    // This leverages FixedCost in EmbeddedAgreement
    @Column(name="NumberOfGallonsPerFixedCost")
    private Long numberOfGallonsPerFixedCost;


    @Embedded
    private EmbeddedAgreement embeddedAgreement;

    // Tracks the Business which agreement is made with
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "BusinessID", nullable = false)
    private Business business;

    // Tracks Property at which the utility is being provided
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "UtilityPropertyID", nullable = false)
    private Property utilityProperty;


    @OneToMany(cascade=CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "waterServiceAgreement")
    private Set<WaterUtilization> waterUtilizations = new HashSet<>();

    public WaterServiceAgreement() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    @Override
    public EmbeddedAgreement getEmbeddedAgreement() {
        return embeddedAgreement;
    }

    public void setEmbeddedAgreement(EmbeddedAgreement embeddedAgreement) {
        this.embeddedAgreement = embeddedAgreement;
    }

    public Business getBusiness() {
        return business;
    }

    public void setBusiness(Business business) {
        this.business = business;
    }

    public Property getUtilityProperty() {
        return utilityProperty;
    }

    public void setUtilityProperty(Property utilityProperty) {
        this.utilityProperty = utilityProperty;
    }

    public String getBusinessCustomerAcctID() {
        return businessCustomerAcctID;
    }

    public void setBusinessCustomerAcctID(String businessCustomerAcctID) {
        this.businessCustomerAcctID = businessCustomerAcctID;
    }

    public Long getNumberOfGallonsPerFixedCost() {
        return numberOfGallonsPerFixedCost;
    }

    public void setNumberOfGallonsPerFixedCost(Long numberOfGallonsPerFixedCost) {
        this.numberOfGallonsPerFixedCost = numberOfGallonsPerFixedCost;
    }

    public Set<WaterUtilization> getWaterUtilizations() {
        return ImmutableSet.copyOf(waterUtilizations);
    }

    public void addWaterUtilization(WaterUtilization waterUtilization) {
        Assert.notNull(waterUtilization, "WaterUtilization cannot be null");
        waterUtilizations.add(waterUtilization);
    }

    @Override
    public String describeAgreement() {
        return null;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("businessCustomerAcctID", businessCustomerAcctID)
                .append("numberOfGallonsPerFixedCost", numberOfGallonsPerFixedCost)
                .append("embeddedAgreement", embeddedAgreement)
                .append("business", business)
                .append("utilityProperty", utilityProperty)
                .toString();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private WaterServiceAgreement waterServiceAgreement = new WaterServiceAgreement();

        public Builder businessCustomerAcctID(String businessCustomerAcctID) {
            waterServiceAgreement.businessCustomerAcctID = businessCustomerAcctID;
            return this;
        }

        public Builder numberOfGallonsPerFixedCost(Long numberOfGallonsPerFixedCost) {
            waterServiceAgreement.numberOfGallonsPerFixedCost = numberOfGallonsPerFixedCost;
            return this;
        }

        public Builder embeddedAgreement(EmbeddedAgreement embeddedAgreement) {
            waterServiceAgreement.embeddedAgreement = embeddedAgreement;
            return this;
        }

        public Builder business(Business business) {
            waterServiceAgreement.business = business;
            return this;
        }

        public Builder utilityProperty(Property utilityProperty) {
            waterServiceAgreement.utilityProperty = utilityProperty;
            return this;
        }

        public Builder waterUtilization(WaterUtilization waterUtilization) {
            waterServiceAgreement.addWaterUtilization(waterUtilization);
            return this;
        }

        public WaterServiceAgreement build() {
            return waterServiceAgreement;
        }

    }

}