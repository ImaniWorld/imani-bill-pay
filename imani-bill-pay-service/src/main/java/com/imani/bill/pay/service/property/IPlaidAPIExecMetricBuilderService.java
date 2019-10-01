package com.imani.bill.pay.service.property;

import com.imani.bill.pay.domain.user.UserRecord;
import org.joda.time.DateTime;

/**
 * Service for building and persisting different metrics around Plaid API calls.  All these calls should be executed on
 * a different thread and should not block main calling thread.
 *
 * @author manyce400
 */
public interface IPlaidAPIExecMetricBuilderService {


    /**
     * Build a PlaidAPIExecMetric on successful execution of a Plaid Balance product call.
     *
     * @param userRecord
     * @param apiInvocationStartDate
     * @param apiInvocationEndDate
     */
    public void buildBalancePlaidAPIExecMetricOnSuccess(UserRecord userRecord, DateTime apiInvocationStartDate, DateTime apiInvocationEndDate);


    /**
     * Build a PlaidAPIExecMetric on failed execution of a Plaid Balance product call.
     *
     * @param userRecord
     * @param apiExecError
     * @param apiInvocationStartDate
     * @param apiInvocationEndDate
     */
    public void buildBalancePlaidAPIExecMetricOnFailure(UserRecord userRecord, String apiExecError, DateTime apiInvocationStartDate, DateTime apiInvocationEndDate);

}
