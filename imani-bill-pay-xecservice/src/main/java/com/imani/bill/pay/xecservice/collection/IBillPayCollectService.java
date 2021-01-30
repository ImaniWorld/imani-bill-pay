package com.imani.bill.pay.xecservice.collection;

import com.imani.bill.pay.domain.billing.ImaniBillExplained;
import com.imani.bill.pay.domain.execution.ExecutionResult;
import com.imani.bill.pay.domain.payment.record.ImaniBillPayRecord;

/**
 * @author manyce400
 */
public interface IBillPayCollectService {

    public void processPayment(ImaniBillPayRecord imaniBillPayRecord, ExecutionResult<ImaniBillExplained> paymentExecutionResult);

}