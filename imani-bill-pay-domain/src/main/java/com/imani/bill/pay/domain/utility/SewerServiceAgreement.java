package com.imani.bill.pay.domain.utility;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.imani.bill.pay.domain.AuditableRecord;
import com.imani.bill.pay.domain.agreement.EmbeddedAgreement;
import com.imani.bill.pay.domain.agreement.IHasBillingAgreement;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;

/**
 * @author manyce400
 */
@Entity
@Table(name="WaterServiceAgreement")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SewerServiceAgreement extends AuditableRecord implements IHasBillingAgreement {



    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ID", nullable=false)
    private Long id;

    @Embedded
    private EmbeddedUtilityService embeddedUtilityService;

    @Embedded
    private EmbeddedAgreement embeddedAgreement;


    public SewerServiceAgreement() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EmbeddedUtilityService getEmbeddedUtilityService() {
        return embeddedUtilityService;
    }

    public void setEmbeddedUtilityService(EmbeddedUtilityService embeddedUtilityService) {
        this.embeddedUtilityService = embeddedUtilityService;
    }

    @Override
    public EmbeddedAgreement getEmbeddedAgreement() {
        return embeddedAgreement;
    }

    public void setEmbeddedAgreement(EmbeddedAgreement embeddedAgreement) {
        this.embeddedAgreement = embeddedAgreement;
    }

    @Override
    public String describeAgreement() {
        return null;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("embeddedUtilityService", embeddedUtilityService)
                .append("embeddedAgreement", embeddedAgreement)
                .toString();
    }

    public static class Builder {

        private SewerServiceAgreement sewerServiceAgreement = new SewerServiceAgreement();

        public Builder embeddedUtilityService(EmbeddedUtilityService embeddedUtilityService) {
            sewerServiceAgreement.embeddedUtilityService = embeddedUtilityService;
            return this;
        }

        public Builder embeddedAgreement(EmbeddedAgreement embeddedAgreement) {
            sewerServiceAgreement.embeddedAgreement = embeddedAgreement;
            return this;
        }

        public SewerServiceAgreement build() {
            return sewerServiceAgreement;
        }

    }

}