package com.imani.bill.pay.service.billing.compute;

import com.imani.bill.pay.domain.agreement.EmbeddedAgreement;
import com.imani.bill.pay.domain.billing.*;
import com.imani.bill.pay.domain.billing.repository.IBillPayFeeRepository;
import com.imani.bill.pay.domain.utility.EmbeddedUtilityService;
import com.imani.bill.pay.service.billing.IImaniBillService;
import com.imani.bill.pay.service.billing.utility.UtilityBillingDateService;
import com.imani.bill.pay.service.util.DateTimeUtil;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;
import java.util.Set;

/**
 * @author manyce400
 */
@RunWith(MockitoJUnitRunner.class)
public class BillingFeeComputeServiceTest {

    
    private ImaniBill imaniBill;
    
    @Mock
    private IBillPayFeeRepository iBillPayFeeRepository;
    
    @Mock
    private IImaniBillService imaniBillService;

    @Spy
    private DateTimeUtil iDateTimeUtil;

    @Spy
    private UtilityBillingDateService utilityBillingDateService;
    
    @InjectMocks
    private BillingFeeComputeService iBillingFeeComputeService;

    private BillPayFee billPayFee;

    public static final DateTimeFormatter DEFAULT_FORMATTER = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");

    
    @Before
    public void before() {
        // Always schedule bill for start of previous quarter
        DateTime schedDate = iDateTimeUtil.getDateTimeAtStartOfPrevQuarter();
        imaniBill = ImaniBill.builder()
                .amountOwed(100d)
                .billScheduleDate(schedDate)
                .build();

        billPayFee = BillPayFee.builder()
                .feeTypeE(FeeTypeE.LATE_FEE)
                .optionalFlatAmount(15d)
                .feePaymentChargeTypeE(FeePaymentChargeTypeE.FLAT_AMOUNT_FEE)
                .build();

        // Spring hack to inject iDateTimeUtil into utilityBillingDateService instance
        ReflectionTestUtils.setField(utilityBillingDateService, "iDateTimeUtil", iDateTimeUtil);
    }

    @Test
    public void testHasLateFeeBeenLeviedInCurrQtr() {
        // In this scenario, no fee levied so we expect this to be false
        boolean feeLevied = iBillingFeeComputeService.hasLateFeeBeenLeviedInCurrQtr(imaniBill);
        Assert.assertFalse(feeLevied);

        // Levy a fee in current qtr and try again
        DateTime dateTime = iDateTimeUtil.getDateTimeAStartOfNextQuarter();
        imaniBill.levyLateFee(billPayFee, dateTime);
        feeLevied = iBillingFeeComputeService.hasLateFeeBeenLeviedInCurrQtr(imaniBill);
        Assert.assertTrue(feeLevied);
    }

    @Test
    public void testHasLateFeeBeenLeviedInBillSchedQtr() {
        boolean feeLevied = iBillingFeeComputeService.hasLateFeeBeenLeviedInBillSchedQtr(imaniBill);
        Assert.assertFalse(feeLevied);

        // Levy a fee in current qtr and try again
        DateTime dateTime = iDateTimeUtil.getDateTimeAStartOfNextQuarter();
        imaniBill.levyLateFee(billPayFee, dateTime);
        feeLevied = iBillingFeeComputeService.hasLateFeeBeenLeviedInBillSchedQtr(imaniBill);
        Assert.assertFalse(feeLevied);
    }

    @Test
    public void testcomputeUpdateAmountOwedWithLateFee() {
        Mockito.when(imaniBillService.isBillPaymentLate(Mockito.any(), Mockito.any())).thenReturn(true);
        Mockito.when(iBillPayFeeRepository.findBillPayFeeByFeeType(Mockito.any(), Mockito.any())).thenReturn(Optional.of(billPayFee));

        EmbeddedAgreement embeddedAgreement = EmbeddedAgreement.builder()
                .numberOfDaysTillLate(15)
                .build();
        EmbeddedUtilityService embeddedUtilityService = EmbeddedUtilityService.builder().build();

        // Scheduled late to 2nd quarter start date so this assumes services renderd in 1st quarter
        DateTime schedDate = DateTime.parse("2021-04-01 00:00:00", DEFAULT_FORMATTER);
        imaniBill.setBillScheduleDate(schedDate);

        iBillingFeeComputeService.computeUpdateAmountOwedWithLateFee(embeddedAgreement, embeddedUtilityService, imaniBill);

        Set<ImaniBillToFee> billToFees = imaniBill.getBillPayFeesByFeeTypeE(FeeTypeE.LATE_FEE);
        // Assert.assertTrue(billToFees.size() == 2);

        billToFees.forEach(billToFee -> {
            System.out.println("Levied Date = " + billToFee.getFeeLeviedDate() + " FeeAmount = " + billToFee.getFeeAmount());
        });
    }

}