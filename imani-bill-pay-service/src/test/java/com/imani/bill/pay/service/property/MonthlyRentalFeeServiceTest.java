package com.imani.bill.pay.service.property;

import com.imani.bill.pay.domain.property.repository.IMonthlyRentalFeeRepository;
import com.imani.bill.pay.service.util.DateTimeUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * @author manyce400
 */
@RunWith(MockitoJUnitRunner.class)
public class MonthlyRentalFeeServiceTest extends AbstractMonthlyRentalBillingTest {



    @Mock
    private IMonthlyRentalFeeRepository iMonthlyRentalFeeRepository;

    @Spy
    private DateTimeUtil iDateTimeUtil;

    @InjectMocks
    private MonthlyRentalFeeService monthlyRentalFeeService;


    @Test
    public void testIsCurrentMonthPaymentLate() {
//        // Payment due date
//        DateTime rentalMonth = DateTime.parse("2019-09-01 00:00:00", DateTimeUtilTest.DEFAULT_FORMATTER);
//
//        // Build mock bill
//        MonthlyRentalBill monthlyRentalBill = MonthlyRentalBill.builder()
//                .userResidence(userResidence)
//                .rentalAgreement(userResidence.getLeaseAgreement())
//                .rentalMonth(rentalMonth)
//                .build();
//
//        // Mock Property is setup to only allow grace period of 4days so if payment is past 4days then its late
//        boolean isPaymentLate = monthlyRentalFeeService.isCurrentMonthPaymentLate(userResidence.getProperty(), monthlyRentalBill);
//        Assert.assertTrue(isPaymentLate);
    }
//
//    @Test
//    public void testApplyMonthlyRentalFees() {
//        // Payment due date, we want to trigger a Late Payment fee in this test
//        DateTime rentalMonth = DateTime.parse("2019-09-01 00:00:00", DateTimeUtilTest.DEFAULT_FORMATTER);
//
//        // Build mock bill
//        MonthlyRentalBill monthlyRentalBill = MonthlyRentalBill.builder()
//                .userResidence(userResidence)
//                .rentalAgreement(userResidence.getLeaseAgreement())
//                .rentalMonth(rentalMonth)
//                .build();
//
//        // Return a Late Fee
//        MonthlyRentalFee lateRentalFee = MonthlyRentalFee.builder()
//                .feeName("Monthly Rent Late Fee")
//                .feePaymentChargeTypeE(FeePaymentChargeTypeE.FLAT_RATE_FEE)
//                .optionalFlatRate(10.00)
//                .rentalFeeTypeE(RentalFeeTypeE.LATE_FEE)
//                .build();
//        Mockito.when(iMonthlyRentalFeeRepository.findPropertyMonthlyRentalFeeByType(Mockito.any(), Mockito.any())).thenReturn(lateRentalFee);
//
//        // Rental agreement is for a monthly charge of 1800 so a 10% late fee should trigger a $180 late fee charge
//        Optional<List<MonthlyRentalFeeExplained>> feeExplainedList = monthlyRentalFeeService.applyMonthlyRentalFees(userResidence, monthlyRentalBill);
//
//        // Verify fee explantion
//        Assert.assertEquals(1, feeExplainedList.get().size());
//        Assert.assertEquals(new Double(180), feeExplainedList.get().get(0).getFeeCharge());
//        Assert.assertEquals("Monthly Rent Late Fee", feeExplainedList.get().get(0).getFeeName());
//    }
}
