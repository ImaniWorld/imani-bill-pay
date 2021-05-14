package com.imani.bill.pay.service.utility;

import com.imani.bill.pay.domain.agreement.AgreementToScheduleBillPayFee;
import com.imani.bill.pay.domain.agreement.EmbeddedAgreement;
import com.imani.bill.pay.domain.billing.BillPayFee;
import com.imani.bill.pay.domain.business.Business;
import com.imani.bill.pay.domain.business.repository.IBusinessRepository;
import com.imani.bill.pay.domain.execution.ExecutionResult;
import com.imani.bill.pay.domain.geographical.Community;
import com.imani.bill.pay.domain.geographical.repository.ICommunityRepository;
import com.imani.bill.pay.domain.property.Property;
import com.imani.bill.pay.domain.property.repository.IPropertyRepository;
import com.imani.bill.pay.domain.user.UserRecord;
import com.imani.bill.pay.domain.user.repository.IUserRecordRepository;
import com.imani.bill.pay.domain.utility.EmbeddedUtilityService;
import com.imani.bill.pay.domain.utility.SewerServiceAgreement;
import com.imani.bill.pay.domain.utility.repository.ISewerServiceAgreementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Optional;

/**
 * @author manyce400
 */
@Service(SewerSvcAgreementService.SPRING_BEAN)
public class SewerSvcAgreementService implements ISewerSvcAgreementService {


    @Autowired
    private IUserRecordRepository iUserRecordRepository;

    @Autowired
    private IBusinessRepository iBusinessRepository;

    @Autowired
    private IPropertyRepository iPropertyRepository;

    @Autowired
    private ICommunityRepository iCommunityRepository;

    @Autowired
    private ISewerServiceAgreementRepository iSewerServiceAgreementRepository;


    public static final String SPRING_BEAN = "com.imani.bill.pay.service.utility.SewerSvcAgreementService";

    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(SewerSvcAgreementService.class);


    @Override
    public void createAgreement(SewerServiceAgreement sewerServiceAgreement, ExecutionResult executionResult, List<BillPayFee> billPayFees) {
        Assert.notNull(sewerServiceAgreement, "SewerServiceAgreement cannot be null");
        Assert.isNull(sewerServiceAgreement.getId(), "SewerServiceAgreement is already persisted");

        // Fetch persisted pieces to make sure that they are all valid
        Business utilityProviderBusiness = iBusinessRepository.getOne(sewerServiceAgreement.getEmbeddedUtilityService().getUtilityProviderBusiness().getId());

        // Validate to make sure all good to proceed
        validateBusiness(utilityProviderBusiness, executionResult);

        if(!executionResult.hasValidationAdvice()) {
            // Try to associate this water service agreement with a user, property, business or community
            Optional<UserRecord> userRecord = findSvcUserRecord(sewerServiceAgreement.getEmbeddedAgreement());
            Optional<Property> property = findSvcProperty(sewerServiceAgreement.getEmbeddedAgreement());
            Optional<Business> agreementBusiness = findSvcBusiness(sewerServiceAgreement.getEmbeddedAgreement());
            Optional<Community> community = findSvcCommunity(sewerServiceAgreement.getEmbeddedAgreement());

            EmbeddedAgreement embeddedAgreement = EmbeddedAgreement.builder()
                    .agreementInForce(true)
                    .billScheduleTypeE(sewerServiceAgreement.getEmbeddedAgreement().getBillScheduleTypeE())
                    .fixedCost(sewerServiceAgreement.getEmbeddedAgreement().getFixedCost())
                    .numberOfDaysTillLate(sewerServiceAgreement.getEmbeddedAgreement().getNumberOfDaysTillLate())
                    .effectiveDate(sewerServiceAgreement.getEmbeddedAgreement().getEffectiveDate())
                    .terminationDate(sewerServiceAgreement.getEmbeddedAgreement().getTerminationDate())
                    .build();

            enrichAgreement(userRecord, property, agreementBusiness, community, embeddedAgreement, executionResult);

            if (!executionResult.hasValidationAdvice()) {
                EmbeddedUtilityService embeddedUtilityService = EmbeddedUtilityService.builder()
                        .utilityProviderBusiness(utilityProviderBusiness)
                        .svcCustomerAcctID(sewerServiceAgreement.getEmbeddedUtilityService().getSvcCustomerAcctID())
                        .svcDescription(sewerServiceAgreement.getEmbeddedUtilityService().getSvcDescription())
                        .build();

                SewerServiceAgreement newSewerServiceAgreementt = SewerServiceAgreement.builder()
                        .embeddedAgreement(embeddedAgreement)
                        .embeddedUtilityService(embeddedUtilityService)
                        .build();

                // Apply all the late fees
                for(BillPayFee billPayFee : billPayFees) {
                    AgreementToScheduleBillPayFee agreementToScheduleBillPayFee = AgreementToScheduleBillPayFee.builder()
                            .sewerServiceAgreement(newSewerServiceAgreementt)
                            .billPayFee(billPayFee)
                            .enforced(true)
                            .build();
                    newSewerServiceAgreementt.addAgreementToScheduleBillPayFee(agreementToScheduleBillPayFee);
                }

                iSewerServiceAgreementRepository.save(newSewerServiceAgreementt);
            }
        }
    }


    Optional<UserRecord> findSvcUserRecord(EmbeddedAgreement embeddedAgreement) {
        if (embeddedAgreement.getAgreementUserRecord() != null) {
            UserRecord userRecord = iUserRecordRepository.findByUserEmail(embeddedAgreement.getAgreementUserRecord().getEmbeddedContactInfo().getEmail());
            return Optional.of(userRecord);
        }

        return Optional.empty();
    }

    Optional<Property> findSvcProperty(EmbeddedAgreement embeddedAgreement) {
        if (embeddedAgreement.getAgreementProperty() != null) {
            Property property = iPropertyRepository.getOne(embeddedAgreement.getAgreementProperty().getId());
            return Optional.of(property);
        }

        return Optional.empty();
    }

    Optional<Community> findSvcCommunity(EmbeddedAgreement embeddedAgreement) {
        if (embeddedAgreement.getAgreementCommunity() != null) {
            Community community = iCommunityRepository.getOne(embeddedAgreement.getAgreementCommunity().getId());
            return Optional.of(community);
        }

        return Optional.empty();
    }

    Optional<Business> findSvcBusiness(EmbeddedAgreement embeddedAgreement) {
        if (embeddedAgreement.getAgreementBusiness() != null) {
            Business business = iBusinessRepository.getOne((embeddedAgreement.getAgreementBusiness().getId()));
            return Optional.of(business);
        }

        return Optional.empty();
    }

}