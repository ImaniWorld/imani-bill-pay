package com.imani.bill.pay.service.payment.plaid;

import com.google.common.util.concurrent.ListeningExecutorService;
import com.imani.bill.pay.domain.payment.IHasPaymentInfo;
import com.imani.bill.pay.domain.payment.plaid.PlaidAPIInvocationStatistic;
import com.imani.bill.pay.domain.payment.plaid.repository.IPlaidAPIInvocationStatisticRepository;
import com.imani.bill.pay.domain.property.PropertyManager;
import com.imani.bill.pay.domain.user.UserRecord;
import com.imani.bill.pay.service.concurrency.AppConcurrencyConfigurator;
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
@Service(PlaidAPIInvocationStatisticService.SPRING_BEAN)
public class PlaidAPIInvocationStatisticService implements IPlaidAPIInvocationStatisticService {


    @Autowired
    @Qualifier(DateTimeUtil.SPRING_BEAN)
    private IDateTimeUtil iDateTimeUtil;

    @Autowired
    @Qualifier(AppConcurrencyConfigurator.STATISTICS_THREAD_POOL)
    private ListeningExecutorService listeningExecutorService;

    @Autowired
    private IPlaidAPIInvocationStatisticRepository iPlaidAPIInvocationStatisticRepository;

    public static final String SPRING_BEAN = "com.imani.bill.pay.service.payment.plaid.PlaidAPIInvocationStatisticService";

    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(PlaidAPIInvocationStatisticService.class);


    @Override
    public void save(PlaidAPIInvocationStatistic plaidAPIInvocationStatistic) {
        Assert.notNull(plaidAPIInvocationStatistic, "PlaidAPIInvocationStatistic cannot be null");

        Runnable runnable = () -> {
            LOGGER.debug("Saving plaidAPIInvocationStatistic:=> {}", plaidAPIInvocationStatistic);
            iPlaidAPIInvocationStatisticRepository.save(plaidAPIInvocationStatistic);
        };

        listeningExecutorService.submit(runnable);
    }


    @Override
    public List<PlaidAPIInvocationStatistic> findFailedAccessTokenRequestCurrentDay(IHasPaymentInfo iHasPaymentInfo) {
        Assert.notNull(iHasPaymentInfo, "iHasPaymentInfo cannot be null");

        // Get the start and end of day DateTime
        DateTime start = iDateTimeUtil.getDateTimeAtStartOfCurrentDay();
        DateTime end = iDateTimeUtil.getDateTimeAtEndOfCurrentDay();

        if(iHasPaymentInfo instanceof UserRecord) {
            UserRecord userRecord = (UserRecord)iHasPaymentInfo;
            LOGGER.debug("Finding all failed attempts to retrieve Plaid AccessToken for user:=> {}", userRecord.getEmbeddedContactInfo().getEmail());
            return iPlaidAPIInvocationStatisticRepository.findFailedAccessTokenRequestInRange(userRecord, start, end);
        } else if(iHasPaymentInfo instanceof PropertyManager) {
            PropertyManager propertyManager = (PropertyManager)iHasPaymentInfo;
            LOGGER.debug("Finding all failed attempts to retrieve Plaid AccessToken for propertyManager:=> {}", propertyManager.getName());
            return iPlaidAPIInvocationStatisticRepository.findFailedAccessTokenRequestInRange(propertyManager, start, end);
        } else {
            String message = "Unsupported for implementation of IHasPaymentInfo:=> " + iHasPaymentInfo;
            throw new UnsupportedOperationException(message);
        }
    }
}
