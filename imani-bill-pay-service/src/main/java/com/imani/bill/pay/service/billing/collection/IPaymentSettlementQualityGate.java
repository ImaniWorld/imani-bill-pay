package com.imani.bill.pay.service.billing.collection;

import com.imani.bill.pay.domain.billing.BillingDetail;
import com.imani.bill.pay.domain.execution.ExecutionResult;
import com.imani.bill.pay.domain.payment.record.ImaniBillPayRecord;

/**
 * Implementations can define and implement different quality gate criterias that all payments need to pass in order to
 * be processed successfully.
 *
 * @author manyce400
 */
interface IPaymentSettlementQualityGate {

    public void vetQuality(ExecutionResult<BillingDetail> executionResult, ImaniBillPayRecord imaniBillPayRecord);

}
