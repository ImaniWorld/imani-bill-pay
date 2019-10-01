package com.imani.bill.pay.service.concurrency;

/**
 * Uncaught exception handler that will log exception to error log.
 *
 * @author manyce400
 */
public class UncaughtExceptionLogger implements Thread.UncaughtExceptionHandler {



    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(UncaughtExceptionLogger.class);


    public UncaughtExceptionLogger() {

    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        String threadName = t.getName();
        String threadGroupName = t.getThreadGroup().getName();
        LOGGER.warn("Thread: {} from Group: {} threw an UncaughtException: {}", threadName, threadGroupName, e);
        e.printStackTrace();
    }
}
