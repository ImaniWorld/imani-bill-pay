package com.imani.bill.pay.service.util;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.joda.time.DateTime;

import java.util.Set;

/**
 * @author manyce400
 */
public interface IDateTimeUtil {

    public DateTime getDateTimeAtStartOfCurrentDay();

    public DateTime getDateTimeAtEndOfCurrentDay();

    public DateTime getDateTimeAtStartOfMonth(DateTime dateTime);

    public DateTime getDateTimeAtEndOfMonth(DateTime dateTime);

    public DateTime getDateTimeAtStartOfYear(DateTime dateTime);

    public DateTime getDateTimeAtEndOfYear(DateTime dateTime);

    public Integer getDaysBetweenDates(DateTime start, DateTime end);

    public String toDisplayFriendlyNoTime(DateTime dateTime);

    public String toDisplayDefault(DateTime dateTime);

    public DateTime getDateTimeAtStartOfPrevQuarter();

    public DateTime getDateTimeAtStartOfCurrentQuarter();

    public DateTime getDateTimeAtStartOfQuarter(DateTime dateTime);

    public DateTime getDateTimeAtStartOfNextQuarter(DateTime dateTime);

    public DateTime getDateTimeAtEndOfCurrentQuarter();

    public ImmutablePair<DateTime, DateTime> getCurrQtrStartEndDates();

    public ImmutablePair<DateTime, DateTime> getQuarterStartEndDates(DateTime dateTime);

    public DateTime getDateTimeAtEndOfCurrentQuarter(DateTime dateTime);

    public DateTime getDateTimeAStartOfNextQuarter();

    public Set<DateTime> getAllQtrStartDatesBetween(DateTime start, DateTime end);

    public Integer getCurrentQuaterOfCurrentYear();

    public Integer getPrevQuaterOfCurrentYear();

    public Integer getDateTimeQuarter(DateTime dateTime);

    public boolean isDateTimeInCurrentQuarter(DateTime dateTime);

}
