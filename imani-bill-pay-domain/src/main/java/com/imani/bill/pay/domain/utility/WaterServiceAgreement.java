package com.imani.bill.pay.domain.utility;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.google.common.collect.ImmutableSet;
import com.imani.bill.pay.domain.AuditableRecord;
import com.imani.bill.pay.domain.agreement.EmbeddedAgreement;
import com.imani.bill.pay.domain.agreement.IHasBillingAgreement;
import com.imani.bill.pay.domain.billing.BillScheduleTypeE;
import com.imani.bill.pay.domain.business.Business;
import com.imani.bill.pay.domain.contact.Address;
import com.imani.bill.pay.domain.geographical.Community;
import com.imani.bill.pay.domain.user.UserRecord;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.joda.time.DateTime;
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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ServiceAddressID", nullable = false)
    private Address serviceAddress;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "CommunityID")
    private Community community;


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

    public Address getServiceAddress() {
        return serviceAddress;
    }

    public void setServiceAddress(Address serviceAddress) {
        this.serviceAddress = serviceAddress;
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

    public Community getCommunity() {
        return community;
    }

    public void setCommunity(Community community) {
        this.community = community;
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
                .append("serviceAddress", serviceAddress)
                .append("community", community)
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

        public Builder serviceAddress(Address address) {
            waterServiceAgreement.serviceAddress = address;
            return this;
        }

        public Builder community(Community community) {
            waterServiceAgreement.community = community;
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

    public static void main(String[] args) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new Jdk8Module());
        objectMapper.registerModule(new JodaModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        UserRecord userRecord = new UserRecord();
        userRecord.setId(5L);

        EmbeddedAgreement embeddedAgreement = EmbeddedAgreement.builder()
                .fixedCost(3.50)
                .agreementInForce(true)
                .billScheduleTypeE(BillScheduleTypeE.QUARTERLY)
                .numberOfDaysTillLate(15)
                .effectiveDate(new DateTime())
                .userRecord(userRecord)
                .build();

        Address address = new Address();
        address.setId(1L);

        Business business = new Business();
        business.setId(1L);

        WaterServiceAgreement waterServiceAgreement = WaterServiceAgreement.builder()
                .serviceAddress(address)
                .embeddedAgreement(embeddedAgreement)
                .business(business)
                .numberOfGallonsPerFixedCost(1000L)
                .businessCustomerAcctID("Addy-001")
                .build();

        try {
            String json = objectMapper.writeValueAsString(waterServiceAgreement);
            System.out.println("json = " + json);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

    }

}