package com.imani.bill.pay.xecservice.utility;

import com.imani.bill.pay.domain.billing.BillPayFee;
import com.imani.bill.pay.domain.execution.ExecutionResult;
import com.imani.bill.pay.domain.utility.SewerServiceAgreement;

import java.util.List;

/**
 * @author manyce400
 */
public interface ISewerSvcAgreementExecService {

    public void processSewerSvcAgreement(ExecutionResult<SewerServiceAgreement> executionResult, List<BillPayFee> billPayFees);

}
