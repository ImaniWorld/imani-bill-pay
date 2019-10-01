package com.imani.bill.pay.domain.payment;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.imani.bill.pay.domain.property.MonthlyRentalBill;
import com.imani.bill.pay.domain.user.UserRecord;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;

/**
 * @author manyce400
 */
@Entity
@Table(name="RentalPaymentHistory")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RentalPaymentHistory {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ID", nullable=false)
    private Long id;


    // Information about the payment made.
    @Embedded
    private EmbeddedPayment embeddedPayment;


    // Tracks the user responsible for making the payment
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "UserRecordID", nullable = false)
    private UserRecord userRecord;


    // Tracks the Bank Account that the payment was made from.
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ACHPaymentInfoID", nullable = true)
    private ACHPaymentInfo achPaymentInfo;


    // Tracks the MonthlyRentalBill for which payment was made.
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "MonthlyRentalBillID", nullable = true)
    private MonthlyRentalBill monthlyRentalBill;


    public RentalPaymentHistory() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EmbeddedPayment getEmbeddedPayment() {
        return embeddedPayment;
    }

    public void setEmbeddedPayment(EmbeddedPayment embeddedPayment) {
        this.embeddedPayment = embeddedPayment;
    }

    public UserRecord getUserRecord() {
        return userRecord;
    }

    public void setUserRecord(UserRecord userRecord) {
        this.userRecord = userRecord;
    }

    public ACHPaymentInfo getAchPaymentInfo() {
        return achPaymentInfo;
    }

    public void setAchPaymentInfo(ACHPaymentInfo achPaymentInfo) {
        this.achPaymentInfo = achPaymentInfo;
    }

    public MonthlyRentalBill getMonthlyRentalBill() {
        return monthlyRentalBill;
    }

    public void setMonthlyRentalBill(MonthlyRentalBill monthlyRentalBill) {
        this.monthlyRentalBill = monthlyRentalBill;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("embeddedPayment", embeddedPayment)
                .append("userRecord", userRecord)
                .append("achPaymentInfo", achPaymentInfo)
                .append("monthlyRentalBill", monthlyRentalBill)
                .toString();
    }


    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private RentalPaymentHistory rentalPaymentHistory = new RentalPaymentHistory();

        public Builder embeddedPayment(EmbeddedPayment embeddedPayment) {
            rentalPaymentHistory.embeddedPayment = embeddedPayment;
            return this;
        }

        public Builder userRecord(UserRecord userRecord) {
            rentalPaymentHistory.userRecord = userRecord;
            return this;
        }

        public Builder achPaymentInfo(ACHPaymentInfo achPaymentInfo) {
            rentalPaymentHistory.achPaymentInfo = achPaymentInfo;
            return this;
        }

        public Builder monthlyRentalBill(MonthlyRentalBill monthlyRentalBill) {
            rentalPaymentHistory.monthlyRentalBill = monthlyRentalBill;
            return this;
        }

        public RentalPaymentHistory build() {
            return rentalPaymentHistory;
        }
    }
}
