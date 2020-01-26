package com.imani.bill.pay.service.payment.plaid;

import com.imani.bill.pay.domain.payment.plaid.PlaidAPIInvocationStatistic;
import com.imani.bill.pay.domain.payment.plaid.PlaidAPIResponse;

/**
 * @author manyce400
 */
public interface IPlaidAPIEndPointFacade {

    public <O extends PlaidAPIResponse> O invokePlaidAPIEndPoint(PlaidAPIInvocationStatistic plaidAPIInvocationStatistic, String apiURL, O responseObj);
}
