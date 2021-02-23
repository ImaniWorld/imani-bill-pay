package com.imani.bill.pay.service.utility;

import com.imani.bill.pay.domain.execution.ExecutionResult;
import com.imani.bill.pay.domain.utility.WaterServiceAgreement;

/**
 * @author manyce400
 */
public interface IWaterSvcAgreementService {

    public void createAgreement(WaterServiceAgreement waterServiceAgreement, ExecutionResult executionResult);

}