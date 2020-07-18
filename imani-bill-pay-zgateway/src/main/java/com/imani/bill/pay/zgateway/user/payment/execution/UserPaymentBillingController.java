package com.imani.bill.pay.zgateway.user.payment.execution;

import com.imani.bill.pay.domain.gateway.APIGatewayRequest;
import com.imani.bill.pay.domain.property.MonthlyRentalBillExplained;
import com.imani.bill.pay.service.property.IMonthlyRentalBillDescService;
import com.imani.bill.pay.service.property.MonthlyRentalBillDescService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

/**
 * @author manyce400
 */
@RestController
@RequestMapping("/user/billing")
public class UserPaymentBillingController {


    @Autowired
    @Qualifier(MonthlyRentalBillDescService.SPRING_BEAN)
    private IMonthlyRentalBillDescService iMonthlyRentalBillDescService;

    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(UserPaymentBillingController.class);


    @PostMapping("/monthly/rental")
    public MonthlyRentalBillExplained getCurrMonthRentalBill(@RequestBody APIGatewayRequest apiGatewayRequest) {
        LOGGER.info("Generating monthly rental bill for apiGatewayRequest:=> {}", apiGatewayRequest);
        Optional<MonthlyRentalBillExplained> monthlyRentalBillExplained = iMonthlyRentalBillDescService.explainCurrentMonthRentalBill(apiGatewayRequest.getOnBehalfOf());
        return monthlyRentalBillExplained.get();
    }


}
