package com.imani.bill.pay.service.payment.plaid;

import com.google.common.util.concurrent.ListeningExecutorService;
import com.imani.bill.pay.domain.payment.plaid.PlaidAPIInvocationStatistic;
import com.imani.bill.pay.domain.payment.plaid.repository.IPlaidAPIInvocationStatisticRepository;
import com.imani.bill.pay.service.concurrency.AppConcurrencyConfigurator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * @author manyce400
 */
@Service(PlaidAPIInvocationStatisticService.SPRING_BEAN)
public class PlaidAPIInvocationStatisticService implements IPlaidAPIInvocationStatisticService {


    @Autowired
    @Qualifier(AppConcurrencyConfigurator.SERVICE_THREAD_POOL)
    private ListeningExecutorService listeningExecutorService;

    @Autowired
    private IPlaidAPIInvocationStatisticRepository iPlaidAPIInvocationStatisticRepository;

    public static final String SPRING_BEAN = "com.imani.bill.pay.service.payment.plaid.PlaidAPIInvocationStatisticService";

    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(PlaidAPIInvocationStatisticService.class);


    @Override
    public void save(PlaidAPIInvocationStatistic plaidAPIInvocationStatistic) {
        Assert.notNull(plaidAPIInvocationStatistic, "PlaidAPIInvocationStatistic cannot be null");

        Runnable runnable = () -> {
            LOGGER.info("Saving plaidAPIInvocationStatistic:=> {}", plaidAPIInvocationStatistic);
            iPlaidAPIInvocationStatisticRepository.save(plaidAPIInvocationStatistic);
        };

        listeningExecutorService.submit(runnable);
    }

}
