package com.imani.bill.pay.service.property.lease;

import com.imani.bill.pay.domain.gateway.APIGatewayEvent;
import com.imani.bill.pay.domain.gateway.GenericAPIGatewayResponse;
import com.imani.bill.pay.domain.property.Apartment;
import com.imani.bill.pay.domain.property.LeaseAgreement;
import com.imani.bill.pay.domain.property.LeaseAgreementTypeE;
import com.imani.bill.pay.domain.property.PropertyManager;
import com.imani.bill.pay.domain.property.gateway.LeaseAgreementRequest;
import com.imani.bill.pay.domain.property.repository.IApartmentRepository;
import com.imani.bill.pay.domain.property.repository.IPropertyManagerRepository;
import com.imani.bill.pay.domain.user.UserRecord;
import com.imani.bill.pay.domain.user.UserResidence;
import com.imani.bill.pay.domain.user.repository.IUserRecordRepository;
import com.imani.bill.pay.service.property.ILeaseAgreementService;
import com.imani.bill.pay.service.property.LeaseAgreementService;
import com.imani.bill.pay.service.user.IUserResidenceService;
import com.imani.bill.pay.service.user.UserResidenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.transaction.Transactional;
import java.util.Optional;

/**
 * @author manyce400
 */
@Service(ApartmentLeaseService.SPRING_BEAN)
public class ApartmentLeaseService implements IApartmentLeaseService {


    @Autowired
    private IUserRecordRepository userRecordRepository;

    @Autowired
    private IApartmentRepository iApartmentRepository;

    @Autowired
    private IPropertyManagerRepository iPropertyManagerRepository;

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

        Double monthlyRentalCost = apiGatewayEvent.getRequestBody().get().getMonthlyRentalCost();
        LeaseAgreementTypeE leaseAgreementTypeE = apiGatewayEvent.getRequestBody().get().getLeaseAgreementTypeE();

        // Fetch JPA versions of these objects to get the most recent versions
        UserRecord userRecord = userRecordRepository.findByUserEmail(apiGatewayEvent.getRequestBody().get().getUserRecord().getEmbeddedContactInfo().getEmail());
        Optional<Apartment> apartment = iApartmentRepository.findById(apiGatewayEvent.getRequestBody().get().getApartment().getId());
        Optional<PropertyManager> propertyManager = iPropertyManagerRepository.findById(apiGatewayEvent.getRequestBody().get().getPropertyManager().getId());


        LeaseAgreement existingLeaseAgreement = null;
        if (userRecord != null &&
                apartment.isPresent() &&
                propertyManager.isPresent()) {
            LOGGER.info("Leasing apartment => {} to user => {}", apartment, userRecord.getEmbeddedContactInfo().getEmail());

            // Verify that this Apartment is not already leased
            existingLeaseAgreement = iLeaseAgreementService.findApartmentLeaseAgreement(apartment.get());
            if(existingLeaseAgreement == null) {
                // Create a LeaseAgreement to reflect this transation.
                LeaseAgreement leaseAgreement = iLeaseAgreementService.buildLeaseAgreement(userRecord, apartment.get(), propertyManager.get(), monthlyRentalCost, leaseAgreementTypeE);

                // Record the UserResidence to be this new apartment
                UserResidence userResidence = iUserResidenceService.buildUserResidence(userRecord, apartment.get(), leaseAgreement, true);
                return APIGatewayEvent.<LeaseAgreementRequest, GenericAPIGatewayResponse>getSuccessGenericAPIGatewayResponse(userRecord);
            }
        } else {
            LOGGER.warn("Apartment lease operation cannot be executed, invalid data passed in request");
            return APIGatewayEvent.<LeaseAgreementRequest, GenericAPIGatewayResponse>getInvalidGenericAPIGatewayResponse("ApiGatewayEvent attributes passed are invalid. UserRecord, Apartment or PropertManager not found.");
        }

        LOGGER.info("Apartment is currently leased already with existingLeaseAgreement:=> {}", existingLeaseAgreement);
        return APIGatewayEvent.<LeaseAgreementRequest, GenericAPIGatewayResponse>getFailedGenericAPIGatewayResponse("Apartment is currently already leased.", userRecord);
    }
}
