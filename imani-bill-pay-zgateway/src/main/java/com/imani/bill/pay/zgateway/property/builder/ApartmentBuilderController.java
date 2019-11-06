package com.imani.bill.pay.zgateway.property.builder;

import com.imani.bill.pay.domain.gateway.APIGatewayEvent;
import com.imani.bill.pay.domain.gateway.GenericAPIGatewayResponse;
import com.imani.bill.pay.domain.property.gateway.ApartmentBuilderRequest;
import com.imani.bill.pay.service.property.builder.ApartmentBuilderService;
import com.imani.bill.pay.service.property.builder.IApartmentBuilderService;
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
@RequestMapping("/apartment/builder")
public class ApartmentBuilderController {


    @Autowired
    @Qualifier(ApartmentBuilderService.SPRING_BEAN)
    private IApartmentBuilderService iApartmentBuilderService;

    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(ApartmentBuilderController.class);


    @PostMapping("/new")
    public APIGatewayEvent<ApartmentBuilderRequest, GenericAPIGatewayResponse> buildApartment(@RequestBody APIGatewayEvent<ApartmentBuilderRequest, GenericAPIGatewayResponse> apiGatewayEvent) {
        LOGGER.info("Executing build apartment event with:=> {}", apiGatewayEvent);
        APIGatewayEvent<ApartmentBuilderRequest, GenericAPIGatewayResponse> apiGatewayEventResult = iApartmentBuilderService.buildApartment(apiGatewayEvent.getRequestBody().get());
        return apiGatewayEventResult;
    }

}
