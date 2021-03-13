package com.imani.bill.pay.service.utility;

import com.imani.bill.pay.domain.agreement.AgreementToScheduleBillPayFee;
import com.imani.bill.pay.domain.agreement.EmbeddedAgreement;
import com.imani.bill.pay.domain.agreement.repository.IAgreementToBillPayFeeRepository;
import com.imani.bill.pay.domain.billing.BillPayFee;
import com.imani.bill.pay.domain.billing.FeeTypeE;
import com.imani.bill.pay.domain.business.Business;
import com.imani.bill.pay.domain.business.repository.IBusinessRepository;
import com.imani.bill.pay.domain.execution.ExecutionResult;
import com.imani.bill.pay.domain.execution.ValidationAdvice;
import com.imani.bill.pay.domain.geographical.Community;
import com.imani.bill.pay.domain.geographical.repository.ICommunityRepository;
import com.imani.bill.pay.domain.property.Property;
import com.imani.bill.pay.domain.property.repository.IPropertyRepository;
import com.imani.bill.pay.domain.user.UserRecord;
import com.imani.bill.pay.domain.user.repository.IUserRecordRepository;
import com.imani.bill.pay.domain.utility.EmbeddedUtilityService;
import com.imani.bill.pay.domain.utility.UtilityServiceArea;
import com.imani.bill.pay.domain.utility.WaterServiceAgreement;
import com.imani.bill.pay.domain.utility.repository.IUtilityServiceAreaRepository;
import com.imani.bill.pay.domain.utility.repository.IWaterServiceAgreementRepository;
import com.imani.bill.pay.domain.utility.repository.IWaterUtilizationRepository;
import com.imani.bill.pay.service.util.DateTimeUtil;
import com.imani.bill.pay.service.util.IDateTimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.transaction.Transactional;
import java.util.List;
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
    private IUtilityServiceAreaRepository iUtilityServiceAreaRepository;

    @Autowired
    private IWaterServiceAgreementRepository iWaterServiceAgreementRepository;

    @Autowired
    private IWaterUtilizationRepository iWaterUtilizationRepository;

    @Autowired
    private IAgreementToBillPayFeeRepository iAgreementToBillPayFeeRepository;

    @Autowired
    @Qualifier(DateTimeUtil.SPRING_BEAN)
    private IDateTimeUtil iDateTimeUtil;

    public static final String SPRING_BEAN = "com.imani.bill.pay.service.utility.WaterSvcAgreementService";

    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(WaterSvcAgreementService.class);


    @Transactional
    @Override
    public void createAgreement(ExecutionResult<WaterServiceAgreement> executionResult, List<BillPayFee> billPayFees) {
        Assert.notNull(executionResult, "ExecutionResult cannot be null");
        Assert.notNull(executionResult.getResult().get(), "WaterServiceAgreement cannot be null");
        Assert.isNull(executionResult.getResult().get().getId(), "WaterServiceAgreement is already persisted");

        WaterServiceAgreement waterServiceAgreement = executionResult.getResult().get();
        LOGGER.info("Creating new WaterServiceAgreement with billPayFees => {}", waterServiceAgreement.describeAgreement());

        // Fetch persisted pieces to make sure that they are all valid
        Business utilityProviderBusiness = iBusinessRepository.getOne(waterServiceAgreement.getEmbeddedUtilityService().getUtilityProviderBusiness().getId());


        // Execute validations before proceeding
        validateBusiness(utilityProviderBusiness, executionResult);
        validateBillPayFees(billPayFees, executionResult);

        if(!executionResult.hasValidationAdvice()) {
            // Try to associate this water service agreement with a user, property, business or community
            Optional<UserRecord> userRecord = findWaterSvcUserRecord(waterServiceAgreement);
            Optional<Property> property = findWaterSvcProperty(waterServiceAgreement);
            Optional<Business> agreementBusiness = findWaterSvcBusiness(waterServiceAgreement);
            Optional<Community> community = findWaterSvcCommunity(waterServiceAgreement);
            Optional<UtilityServiceArea> utilityServiceArea = findWaterSvcUtilityServiceArea(waterServiceAgreement);

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
                enrichUtilityService(embeddedUtilityService, utilityServiceArea);

                WaterServiceAgreement newWaterServiceAgreement = WaterServiceAgreement.builder()
                        .embeddedAgreement(embeddedAgreement)
                        .embeddedUtilityService(embeddedUtilityService)
                        .nbrOfGallonsPerFixedCost(waterServiceAgreement.getNbrOfGallonsPerFixedCost())
                        .build();

                // Apply all the late fees
                for(BillPayFee billPayFee : billPayFees) {
                    AgreementToScheduleBillPayFee agreementToScheduleBillPayFee = AgreementToScheduleBillPayFee.builder()
                            .waterServiceAgreement(newWaterServiceAgreement)
                            .billPayFee(billPayFee)
                            .enforced(true)
                            .build();
                    newWaterServiceAgreement.addAgreementToScheduleBillPayFee(agreementToScheduleBillPayFee);
                }

                iWaterServiceAgreementRepository.save(newWaterServiceAgreement);
            }
        }
    }

    void validateBillPayFees(List<BillPayFee> billPayFees, ExecutionResult<WaterServiceAgreement> executionResult) {
        LOGGER.info("Validating bill pay fee's to be applied on agreement.  Only scheduled fees allowed");

        for(BillPayFee billPayFee : billPayFees) {
            if(FeeTypeE.Scheduled_Fee != billPayFee.getFeeTypeE()) {
                executionResult.addValidationAdvice(ValidationAdvice.newInstance("Only Scheduled fee's can be applied to an agreement"));
                break;
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

    Optional<UtilityServiceArea> findWaterSvcUtilityServiceArea(WaterServiceAgreement waterServiceAgreement) {
        if (waterServiceAgreement.getEmbeddedUtilityService().getUtilityServiceArea() != null) {
            UtilityServiceArea utilityServiceArea = iUtilityServiceAreaRepository.getOne(waterServiceAgreement.getEmbeddedUtilityService().getUtilityServiceArea().getId());
            return Optional.of(utilityServiceArea);
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

}
