package com.imani.bill.pay.service.payment;

import com.imani.bill.pay.domain.payment.PlaidAPIStatistic;
import com.imani.bill.pay.domain.payment.plaid.PlaidProductE;
import com.imani.bill.pay.domain.user.UserRecord;

import java.util.List;

/**
 * @author manyce400
 */
public interface IPlaidAPIStatisticFinderService {


    /**
     * Find a User's Plaid API Metrics by a specific PlaidProductE.
     *
     * @param userRecord
     * @return List<PlaidAPIStatistic>
     */
    public List<PlaidAPIStatistic> findUserPlaidAPIExecMetricCurrentMonth(UserRecord userRecord, PlaidProductE plaidProductE);

}
