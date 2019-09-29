package com.imani.bill.pay.domain.payment;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * @author manyce400
 */
@RunWith(MockitoJUnitRunner.class)
public class IFeePaymentModelTest {



    @Test
    public void testCalculatePaymentWithFeesFlatAmount() {
        // Flat amount scenario so we expect the final payment to be incremented with a fee of 100 flat rate to 600
        MockFlatAmtIFeePaymentModel mockFlatAmtIFeePaymentModel = new MockFlatAmtIFeePaymentModel();
        double finalPayment = mockFlatAmtIFeePaymentModel.calculatePaymentWithFees(500);
        Assert.assertEquals(new Double(600.00), new Double(finalPayment));
    }

    @Test
    public void testCalculatePaymentWithFeesFlatRate() {
        // Flat rate scenario so we expect the final payment to be incremented by a percent of 25% to 625
        MockFlatRateIFeePaymentModel mockFlatAmtIFeePaymentModel = new MockFlatRateIFeePaymentModel();
        double finalPayment = mockFlatAmtIFeePaymentModel.calculatePaymentWithFees(500);
        Assert.assertEquals(new Double(625.00), new Double(finalPayment));
    }


    private class MockFlatAmtIFeePaymentModel implements IFeePaymentModel {

        @Override
        public Double getOptionalFlatAmount() {
            return 100.00;
        }

        @Override
        public Double getOptionalFlatRate() {
            return 25.00;
        }

        @Override
        public FeePaymentChargeTypeE getFeePaymentChargeTypeE() {
            return FeePaymentChargeTypeE.FLAT_AMOUNT_FEE;
        }
    }

    private class MockFlatRateIFeePaymentModel implements IFeePaymentModel {

        @Override
        public Double getOptionalFlatAmount() {
            return 100.00;
        }

        @Override
        public Double getOptionalFlatRate() {
            return 25.00;
        }

        @Override
        public FeePaymentChargeTypeE getFeePaymentChargeTypeE() {
            return FeePaymentChargeTypeE.FLAT_RATE_FEE;
        }
    }
}
