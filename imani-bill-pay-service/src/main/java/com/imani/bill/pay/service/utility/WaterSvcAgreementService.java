package com.imani.bill.pay.service.utility;

import com.imani.bill.pay.domain.agreement.EmbeddedAgreement;
import com.imani.bill.pay.domain.business.Business;
import com.imani.bill.pay.domain.business.repository.IBusinessRepository;
import com.imani.bill.pay.domain.contact.Address;
import com.imani.bill.pay.domain.execution.ExecutionResult;
import com.imani.bill.pay.domain.execution.ValidationAdvice;
import com.imani.bill.pay.domain.geographical.Community;
import com.imani.bill.pay.domain.geographical.repository.ICommunityRepository;
import com.imani.bill.pay.domain.property.Property;
import com.imani.bill.pay.domain.property.repository.IPropertyRepository;
import com.imani.bill.pay.domain.user.UserRecord;
import com.imani.bill.pay.domain.user.repository.IUserRecordRepository;
import com.imani.bill.pay.domain.utility.EmbeddedUtilityService;
import com.imani.bill.pay.domain.utility.WaterServiceAgreement;
import com.imani.bill.pay.domain.utility.repository.IWaterServiceAgreementRepository;
import com.imani.bill.pay.domain.utility.repository.IWaterUtilizationRepository;
import com.imani.bill.pay.service.util.DateTimeUtil;
import com.imani.bill.pay.service.util.IDateTimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.transaction.Transactional;
import java.util.Optional;

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
    private IPropertyRepository iPropertyRepository;

    @Autowired
    private ICommunityRepository iCommunityRepository;

    @Autowired
    private IWaterServiceAgreementRepository iWaterServiceAgreementRepository;

    @Autowired
    private IWaterUtilizationRepository iWaterUtilizationRepository;

    @Autowired
    @Qualifier(DateTimeUtil.SPRING_BEAN)
    private IDateTimeUtil iDateTimeUtil;

    public static final String SPRING_BEAN = "com.imani.bill.pay.service.utility.WaterSvcAgreementService";

    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(WaterSvcAgreementService.class);


    @Transactional
    @Override
    public void createAgreement(WaterServiceAgreement waterServiceAgreement, ExecutionResult executionResult) {
        Assert.notNull(waterServiceAgreement, "WaterServiceAgreement cannot be null");
        Assert.isNull(waterServiceAgreement.getId(), "WaterServiceAgreement is already persisted");

        // Fetch persisted pieces to make sure that they are all valid
        Business utilityProviderBusiness = iBusinessRepository.getOne(waterServiceAgreement.getEmbeddedUtilityService().getUtilityProviderBusiness().getId());


        // Validate to make sure all good to proceed
        validateBusiness(utilityProviderBusiness, executionResult);

        if(!executionResult.hasValidationAdvice()) {
            // Try to associate this water service agreement with a user, property, business or community
            Optional<UserRecord> userRecord = findWaterSvcUserRecord(waterServiceAgreement);
            Optional<Property> property = findWaterSvcProperty(waterServiceAgreement);
            Optional<Business> agreementBusiness = findWaterSvcBusiness(waterServiceAgreement);
            Optional<Community> community = findWaterSvcCommunity(waterServiceAgreement);

            EmbeddedAgreement embeddedAgreement = EmbeddedAgreement.builder()
                    .agreementInForce(true)
                    .billScheduleTypeE(waterServiceAgreement.getEmbeddedAgreement().getBillScheduleTypeE())
                    .fixedCost(waterServiceAgreement.getEmbeddedAgreement().getFixedCost())
                    .numberOfDaysTillLate(waterServiceAgreement.getEmbeddedAgreement().getNumberOfDaysTillLate())
                    .effectiveDate(waterServiceAgreement.getEmbeddedAgreement().getEffectiveDate())
                    .terminationDate(waterServiceAgreement.getEmbeddedAgreement().getTerminationDate())
                    .build();

            enrichAgreement(userRecord, property, agreementBusiness, community, embeddedAgreement, executionResult);

            if (!executionResult.hasValidationAdvice()) {
                EmbeddedUtilityService embeddedUtilityService = EmbeddedUtilityService.builder()
                        .utilityProviderBusiness(utilityProviderBusiness)
                        .svcCustomerAcctID(waterServiceAgreement.getEmbeddedUtilityService().getSvcCustomerAcctID())
                        .svcDescription(waterServiceAgreement.getEmbeddedUtilityService().getSvcDescription())
                        .build();

                WaterServiceAgreement newWaterServiceAgreement = WaterServiceAgreement.builder()
                        .embeddedAgreement(embeddedAgreement)
                        .embeddedUtilityService(embeddedUtilityService)
                        .nbrOfGallonsPerFixedCost(waterServiceAgreement.getNbrOfGallonsPerFixedCost())
                        .build();
                iWaterServiceAgreementRepository.save(newWaterServiceAgreement);
            }
        }
    }

    Optional<UserRecord> findWaterSvcUserRecord(WaterServiceAgreement waterServiceAgreement) {
        if (waterServiceAgreement.getEmbeddedAgreement().getAgreementUserRecord() != null) {
            UserRecord userRecord = iUserRecordRepository.findByUserEmail(waterServiceAgreement.getEmbeddedAgreement().getAgreementUserRecord().getEmbeddedContactInfo().getEmail());
            return Optional.of(userRecord);
        }

        return Optional.empty();
    }

    Optional<Property> findWaterSvcProperty(WaterServiceAgreement waterServiceAgreement) {
        if (waterServiceAgreement.getEmbeddedAgreement().getAgreementProperty() != null) {
            Property property = iPropertyRepository.getOne(waterServiceAgreement.getEmbeddedAgreement().getAgreementProperty().getId());
            return Optional.of(property);
        }

        return Optional.empty();
    }

    Optional<Community> findWaterSvcCommunity(WaterServiceAgreement waterServiceAgreement) {
        if (waterServiceAgreement.getEmbeddedAgreement().getAgreementCommunity() != null) {
            Community community = iCommunityRepository.getOne(waterServiceAgreement.getEmbeddedAgreement().getAgreementCommunity().getId());
            return Optional.of(community);
        }

        return Optional.empty();
    }

    Optional<Business> findWaterSvcBusiness(WaterServiceAgreement waterServiceAgreement) {
        if (waterServiceAgreement.getEmbeddedAgreement().getAgreementBusiness() != null) {
            Business business = iBusinessRepository.getOne((waterServiceAgreement.getEmbeddedAgreement().getAgreementBusiness().getId()));
            return Optional.of(business);
        }

        return Optional.empty();
    }

    void validateBusiness(Business utilityProviderBusiness, ExecutionResult executionResult) {
        if(utilityProviderBusiness == null) {
            executionResult.addValidationAdvice(ValidationAdvice.newInstance("Failed to find the Water Utility Business provided for this agreement."));
        }
    }

    void validateServiceAddress(Address serviceAddress, ExecutionResult executionResult) {
        if(serviceAddress == null) {
            executionResult.addValidationAdvice(ValidationAdvice.newInstance("Failed to find the Service Address provided for agreement."));
        }
    }

    void enrichAgreement(Optional<UserRecord> userRecord, Optional<Property> property, Optional<Business> agreementBusiness, Optional<Community> community, EmbeddedAgreement embeddedAgreement, ExecutionResult executionResult) {
        if(userRecord.isPresent()) {
            // We expect a specific Property to be specified here for this kind of agreement
            if(property.isPresent()) {
                LOGGER.info("Creating a new WaterServiceAgreement for User[{}] on Property[{}]", userRecord.get().getEmbeddedContactInfo().getEmail(), property.get().getAddress().getPrintableAddress());
                embeddedAgreement.setAgreementUserRecord(userRecord.get());
                embeddedAgreement.setAgreementProperty(property.get());
            } else {
                executionResult.addValidationAdvice(ValidationAdvice.newInstance("Failed to find the Service Address provided for agreement."));
            }
        } else {
            if(agreementBusiness.isPresent()) {
                LOGGER.info("Creating a new WaterServiceAgreement for Business[{}] ", agreementBusiness.get().getName());
                embeddedAgreement.setAgreementBusiness(agreementBusiness.get());
            }
            if(community.isPresent()) {
                LOGGER.info("Creating a new WaterServiceAgreement for Community[{}] ", community.get().getCommunityName());
                embeddedAgreement.setAgreementCommunity(community.get());
            }
        }
    }

}
