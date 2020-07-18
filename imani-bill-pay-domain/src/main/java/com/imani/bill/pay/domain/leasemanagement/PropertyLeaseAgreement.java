package com.imani.bill.pay.domain.leasemanagement;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.imani.bill.pay.domain.AuditableRecord;
import com.imani.bill.pay.domain.agreement.EmbeddedAgreement;
import com.imani.bill.pay.domain.agreement.IHasBillingAgreement;
import com.imani.bill.pay.domain.billing.BillScheduleTypeE;
import com.imani.bill.pay.domain.property.Apartment;
import com.imani.bill.pay.domain.property.Property;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;

/**
 * @author manyce400
 */
@Entity
@Table(name="PropertyLeaseAgreement")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PropertyLeaseAgreement extends AuditableRecord implements IHasBillingAgreement {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ID", nullable=false)
    private Long id;


    @Embedded
    private EmbeddedAgreement embeddedAgreement;


    // Optional if only apartment in entire property was leased
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "LeasedApartmentID", nullable = true)
    private Apartment leasedApartment;


    // Optional if entire property is leased
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "LeasedPropertyID", nullable = true)
    private Property leasedProperty;


    public PropertyLeaseAgreement() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EmbeddedAgreement getEmbeddedAgreement() {
        return embeddedAgreement;
    }

    public void setEmbeddedAgreement(EmbeddedAgreement embeddedAgreement) {
        this.embeddedAgreement = embeddedAgreement;
    }

    public Apartment getLeasedApartment() {
        return leasedApartment;
    }

    public void setLeasedApartment(Apartment leasedApartment) {
        this.leasedApartment = leasedApartment;
    }

    public Property getLeasedProperty() {
        return leasedProperty;
    }

    public void setLeasedProperty(Property leasedProperty) {
        this.leasedProperty = leasedProperty;
    }

    public PropertyLeaseAgreementLite toPropertyLeaseAgreementLite() {
        PropertyLeaseAgreementLite leaseAgreementLite = PropertyLeaseAgreementLite.builder()
                .id(id)
                .fixedCost(embeddedAgreement.getFixedCost())
                .effectiveDate(embeddedAgreement.getEffectiveDate())
                .terminationDate(embeddedAgreement.getTerminationDate())
                .agreementInForce(embeddedAgreement.isAgreementInForce())
                .billScheduleTypeE(BillScheduleTypeE.MONTHLY)
                .build();
        return leaseAgreementLite;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("embeddedAgreement", embeddedAgreement)
                .append("leasedApartment", leasedApartment)
                .append("leasedProperty", leasedProperty)
                .toString();
    }

    public static Builder builder() {
        return new Builder();
    }


    public static final class Builder {

        private PropertyLeaseAgreement leaseAgreement = new PropertyLeaseAgreement();

        public Builder embeddedAgreement(EmbeddedAgreement embeddedAgreement) {
            leaseAgreement.embeddedAgreement = embeddedAgreement;
            return this;
        }

        public Builder leasedApartment(Apartment leasedApartment) {
            leaseAgreement.leasedApartment = leasedApartment;
            return this;
        }

        public Builder leasedProperty(Property leasedProperty) {
            leaseAgreement.leasedProperty = leasedProperty;
            return this;
        }

        public PropertyLeaseAgreement build() {
            return leaseAgreement;
        }

    }

}
