package com.imani.bill.pay.xecservice.utility;

import com.imani.bill.pay.domain.execution.ExecutionResult;
import com.imani.bill.pay.domain.utility.WaterServiceAgreement;

/**
 * @author manyce400
 */
public interface IWaterSvcAgreementExecService {

    public void processWaterSvcAgreement(ExecutionResult<WaterServiceAgreement> waterServiceAgreementExecutionResult);

}
