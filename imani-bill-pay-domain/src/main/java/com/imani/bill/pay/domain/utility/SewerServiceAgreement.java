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
@Table(name="SewerServiceAgreement")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SewerServiceAgreement extends AuditableRecord implements IHasBillingAgreement {



    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ID", nullable=false)
    private Long id;

    @Embedded
    private EmbeddedAgreement embeddedAgreement;

    @Embedded
    private EmbeddedUtilityService embeddedUtilityService;


    @OneToMany(cascade=CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "sewerServiceAgreement")
    private Set<AgreementToScheduleBillPayFee> agreementToScheduleBillPayFees = new HashSet<>();


    public SewerServiceAgreement() {

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

    public SewerServiceAgreementLite toSewerServiceAgreementLite() {
        SewerServiceAgreementLite sewerServiceAgreementLite = SewerServiceAgreementLite.builder()
                .id(id)
                .business(embeddedUtilityService.getUtilityProviderBusiness().toBusinessLite())
                .embeddedAgreement(embeddedAgreement.toEmbeddedLite())
                .businessCustomerAcctID(embeddedUtilityService.getSvcCustomerAcctID())
                .build();
        return sewerServiceAgreementLite;
    }


    @Override
    public String describeAgreement() {
        return null;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("embeddedAgreement", embeddedAgreement)
                .append("embeddedUtilityService", embeddedUtilityService)
                .toString();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private SewerServiceAgreement sewerServiceAgreement = new SewerServiceAgreement();

        public Builder embeddedAgreement(EmbeddedAgreement embeddedAgreement) {
            sewerServiceAgreement.embeddedAgreement = embeddedAgreement;
            return this;
        }

        public Builder embeddedUtilityService(EmbeddedUtilityService embeddedUtilityService) {
            sewerServiceAgreement.embeddedUtilityService = embeddedUtilityService;
            return this;
        }

        public Builder agreementToScheduleBillPayFee(AgreementToScheduleBillPayFee agreementToScheduleBillPayFee) {
            sewerServiceAgreement.addAgreementToScheduleBillPayFee(agreementToScheduleBillPayFee);
            return this;
        }

        public SewerServiceAgreement build() {
            return sewerServiceAgreement;
        }

    }

}