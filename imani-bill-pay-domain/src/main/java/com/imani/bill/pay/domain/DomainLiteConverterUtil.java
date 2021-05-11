package com.imani.bill.pay.domain;

import com.imani.bill.pay.domain.utility.WaterServiceAgreement;
import com.imani.bill.pay.domain.utility.WaterServiceAgreementLite;
import com.imani.bill.pay.domain.utility.WaterUtilization;
import com.imani.bill.pay.domain.utility.WaterUtilizationLite;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

/**
 * @author manyce400
 */
public class DomainLiteConverterUtil {


    public static List<WaterServiceAgreementLite> toLite(List<WaterServiceAgreement> waterServiceAgreements) {
        Assert.notNull(waterServiceAgreements, "WaterServiceAgreement list cannot be null");
        List<WaterServiceAgreementLite> results = new ArrayList<>();

        // Convert to WaterServiceAgreementLite instances
        waterServiceAgreements.forEach( waterServiceAgreement -> {
            WaterServiceAgreementLite waterServiceAgreementLite = waterServiceAgreement.toAgreementLite();
            results.add(waterServiceAgreementLite);
        });

        return results;
    }

    public static List<WaterUtilizationLite> toWaterUtilizationLite(List<WaterUtilization> waterUtilizations) {
        Assert.notNull(waterUtilizations, "WaterUtilization list cannot be null");
        List<WaterUtilizationLite> results = new ArrayList<>();

        waterUtilizations.forEach(waterUtilization -> {
            WaterUtilizationLite waterUtilizationLite = toWaterUtilizationLite(waterUtilization);
            results.add(waterUtilizationLite);
        });

        return results;
    }

    public static WaterUtilizationLite toWaterUtilizationLite(WaterUtilization waterUtilization) {
        Assert.notNull(waterUtilization, "WaterUtilization cannot be null");
        WaterUtilizationLite waterUtilizationLite = waterUtilization.toWaterUtilizationLite();
        return waterUtilizationLite;
    }

}
