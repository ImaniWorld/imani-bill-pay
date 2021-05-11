package com.imani.bill.pay.service.utility;

import com.imani.bill.pay.domain.billing.ImaniBill;
import com.imani.bill.pay.domain.utility.WaterUtilization;
import com.imani.bill.pay.domain.utility.WaterUtilizationCharge;
import com.imani.bill.pay.domain.utility.WaterUtilizationLite;

import java.util.Optional;

/**
 * @author manyce400
 */
public interface IWaterUtilizationService {

    public WaterUtilizationCharge computeUtilizationCharge(ImaniBill imaniBill);

    public double computeUtilizationChargeWithSchdFees(ImaniBill imaniBill);

    public Optional<WaterUtilization> logWaterUtilization(WaterUtilizationLite waterUtilizationLite);

}
