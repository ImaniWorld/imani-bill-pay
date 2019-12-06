package com.imani.bill.pay.service.geographical;

import com.imani.bill.pay.domain.geographical.GeographicalRegion;
import com.imani.bill.pay.domain.geographical.repository.IGeographicalRegionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * @author manyce400
 */
@Service(GeographicalRegionService.SPRING_BEAN)
public class GeographicalRegionService implements IGeographicalRegionService {


//    @Autowired
//    private StripeAPIConfig stripeAPIConfig;
//
//    @Autowired
//    private PlaidAPIConfig plaidAPIConfig;

    @Autowired
    private IGeographicalRegionRepository iGeographicalRegionRepository;



    public static final String SPRING_BEAN = "com.imani.bill.pay.service.geographical.GeographicalRegionService";


    @Override
    public GeographicalRegion findGeographicalRegionByCode(String regionCode) {
        Assert.notNull(regionCode, "regionCode cannot be null");
        GeographicalRegion geographicalRegion = iGeographicalRegionRepository.findGeographicalRegionByCode(regionCode);
        return geographicalRegion;
    }


//    @PostConstruct
//    public void runPostConstruct() {
//        System.out.println("\nStripe Configurations");
//        String publicKey = stripeAPIConfig.getPublicKey();
//        String apiKey = stripeAPIConfig.getApiKey();
//        System.out.println("publicKey = " + publicKey);
//        System.out.println("apiKey = " + apiKey);
//
//        System.out.println("\nPlaid Configurations");
//        String plaidClientID = plaidAPIConfig.getClientID();
//        String plaidPublicKey = plaidAPIConfig.getPublicKey();
//        String secret = plaidAPIConfig.getSecret();
//        System.out.println("plaidClientID = " + plaidClientID);
//        System.out.println("plaidPublicKey = " + plaidPublicKey);
//        System.out.println("secret = " + secret);
//        System.out.println("\n");
//    }

}
