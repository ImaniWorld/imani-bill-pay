package com.imani.bill.pay.service.utility;

import com.imani.bill.pay.domain.billing.ImaniBill;
import com.imani.bill.pay.domain.execution.ExecutionResult;
import com.imani.bill.pay.domain.utility.WaterUtilization;
import com.imani.bill.pay.domain.utility.WaterUtilizationCharge;

/**
 * @author manyce400
 */
public interface IWaterUtilizationService {

    public WaterUtilizationCharge computeUtilizationCharge(ImaniBill imaniBill);

    public double computeUtilizationChargeWithSchdFees(ImaniBill imaniBill);

    public void logWaterUtilization(WaterUtilization waterUtilization, ExecutionResult executionResult);

}
