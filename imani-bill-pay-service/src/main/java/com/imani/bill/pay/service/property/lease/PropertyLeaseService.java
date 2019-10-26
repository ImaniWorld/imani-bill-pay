package com.imani.bill.pay.service.property.lease;

import com.imani.bill.pay.domain.property.LeaseAgreementTypeE;
import com.imani.bill.pay.domain.property.Property;
import com.imani.bill.pay.domain.property.PropertyTypeE;
import com.imani.bill.pay.domain.user.UserRecord;
import com.imani.bill.pay.service.property.ILeaseAgreementService;
import com.imani.bill.pay.service.property.LeaseAgreementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * @author manyce400
 */
@Service(PropertyLeaseService.SPRING_BEAN)
public class PropertyLeaseService implements IPropertyLeaseService {


    @Autowired
    @Qualifier(LeaseAgreementService.SPRING_BEAN)
    private ILeaseAgreementService iLeaseAgreementService;

    public static final String SPRING_BEAN = "com.imani.bill.pay.service.property.lease.PropertyLeaseService";

    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(PropertyLeaseService.class);


    @Override
    public void leaseProperty(UserRecord userRecord, Property property, Double monthlyRentalCost, LeaseAgreementTypeE leaseAgreementTypeE) {
        Assert.notNull(userRecord, "userRecord cannot be null");
        Assert.notNull(monthlyRentalCost, "monthlyRentalCost cannot be null");
        Assert.notNull(leaseAgreementTypeE, "leaseAgreementTypeE cannot be null");
        Assert.isTrue(property.getPropertyTypeE() == PropertyTypeE.SingleFamily, "Only single family properties can be leased to individual users");

        LOGGER.info("Leasing property @ => to user:=> {}", property.getPrintableAddress(), userRecord.getEmbeddedContactInfo().getEmail());

        //TODO make sure that this specific property is not rented already.
    }

}
