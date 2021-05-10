package com.imani.bill.pay.domain;

import com.imani.bill.pay.domain.utility.WaterServiceAgreement;
import com.imani.bill.pay.domain.utility.WaterServiceAgreementLite;
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

}
