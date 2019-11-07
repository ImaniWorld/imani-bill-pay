package com.imani.bill.pay.service.property.lease;

import com.imani.bill.pay.domain.gateway.APIGatewayEvent;
import com.imani.bill.pay.domain.gateway.GenericAPIGatewayResponse;
import com.imani.bill.pay.domain.property.Apartment;
import com.imani.bill.pay.domain.property.LeaseAgreement;
import com.imani.bill.pay.domain.property.LeaseAgreementTypeE;
import com.imani.bill.pay.domain.property.PropertyManager;
import com.imani.bill.pay.domain.property.gateway.LeaseAgreementRequest;
import com.imani.bill.pay.domain.user.UserRecord;
import com.imani.bill.pay.domain.user.UserResidence;
import com.imani.bill.pay.service.property.ILeaseAgreementService;
import com.imani.bill.pay.service.property.LeaseAgreementService;
import com.imani.bill.pay.service.user.IUserResidenceService;
import com.imani.bill.pay.service.user.UserResidenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.transaction.Transactional;

/**
 * @author manyce400
 */
@Service(ApartmentLeaseService.SPRING_BEAN)
public class ApartmentLeaseService implements IApartmentLeaseService {


    @Autowired
    @Qualifier(LeaseAgreementService.SPRING_BEAN)
    private ILeaseAgreementService iLeaseAgreementService;

    @Autowired
    @Qualifier(UserResidenceService.SPRING_BEAN)
    private IUserResidenceService iUserResidenceService;

    public static final String SPRING_BEAN = "com.imani.bill.pay.service.property.lease.ApartmentLeaseService";

    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(PropertyLeaseService.class);


    @Transactional
    @Override
    public APIGatewayEvent<LeaseAgreementRequest, GenericAPIGatewayResponse> leaseApartment(APIGatewayEvent<LeaseAgreementRequest, GenericAPIGatewayResponse> apiGatewayEvent) {
        Assert.notNull(apiGatewayEvent, "APIGatewayEvent cannot be null");
        Assert.isTrue(apiGatewayEvent.getRequestBody().isPresent(), "LeaseAgreementRequest must be present");
        Assert.notNull(apiGatewayEvent.getRequestBody().get().getUserRecord(), "UserRecord to lease apartment cannot be null");
        Assert.notNull(apiGatewayEvent.getRequestBody().get().getApartment(), "apartment cannot be null");
        Assert.notNull(apiGatewayEvent.getRequestBody().get().getPropertyManager(), "propertyManager cannot be null");
        Assert.notNull(apiGatewayEvent.getRequestBody().get().getMonthlyRentalCost(), "monthlyRentalCost cannot be null");
        Assert.notNull(apiGatewayEvent.getRequestBody().get().getLeaseAgreementTypeE(), "leaseAgreementTypeE cannot be null");

        UserRecord userRecord = apiGatewayEvent.getRequestBody().get().getUserRecord();
        Double monthlyRentalCost = apiGatewayEvent.getRequestBody().get().getMonthlyRentalCost();
        Apartment apartment = apiGatewayEvent.getRequestBody().get().getApartment();
        PropertyManager propertyManager = apiGatewayEvent.getRequestBody().get().getPropertyManager();
        LeaseAgreementTypeE leaseAgreementTypeE = apiGatewayEvent.getRequestBody().get().getLeaseAgreementTypeE();

        LOGGER.info("Leasing apartment => {} to user => {}", apartment, userRecord.getEmbeddedContactInfo().getEmail());

        // Verify that this Apartment is not already leased
        LeaseAgreement existingLeaseAgreement = iLeaseAgreementService.findApartmentLeaseAgreement(apartment);
        if(existingLeaseAgreement == null) {
            // Create a LeaseAgreement to reflect this transation.
            LeaseAgreement leaseAgreement = iLeaseAgreementService.buildLeaseAgreement(userRecord, apartment, propertyManager, monthlyRentalCost, leaseAgreementTypeE);

            // Record the UserResidence to be this new apartment
            UserResidence userResidence = iUserResidenceService.buildUserResidence(userRecord, apartment, leaseAgreement, true);
            return APIGatewayEvent.<LeaseAgreementRequest, GenericAPIGatewayResponse>getSuccessGenericAPIGatewayResponse(userRecord);
        }

        LOGGER.info("Apartment is currently leased already with existingLeaseAgreement:=> {}", existingLeaseAgreement);
        return APIGatewayEvent.<LeaseAgreementRequest, GenericAPIGatewayResponse>getFailedGenericAPIGatewayResponse("Apartment is currently already leased.", userRecord);
    }
}
