package com.imani.bill.pay.service.payment.plaid;

import com.imani.bill.pay.domain.payment.IHasPaymentInfo;
import com.imani.bill.pay.domain.payment.plaid.PlaidAPIInvocationStatistic;

import java.util.List;

/**
 * @author manyce400
 */
public interface IPlaidAPIInvocationStatisticService {

    public void save(PlaidAPIInvocationStatistic plaidAPIInvocationStatistic);

    public List<PlaidAPIInvocationStatistic> findFailedAccessTokenRequestCurrentDay(IHasPaymentInfo iHasPaymentInfo);

}
