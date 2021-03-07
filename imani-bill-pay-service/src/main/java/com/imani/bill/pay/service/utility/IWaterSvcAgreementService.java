package com.imani.bill.pay.service.utility;

import com.imani.bill.pay.domain.billing.BillPayFee;
import com.imani.bill.pay.domain.execution.ExecutionResult;
import com.imani.bill.pay.domain.utility.WaterServiceAgreement;

import java.util.List;

/**
 * @author manyce400
 */
public interface IWaterSvcAgreementService extends IUtilitySvcAgreementService {

    public void createAgreement(ExecutionResult<WaterServiceAgreement> executionResult, List<BillPayFee> billPayFees);

}