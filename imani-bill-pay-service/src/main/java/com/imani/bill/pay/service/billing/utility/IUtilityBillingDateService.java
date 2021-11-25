package com.imani.bill.pay.service.billing.utility;

import com.imani.bill.pay.domain.billing.BillScheduleTypeE;
import com.imani.bill.pay.domain.utility.UtilityBillDate;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.joda.time.DateTime;

import java.util.List;
import java.util.Optional;

/**
 * @author manyce400
 */
public interface IUtilityBillingDateService {

    public Optional<UtilityBillDate> computeBillDate(BillScheduleTypeE billScheduleTypeE);

    /**
     * Given a start and end date, this will return all the quarterly(start,end) dates between the given range.
     * @param start
     * @param end
     * @param billScheduleTypeE
     * @return Qtr Start, Qtr End
     */
    public List<ImmutablePair<DateTime, DateTime>> computeQtlyBillingDatesBetween(DateTime start, DateTime end, BillScheduleTypeE billScheduleTypeE);

}