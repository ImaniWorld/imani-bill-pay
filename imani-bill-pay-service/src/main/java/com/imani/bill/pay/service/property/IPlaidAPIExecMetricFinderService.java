package com.imani.bill.pay.service.property;

import com.imani.bill.pay.domain.payment.PlaidAPIExecMetric;
import com.imani.bill.pay.domain.payment.PlaidProductE;
import com.imani.bill.pay.domain.user.UserRecord;

import java.util.List;

/**
 * @author manyce400
 */
public interface IPlaidAPIExecMetricFinderService {


    /**
     * Find a User's Plaid API Metrics by a specific PlaidProductE.
     *
     * @param userRecord
     * @return List<PlaidAPIExecMetric>
     */
    public List<PlaidAPIExecMetric> findUserPlaidAPIExecMetricCurrentMonth(UserRecord userRecord, PlaidProductE plaidProductE);

}
