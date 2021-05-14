package com.imani.bill.pay.domain.billing;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.imani.bill.pay.domain.AuditableRecord;
import com.imani.bill.pay.domain.user.UserRecord;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.*;

/**
 * Tracks all the fees that have been applied on an Imani Bill.
 *
 * @author manyce400
 */
@Entity
@Table(name="ImaniBillToFee")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ImaniBillToFee extends AuditableRecord {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ID", nullable=false)
    private Long id;


    // Tracks the total amount that is owed on this bill
    @Column(name="FeeAmount", nullable=true)
    private Double feeAmount;


    // True by default.  IF fee is waved, this will be updated and bill will have to be re-calculated
    @Column(name="FeeEnforced", nullable = false, columnDefinition = "TINYINT", length = 1)
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean feeEnforced;


    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ImaniBillID", nullable = false)
    private ImaniBill imaniBill;


    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BillPayFeeID", nullable = false)
    private BillPayFee billPayFee;


    // Tracks optional user that can apply a fee.  IF system generated fee this should be null
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UserApplyingFeeID")
    private UserRecord userApplyingFee;


    // Tracks optional user that waved fee.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UserWavingFeeID")
    private UserRecord userWavingFee;


    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "FeeLeviedDate")
    @Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime feeLeviedDate;


    public ImaniBillToFee() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getFeeAmount() {
        return feeAmount;
    }

    public void setFeeAmount(Double feeAmount) {
        this.feeAmount = feeAmount;
    }

    public boolean isFeeEnforced() {
        return feeEnforced;
    }

    public void setFeeEnforced(boolean feeEnforced) {
        this.feeEnforced = feeEnforced;
    }

    public ImaniBill getImaniBill() {
        return imaniBill;
    }

    public void setImaniBill(ImaniBill imaniBill) {
        this.imaniBill = imaniBill;
    }

    public BillPayFee getBillPayFee() {
        return billPayFee;
    }

    public void setBillPayFee(BillPayFee billPayFee) {
        this.billPayFee = billPayFee;
    }

    public UserRecord getUserApplyingFee() {
        return userApplyingFee;
    }

    public void setUserApplyingFee(UserRecord userApplyingFee) {
        this.userApplyingFee = userApplyingFee;
    }

    public UserRecord getUserWavingFee() {
        return userWavingFee;
    }

    public void setUserWavingFee(UserRecord userWavingFee) {
        this.userWavingFee = userWavingFee;
    }

    public DateTime getFeeLeviedDate() {
        return feeLeviedDate;
    }

    public void setFeeLeviedDate(DateTime feeLeviedDate) {
        this.feeLeviedDate = feeLeviedDate;
    }

    public BillPayFeeExplained toBillPayFeeExplained() {
        BillPayFeeExplained billPayFeeExplained = BillPayFeeExplained.builder()
                .feeName(billPayFee.getFeeName())
                .feeCharge(feeAmount)
                .feeAppliedDate(feeLeviedDate)
                .build();
        return billPayFeeExplained;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("feeAmount", feeAmount)
                .append("feeEnforced", feeEnforced)
                .append("imaniBill", imaniBill)
                .append("billPayFee", billPayFee)
                .append("userApplyingFee", userApplyingFee)
                .append("userWavingFee", userWavingFee)
                .append("feeLeviedDate", feeLeviedDate)
                .toString();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final ImaniBillToFee imaniBillToFee = new ImaniBillToFee();

        public Builder feeAmount(Double feeAmount) {
            imaniBillToFee.feeAmount = feeAmount;
            return this;
        }

        public Builder feeEnforced(boolean feeEnforced) {
            imaniBillToFee.feeEnforced = feeEnforced;
            return this;
        }

        public Builder imaniBill(ImaniBill imaniBill) {
            imaniBillToFee.imaniBill = imaniBill;
            return this;
        }

        public Builder billPayFee(BillPayFee billPayFee) {
            imaniBillToFee.billPayFee = billPayFee;
            return this;
        }

        public Builder userApplyingFee(UserRecord userApplyingFee) {
            imaniBillToFee.userApplyingFee = userApplyingFee;
            return this;
        }

        public Builder userWavingFee(UserRecord userWavingFee) {
            imaniBillToFee.userWavingFee = userWavingFee;
            return this;
        }

        public Builder feeLeviedDate(DateTime feeLeviedDate) {
            imaniBillToFee.feeLeviedDate = feeLeviedDate;
            return this;
        }

        public ImaniBillToFee build() {
            return imaniBillToFee;
        }
    }
}
