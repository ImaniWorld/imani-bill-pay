package com.imani.bill.pay.zgateway.property.builder;

import com.imani.bill.pay.domain.property.gateway.ApartmentBuilderEvent;
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
    public ApartmentBuilderEvent buildApartment(@RequestBody ApartmentBuilderEvent apartmentBuilderEvent) {
        LOGGER.info("Executing build apartment event with:=> {}", apartmentBuilderEvent);
        ApartmentBuilderEvent apartmentBuilderEventResult =iApartmentBuilderService.buildApartment(apartmentBuilderEvent);
        return apartmentBuilderEventResult;
    }

}
