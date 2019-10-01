package com.imani.bill.pay.service.payment;

import com.imani.bill.pay.domain.payment.ACHPaymentInfo;
import org.joda.time.DateTime;

/**
 * Service for building and persisting different metrics around Plaid API calls.  All these calls should be executed on
 * a different thread and should not block main calling thread.
 *
 * @author manyce400
 */
public interface IPlaidAPIStatisticBuilderService {


    /**
     * Build a PlaidAPIStatistic on successful execution of a Plaid Balance product call.
     *
     * @param achPaymentInfo
     * @param apiInvocationStartDate
     * @param apiInvocationEndDate
     */
    public void buildBalancePlaidAPIExecMetricOnSuccess(ACHPaymentInfo achPaymentInfo, DateTime apiInvocationStartDate, DateTime apiInvocationEndDate);


    /**
     * Build a PlaidAPIStatistic on failed execution of a Plaid Balance product call.
     *
     * @param achPaymentInfo
     * @param apiExecError
     * @param apiInvocationStartDate
     * @param apiInvocationEndDate
     */
    public void buildBalancePlaidAPIExecMetricOnFailure(ACHPaymentInfo achPaymentInfo, String apiExecError, DateTime apiInvocationStartDate, DateTime apiInvocationEndDate);

}
