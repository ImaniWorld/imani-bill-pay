package com.imani.bill.pay.domain.agreement;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.imani.bill.pay.domain.billing.BillPayFee;
import com.imani.bill.pay.domain.utility.SewerServiceAgreement;
import com.imani.bill.pay.domain.utility.WaterServiceAgreement;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.annotations.Type;

import javax.persistence.*;

/**
 * @author manyce400
 */
@Entity
@Table(name="AgreementToScheduleBillPayFee")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AgreementToScheduleBillPayFee {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ID", nullable=false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "WaterServiceAgreementID", nullable = true)
    private WaterServiceAgreement waterServiceAgreement;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SewerServiceAgreementID", nullable = true)
    private SewerServiceAgreement sewerServiceAgreement;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "BillPayFeeID", nullable = true)
    private BillPayFee billPayFee;

    @Column(name="Enforced", columnDefinition = "TINYINT", length = 1)
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean enforced;


    public AgreementToScheduleBillPayFee() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public WaterServiceAgreement getWaterServiceAgreement() {
        return waterServiceAgreement;
    }

    public void setWaterServiceAgreement(WaterServiceAgreement waterServiceAgreement) {
        this.waterServiceAgreement = waterServiceAgreement;
    }

    public SewerServiceAgreement getSewerServiceAgreement() {
        return sewerServiceAgreement;
    }

    public void setSewerServiceAgreement(SewerServiceAgreement sewerServiceAgreement) {
        this.sewerServiceAgreement = sewerServiceAgreement;
    }

    public BillPayFee getBillPayFee() {
        return billPayFee;
    }

    public void setBillPayFee(BillPayFee billPayFee) {
        this.billPayFee = billPayFee;
    }

    public boolean isEnforced() {
        return enforced;
    }

    public void setEnforced(boolean enforced) {
        this.enforced = enforced;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("waterServiceAgreement", waterServiceAgreement)
                .append("sewerServiceAgreement", sewerServiceAgreement)
                .append("billPayFee", billPayFee)
                .append("enforced", enforced)
                .toString();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private AgreementToScheduleBillPayFee agreementToScheduleBillPayFee = new AgreementToScheduleBillPayFee();

        public Builder waterServiceAgreement(WaterServiceAgreement waterServiceAgreement) {
            agreementToScheduleBillPayFee.waterServiceAgreement = waterServiceAgreement;
            return this;
        }

        public Builder sewerServiceAgreement(SewerServiceAgreement sewerServiceAgreement) {
            agreementToScheduleBillPayFee.sewerServiceAgreement = sewerServiceAgreement;
            return this;
        }

        public Builder billPayFee(BillPayFee billPayFee) {
            agreementToScheduleBillPayFee.billPayFee = billPayFee;
            return this;
        }

        public Builder enforced(boolean enforced) {
            agreementToScheduleBillPayFee.enforced = enforced;
            return this;
        }

        public AgreementToScheduleBillPayFee build() {
            return agreementToScheduleBillPayFee;
        }

    }
}