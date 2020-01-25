package com.imani.bill.pay.service.payment.plaid;

import com.imani.bill.pay.domain.payment.plaid.PlaidAPIInvocationE;
import com.imani.bill.pay.domain.payment.plaid.PlaidAPIInvocationStatistic;
import com.imani.bill.pay.domain.payment.plaid.PlaidAPIRequest;
import com.imani.bill.pay.domain.payment.plaid.PlaidAPIResponse;
import com.imani.bill.pay.domain.user.UserRecord;

/**
 * @author manyce400
 */
public interface IPlaidAPIInvocationStatisticService {

    public void save(PlaidAPIInvocationStatistic plaidAPIInvocationStatistic);

    public PlaidAPIInvocationStatistic startPlaidAPIInvocation(UserRecord userRecord, PlaidAPIInvocationE plaidAPIInvocationE, PlaidAPIRequest plaidAPIRequest);

    public <O extends PlaidAPIResponse> void endPlaidAPIInvocation(PlaidAPIInvocationStatistic plaidAPIInvocationStatistic, O plaidAPIResponse);

}
