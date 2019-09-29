package com.imani.bill.pay.domain.property;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.imani.bill.pay.domain.payment.PaymentStatusE;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Result of user trying to execute a monthly rental bill pay.
 *
 * @author manyce400
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RentalBillPayResult {


    private PaymentStatusE paymentStatusE;

    private String paymentMessage;

    private MonthlyRentalBillExplained monthlyRentalBillExplained;


    public RentalBillPayResult() {

    }

    public String getPaymentMessage() {
        return paymentMessage;
    }

    public void setPaymentMessage(String paymentMessage) {
        this.paymentMessage = paymentMessage;
    }

    public PaymentStatusE getPaymentStatusE() {
        return paymentStatusE;
    }

    public void setPaymentStatusE(PaymentStatusE paymentStatusE) {
        this.paymentStatusE = paymentStatusE;
    }

    public MonthlyRentalBillExplained getMonthlyRentalBillExplained() {
        return monthlyRentalBillExplained;
    }

    public void setMonthlyRentalBillExplained(MonthlyRentalBillExplained monthlyRentalBillExplained) {
        this.monthlyRentalBillExplained = monthlyRentalBillExplained;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("paymentStatusE", paymentStatusE)
                .append("paymentMessage", paymentMessage)
                .append("monthlyRentalBillExplained", monthlyRentalBillExplained)
                .toString();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private RentalBillPayResult rentalBillPayResult = new RentalBillPayResult();

        public Builder paymentStatusE(PaymentStatusE paymentStatusE) {
            rentalBillPayResult.paymentStatusE = paymentStatusE;
            return this;
        }

        public Builder paymentMessage(String paymentMessage) {
            rentalBillPayResult.paymentMessage = paymentMessage;
            return this;
        }

        public Builder monthlyRentalBillExplained(MonthlyRentalBillExplained monthlyRentalBillExplained) {
            rentalBillPayResult.monthlyRentalBillExplained = monthlyRentalBillExplained;
            return this;
        }

        public RentalBillPayResult build() {
            return rentalBillPayResult;
        }
    }
}
