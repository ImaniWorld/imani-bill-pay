package com.imani.bill.pay.domain.utility;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.common.collect.ImmutableSet;
import com.imani.bill.pay.domain.AuditableRecord;
import com.imani.bill.pay.domain.agreement.EmbeddedAgreement;
import com.imani.bill.pay.domain.agreement.IHasBillingAgreement;
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

    // Tracks the usage rate according to number of gallons billed per each fixed cost/
    // This leverages FixedCost in EmbeddedAgreement
    @Column(name="NumberOfGallonsPerFixedCost")
    private Long numberOfGallonsPerFixedCost;

    @Embedded
    private EmbeddedAgreement embeddedAgreement;

    @Embedded
    private EmbeddedUtilityService embeddedUtilityService;

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

    public EmbeddedUtilityService getEmbeddedUtilityService() {
        return embeddedUtilityService;
    }

    public void setEmbeddedUtilityService(EmbeddedUtilityService embeddedUtilityService) {
        this.embeddedUtilityService = embeddedUtilityService;
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
        StringBuffer sb = new StringBuffer("WaterServiceAgreement[BillPayer: ")
                .append(embeddedAgreement.getUserRecord().getEmbeddedContactInfo().getEmail())
                .append("; FixedCost: ")
                .append(embeddedAgreement.getFixedCost())
                .append("; numberOfGallonsPerFixedCost: ")
                .append(numberOfGallonsPerFixedCost)
                .append("; Business: ")
                .append(embeddedUtilityService.getUtilityProvider().getName())
                .append("]");
        return sb.toString();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("numberOfGallonsPerFixedCost", numberOfGallonsPerFixedCost)
                .append("embeddedAgreement", embeddedAgreement)
                .append("embeddedUtilityService", embeddedUtilityService)
                .append("waterUtilizations", waterUtilizations)
                .toString();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private WaterServiceAgreement waterServiceAgreement = new WaterServiceAgreement();

        public Builder embeddedUtilityService(EmbeddedUtilityService embeddedUtilityService) {
            waterServiceAgreement.embeddedUtilityService = embeddedUtilityService;
            return this;
        }

        public Builder embeddedAgreement(EmbeddedAgreement embeddedAgreement) {
            waterServiceAgreement.embeddedAgreement = embeddedAgreement;
            return this;
        }

        public Builder numberOfGallonsPerFixedCost(Long numberOfGallonsPerFixedCost) {
            waterServiceAgreement.numberOfGallonsPerFixedCost = numberOfGallonsPerFixedCost;
            return this;
        }

        public WaterServiceAgreement build() {
            return waterServiceAgreement;
        }

    }

    public static void main(String[] args) {
//        ObjectMapper objectMapper = new ObjectMapper();
//        objectMapper.registerModule(new Jdk8Module());
//        objectMapper.registerModule(new JodaModule());
//        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
//
//        UserRecord userRecord = new UserRecord();
//        userRecord.setId(5L);
//
//        EmbeddedAgreement embeddedAgreement = EmbeddedAgreement.builder()
//                .fixedCost(3.50)
//                .agreementInForce(true)
//                .billScheduleTypeE(BillScheduleTypeE.QUARTERLY)
//                .numberOfDaysTillLate(15)
//                .effectiveDate(new DateTime())
//                .userRecord(userRecord)
//                .build();
//
//        Address address = new Address();
//        address.setId(1L);
//
//        Business business = new Business();
//        business.setId(1L);
//
//        WaterServiceAgreement waterServiceAgreement = WaterServiceAgreement.builder()
//                .serviceAddress(address)
//                .embeddedAgreement(embeddedAgreement)
//                .business(business)
//                .numberOfGallonsPerFixedCost(1000L)
//                .businessCustomerAcctID("Addy-001")
//                .build();
//
//        try {
//            String json = objectMapper.writeValueAsString(waterServiceAgreement);
//            System.out.println("json = " + json);
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//        }

    }

}