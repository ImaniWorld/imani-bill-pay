package com.imani.bill.pay.zgateway.property.lease;

import com.imani.bill.pay.domain.execution.ExecutionResult;
import com.imani.bill.pay.domain.gateway.APIGatewayRequest;
import com.imani.bill.pay.domain.gateway.APIGatewayResponse;
import com.imani.bill.pay.domain.leasemanagement.PropertyLeaseAgreementLite;
import com.imani.bill.pay.service.property.lease.ApartmentLeaseService;
import com.imani.bill.pay.service.property.lease.IResidentialPropertyLeaseService;
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
@RequestMapping("/property/management")
public class ResidentialPropertyLeaseController {


    @Autowired
    @Qualifier(ApartmentLeaseService.SPRING_BEAN)
    private IResidentialPropertyLeaseService iResidentialPropertyLeaseService;

    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(ResidentialPropertyLeaseController.class);

    @PostMapping("/residential/apartment/lease")
    public APIGatewayResponse leaseApartmentWithAgreement(@RequestBody APIGatewayRequest<PropertyLeaseAgreementLite> apiGatewayRequest) {
        LOGGER.info("Processing residential apartment property lease agreement for user:=> {}",  apiGatewayRequest.getUserRecordLite());
        ExecutionResult<PropertyLeaseAgreementLite> executionResult = iResidentialPropertyLeaseService.leaseProperty(apiGatewayRequest.getUserRecordLite(), apiGatewayRequest.getRequestObject());
        return APIGatewayResponse.fromExecutionResult(executionResult);
    }

}