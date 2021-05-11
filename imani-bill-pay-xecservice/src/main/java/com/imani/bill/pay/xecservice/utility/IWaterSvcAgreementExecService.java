package com.imani.bill.pay.xecservice.utility;

import com.imani.bill.pay.domain.billing.BillPayFee;
import com.imani.bill.pay.domain.execution.ExecutionResult;
import com.imani.bill.pay.domain.utility.WaterServiceAgreement;
import com.imani.bill.pay.domain.utility.WaterUtilizationLite;

import java.util.List;

/**
 * @author manyce400
 */
public interface IWaterSvcAgreementExecService {

    public void processWaterSvcAgreement(ExecutionResult<WaterServiceAgreement> waterServiceAgreementExecutionResult, List<BillPayFee> billPayFees);

    public void processWaterUtilization(WaterUtilizationLite waterUtilizationLite, ExecutionResult<WaterUtilizationLite> executionResult);

}
