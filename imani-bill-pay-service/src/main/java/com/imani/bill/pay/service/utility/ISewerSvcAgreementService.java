package com.imani.bill.pay.service.utility;

import com.imani.bill.pay.domain.billing.BillPayFee;
import com.imani.bill.pay.domain.execution.ExecutionResult;
import com.imani.bill.pay.domain.utility.SewerServiceAgreement;

import java.util.List;

/**
 * @author manyce400
 */
public interface ISewerSvcAgreementService extends IUtilitySvcAgreementService {

    public void createAgreement(SewerServiceAgreement sewerServiceAgreement, ExecutionResult executionResult, List<BillPayFee> billPayFees);

}
