package com.imani.bill.pay.service.util;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.MutableDateTime;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * @author manyce400
 */
@Service(DateTimeUtil.SPRING_BEAN)
public class DateTimeUtil implements IDateTimeUtil {



    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(DateTimeUtil.class);

    public static final String SPRING_BEAN = "com.imani.cash.domain.service.util.DateTimeUtil";


    @Override
    public DateTime getDateTimeAtStartOfMonth(DateTime dateTime) {
        Assert.notNull(dateTime, "DateTime cannot be null");

        LOGGER.debug("Creating new date at start of month for dateTime: {}", dateTime);

        MutableDateTime mutableDateTime = new MutableDateTime(dateTime);
        mutableDateTime.setDayOfMonth(1);
        mutableDateTime.setMillisOfDay(0);
        return mutableDateTime.toDateTime().withTimeAtStartOfDay();
    }

    @Override
    public DateTime getDateTimeAtEndOfMonth(DateTime dateTime) {
        Assert.notNull(dateTime, "DateTime cannot be null");
        LOGGER.debug("Creating new date at end of month for dateTime: {}", dateTime);
        DateTime endOfMonthDateTime = dateTime.dayOfMonth().withMaximumValue().withTimeAtStartOfDay();
        return endOfMonthDateTime;
    }

    @Override
    public Integer getDaysBetweenDates(DateTime start, DateTime end) {
        Assert.notNull(start, "start cannot be null");
        Assert.notNull(end, "end cannot be null");
        return Days.daysBetween(start, end).getDays();
    }
}
