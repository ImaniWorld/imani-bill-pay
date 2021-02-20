package com.imani.bill.pay.service.util;

import org.joda.time.DateTime;

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

    public DateTime getDateTimeAStartOfCurrentQuarter();

    public DateTime getDateTimeAEndOfCurrentQuarter();

    public Integer getCurrentQuaterOfCurrentYear();

    public Integer getDateTimeQuarter(DateTime dateTime);

    public boolean isDateTimeInCurrentQuarter(DateTime dateTime);

}
