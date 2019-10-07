package com.imani.bill.pay.service.payment;

import com.google.common.util.concurrent.ListeningExecutorService;
import com.imani.bill.pay.domain.payment.ACHPaymentInfo;
import com.imani.bill.pay.domain.payment.PaymentAPIExecResultE;
import com.imani.bill.pay.domain.payment.PlaidAPIStatistic;
import com.imani.bill.pay.domain.payment.PlaidProductE;
import com.imani.bill.pay.domain.payment.repository.IPlaidAPIStatisticRepository;
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
@Service(PlaidAPIStatisticBuilderService.SPRING_BEAN)
public class PlaidAPIStatisticBuilderService implements IPlaidAPIStatisticBuilderService {


    @Autowired
    @Qualifier(AppConcurrencyConfigurator.SERVICE_THREAD_POOL)
    private ListeningExecutorService listeningExecutorService;

    @Autowired
    private IPlaidAPIStatisticRepository iPlaidAPIStatisticRepository;

    @Autowired
    @Qualifier(DateTimeUtil.SPRING_BEAN)
    private IDateTimeUtil iDateTimeUtil;

    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(PlaidAPIStatisticBuilderService.class);

    public static final String SPRING_BEAN = "com.imani.bill.pay.service.payment.PlaidAPIStatisticBuilderService";



    @Override
    public void buildBalancePlaidAPIExecMetricOnSuccess(final ACHPaymentInfo achPaymentInfo, final DateTime apiInvocationStartDate, final DateTime apiInvocationEndDate) {
        Runnable runnable = () ->  {
            Assert.notNull(achPaymentInfo, "UserRecord cannot be null");
            Assert.notNull(apiInvocationStartDate, "apiInvocationStartDate cannot be null");
            Assert.notNull(apiInvocationEndDate, "apiInvocationEndDate cannot be null");

            LOGGER.debug("Building PlaidAPIStatistic on successful balance call for achPaymentInfo:=> {}", achPaymentInfo);

            PlaidAPIStatistic plaidAPIStatistic = PlaidAPIStatistic.builder()
                    .achPaymentInfo(achPaymentInfo)
                    .plaidProductE(PlaidProductE.balance)
                    .apiInvocationStartDate(apiInvocationStartDate)
                    .apiInvocationEndDate(apiInvocationEndDate)
                    .paymentAPIExecResultE(PaymentAPIExecResultE.Success)
                    .build();
            iPlaidAPIStatisticRepository.save(plaidAPIStatistic);
        };

        listeningExecutorService.submit(runnable);
    }

    @Override
    public void buildBalancePlaidAPIExecMetricOnFailure(ACHPaymentInfo achPaymentInfo, String apiExecError, DateTime apiInvocationStartDate, DateTime apiInvocationEndDate) {
        // Execute entire recording of metrics on a different thread.
        Runnable runnable = () -> {
            Assert.notNull(achPaymentInfo, "ACHPaymentInfo cannot be null");
            Assert.notNull(apiInvocationStartDate, "apiInvocationStartDate cannot be null");
            Assert.notNull(apiInvocationEndDate, "apiInvocationEndDate cannot be null");

            LOGGER.debug("Building PlaidAPIStatistic on failed balance call for achPaymentInfo:=> {}", achPaymentInfo);

            PlaidAPIStatistic plaidAPIStatistic = PlaidAPIStatistic.builder()
                    .achPaymentInfo(achPaymentInfo)
                    .plaidProductE(PlaidProductE.balance)
                    .apiInvocationStartDate(apiInvocationStartDate)
                    .apiInvocationEndDate(apiInvocationEndDate)
                    .paymentAPIExecResultE(PaymentAPIExecResultE.Failed)
                    .apiExecError(apiExecError)
                    .build();
            iPlaidAPIStatisticRepository.save(plaidAPIStatistic);
        };

        listeningExecutorService.submit(runnable);
    }
}
