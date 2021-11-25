package com.imani.bill.pay.service.billing.utility;

import com.imani.bill.pay.domain.billing.BillScheduleTypeE;
import com.imani.bill.pay.domain.utility.UtilityBillDate;
import com.imani.bill.pay.service.util.DateTimeUtil;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;
import java.util.Optional;

/**
 * @author manyce400 
 */
@RunWith(MockitoJUnitRunner.class)
public class UtilityBillingDateServiceTest {


    @Spy
    private DateTimeUtil iDateTimeUtil;
    
    @InjectMocks
    private UtilityBillingDateService utilityBillDateService;

    public static final DateTimeFormatter DEFAULT_FORMATTER = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
    
    
    @Test
    public void testComputeBillDate() {
        Optional<UtilityBillDate> utilityBillDate = utilityBillDateService.computeBillDate(BillScheduleTypeE.QUARTERLY);
        System.out.println("utilityBillDate.get() = " + utilityBillDate.get());
    }
    
    @Test
    public void testComputeBillingDatesBetween() {
        DateTime start = DateTime.parse("2021-01-01 00:00:00", DEFAULT_FORMATTER);
        DateTime end = DateTime.parse("2021-09-05 00:00:00", DEFAULT_FORMATTER);

        List<ImmutablePair<DateTime, DateTime>> billingDatesBetween = utilityBillDateService.computeQtlyBillingDatesBetween(start, end, BillScheduleTypeE.QUARTERLY);
        Assert.assertTrue((billingDatesBetween.size() == 3));

        ImmutablePair<DateTime, DateTime> first = billingDatesBetween.get(0);
        Assert.assertEquals("2021-01-01 00:00:00", DEFAULT_FORMATTER.print(first.getLeft()));
        Assert.assertEquals("2021-03-31 00:00:00", DEFAULT_FORMATTER.print(first.getRight()));

        ImmutablePair<DateTime, DateTime> second = billingDatesBetween.get(1);
        Assert.assertEquals("2021-04-01 00:00:00", DEFAULT_FORMATTER.print(second.getLeft()));
        Assert.assertEquals("2021-06-30 00:00:00", DEFAULT_FORMATTER.print(second.getRight()));

        ImmutablePair<DateTime, DateTime> third = billingDatesBetween.get(2);
        Assert.assertEquals("2021-07-01 00:00:00", DEFAULT_FORMATTER.print(third.getLeft()));
        Assert.assertEquals("2021-09-30 00:00:00", DEFAULT_FORMATTER.print(third.getRight()));

        System.out.println("billingDatesBetween = " + billingDatesBetween);
    }
}
