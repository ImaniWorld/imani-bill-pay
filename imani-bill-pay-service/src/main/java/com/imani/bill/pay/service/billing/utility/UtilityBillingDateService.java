package com.imani.bill.pay.service.billing.utility;

import com.imani.bill.pay.domain.billing.BillScheduleTypeE;
import com.imani.bill.pay.domain.utility.UtilityBillDate;
import com.imani.bill.pay.service.util.DateTimeUtil;
import com.imani.bill.pay.service.util.IDateTimeUtil;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

/**
 * @author manyce400
 */
@Service(UtilityBillingDateService.SPRING_BEAN)
public class UtilityBillingDateService implements IUtilityBillingDateService {


    @Autowired
    @Qualifier(DateTimeUtil.SPRING_BEAN)
    private IDateTimeUtil iDateTimeUtil;

    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(UtilityBillingDateService.class);

    public static final String SPRING_BEAN = "com.imani.bill.pay.service.billing.utility.UtilityBillingDateService";

    @Override
    public Optional<UtilityBillDate> computeBillDate(BillScheduleTypeE billScheduleTypeE) {
        Assert.notNull(billScheduleTypeE, "BillScheduleTypeE cannot be null");
        UtilityBillDate utilityBillDate = null;

        switch (billScheduleTypeE) {
            case QUARTERLY:
                utilityBillDate = getQtlyUtilityBillDate(billScheduleTypeE);
                break;
            default:
                break;
        }

        return utilityBillDate != null ? Optional.of(utilityBillDate) : Optional.empty();
    }

    @Override
    public List<ImmutablePair<DateTime, DateTime>> computeQtlyBillingDatesBetween(DateTime start, DateTime end, BillScheduleTypeE billScheduleTypeE) {
        Assert.notNull(start, "start cannot be null");
        Assert.notNull(end, "end cannot be null");
        Assert.notNull(billScheduleTypeE, "BillScheduleTypeE cannot be null");

        List<ImmutablePair<DateTime, DateTime>> billingDateRanges = new LinkedList<>();


        switch (billScheduleTypeE) {
            case QUARTERLY:
                addQtlyStartEndBillDatesBetween(start, end, billingDateRanges);
                break;
            default:
                break;
        }

        return billingDateRanges;
    }

    private UtilityBillDate getQtlyUtilityBillDate(BillScheduleTypeE billScheduleTypeE) {
        DateTime dueDate = iDateTimeUtil.getDateTimeAStartOfNextQuarter();
        DateTime utilStart = iDateTimeUtil.getDateTimeAtStartOfCurrentQuarter();
        DateTime utilEnd =  iDateTimeUtil.getDateTimeAtEndOfCurrentQuarter();

        UtilityBillDate utilityBillDate = UtilityBillDate.builder()
                .due(dueDate)
                .utilStart(utilStart)
                .utilEnd(utilEnd)
                .billScheduleTypeE(billScheduleTypeE)
                .build();
        return utilityBillDate;
    }

    private void addQtlyStartEndBillDatesBetween(DateTime start, DateTime end, List<ImmutablePair<DateTime, DateTime>> billingDateRanges) {
        // First normalize the start and end range dates by getting at start of quarter
        start = iDateTimeUtil.getDateTimeAtStartOfQuarter(start);
        end = iDateTimeUtil.getDateTimeAtStartOfQuarter(end);

        DateTime qtrEnd = null;

        do {
            qtrEnd = iDateTimeUtil.getDateTimeAtEndOfCurrentQuarter(start);
            ImmutablePair<DateTime, DateTime> pair = new ImmutablePair<>(start, qtrEnd);
            billingDateRanges.add(pair);

            // Move start to start of next quarter
            start = iDateTimeUtil.getDateTimeAtStartOfNextQuarter(start);
        } while (!qtrEnd.isAfter(end));
    }

}