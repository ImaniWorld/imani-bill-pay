package com.imani.bill.pay.service.payment;

import com.imani.bill.pay.domain.payment.Balance;
import com.imani.bill.pay.domain.payment.PaymentStatusE;
import com.imani.bill.pay.domain.payment.repository.IACHPaymentInfoRepository;
import com.imani.bill.pay.domain.property.MonthlyRentalBill;
import com.imani.bill.pay.domain.property.MonthlyRentalBillExplained;
import com.imani.bill.pay.domain.property.RentalBillPayResult;
import com.imani.bill.pay.domain.property.repository.IMonthlyRentalBillRepository;
import com.imani.bill.pay.domain.user.repository.IUserRecordRepository;
import com.imani.bill.pay.service.property.AbstractMonthlyRentalBillingTest;
import com.imani.bill.pay.service.property.IMonthlyRentalBillDescService;
import com.imani.bill.pay.service.util.DateTimeUtilTest;
import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Optional;

/**
 * @author manyce400
 */
@RunWith(MockitoJUnitRunner.class)
public class MonthlyRentalBillPayServiceTest extends AbstractMonthlyRentalBillingTest {


    private DateTime rentalMonth;

    private MonthlyRentalBill monthlyRentalBill;

    @Mock
    private IUserRecordRepository iUserRecordRepository;

    @Mock
    private IACHPaymentInfoRepository iachPaymentInfoRepository;

    @Mock
    private IMonthlyRentalBillRepository iMonthlyRentalBillRepository;

    @Mock
    private IMonthlyRentalBillDescService iMonthlyRentalBillDescService;

    @Mock
    private IRentalPaymentHistoryService iRentalPaymentHistoryService;

    @Mock
    private IPlaidAccountBalanceService iPlaidAccountBalanceService;

    @InjectMocks
    private MonthlyRentalBillPayService monthlyRentalBillPayService;

    @Before
    public void beforeTest() {
        rentalMonth = DateTime.parse("2019-09-01 00:00:00", DateTimeUtilTest.DEFAULT_FORMATTER);
        monthlyRentalBill = MonthlyRentalBill.builder()
                .userResidence(userResidence)
                .rentalAgreement(userResidence.getLeaseAgreement())
                .amountPaid(0.0)
                .rentalMonth(rentalMonth)
                .build();
    }


    @Test
    public void testCalculateTotalAmountPaidAfterNewPaymentIsComplete() {
        // As part of rental payment, user has a monthly rental of $1800.00 so 
        double amountPaidPostPayment = monthlyRentalBillPayService.calculateTotalAmountPaidAfterNewPaymentIsComplete(monthlyRentalBill, 500d);
        Assert.assertEquals(new Double(500), new Double(amountPaidPostPayment));

        monthlyRentalBill.setAmountPaid(200.00);
        amountPaidPostPayment = monthlyRentalBillPayService.calculateTotalAmountPaidAfterNewPaymentIsComplete(monthlyRentalBill, 500d);
        Assert.assertEquals(new Double(700), new Double(amountPaidPostPayment));

        monthlyRentalBill.setAmountPaid(500.00);
        amountPaidPostPayment = monthlyRentalBillPayService.calculateTotalAmountPaidAfterNewPaymentIsComplete(monthlyRentalBill, 500d);
        Assert.assertEquals(new Double(1000), new Double(amountPaidPostPayment));
    }

    @Test
    public void testCanMakePaymentOnBill() {
        // Computed total amount will be monthly rental of $1,800 + fees of 200 => $2,000
        Double computedTotalAmtDue = 2000.00;
        Double amtBeingPaid = 500.00;

        // Signifies that a partial payment of $200 has already been made, so a payment of $500 will not exceed $2,000
        monthlyRentalBill.setAmountPaid(200.00);
        boolean canMakePayment = monthlyRentalBillPayService.canMakePaymentOnBill(monthlyRentalBill, computedTotalAmtDue, amtBeingPaid);
        Assert.assertTrue(canMakePayment);

        // Signifies that a partial payment of $1,500 has already been made, so a payment of $500 will now mean that all payments have been made in full
        monthlyRentalBill.setAmountPaid(1500.00);
        canMakePayment = monthlyRentalBillPayService.canMakePaymentOnBill(monthlyRentalBill, computedTotalAmtDue, amtBeingPaid);
        Assert.assertTrue(canMakePayment);

        // Signifies that a partial payment of $1,800 has already been made, so a payment of $500 will exceed $2,000 total amount due.  We cannot allow clients to overpay accidentally.
        monthlyRentalBill.setAmountPaid(1800.00);
        canMakePayment = monthlyRentalBillPayService.canMakePaymentOnBill(monthlyRentalBill, computedTotalAmtDue, amtBeingPaid);
        Assert.assertFalse(canMakePayment);
    }

    @Test
    public void testPayMonthlyRental() {
        MonthlyRentalBillExplained monthlyRentalBillExplained = MonthlyRentalBillExplained.builder()
                .amtPaid(0.0)
                .monthlyRentalCost(1800.00)
                .totalAmtDue(2000.00) // factors in additional 200 for all service charges and fees
                .amtBeingPaid(2000.00) // User is paying amount in full.
                .rentalMonth(rentalMonth)
                .userResidence(userResidence)
                .build();

        // Build mock balance for balance check call
        Balance balance = Balance.builder()
                .available(500.00)
                .build();

        // Mockout dependency calls
        Mockito.when(iRentalPaymentHistoryService.hasPendingUserRentalPaymentForCurrentMonth(userResidence.getUserRecord())).thenReturn(false);
        Mockito.when(iMonthlyRentalBillRepository.getUserMonthlyRentalBill(Mockito.any(), Mockito.any())).thenReturn(monthlyRentalBill);
        Mockito.when( iUserRecordRepository.findByUserEmail(Mockito.any())).thenReturn(userResidence.getUserRecord());
        Mockito.when(iMonthlyRentalBillDescService.calculateTotalAmountDue(Mockito.any(), Mockito.any())).thenReturn(2000.00);
        Mockito.when(iPlaidAccountBalanceService.getACHPaymentInfoBalances(userResidence.getUserRecord())).thenReturn(Optional.of(balance));

        // Invoke payment.  User is making payment in full of $2,000 however the available balance on their account is only $500 so we expect that this payment should be blocked.
        RentalBillPayResult rentalBillPayResult = monthlyRentalBillPayService.payMonthlyRental(monthlyRentalBillExplained);
        Assert.assertEquals(PaymentStatusE.InsufficientFunds, rentalBillPayResult.getPaymentStatusE());
        Assert.assertTrue(rentalBillPayResult.getPaymentMessage().contains("Insufficient Available Funds"));
    }

}