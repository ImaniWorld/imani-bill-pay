package com.imani.bill.pay.service.concurrency;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.springframework.util.Assert;

import java.util.concurrent.ThreadFactory;

/**
 * @author manyce400
 */
public class ApplicationThreadFactoryBuilder {

    public static ThreadFactory buildThreadFactory(String namePattern) {
        Assert.notNull(namePattern, "namePattern cannot be null");
        ThreadFactory threadFactory = new ThreadFactoryBuilder()
                .setNameFormat(namePattern)
                .setUncaughtExceptionHandler(new UncaughtExceptionLogger())
                .build();
        return threadFactory;
    }
}
