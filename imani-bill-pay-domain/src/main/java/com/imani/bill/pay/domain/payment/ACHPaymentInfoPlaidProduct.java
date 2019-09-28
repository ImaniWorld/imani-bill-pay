package com.imani.bill.pay.domain.payment;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.imani.bill.pay.domain.AuditableRecord;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;

/**
 * Tracks all the available Plaid Products on an ACH account.
 *
 * @author manyce400
 */
@Entity
@Table(name="ACHPaymentInfo")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ACHPaymentInfoPlaidProduct extends AuditableRecord {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ID", nullable=false)
    private Long id;


    @Column(name="PlaidProduct", nullable=false, length=20)
    @Enumerated(EnumType.STRING)
    private PlaidProductE plaidProductE;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ACHPaymentInfoID", nullable = false)
    private ACHPaymentInfo achPaymentInfo;

    public ACHPaymentInfoPlaidProduct() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PlaidProductE getPlaidProductE() {
        return plaidProductE;
    }

    public void setPlaidProductE(PlaidProductE plaidProductE) {
        this.plaidProductE = plaidProductE;
    }

    public ACHPaymentInfo getAchPaymentInfo() {
        return achPaymentInfo;
    }

    public void setAchPaymentInfo(ACHPaymentInfo achPaymentInfo) {
        this.achPaymentInfo = achPaymentInfo;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("plaidProductE", plaidProductE)
                .append("achPaymentInfo", achPaymentInfo)
                .toString();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private ACHPaymentInfoPlaidProduct achPaymentInfoPlaidProduct = new ACHPaymentInfoPlaidProduct();

        public Builder plaidProductE(PlaidProductE plaidProductE) {
            achPaymentInfoPlaidProduct.plaidProductE = plaidProductE;
            return this;
        }

        public Builder achPaymentInfo(ACHPaymentInfo achPaymentInfo) {
            achPaymentInfoPlaidProduct.achPaymentInfo = achPaymentInfo;
            return this;
        }

        public ACHPaymentInfoPlaidProduct build() {
            return achPaymentInfoPlaidProduct;
        }
    }
}
