package com.imani.bill.pay.service.concurrency;

import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executors;

/**
 * @author manyce400
 */
@Configuration
public class AppConcurrencyConfigurator {


    public static final String SERVICE_THREAD_POOL = "com.imani.bill.pay.service.concurrency.AppConcurrencyConfigurator";

    // TODO customize the number of threads used in this pool
    @Bean(AppConcurrencyConfigurator.SERVICE_THREAD_POOL)
    public ListeningExecutorService configureServiceThreadPool() {
        ListeningExecutorService executorService = MoreExecutors.listeningDecorator(
                Executors.newFixedThreadPool(5, ApplicationThreadFactoryBuilder.buildThreadFactory("STATISTICS-QPalX-ThreadPool-%d"))
        );
        return executorService;
    }
}
