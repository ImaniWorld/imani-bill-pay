package com.imani.bill.pay.zgateway.property.lease;

import com.imani.bill.pay.domain.gateway.APIGatewayEvent;
import com.imani.bill.pay.domain.gateway.GenericAPIGatewayResponse;
import com.imani.bill.pay.domain.property.gateway.LeaseAgreementRequest;
import com.imani.bill.pay.service.property.lease.ApartmentLeaseService;
import com.imani.bill.pay.service.property.lease.IApartmentLeaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author manyce400
 */
@RestController
@RequestMapping("/apartment/lease")
public class ApartmentLeaseController {


    @Autowired
    @Qualifier(ApartmentLeaseService.SPRING_BEAN)
    private IApartmentLeaseService iApartmentLeaseService;

    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(ApartmentLeaseController.class);

    @PostMapping("/propertymanager/with/agreement")
    public APIGatewayEvent<LeaseAgreementRequest, GenericAPIGatewayResponse> leaseApartmentWithAgreement(@RequestBody APIGatewayEvent<LeaseAgreementRequest, GenericAPIGatewayResponse> apiGatewayEvent) {
        LOGGER.info("Processing Apartment lease request with apiGatewayEvent:=> {}", apiGatewayEvent);
        return iApartmentLeaseService.leaseApartment(apiGatewayEvent);
    }

}