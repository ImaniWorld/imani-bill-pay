package com.imani.bill.pay.domain.billing;

/**
 * @author manyce400
 */
public interface IFeePaymentModel {


    public Double getOptionalFlatAmount();

    public Double getOptionalFlatRate();

    public FeePaymentChargeTypeE getFeePaymentChargeTypeE();

    public default double calculatFeeCharge(double originalPaymentAmt) {
        FeePaymentChargeTypeE feePaymentChargeTypeE = getFeePaymentChargeTypeE();

        double feeCharge = 0;
        switch (feePaymentChargeTypeE) {
            case FLAT_AMOUNT_FEE:
                feeCharge = getOptionalFlatAmount();
                break;
            case FLAT_RATE_FEE:
                // We expect rate will be expressed in percent so between 0 and 100
                double percentageOfPaymentAmt = originalPaymentAmt * (getOptionalFlatRate() / 100);
                feeCharge = percentageOfPaymentAmt;
                break;
            default:
                break;
        }

        return feeCharge;
    }

    public default double calculatePaymentWithFees(double originalPaymentAmt) {
        FeePaymentChargeTypeE feePaymentChargeTypeE = getFeePaymentChargeTypeE();

        double finalPayment = 0;
        switch (feePaymentChargeTypeE) {
            case FLAT_AMOUNT_FEE:
                finalPayment = originalPaymentAmt + getOptionalFlatAmount();
                break;
            case FLAT_RATE_FEE:
                // We expect rate will be expressed in percent so between 0 and 100
                double percentageOfPaymentAmt = originalPaymentAmt * (getOptionalFlatRate() / 100);
                finalPayment = originalPaymentAmt + percentageOfPaymentAmt;
                break;
            default:
                break;
        }

        return finalPayment;
    }



}
