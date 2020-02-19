package com.imani.bill.pay.zgateway.property.manager;

import com.imani.bill.pay.domain.execution.ExecutionResult;
import com.imani.bill.pay.domain.gateway.APIGatewayRequest;
import com.imani.bill.pay.domain.gateway.APIGatewayResponse;
import com.imani.bill.pay.domain.property.PropertyManager;
import com.imani.bill.pay.service.property.manager.IPropertyMangerBuilderService;
import com.imani.bill.pay.service.property.manager.PropertyMangerBuilderService;
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
@RequestMapping("/propertymanager/builder")
public class PropertyManagerBuilderController {



    @Autowired
    @Qualifier(PropertyMangerBuilderService.SPRING_BEAN)
    private IPropertyMangerBuilderService iPropertyMangerBuilderService;


    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(PropertyManagerBuilderController.class);


    @PostMapping("/new")
    public APIGatewayResponse executeBillPayUserRegistration(@RequestBody APIGatewayRequest<PropertyManager> apiGatewayRequest) {
        LOGGER.info("Processing request to create new Imani BillPay PropertyManager with request:=>  {}", apiGatewayRequest);
        ExecutionResult executionResult = iPropertyMangerBuilderService.newPropertyManger(apiGatewayRequest.getRequestObject());
        return APIGatewayResponse.fromExecutionResult(executionResult);
    }
}
