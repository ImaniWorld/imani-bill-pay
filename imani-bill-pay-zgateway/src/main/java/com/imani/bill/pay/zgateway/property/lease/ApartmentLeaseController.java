package com.imani.bill.pay.zgateway.property.lease;

import com.imani.bill.pay.domain.property.Apartment;
import com.imani.bill.pay.domain.property.LeaseAgreement;
import com.imani.bill.pay.domain.property.PropertyManager;
import com.imani.bill.pay.domain.user.UserRecord;
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
    public LeaseAgreement leaseApartmentWithAgreement(@RequestBody UserRecord userRecord, @RequestBody Apartment apartment, @RequestBody PropertyManager propertyManager, @RequestBody LeaseAgreement leaseAgreement) {
        LOGGER.info("Attempting to lease apartment to user for Property Manager:=> {}", propertyManager.getName());
        LeaseAgreement finalLeaseAgreement = iApartmentLeaseService.leaseApartment(userRecord, apartment, propertyManager, leaseAgreement.getMonthlyRentalCost(), leaseAgreement.getLeaseAgreementTypeE());
        return finalLeaseAgreement;
    }

}