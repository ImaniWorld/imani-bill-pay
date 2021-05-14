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
import com.imani.bill.pay.domain.utility.SewerServiceAgreement;
import com.imani.bill.pay.domain.utility.WaterServiceAgreement;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;
import org.springframework.util.Assert;

import javax.persistence.*;
import java.util.*;

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
    @Column(name="AmountOwed", nullable=false)
    private Double amountOwed;


    // Tracks the total amount that has currently been paid on this MonthlyRentalBill
    @Column(name="AmountPaid", columnDefinition = "double default 0")
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

    // Tracks optional UtilityServiceAgreement linked to this generated bill.
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "WaterServiceAgreementID")
    private WaterServiceAgreement waterServiceAgreement;

    // Tracks optional UtilityServiceAgreement linked to this generated bill.
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "SewerServiceAgreementID")
    private SewerServiceAgreement sewerServiceAgreement;


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

    public TuitionAgreement getTuitionAgreement() {
        return tuitionAgreement;
    }

    public void setTuitionAgreement(TuitionAgreement tuitionAgreement) {
        this.tuitionAgreement = tuitionAgreement;
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

    public boolean isPaidInFull() {
        return amountOwed > 0 && amountOwed.equals(amountPaid);
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
                .feeLeviedDate(DateTime.now())
                .build();
        imaniBillToFees.add(imaniBillToFee);
    }

    public void levyLateFee(BillPayFee billPayFee, Double feeAmount, DateTime qtrEndDateTime) {
        Assert.notNull(billPayFee, "BillPayFee cannot be null");
        Assert.isTrue(billPayFee.getFeeTypeE() == FeeTypeE.LATE_FEE, "Levied fee must be of type LATE_FEE");
        Assert.notNull(feeAmount, "feeAmount cannot be null");
        Assert.notNull(qtrEndDateTime, "qtrEndDateTime cannot be null");

        ImaniBillToFee imaniBillToFee = ImaniBillToFee.builder()
                .billPayFee(billPayFee)
                .imaniBill(this)
                .feeEnforced(true)
                .feeAmount(feeAmount)
                .build();

        // IF qtrEndDateTime > now then use now else use qtrEndDateTime.
        // This ensures that IF now is after qtr end date, we will default to quarter end date
        DateTime now = DateTime.now();
        if(now.isAfter(qtrEndDateTime)) {
            imaniBillToFee.setFeeLeviedDate(qtrEndDateTime);
        } else {
            imaniBillToFee.setFeeLeviedDate(now);
        }

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
        Assert.notNull(feeTypeE, "feeTypeE cannot be null");

        Set<ImaniBillToFee> billToFeeSet = new HashSet<>();
        for(ImaniBillToFee imaniBillToFee : imaniBillToFees) {
            if(feeTypeE == imaniBillToFee.getBillPayFee().getFeeTypeE()) {
                billToFeeSet.add(imaniBillToFee);
            }
        }

        return ImmutableSet.copyOf(billToFeeSet);
    }

    public Optional<ImaniBillToFee> getLateFeeBetweenPeriod(DateTime start, DateTime end) {
        ImaniBillToFee feeInPeriod = null;

        for(ImaniBillToFee imaniBillToFee : imaniBillToFees) {
            if (FeeTypeE.LATE_FEE == imaniBillToFee.getBillPayFee().getFeeTypeE()) {
                DateTime leviedDate = imaniBillToFee.getFeeLeviedDate();

                // Special case:  IF fee was levied after quarter ended, system defaults levied date to last day in quarter
                if(leviedDate.equals(end)) {
                    feeInPeriod = imaniBillToFee;
                    break;
                } else if(leviedDate.isAfter(start) && leviedDate.isBefore(end)) {
                    feeInPeriod = imaniBillToFee;
                    break;
                }
            }
        }

        return feeInPeriod == null ? Optional.empty() : Optional.of(feeInPeriod);
    }

    public List<ImaniBillToFee> getScheduledFees() {
        List<ImaniBillToFee> scheduledFees = new ArrayList<>();

        for(ImaniBillToFee imaniBillToFee : imaniBillToFees) {
            if (FeeTypeE.Scheduled_Fee == imaniBillToFee.getBillPayFee().getFeeTypeE()) {
                scheduledFees.add(imaniBillToFee);
            }
        }

        return scheduledFees;
    }

    public double computeTotalFeeAmountByFeeTypeE(FeeTypeE feeTypeE) {
        Assert.notNull(feeTypeE, "feeTypeE cannot be null");

        double totalFeesLeviedAmount = 0;
        for(ImaniBillToFee imaniBillToFee : imaniBillToFees) {
            if (feeTypeE == imaniBillToFee.getBillPayFee().getFeeTypeE()) {
                totalFeesLeviedAmount = totalFeesLeviedAmount + imaniBillToFee.getFeeAmount();
            }
        }

        return totalFeesLeviedAmount;
    }

    public ImaniBillExplained toImaniBillExplained() {
        BillPurposeExplained billPurposeExplained = BillPurposeExplained.builder()
                .billScheduleDate(billScheduleDate)
                .billScheduleTypeE(billScheduleTypeE)
                .billServiceRenderedTypeE(billServiceRenderedTypeE)
                .build();

        ImaniBillExplained imaniBillExplained = ImaniBillExplained.builder()
                .imaniBillID(id)
                .amountOwed(amountOwed)
                .amountPaid(amountPaid)
                .billPurposeExplained(billPurposeExplained)
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
        return new ToStringBuilder(this)
                .append("id", id)
                .append("amountOwed", amountOwed)
                .append("amountPaid", amountPaid)
                .append("billScheduleTypeE", billScheduleTypeE)
                .append("billServiceRenderedTypeE", billServiceRenderedTypeE)
                .append("billScheduleDate", billScheduleDate)
                .append("childCareAgreement", childCareAgreement)
                .append("propertyLeaseAgreement", propertyLeaseAgreement)
                .append("tuitionAgreement", tuitionAgreement)
                .append("waterServiceAgreement", waterServiceAgreement)
                .append("sewerServiceAgreement", sewerServiceAgreement)
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

        public Builder utilityServiceAgreement(WaterServiceAgreement waterServiceAgreement) {
            imaniBill.waterServiceAgreement = waterServiceAgreement;
            return this;
        }

        public Builder waterServiceAgreement(WaterServiceAgreement waterServiceAgreement) {
            imaniBill.waterServiceAgreement = waterServiceAgreement;
            return this;
        }

        public Builder sewerServiceAgreement(SewerServiceAgreement sewerServiceAgreement) {
            imaniBill.sewerServiceAgreement = sewerServiceAgreement;
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
