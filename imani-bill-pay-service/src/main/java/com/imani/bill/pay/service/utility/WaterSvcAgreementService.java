package com.imani.bill.pay.service.utility;

import com.imani.bill.pay.domain.agreement.EmbeddedAgreement;
import com.imani.bill.pay.domain.business.Business;
import com.imani.bill.pay.domain.business.repository.IBusinessRepository;
import com.imani.bill.pay.domain.contact.Address;
import com.imani.bill.pay.domain.contact.repository.IAddressRepository;
import com.imani.bill.pay.domain.execution.ExecutionResult;
import com.imani.bill.pay.domain.execution.ValidationAdvice;
import com.imani.bill.pay.domain.geographical.Community;
import com.imani.bill.pay.domain.geographical.repository.ICommunityRepository;
import com.imani.bill.pay.domain.user.UserRecord;
import com.imani.bill.pay.domain.user.repository.IUserRecordRepository;
import com.imani.bill.pay.domain.utility.WaterServiceAgreement;
import com.imani.bill.pay.domain.utility.repository.IWaterServiceAgreementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.transaction.Transactional;

/**
 * @author manyce400
 */
@Service(WaterSvcAgreementService.SPRING_BEAN)
public class WaterSvcAgreementService implements IWaterSvcAgreementService {

    @Autowired
    private IUserRecordRepository iUserRecordRepository;

    @Autowired
    private IBusinessRepository iBusinessRepository;

    @Autowired
    private IAddressRepository iAddressRepository;

    @Autowired
    private ICommunityRepository iCommunityRepository;

    @Autowired
    private IWaterServiceAgreementRepository iWaterServiceAgreementRepository;

    public static final String SPRING_BEAN = "com.imani.bill.pay.service.utility.WaterSvcAgreementService";

    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(WaterSvcAgreementService.class);


    @Transactional
    @Override
    public void createAgreement(WaterServiceAgreement waterServiceAgreement, ExecutionResult executionResult) {
        Assert.notNull(waterServiceAgreement, "WaterServiceAgreement cannot be null");
        Assert.isNull(waterServiceAgreement.getId(), "WaterServiceAgreement is already persisted");

        // Fetch persisted pieces to make sure that they are all valid
        Business business = iBusinessRepository.getOne(waterServiceAgreement.getBusiness().getId());
        Address serviceAddress = iAddressRepository.getOne(waterServiceAgreement.getServiceAddress().getId());
        UserRecord userRecord = iUserRecordRepository.findByUserEmail(waterServiceAgreement.getEmbeddedAgreement().getUserRecord().getEmbeddedContactInfo().getEmail());

        // Validate to make sure all good to proceed
        validateBusiness(business, executionResult);
        validateServiceAddress(serviceAddress, executionResult);
        validateUserRecord(userRecord, executionResult);

        if(!executionResult.hasValidationAdvice()) {
            LOGGER.info("Creating and save new WaterServiceAgreement between User[{}] and Business[{}]", userRecord.getEmbeddedContactInfo().getEmail(), business.getName());

            // Lookup the Community if it has been passed
            Community community = null;
            if (waterServiceAgreement.getCommunity() != null) {
                community = iCommunityRepository.getOne(waterServiceAgreement.getCommunity().getId());
            }

            EmbeddedAgreement embeddedAgreement = EmbeddedAgreement.builder()
                    .agreementInForce(true)
                    .billScheduleTypeE(waterServiceAgreement.getEmbeddedAgreement().getBillScheduleTypeE())
                    .fixedCost(waterServiceAgreement.getEmbeddedAgreement().getFixedCost())
                    .numberOfDaysTillLate(waterServiceAgreement.getEmbeddedAgreement().getNumberOfDaysTillLate())
                    .userRecord(userRecord)
                    .effectiveDate(waterServiceAgreement.getEmbeddedAgreement().getEffectiveDate())
                    .terminationDate(waterServiceAgreement.getEmbeddedAgreement().getTerminationDate())
                    .build();

            WaterServiceAgreement newWaterServiceAgreement = WaterServiceAgreement.builder()
                    .embeddedAgreement(embeddedAgreement)
                    .business(business)
                    .serviceAddress(serviceAddress)
                    .community(community)
                    .businessCustomerAcctID(waterServiceAgreement.getBusinessCustomerAcctID())
                    .numberOfGallonsPerFixedCost(waterServiceAgreement.getNumberOfGallonsPerFixedCost())
                    .build();
            iWaterServiceAgreementRepository.save(newWaterServiceAgreement);
        }
    }

    void validateBusiness(Business business, ExecutionResult executionResult) {
        if(business == null) {
            executionResult.addValidationAdvice(ValidationAdvice.newInstance("Failed to find the Water Utility Business provided for agreement."));
        }
    }

    void validateServiceAddress(Address serviceAddress, ExecutionResult executionResult) {
        if(serviceAddress == null) {
            executionResult.addValidationAdvice(ValidationAdvice.newInstance("Failed to find the Service Address provided for agreement."));
        }
    }

    void validateUserRecord(UserRecord userRecord, ExecutionResult executionResult) {
        // TODO make sure that this user is affiliated with this address.  IF not we cant create an agreement for this user.
        if(userRecord == null) {
            executionResult.addValidationAdvice(ValidationAdvice.newInstance("Failed to find the User provided for agreement."));
        }
    }
}
