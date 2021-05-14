package com.imani.bill.pay.service.concurrency;

import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executors;

/**
 * TODO customize the number of threads used in this pool
 *
 * @author manyce400
 */
@Configuration
public class AppConcurrencyConfigurator {


    public static final String STATISTICS_THREAD_POOL = "com.imani.bill.pay.service.concurrency.STATISTICS_THREAD_POOL";

    public static final String AUTO_BILLING_THREAD_POOL = "com.imani.bill.pay.service.concurrency.AUTO_BILLING_THREAD_POOL";


    // This thread pool should primarily be used for services that execute statistics based functions.
    @Bean(AppConcurrencyConfigurator.STATISTICS_THREAD_POOL)
    public ListeningExecutorService configureStatisticsSvcThreadPool() {
        ListeningExecutorService executorService = MoreExecutors.listeningDecorator(
                Executors.newFixedThreadPool(5, ApplicationThreadFactoryBuilder.buildThreadFactory("STATISTICS-Thread-%d"))
        );
        return executorService;
    }

    // This thread pool should primarily be used for automated billing services
    @Bean(AppConcurrencyConfigurator.AUTO_BILLING_THREAD_POOL)
    public ListeningExecutorService configureAutomatedBillingSvcThreadPool() {
        ListeningExecutorService executorService = MoreExecutors.listeningDecorator(
                Executors.newFixedThreadPool(10, ApplicationThreadFactoryBuilder.buildThreadFactory("AUTO-BILLING-%d"))
        );
        return executorService;
    }
}
