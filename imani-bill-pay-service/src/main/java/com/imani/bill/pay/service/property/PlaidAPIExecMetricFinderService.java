package com.imani.bill.pay.service.property;


import com.imani.bill.pay.domain.payment.PlaidAPIExecMetric;
import com.imani.bill.pay.domain.payment.PlaidProductE;
import com.imani.bill.pay.domain.payment.repository.IPlaidAPIExecMetricRepository;
import com.imani.bill.pay.domain.user.UserRecord;
import com.imani.bill.pay.service.util.DateTimeUtil;
import com.imani.bill.pay.service.util.IDateTimeUtil;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;

/**
 * @author manyce400
 */
@Service(PlaidAPIExecMetricFinderService.SPRING_BEAN)
public class PlaidAPIExecMetricFinderService implements IPlaidAPIExecMetricFinderService {


    @Autowired
    private IPlaidAPIExecMetricRepository iPlaidAPIExecMetricRepository;

    @Autowired
    @Qualifier(DateTimeUtil.SPRING_BEAN)
    private IDateTimeUtil iDateTimeUtil;

    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(PlaidAPIExecMetricFinderService.class);

    public static final String SPRING_BEAN = "com.imani.bill.pay.service.property.PlaidAPIExecMetricFinderService";



    @Override
    public List<PlaidAPIExecMetric> findUserPlaidAPIExecMetricCurrentMonth(UserRecord userRecord, PlaidProductE plaidProductE) {
        Assert.notNull(userRecord, "userRecord cannot be null");
        Assert.notNull(plaidProductE, "plaidProductE cannot be null");

        DateTime now = DateTime.now();
        DateTime dateTimeAtStartOfMonth = iDateTimeUtil.getDateTimeAtStartOfMonth(now);
        DateTime dateTimeAtEndOfMonth = iDateTimeUtil.getDateTimeAtEndOfMonth(now);

        LOGGER.info("Finding all Plaid API Metrics for User:=> {} PlaidProduct:=> {} dateTimeAtStartOfMonth:=> {}  dateTimeAtEndOfMonth:=> {}", userRecord.getEmbeddedContactInfo().getEmail(), plaidProductE, dateTimeAtStartOfMonth, dateTimeAtEndOfMonth);
        return iPlaidAPIExecMetricRepository.findUserPlaidAPIExecMetricByProductBetweenDates(userRecord, plaidProductE, dateTimeAtStartOfMonth, dateTimeAtEndOfMonth);
    }
}
