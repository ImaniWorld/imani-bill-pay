package com.imani.bill.pay.domain.utility;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.common.collect.ImmutableSet;
import com.imani.bill.pay.domain.AuditableRecord;
import com.imani.bill.pay.domain.agreement.AgreementToScheduleBillPayFee;
import com.imani.bill.pay.domain.agreement.EmbeddedAgreement;
import com.imani.bill.pay.domain.agreement.IHasBillingAgreement;
import com.imani.bill.pay.domain.billing.BillPayFee;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.util.Assert;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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
    @Column(name="NbrOfGallonsPerFixedCost")
    private Long nbrOfGallonsPerFixedCost;

    @Embedded
    private EmbeddedAgreement embeddedAgreement;

    @Embedded
    private EmbeddedUtilityService embeddedUtilityService;

    @OneToMany(cascade=CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "waterServiceAgreement")
    private Set<WaterUtilization> waterUtilizations = new HashSet<>();

    @OneToMany(cascade=CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "waterServiceAgreement")
    private Set<AgreementToScheduleBillPayFee> agreementToScheduleBillPayFees = new HashSet<>();


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

    public Long getNbrOfGallonsPerFixedCost() {
        return nbrOfGallonsPerFixedCost;
    }

    public void setNbrOfGallonsPerFixedCost(Long nbrOfGallonsPerFixedCost) {
        this.nbrOfGallonsPerFixedCost = nbrOfGallonsPerFixedCost;
    }

    public Set<WaterUtilization> getWaterUtilizations() {
        return ImmutableSet.copyOf(waterUtilizations);
    }

    public void addWaterUtilization(WaterUtilization waterUtilization) {
        Assert.notNull(waterUtilization, "WaterUtilization cannot be null");
        waterUtilizations.add(waterUtilization);
    }

    public Set<AgreementToScheduleBillPayFee> getAgreementToScheduleBillPayFees() {
        return ImmutableSet.copyOf(agreementToScheduleBillPayFees);
    }

    public List<BillPayFee> getScheduledBillPayFees() {
        final List<BillPayFee> billPayFees = new ArrayList<>();
        agreementToScheduleBillPayFees.forEach(agreementToScheduleBillPayFee -> {
            billPayFees.add(agreementToScheduleBillPayFee.getBillPayFee());
        });
        return billPayFees;
    }

    public void addAgreementToScheduleBillPayFee(AgreementToScheduleBillPayFee agreementToScheduleBillPayFee) {
        Assert.notNull(agreementToScheduleBillPayFee, "AgreementToScheduleBillPayFee cannot be null");
        agreementToScheduleBillPayFees.add(agreementToScheduleBillPayFee);
    }

    @Override
    public String describeAgreement() {
        StringBuffer sb = new StringBuffer("WaterServiceAgreement[Billed Entity: ")
                .append(embeddedAgreement.getBilledEntity())
                .append("; FixedCost: ")
                .append(embeddedAgreement.getFixedCost())
                .append("; numberOfGallonsPerFixedCost: ")
                .append(nbrOfGallonsPerFixedCost)
                .append("; Business: ")
                .append(embeddedUtilityService.getUtilityProviderBusiness().getName())
                .append("; Effective Date: ")
                .append(embeddedAgreement.getEffectiveDate())
                .append("; Termination Date: ")
                .append(embeddedAgreement.getTerminationDate())
                .append("]");
        return sb.toString();
    }

    public WaterServiceAgreementLite toAgreementLite() {
        WaterServiceAgreementLite waterServiceAgreementLite = WaterServiceAgreementLite.builder()
                .businessCustomerAcctID(embeddedUtilityService.getSvcCustomerAcctID())
                .numberOfGallonsPerFixedCost(nbrOfGallonsPerFixedCost)
                .embeddedAgreement(embeddedAgreement.toEmbeddedLite())
                .business(embeddedUtilityService.getUtilityProviderBusiness().toBusinessLite())
                .build();
        return waterServiceAgreementLite;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("nbrOfGallonsPerFixedCost", nbrOfGallonsPerFixedCost)
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

        public Builder nbrOfGallonsPerFixedCost(Long nbrOfGallonsPerFixedCost) {
            waterServiceAgreement.nbrOfGallonsPerFixedCost = nbrOfGallonsPerFixedCost;
            return this;
        }

        public Builder agreementToScheduleBillPayFee(AgreementToScheduleBillPayFee agreementToScheduleBillPayFee) {
            waterServiceAgreement.addAgreementToScheduleBillPayFee(agreementToScheduleBillPayFee);
            return this;
        }

        public WaterServiceAgreement build() {
            return waterServiceAgreement;
        }

    }

}