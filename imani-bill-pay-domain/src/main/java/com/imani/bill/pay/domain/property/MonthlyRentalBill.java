package com.imani.bill.pay.domain.property;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.common.collect.ImmutableSet;
import com.imani.bill.pay.domain.AuditableRecord;
import com.imani.bill.pay.domain.user.UserResidence;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
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
@Table(name="MonthlyRentalBill")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MonthlyRentalBill extends AuditableRecord {



    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ID", nullable=false)
    private Long id;


    // Tracks the total amount that has currently been paid on this MonthlyRentalBill
    @Column(name="AmountPaid", nullable=true)
    private Double amountPaid;


    // Tracks the month for which this monthly rental bill is due for
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "RentalMonth", nullable = false)
    @Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime rentalMonth;


    // Bill should be closed at the end of the monthly billing cycle.
    // All unpaid/uncollected charges should be moved forward to the next billing cycle.
    @Column(name="BillClosed", nullable = true, columnDefinition = "TINYINT", length = 1)
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean billClosed;


    // Tracks the User renter
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "UserResidenceID", nullable = false)
    private UserResidence userResidence;


    // Tracks the LeaseAgreement linked to this monthly payment
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "RentalAgreementID", nullable = false)
    private LeaseAgreement leaseAgreement;


    // Tracks all additional fees that should be applied to this bill
    @OneToMany(cascade=CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "monthlyRentalBill")
    private Set<MonthlyRentalBillFee> monthlyRentalBillFees = new HashSet<>();


    public MonthlyRentalBill() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(Double amountPaid) {
        this.amountPaid = amountPaid;
    }

    public DateTime getRentalMonth() {
        return rentalMonth;
    }

    public void setRentalMonth(DateTime rentalMonth) {
        this.rentalMonth = rentalMonth;
    }

    public boolean isBillClosed() {
        return billClosed;
    }

    public void setBillClosed(boolean billClosed) {
        this.billClosed = billClosed;
    }

    public UserResidence getUserResidence() {
        return userResidence;
    }

    public void setUserResidence(UserResidence userResidence) {
        this.userResidence = userResidence;
    }

    public LeaseAgreement getLeaseAgreement() {
        return leaseAgreement;
    }

    public void setLeaseAgreement(LeaseAgreement leaseAgreement) {
        this.leaseAgreement = leaseAgreement;
    }

    public void addMonthlyRentalFee(MonthlyRentalFee monthlyRentalFee) {
        Assert.notNull(monthlyRentalFee, "MonthlyRentalFee cannot be null");
        MonthlyRentalBillFee monthlyRentalBillFee = MonthlyRentalBillFee.builder()
                .monthlyRentalFee(monthlyRentalFee)
                .monthlyRentalBill(this)
                .build();
        monthlyRentalBillFees.add(monthlyRentalBillFee);
    }

    public Set<MonthlyRentalBillFee> getMonthlyRentalBillFees() {
        return ImmutableSet.copyOf(monthlyRentalBillFees);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        MonthlyRentalBill that = (MonthlyRentalBill) o;

        return new EqualsBuilder()
                .append(billClosed, that.billClosed)
                .append(id, that.id)
                .append(amountPaid, that.amountPaid)
                .append(rentalMonth, that.rentalMonth)
                .append(userResidence, that.userResidence)
                .append(leaseAgreement, that.leaseAgreement)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .append(amountPaid)
                .append(rentalMonth)
                .append(billClosed)
                .append(userResidence)
                .append(leaseAgreement)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("amountPaid", amountPaid)
                .append("rentalMonth", rentalMonth)
                .append("billClosed", billClosed)
                .append("userResidence", userResidence)
                .append("leaseAgreement", leaseAgreement)
                .toString();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private MonthlyRentalBill monthlyRentalBill = new MonthlyRentalBill();

        public Builder amountPaid(Double amountPaid) {
            monthlyRentalBill.amountPaid = amountPaid;
            return this;
        }

        public Builder rentalMonth(DateTime rentalMonth) {
            monthlyRentalBill.rentalMonth = rentalMonth;
            return this;
        }

        public Builder billClosed(boolean billClosed) {
            monthlyRentalBill.billClosed = billClosed;
            return this;
        }

        public Builder userResidence(UserResidence userResidence) {
            monthlyRentalBill.userResidence = userResidence;
            return this;
        }

        public Builder rentalAgreement(LeaseAgreement leaseAgreement) {
            monthlyRentalBill.leaseAgreement = leaseAgreement;
            return this;
        }

        public Builder monthlyRentalFee(MonthlyRentalFee monthlyRentalFee) {
            monthlyRentalBill.addMonthlyRentalFee(monthlyRentalFee);
            return this;
        }

        public MonthlyRentalBill build() {
            return monthlyRentalBill;
        }

    }
}
