package com.imani.bill.pay.service.property;

import com.google.common.util.concurrent.ListeningExecutorService;
import com.imani.bill.pay.domain.payment.PaymentAPIExecResultE;
import com.imani.bill.pay.domain.payment.PlaidAPIExecMetric;
import com.imani.bill.pay.domain.payment.PlaidProductE;
import com.imani.bill.pay.domain.payment.repository.IPlaidAPIExecMetricRepository;
import com.imani.bill.pay.domain.user.UserRecord;
import com.imani.bill.pay.service.concurrency.AppConcurrencyConfigurator;
import com.imani.bill.pay.service.util.DateTimeUtil;
import com.imani.bill.pay.service.util.IDateTimeUtil;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * @author manyce400
 */
@Service(PlaidAPIExecMetricBuilderService.SPRING_BEAN)
public class PlaidAPIExecMetricBuilderService implements IPlaidAPIExecMetricBuilderService {


    @Autowired
    @Qualifier(AppConcurrencyConfigurator.SERVICE_THREAD_POOL)
    private ListeningExecutorService listeningExecutorService;

    @Autowired
    private IPlaidAPIExecMetricRepository iPlaidAPIExecMetricRepository;

    @Autowired
    @Qualifier(DateTimeUtil.SPRING_BEAN)
    private IDateTimeUtil iDateTimeUtil;

    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(PlaidAPIExecMetricBuilderService.class);

    public static final String SPRING_BEAN = "com.imani.cash.domain.service.payment.ach.plaid.PlaidAPIExecMetricBuilderService";



    @Override
    public void buildBalancePlaidAPIExecMetricOnSuccess(final UserRecord userRecord, final DateTime apiInvocationStartDate, final DateTime apiInvocationEndDate) {
        Runnable runnable = () ->  {
            Assert.notNull(userRecord, "UserRecord cannot be null");
            Assert.notNull(apiInvocationStartDate, "apiInvocationStartDate cannot be null");
            Assert.notNull(apiInvocationEndDate, "apiInvocationEndDate cannot be null");

            LOGGER.debug("Building PlaidAPIExecMetric on successful balance call for User:=> {}", userRecord.getEmbeddedContactInfo().getEmail());

            PlaidAPIExecMetric plaidAPIExecMetric = PlaidAPIExecMetric.builder()
                    .userRecord(userRecord)
                    .plaidProductE(PlaidProductE.balance)
                    .apiInvocationStartDate(apiInvocationStartDate)
                    .apiInvocationEndDate(apiInvocationEndDate)
                    .paymentAPIExecResultE(PaymentAPIExecResultE.Success)
                    .build();
            iPlaidAPIExecMetricRepository.save(plaidAPIExecMetric);
        };

        listeningExecutorService.submit(runnable);
    }

    @Override
    public void buildBalancePlaidAPIExecMetricOnFailure(UserRecord userRecord, String apiExecError, DateTime apiInvocationStartDate, DateTime apiInvocationEndDate) {
        // Execute entire recording of metrics on a different thread.
        Runnable runnable = () -> {
            Assert.notNull(userRecord, "UserRecord cannot be null");
            Assert.notNull(apiInvocationStartDate, "apiInvocationStartDate cannot be null");
            Assert.notNull(apiInvocationEndDate, "apiInvocationEndDate cannot be null");

            LOGGER.debug("Building PlaidAPIExecMetric on failed balance call for User:=> {}", userRecord.getEmbeddedContactInfo().getEmail());

            PlaidAPIExecMetric plaidAPIExecMetric = PlaidAPIExecMetric.builder()
                    .userRecord(userRecord)
                    .plaidProductE(PlaidProductE.balance)
                    .apiInvocationStartDate(apiInvocationStartDate)
                    .apiInvocationEndDate(apiInvocationEndDate)
                    .paymentAPIExecResultE(PaymentAPIExecResultE.Failed)
                    .apiExecError(apiExecError)
                    .build();
            iPlaidAPIExecMetricRepository.save(plaidAPIExecMetric);
        };

        listeningExecutorService.submit(runnable);
    }
}
