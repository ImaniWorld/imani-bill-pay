package com.imani.bill.pay.domain.property;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.imani.bill.pay.domain.AuditableRecord;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;

/**
 * @author manyce400
 */
@Entity
@Table(name="MonthlyRentalBillFee")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MonthlyRentalBillFee extends AuditableRecord {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ID", nullable=false)
    private Long id;


    // Tracks the MonthlyRentalBill which should be accessed a fee.
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MonthlyRentalBillID", nullable = false)
    private MonthlyRentalBill monthlyRentalBill;


    // Tracks the MonthlyRentalFee which should be applied to the bill
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MonthlyRentalFeeBillID", nullable = false)
    private MonthlyRentalFee monthlyRentalFee;


    public MonthlyRentalBillFee() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public MonthlyRentalBill getMonthlyRentalBill() {
        return monthlyRentalBill;
    }

    public void setMonthlyRentalBill(MonthlyRentalBill monthlyRentalBill) {
        this.monthlyRentalBill = monthlyRentalBill;
    }

    public MonthlyRentalFee getMonthlyRentalFee() {
        return monthlyRentalFee;
    }

    public void setMonthlyRentalFee(MonthlyRentalFee monthlyRentalFee) {
        this.monthlyRentalFee = monthlyRentalFee;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("monthlyRentalBill", monthlyRentalBill)
                .append("monthlyRentalFee", monthlyRentalFee)
                .toString();
    }


    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private MonthlyRentalBillFee monthlyRentalBillFee = new MonthlyRentalBillFee();

        public Builder monthlyRentalBill(MonthlyRentalBill monthlyRentalBill) {
            monthlyRentalBillFee.monthlyRentalBill = monthlyRentalBill;
            return this;
        }

        public Builder monthlyRentalFee(MonthlyRentalFee monthlyRentalFee) {
            monthlyRentalBillFee.monthlyRentalFee = monthlyRentalFee;
            return this;
        }

        public MonthlyRentalBillFee build() {
            return monthlyRentalBillFee;
        }
    }
}
