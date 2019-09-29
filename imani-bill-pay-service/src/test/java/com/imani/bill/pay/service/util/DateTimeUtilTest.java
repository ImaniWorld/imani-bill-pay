package com.imani.bill.pay.service.util;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * @author manyce400 
 */
@RunWith(MockitoJUnitRunner.class)
public class DateTimeUtilTest {
    
    
    @InjectMocks
    private DateTimeUtil dateTimeUtil;

    public static final DateTimeFormatter DEFAULT_FORMATTER = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");

    
    @Test
    public void testGetDateTimeAtStartOfMonth() {
        DateTime dateTime = dateTimeUtil.getDateTimeAtStartOfMonth(DateTime.parse("2019-09-03 09:00:00", DEFAULT_FORMATTER));
        String dateTimeAtStartOfMonth = DEFAULT_FORMATTER.print(dateTime);
        Assert.assertEquals("2019-09-01 00:00:00", dateTimeAtStartOfMonth);
    }
    
    @Test
    public void testGetDateTimeAtEndOfMonth() {
        DateTime dateTime = dateTimeUtil.getDateTimeAtEndOfMonth(DateTime.parse("2019-09-03 09:00:00", DEFAULT_FORMATTER));
        String dateTimeAtEndOfMonth = DEFAULT_FORMATTER.print(dateTime);
        Assert.assertEquals("2019-09-30 00:00:00", dateTimeAtEndOfMonth);
    }

    @Test
    public void testGetDaysBetweenDates() {
        DateTime start = DateTime.parse("2019-09-03 09:00:00", DEFAULT_FORMATTER);
        DateTime end = DateTime.parse("2019-09-05 09:00:00", DEFAULT_FORMATTER);
        int daysBetween = dateTimeUtil.getDaysBetweenDates(start, end);
        Assert.assertEquals(2, daysBetween);
    }
}
