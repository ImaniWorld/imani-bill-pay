package com.imani.bill.pay.service.billing.collection;

import com.imani.bill.pay.domain.billing.ImaniBillExplained;
import com.imani.bill.pay.domain.execution.ExecutionResult;

/**
 * @author manyce400
 */
public interface IBillPayCollectionService {

    public ExecutionResult<ImaniBillExplained> collectPayment(ImaniBillExplained imaniBillExplained);

}
