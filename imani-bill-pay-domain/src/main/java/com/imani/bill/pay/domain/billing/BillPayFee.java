package com.imani.bill.pay.domain.billing;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.imani.bill.pay.domain.AuditableRecord;
import com.imani.bill.pay.domain.business.Business;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.util.Assert;

import javax.persistence.*;

/**
 * @author manyce400
 */
@Entity
@Table(name="BillPayFee")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BillPayFee extends AuditableRecord implements IFeePaymentModel {


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
    @Column(name="OptionalFlatRate", nullable=true)
    private Double optionalFlatRate;


    // Defines the type of fee that is levied
    @Column(name="FeePaymentChargeType", nullable=false, length=25)
    @Enumerated(EnumType.STRING)
    private FeePaymentChargeTypeE feePaymentChargeTypeE;

    // Defines the type of fee that is levied
    @Column(name="FeeTypeE", nullable=false, length=25)
    @Enumerated(EnumType.STRING)
    private FeeTypeE feeTypeE;

    // IF FeeType is scheduled then this will determine the schedule to apply fee
    @Column(name="BillScheduleTypeE", length=20)
    @Enumerated(EnumType.STRING)
    private BillScheduleTypeE billScheduleTypeE;

    // Defines the type of service which we are billing for, this will dictate how billing is computed
    @Column(name="BillServiceRenderedTypeE", nullable=false, length=25)
    @Enumerated(EnumType.STRING)
    private BillServiceRenderedTypeE billServiceRenderedTypeE;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BusinessID", nullable = false)
    private Business business;


    public BillPayFee() {

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

    @Override
    public Double getOptionalFlatAmount() {
        return optionalFlatAmount;
    }

    public void setOptionalFlatAmount(Double optionalFlatAmount) {
        this.optionalFlatAmount = optionalFlatAmount;
    }

    @Override
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

    @Override
    public FeePaymentChargeTypeE getFeePaymentChargeTypeE() {
        return feePaymentChargeTypeE;
    }

    public void setFeePaymentChargeTypeE(FeePaymentChargeTypeE feePaymentChargeTypeE) {
        this.feePaymentChargeTypeE = feePaymentChargeTypeE;
    }

    public FeeTypeE getFeeTypeE() {
        return feeTypeE;
    }

    public void setFeeTypeE(FeeTypeE feeTypeE) {
        this.feeTypeE = feeTypeE;
    }

    public BillScheduleTypeE getBillScheduleTypeE() {
        return billScheduleTypeE;
    }

    public void setBillScheduleTypeE(BillScheduleTypeE billScheduleTypeE) {
        this.billScheduleTypeE = billScheduleTypeE;
    }

    public BillServiceRenderedTypeE getBillServiceRenderedTypeE() {
        return billServiceRenderedTypeE;
    }

    public void setBillServiceRenderedTypeE(BillServiceRenderedTypeE billServiceRenderedTypeE) {
        this.billServiceRenderedTypeE = billServiceRenderedTypeE;
    }

    public Business getBusiness() {
        return business;
    }

    public void setBusiness(Business business) {
        this.business = business;
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
                .append("feeTypeE", feeTypeE)
                .append("billScheduleTypeE", billScheduleTypeE)
                .append("billServiceRenderedTypeE", billServiceRenderedTypeE)
                .append("business", business)
                .toString();
    }

}
