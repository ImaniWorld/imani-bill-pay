package com.imani.bill.pay.domain.daycare;

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
@Table(name="ChildCareAgreement")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChildCareAgreement extends AuditableRecord implements IHasBillingAgreement {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ID", nullable=false)
    private Long id;

    @Column(name="ChildName", nullable = false, length = 100)
    private String childName;


    @Embedded
    private EmbeddedAgreement embeddedAgreement;


    // Tracks DayCare which agreement is made with
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DayCareID", nullable = false)
    private DayCare dayCare;


    public ChildCareAgreement() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getChildName() {
        return childName;
    }

    public void setChildName(String childName) {
        this.childName = childName;
    }

    public EmbeddedAgreement getEmbeddedAgreement() {
        return embeddedAgreement;
    }

    public void setEmbeddedAgreement(EmbeddedAgreement embeddedAgreement) {
        this.embeddedAgreement = embeddedAgreement;
    }

    public DayCare getDayCare() {
        return dayCare;
    }

    public void setDayCare(DayCare dayCare) {
        this.dayCare = dayCare;
    }

    @Override
    public String describeAgreement() {
        return null;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("childName", childName)
                .append("embeddedAgreement", embeddedAgreement)
                .append("dayCare", dayCare)
                .toString();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final ChildCareAgreement childCareAgreement = new ChildCareAgreement();

        public Builder childName(String childName) {
            childCareAgreement.childName = childName;
            return this;
        }

        public Builder embeddedAgreement(EmbeddedAgreement embeddedAgreement) {
            childCareAgreement.embeddedAgreement = embeddedAgreement;
            return this;
        }

        public Builder dayCare(DayCare dayCare) {
            childCareAgreement.dayCare = dayCare;
            return this;
        }

        public ChildCareAgreement build() {
            return childCareAgreement;
        }
    }

}
