package com.imani.bill.pay.service.payment.plaid;

import com.imani.bill.pay.domain.payment.plaid.PlaidAPIInvocationStatistic;

/**
 * @author manyce400
 */
public interface IPlaidAPIInvocationStatisticService {

    public void save(PlaidAPIInvocationStatistic plaidAPIInvocationStatistic);

}
