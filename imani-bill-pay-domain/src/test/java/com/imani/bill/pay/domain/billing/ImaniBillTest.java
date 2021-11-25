package com.imani.bill.pay.domain.billing;

import com.imani.bill.pay.domain.mock.IMockBillBuilder;
import com.imani.bill.pay.domain.mock.IMockBillPayFee;
import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Optional;

/**
 * @author manyce400
 */
@RunWith(MockitoJUnitRunner.class)
public class ImaniBillTest implements IMockBillBuilder, IMockBillPayFee {


    private BillPayFee billPayFee;

    private ImaniBill imaniBill;

    @Before
    public void before() {
        billPayFee = buildLate();
        imaniBill = build();
        Assert.assertFalse(imaniBill.hasFees());
    }

    @Test
    public void testLevyLateFee() {
        // Levy fee and verify the value
        imaniBill.levyLateFee(billPayFee, DateTime.now());
        Assert.assertTrue(imaniBill.hasFees());
        Assert.assertEquals(15.0, imaniBill.totalFees(), 0);

        // Levey additional fees
        imaniBill.levyLateFee(billPayFee, DateTime.now());
        Assert.assertEquals(30.0, imaniBill.totalFees(), 0);
    }

    @Test
    public void testGetLateFeeBetweenPeriodLevyLateFee() {
        // Levy fee and verify we can get by passing date
        DateTime feeDate = DateTime.now();
        imaniBill.levyLateFee(billPayFee, feeDate);
        Optional<ImaniBillToFee> leviedFee = imaniBill.getLateFeeBetweenPeriod(feeDate, feeDate);
        System.out.println("leviedFee.get() = " + leviedFee.get().getBillPayFee());
        System.out.println("billPayFee = " + billPayFee);
        Assert.assertEquals(billPayFee, leviedFee.get().getBillPayFee());
    }

}