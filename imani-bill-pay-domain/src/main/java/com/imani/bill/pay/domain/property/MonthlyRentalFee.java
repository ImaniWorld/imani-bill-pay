package com.imani.bill.pay.domain.property;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.imani.bill.pay.domain.AuditableRecord;
import com.imani.bill.pay.domain.billing.FeePaymentChargeTypeE;
import com.imani.bill.pay.domain.billing.IFeePaymentModel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.util.Assert;

import javax.persistence.*;

/**
 * Monthly rental fee's are additional charges that a property manager can levy against a Property as part of the regular
 * monthly bill for specific defined reasons.
 *
 * @author manyce400
 */
@Entity
@Table(name="MonthlyRentalFee")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MonthlyRentalFee extends AuditableRecord implements IFeePaymentModel {



    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ID", nullable=false)
    private Long id;


    @Column(name="FeeName", nullable=false, length = 50)
    private String feeName;


    @Column(name="FeeDescription", nullable=true, length = 250)
    private String feeDescription;


    // Optional Flat amount to be levied/applied as an additional charge on a monthly bill
    @Column(name="OptionalFlatAmount", nullable=true)
    private Double optionalFlatAmount;


    // Optional fee percentage to be levied against a monthly bill.
    @Column(name="optionalFlatRate", nullable=true)
    private Double optionalFlatRate;


    // Defines the type of fee that is levied
    @Column(name="FeePaymentChargeType", nullable=false, length=25)
    @Enumerated(EnumType.STRING)
    private FeePaymentChargeTypeE feePaymentChargeTypeE;


    // Defines the type of fee that is levied
    @Column(name="RentalFeeType", nullable=false, length=25)
    @Enumerated(EnumType.STRING)
    private RentalFeeTypeE rentalFeeTypeE;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PropertyID", nullable = true)
    private Property property;


    public MonthlyRentalFee() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFeeName() {
        return feeName;
    }

    public void setFeeName(String feeName) {
        this.feeName = feeName;
    }

    public String getFeeDescription() {
        return feeDescription;
    }

    public void setFeeDescription(String feeDescription) {
        this.feeDescription = feeDescription;
    }

    public Double getOptionalFlatAmount() {
        return optionalFlatAmount;
    }

    public void setOptionalFlatAmount(Double optionalFlatAmount) {
        this.optionalFlatAmount = optionalFlatAmount;
    }

    public Double getOptionalFlatRate() {
        return optionalFlatRate;
    }

    public void setOptionalFlatRate(Double optionalFlatRate) {
        // enforce that this has to be between 0 and 100
        Assert.notNull(optionalFlatRate, "optionalFlatRate cannot be null");
        Assert.isTrue(optionalFlatRate > 0, "optionalFlatRate has to be > 0");
        Assert.isTrue(optionalFlatRate <= 100, "optionalFlatRate cannot exceed 100");
        this.optionalFlatRate = optionalFlatRate;
    }

    public FeePaymentChargeTypeE getFeePaymentChargeTypeE() {
        return feePaymentChargeTypeE;
    }

    public void setFeePaymentChargeTypeE(FeePaymentChargeTypeE feePaymentChargeTypeE) {
        this.feePaymentChargeTypeE = feePaymentChargeTypeE;
    }

    public RentalFeeTypeE getRentalFeeTypeE() {
        return rentalFeeTypeE;
    }

    public void setRentalFeeTypeE(RentalFeeTypeE rentalFeeTypeE) {
        this.rentalFeeTypeE = rentalFeeTypeE;
    }

    public Property getProperty() {
        return property;
    }

    public void setProperty(Property property) {
        this.property = property;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("feeName", feeName)
                .append("feeDescription", feeDescription)
                .append("optionalFlatAmount", optionalFlatAmount)
                .append("optionalFlatRate", optionalFlatRate)
                .append("feePaymentChargeTypeE", feePaymentChargeTypeE)
                .append("rentalFeeTypeE", rentalFeeTypeE)
                .append("property", property)
                .toString();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private MonthlyRentalFee monthlyRentalFee = new MonthlyRentalFee();

        public Builder feeName(String feeName) {
            monthlyRentalFee.feeName = feeName;
            return this;
        }

        public Builder feeDescription(String feeDescription) {
            monthlyRentalFee.feeDescription = feeDescription;
            return this;
        }

        public Builder optionalFlatAmount(Double optionalFlatAmount) {
            monthlyRentalFee.optionalFlatAmount = optionalFlatAmount;
            return this;
        }

        public Builder optionalFlatRate(Double optionalFlatRate) {
            monthlyRentalFee.optionalFlatRate = optionalFlatRate;
            return this;
        }

        public Builder feePaymentChargeTypeE(FeePaymentChargeTypeE feePaymentChargeTypeE) {
            monthlyRentalFee.feePaymentChargeTypeE = feePaymentChargeTypeE;
            return this;
        }

        public Builder rentalFeeTypeE(RentalFeeTypeE rentalFeeTypeE) {
            monthlyRentalFee.rentalFeeTypeE = rentalFeeTypeE;
            return this;
        }

        public Builder property(Property property) {
            monthlyRentalFee.property = property;
            return this;
        }

        public MonthlyRentalFee build() {
            return monthlyRentalFee;
        }

    }
}
