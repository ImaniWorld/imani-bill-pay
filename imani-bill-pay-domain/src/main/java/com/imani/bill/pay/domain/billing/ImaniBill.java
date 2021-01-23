package com.imani.bill.pay.domain.billing;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.common.collect.ImmutableSet;
import com.imani.bill.pay.domain.AuditableRecord;
import com.imani.bill.pay.domain.daycare.ChildCareAgreement;
import com.imani.bill.pay.domain.education.TuitionAgreement;
import com.imani.bill.pay.domain.leasemanagement.PropertyLeaseAgreement;
import com.imani.bill.pay.domain.payment.EmbeddedPayment;
import com.imani.bill.pay.domain.payment.record.ImaniBillPayRecord;
import com.imani.bill.pay.domain.user.UserRecord;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;
import org.springframework.util.Assert;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * @author manyce400
 */
@Entity
@Table(name="ImaniBill")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ImaniBill extends AuditableRecord {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ID", nullable=false)
    private Long id;


    // Tracks the total amount that is owed on this bill
    @Column(name="AmountOwed", nullable=true)
    private Double amountOwed;


    // Tracks the total amount that has currently been paid on this MonthlyRentalBill
    @Column(name="AmountPaid", nullable=true)
    private Double amountPaid;

    // Defines the schedule of billing. This is sourced directly from agreement
    @Column(name="BillScheduleTypeE", nullable=false, length=25)
    @Enumerated(EnumType.STRING)
    private BillScheduleTypeE billScheduleTypeE;


    // Defines the type of service which we are billing for, this will dictate how billing is computed
    @Column(name="BillServiceRenderedTypeE", nullable=false, length=25)
    @Enumerated(EnumType.STRING)
    private BillServiceRenderedTypeE billServiceRenderedTypeE;


    // Tracks the week for which this bill is generated, if schedule is weekly
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "BillScheduleDate")
    @Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime billScheduleDate;


    // Tracks the user that this bill was generated for
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "UserRecordID", nullable = false)
    private UserRecord billedUser;


    // Tracks optional LeaseAgreement linked to this generated bill. Only IF residential leases
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ChildCareAgreementID")
    private ChildCareAgreement childCareAgreement;


    // Tracks optional LeaseAgreement linked to this generated bill. Only IF residential leases
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "PropertyLeaseAgreementID")
    private PropertyLeaseAgreement propertyLeaseAgreement;

    // Tracks optional EducationalTermAgreement linked to this generated bill. Only IF residential leases
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "TuitionAgreementID")
    private TuitionAgreement tuitionAgreement;


    // Tracks all additional fees that should be applied to this bill
    @OneToMany(cascade=CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "imaniBill")
    private Set<ImaniBillToFee> imaniBillToFees = new HashSet<>();

    // Tracks all bill payment records. Anytime a user attempts a payment a record is kept
    @OneToMany(cascade=CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "imaniBill")
    private Set<ImaniBillPayRecord> imaniBillPayRecords = new HashSet<>();


    public ImaniBill() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getAmountOwed() {
        return amountOwed;
    }

    public void setAmountOwed(Double amountOwed) {
        this.amountOwed = amountOwed;
    }

    public Double getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(Double amountPaid) {
        this.amountPaid = amountPaid;
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

    public DateTime getBillScheduleDate() {
        return billScheduleDate;
    }

    public void setBillScheduleDate(DateTime billScheduleDate) {
        this.billScheduleDate = billScheduleDate;
    }

    public UserRecord getBilledUser() {
        return billedUser;
    }

    public void setBilledUser(UserRecord billedUser) {
        this.billedUser = billedUser;
    }

    public ChildCareAgreement getChildCareAgreement() {
        return childCareAgreement;
    }

    public void setChildCareAgreement(ChildCareAgreement childCareAgreement) {
        this.childCareAgreement = childCareAgreement;
    }

    public PropertyLeaseAgreement getPropertyLeaseAgreement() {
        return propertyLeaseAgreement;
    }

    public void setPropertyLeaseAgreement(PropertyLeaseAgreement propertyLeaseAgreement) {
        this.propertyLeaseAgreement = propertyLeaseAgreement;
    }

    public boolean isPaidInFull() {
        return amountOwed.equals(amountPaid);
    }

    public boolean isValidPaymentAmount(double amountToBePaid) {
        return amountToBePaid <= getTotalStillOwed();
    }

    public double getTotalStillOwed() {
        return amountOwed - amountPaid;
    }

    public void addImaniBillToFee(BillPayFee billPayFee) {
        Assert.notNull(billPayFee, "BillPayFee cannot be null");
        ImaniBillToFee imaniBillToFee = ImaniBillToFee.builder()
                .billPayFee(billPayFee)
                .imaniBill(this)
                .feeEnforced(true)
                .build();
        imaniBillToFees.add(imaniBillToFee);
    }

    public void addImaniBillToFee(BillPayFee billPayFee, Double feeAmount) {
        Assert.notNull(billPayFee, "BillPayFee cannot be null");
        Assert.notNull(feeAmount, "feeAmount cannot be null");
        ImaniBillToFee imaniBillToFee = ImaniBillToFee.builder()
                .billPayFee(billPayFee)
                .imaniBill(this)
                .feeEnforced(true)
                .feeAmount(feeAmount)
                .build();
        imaniBillToFees.add(imaniBillToFee);
    }


    public Set<ImaniBillToFee> getImaniBillFees() {
        return ImmutableSet.copyOf(imaniBillToFees);
    }

    public boolean hasFees() {
        return getImaniBillFees().size() > 0;
    }

    public void addImaniBillPayRecord(ImaniBillPayRecord imaniBillPayRecord) {
        Assert.notNull(imaniBillPayRecord, "ImaniBillPayRecord cannot be null");
        imaniBillPayRecords.add(imaniBillPayRecord);
    }

    public Set<ImaniBillPayRecord> getImaniBillPayRecords() {
        return ImmutableSet.copyOf(imaniBillPayRecords);
    }

    public Set<ImaniBillToFee> getBillPayFeesByFeeTypeE(FeeTypeE feeTypeE) {
        Assert.notNull(billScheduleTypeE, "BillScheduleTypeE cannot be null");

        Set<ImaniBillToFee> billToFeeSet = new HashSet<>();
        for(ImaniBillToFee imaniBillToFee : imaniBillToFees) {
            if(feeTypeE == imaniBillToFee.getBillPayFee().getFeeTypeE()) {
                billToFeeSet.add(imaniBillToFee);
            }
        }

        return ImmutableSet.copyOf(billToFeeSet);
    }

    public ImaniBillExplained toImaniBillExplained() {
        ImaniBillExplained imaniBillExplained = ImaniBillExplained.builder()
                .imaniBillID(id)
                .amountOwed(amountOwed)
                .amountPaid(amountPaid)
                .billScheduleDate(billScheduleDate)
                .billScheduleTypeE(billScheduleTypeE)
                .billServiceRenderedTypeE(billServiceRenderedTypeE)
                .userRecordLite(billedUser.toUserRecordLite())
                .build();

        // Get any applied fees to buid a fee explanation
        for(ImaniBillToFee imaniBillToFee : imaniBillToFees) {
            BillPayFeeExplained billPayFeeExplained = imaniBillToFee.toBillPayFeeExplained();
            imaniBillExplained.addBillPayFeeExplained(billPayFeeExplained);
        }

        // Add all payment attempts made from records
        imaniBillPayRecords.forEach(imaniBillPayRecord -> {
            EmbeddedPayment embeddedPayment = imaniBillPayRecord.getEmbeddedPayment();
            imaniBillExplained.addEmbeddedPayment(embeddedPayment);
        });

        return imaniBillExplained;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", id)
                .append("amountOwed", amountOwed)
                .append("amountPaid", amountPaid)
                .append("billScheduleTypeE", billScheduleTypeE)
                .append("billServiceRenderedTypeE", billServiceRenderedTypeE)
                .append("billScheduleDate", billScheduleDate)
                .append("billedUser", billedUser)
                .append("childCareAgreement", childCareAgreement)
                .append("propertyLeaseAgreement", propertyLeaseAgreement)
                .append("educationalTermAgreement", tuitionAgreement)
                .toString();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final ImaniBill imaniBill = new ImaniBill();

        public Builder amountOwed(Double amountOwed) {
            imaniBill.amountOwed = amountOwed;
            return this;
        }

        public Builder amountPaid(Double amountPaid) {
            imaniBill.amountPaid = amountPaid;
            return this;
        }

        public Builder billScheduleTypeE(BillScheduleTypeE billScheduleTypeE) {
            imaniBill.billScheduleTypeE = billScheduleTypeE;
            return this;
        }

        public Builder billServiceRenderedTypeE(BillServiceRenderedTypeE billServiceRenderedTypeE) {
            imaniBill.billServiceRenderedTypeE = billServiceRenderedTypeE;
            return this;
        }

        public Builder billScheduleDate(DateTime billScheduleDate) {
            imaniBill.billScheduleDate = billScheduleDate;
            return this;
        }

        public Builder billedUser(UserRecord billedUser) {
            imaniBill.billedUser = billedUser;
            return this;
        }

        public Builder childCareAgreement(ChildCareAgreement childCareAgreement) {
            imaniBill.childCareAgreement = childCareAgreement;
            return this;
        }

        public Builder propertyLeaseAgreement(PropertyLeaseAgreement propertyLeaseAgreement) {
            imaniBill.propertyLeaseAgreement = propertyLeaseAgreement;
            return this;
        }

        public Builder tuitionAgreement(TuitionAgreement tuitionAgreement) {
            imaniBill.tuitionAgreement = tuitionAgreement;
            return this;
        }

        public Builder billPayFee(BillPayFee billPayFee) {
            imaniBill.addImaniBillToFee(billPayFee);
            return this;
        }

        public ImaniBill build() {
            return imaniBill;
        }
    }
}
