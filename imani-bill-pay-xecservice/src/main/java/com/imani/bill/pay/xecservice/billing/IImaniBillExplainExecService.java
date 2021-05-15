package com.imani.bill.pay.xecservice.billing;

import com.imani.bill.pay.domain.billing.ImaniBillExplained;
import com.imani.bill.pay.domain.execution.ExecutionResult;

/**
 * @author manyce400
 */
public interface IImaniBillExplainExecService<O> {

    public void explainImaniBill(Long imaniBillID, ExecutionResult<ImaniBillExplained> executionResult);

}
