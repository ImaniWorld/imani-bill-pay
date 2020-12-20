package com.imani.bill.pay.service.util;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.MutableDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * @author manyce400
 */
@Service(DateTimeUtil.SPRING_BEAN)
public class DateTimeUtil implements IDateTimeUtil {


    public static final DateTimeFormatter DEFAULT_FORMATTER = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");

    public static final DateTimeFormatter DISPLAY_FRIENDLY_NO_TIME = DateTimeFormat.forPattern("MM/dd/yyyy");

    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(DateTimeUtil.class);

    public static final String SPRING_BEAN = "com.imani.cash.domain.service.util.DateTimeUtil";


    @Override
    public DateTime getDateTimeAtStartOfCurrentDay() {
        DateTime dateTime = DateTime.now().withTimeAtStartOfDay();
        return dateTime;
    }

    @Override
    public DateTime getDateTimeAtEndOfCurrentDay() {
        DateTime dateTime = DateTime.now().withHourOfDay(23).withMinuteOfHour(59).withSecondOfMinute(59);
        return dateTime;
    }

    @Override
    public DateTime getDateTimeAtStartOfMonth(DateTime dateTime) {
        Assert.notNull(dateTime, "DateTime cannot be null");

        LOGGER.debug("Creating new date at start of month for dateTime: {}", dateTime);

        MutableDateTime mutableDateTime = new MutableDateTime(dateTime);
        mutableDateTime.setDayOfMonth(1);
        mutableDateTime.setHourOfDay(0);
        mutableDateTime.setMinuteOfHour(0);
        mutableDateTime.setSecondOfMinute(0);
        mutableDateTime.setMillisOfSecond(0);
        return mutableDateTime.toDateTime();
//        return dateTime.withDayOfMonth(1).withTimeAtStartOfDay();
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

    public String toDisplayFriendlyNoTime(DateTime dateTime) {
        Assert.notNull(dateTime,  "dateTime cannot be null");
        return dateTime.toString(DISPLAY_FRIENDLY_NO_TIME);
    }

    public String toDisplayDefault(DateTime dateTime) {
        Assert.notNull(dateTime,  "dateTime cannot be null");
        return dateTime.toString(DEFAULT_FORMATTER);
    }


    public static void main(String[] args) {
        DateTimeUtil dateTimeUtil = new DateTimeUtil();
        DateTime dateTime = dateTimeUtil.getDateTimeAtStartOfMonth(DateTime.now());
        System.out.println("dateTime = " + dateTime);
        String str = dateTimeUtil.toDisplayDefault(dateTime);
        System.out.println("str = " + str);
    }

    
}
