package com.imani.bill.pay.service.util;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.MutableDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.LinkedHashSet;
import java.util.Set;

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
    public DateTime getDateTimeAtStartOfYear(DateTime dateTime) {
        Assert.notNull(dateTime, "DateTime cannot be null");

        LOGGER.debug("Creating new date at start of year for dateTime: {}", dateTime);

        MutableDateTime mutableDateTime = new MutableDateTime(dateTime);
        mutableDateTime.setMonthOfYear(1);
        mutableDateTime.setDayOfMonth(1);
        mutableDateTime.setHourOfDay(0);
        mutableDateTime.setMinuteOfHour(0);
        mutableDateTime.setSecondOfMinute(0);
        mutableDateTime.setMillisOfSecond(0);
        return mutableDateTime.toDateTime();
    }

    @Override
    public DateTime getDateTimeAtEndOfYear(DateTime dateTime) {
        Assert.notNull(dateTime, "DateTime cannot be null");

        LOGGER.debug("Creating new date at end of year for dateTime: {}", dateTime);

        MutableDateTime mutableDateTime = new MutableDateTime(dateTime);
        mutableDateTime.setMonthOfYear(12);
        mutableDateTime.setDayOfMonth(31);
        mutableDateTime.setHourOfDay(0);
        mutableDateTime.setMinuteOfHour(0);
        mutableDateTime.setSecondOfMinute(0);
        mutableDateTime.setMillisOfSecond(0);
        return mutableDateTime.toDateTime();
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

    @Override
    public DateTime getDateTimeAtStartOfPrevQuarter() {
        Integer prevQtr = getPrevQuaterOfCurrentYear();
        DateTime now = DateTime.now();
        int currentYear = now.getYear();
        return getQuarterStartDateTime(currentYear, prevQtr);
    }

    @Override
    public DateTime getDateTimeAtStartOfCurrentQuarter() {
        DateTime now = DateTime.now();
        int currentYear = now.getYear();
        int currentQuarter = getCurrentQuaterOfCurrentYear();
        return getQuarterStartDateTime(currentYear, currentQuarter);
    }

    @Override
    public DateTime getDateTimeAtStartOfQuarter(DateTime dateTime) {
        int year = dateTime.getYear();
        int quarter = getDateTimeQuarter(dateTime);
        return getQuarterStartDateTime(year, quarter);
    }

    @Override
    public DateTime getDateTimeAtStartOfNextQuarter(DateTime dateTime) {
        Assert.notNull(dateTime, "dateTime cannot be null");
        DateTime nextQtrStartDate = getDateTimeAtEndOfCurrentQuarter(dateTime);
        nextQtrStartDate = nextQtrStartDate.plusMonths(1);
        nextQtrStartDate = getDateTimeAtStartOfMonth(nextQtrStartDate);
        return nextQtrStartDate;
    }

    @Override
    public DateTime getDateTimeAtEndOfCurrentQuarter() {
        DateTime now = DateTime.now();
        int currentYear = now.getYear();
        int currentQuarter = getCurrentQuaterOfCurrentYear();
        return getQuarterEndDateTime(currentYear, currentQuarter);
    }

    @Override
    public DateTime getDateTimeAtEndOfCurrentQuarter(DateTime dateTime) {
        int year = dateTime.getYear();
        int quarter = getDateTimeQuarter(dateTime);
        return getQuarterEndDateTime(year, quarter);
    }

    @Override
    public ImmutablePair<DateTime, DateTime> getCurrQtrStartEndDates() {
        DateTime dateTime = getDateTimeAtStartOfCurrentQuarter();
        return getQuarterStartEndDates(dateTime);
    }

    @Override
    public ImmutablePair<DateTime, DateTime> getQuarterStartEndDates(DateTime dateTime) {
        DateTime qtrStart = getDateTimeAtStartOfQuarter(dateTime);
        DateTime qtrEnd = getDateTimeAtEndOfCurrentQuarter(dateTime);
        return new ImmutablePair<>(qtrStart, qtrEnd);
    }

    @Override
    public DateTime getDateTimeAStartOfNextQuarter() {
        // Get date at end of this current qtr and advance by 1month
        DateTime dateTime = getDateTimeAtEndOfCurrentQuarter();
        dateTime = dateTime.plusMonths(1);
        dateTime = getDateTimeAtStartOfMonth(dateTime);
        return dateTime;
    }

    @Override
    public Set<DateTime> getAllQtrStartDatesBetween(DateTime start, DateTime end) {
        Assert.notNull(start, "start cannot be null");
        Assert.notNull(end, "end cannot be null");
        Assert.isTrue(end.isAfter(start), "End date has to be after start");

        Set<DateTime> startDateTimes = new LinkedHashSet<>();

        // First normalize the start and end dates in range by getting at start of quarter
        DateTime rangeStart = getDateTimeAtStartOfQuarter(start);
        DateTime rangeEnd = getDateTimeAtStartOfQuarter(end);
        startDateTimes.add(rangeStart);

        DateTime nextInRange = getDateTimeAtStartOfNextQuarter(rangeStart);
        do {
            startDateTimes.add(nextInRange);
            nextInRange = getDateTimeAtStartOfNextQuarter(nextInRange);
        } while(!nextInRange.isAfter(rangeEnd));

        return startDateTimes;
    }

    @Override
    public Integer getCurrentQuaterOfCurrentYear() {
        DateTime now = DateTime.now();
        int monthOfYear = now.getMonthOfYear();
        return getQuaterByMonthOfYear(monthOfYear);
    }

    @Override
    public Integer getPrevQuaterOfCurrentYear() {
        int currQtr = getCurrentQuaterOfCurrentYear();
        if(currQtr == 1) {
            // reset to 4
            return 4;
        }

        return currQtr - 1;
    }

    @Override
    public Integer getDateTimeQuarter(DateTime dateTime) {
        Assert.notNull(dateTime, "DateTime cannot be null");
        int monthOfYear = dateTime.getMonthOfYear();
        return getQuaterByMonthOfYear(monthOfYear);
    }

    @Override
    public boolean isDateTimeInCurrentQuarter(DateTime dateTime) {
        int currentQuarter = getCurrentQuaterOfCurrentYear();
        int dateTimeQuarter = getDateTimeQuarter(dateTime);
        return currentQuarter == dateTimeQuarter;
    }

    private int getQuaterByMonthOfYear(int monthOfYear) {
        if(monthOfYear >= 1 && monthOfYear <= 3) {
            return 1; // 1 for First Quarter
        } else if(monthOfYear >= 4 && monthOfYear <= 6) {
            return 2; // 2 for Second Quarter
        } else if(monthOfYear >= 7 && monthOfYear <= 9) {
            return 3; // 3 for Third Quarter
        } else if(monthOfYear >= 10 && monthOfYear <= 12) {
            return 4; // 4 for Fourth Quarter
        }

        return -1;
    }

    private DateTime getQuarterStartDateTime(int currentYear, int currentQuarter) {
        if(currentQuarter == 1) {
            MutableDateTime mutableDateTime = new MutableDateTime();
            mutableDateTime.setYear(currentYear);
            mutableDateTime.setMonthOfYear(1);
            mutableDateTime.setDayOfMonth(1);
            mutableDateTime.setHourOfDay(0);
            mutableDateTime.setMinuteOfHour(0);
            mutableDateTime.setSecondOfMinute(0);
            mutableDateTime.setMillisOfSecond(0);
            return mutableDateTime.toDateTime();
        } else if(currentQuarter == 2) {
            MutableDateTime mutableDateTime = new MutableDateTime();
            mutableDateTime.setYear(currentYear);
            mutableDateTime.setMonthOfYear(4);
            mutableDateTime.setDayOfMonth(1);
            mutableDateTime.setHourOfDay(0);
            mutableDateTime.setMinuteOfHour(0);
            mutableDateTime.setSecondOfMinute(0);
            mutableDateTime.setMillisOfSecond(0);
            return mutableDateTime.toDateTime();
        } else if(currentQuarter == 3) {
            MutableDateTime mutableDateTime = new MutableDateTime();
            mutableDateTime.setYear(currentYear);
            mutableDateTime.setMonthOfYear(7);
            mutableDateTime.setDayOfMonth(1);
            mutableDateTime.setHourOfDay(0);
            mutableDateTime.setMinuteOfHour(0);
            mutableDateTime.setSecondOfMinute(0);
            mutableDateTime.setMillisOfSecond(0);
            return mutableDateTime.toDateTime();
        } else if(currentQuarter == 4) {
            MutableDateTime mutableDateTime = new MutableDateTime();
            mutableDateTime.setYear(currentYear);
            mutableDateTime.setMonthOfYear(10);
            mutableDateTime.setDayOfMonth(1);
            mutableDateTime.setHourOfDay(0);
            mutableDateTime.setMinuteOfHour(0);
            mutableDateTime.setSecondOfMinute(0);
            mutableDateTime.setMillisOfSecond(0);
            return mutableDateTime.toDateTime();
        }

        return null;
    }

    private DateTime getQuarterEndDateTime(int currentYear, int currentQuarter) {
        if(currentQuarter == 1) {
            MutableDateTime mutableDateTime = new MutableDateTime();
            mutableDateTime.setYear(currentYear);
            mutableDateTime.setMonthOfYear(3);
            mutableDateTime.setDayOfMonth(1);
            mutableDateTime.setHourOfDay(0);
            mutableDateTime.setMinuteOfHour(0);
            mutableDateTime.setSecondOfMinute(0);
            mutableDateTime.setMillisOfSecond(0);
            return mutableDateTime.toDateTime().dayOfMonth().withMaximumValue(); // Gurantees that we pick the last day of the calendar month
        } else if(currentQuarter == 2) {
            MutableDateTime mutableDateTime = new MutableDateTime();
            mutableDateTime.setYear(currentYear);
            mutableDateTime.setMonthOfYear(6);
            mutableDateTime.setDayOfMonth(1);
            mutableDateTime.setHourOfDay(0);
            mutableDateTime.setMinuteOfHour(0);
            mutableDateTime.setSecondOfMinute(0);
            mutableDateTime.setMillisOfSecond(0);
            return mutableDateTime.toDateTime().dayOfMonth().withMaximumValue(); // Gurantees that we pick the last day of the calendar month
        } else if(currentQuarter == 3) {
            MutableDateTime mutableDateTime = new MutableDateTime();
            mutableDateTime.setYear(currentYear);
            mutableDateTime.setMonthOfYear(9);
            mutableDateTime.setDayOfMonth(1);
            mutableDateTime.setHourOfDay(0);
            mutableDateTime.setMinuteOfHour(0);
            mutableDateTime.setSecondOfMinute(0);
            mutableDateTime.setMillisOfSecond(0);
            return mutableDateTime.toDateTime().dayOfMonth().withMaximumValue(); // Gurantees that we pick the last day of the calendar month
        } else if(currentQuarter == 4) {
            MutableDateTime mutableDateTime = new MutableDateTime();
            mutableDateTime.setYear(currentYear);
            mutableDateTime.setMonthOfYear(12);
            mutableDateTime.setDayOfMonth(1);
            mutableDateTime.setHourOfDay(0);
            mutableDateTime.setMinuteOfHour(0);
            mutableDateTime.setSecondOfMinute(0);
            mutableDateTime.setMillisOfSecond(0);
            return mutableDateTime.toDateTime().dayOfMonth().withMaximumValue(); // Gurantees that we pick the last day of the calendar month
        }

        return null;
    }

    public static void main(String[] args) {
        DateTimeUtil dateTimeUtil = new DateTimeUtil();
        int quarter = dateTimeUtil.getCurrentQuaterOfCurrentYear();
        System.out.println("quarter = " + quarter);

        DateTime dateTime = dateTimeUtil.getDateTimeAtStartOfCurrentQuarter();
        System.out.println("StartOfQtr = " + dateTime);

        dateTime = dateTimeUtil.getDateTimeAtEndOfCurrentQuarter();
        System.out.println("EndOfQtr = " + dateTime);
    }

    
}
