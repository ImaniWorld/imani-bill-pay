package com.imani.bill.pay.service.utility;

import com.imani.bill.pay.domain.agreement.EmbeddedAgreement;
import com.imani.bill.pay.domain.billing.BillScheduleTypeE;
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
import com.imani.bill.pay.domain.utility.WaterUtilization;
import com.imani.bill.pay.domain.utility.repository.IWaterServiceAgreementRepository;
import com.imani.bill.pay.domain.utility.repository.IWaterUtilizationRepository;
import com.imani.bill.pay.service.util.DateTimeUtil;
import com.imani.bill.pay.service.util.IDateTimeUtil;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.transaction.Transactional;
import java.util.List;

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

    @Autowired
    private IWaterUtilizationRepository iWaterUtilizationRepository;

    @Autowired
    @Qualifier(DateTimeUtil.SPRING_BEAN)
    private IDateTimeUtil iDateTimeUtil;

    public static final String SPRING_BEAN = "com.imani.bill.pay.service.utility.WaterSvcAgreementService";

    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(WaterSvcAgreementService.class);


    @Override
    public double computeWaterChargeOnQuarterlyUtilization(WaterServiceAgreement waterServiceAgreement) {
        Assert.notNull(waterServiceAgreement, "WaterServiceAgreement cannot be null");

        BillScheduleTypeE billScheduleTypeE = waterServiceAgreement.getEmbeddedAgreement().getBillScheduleTypeE();

        DateTime atStartOfQuarter = iDateTimeUtil.getDateTimeAStartOfCurrentQuarter();
        DateTime atEndOfQuarter = iDateTimeUtil.getDateTimeAEndOfCurrentQuarter();
        LOGGER.info("Attempting to compute water utilization charge for service Period[{}] with DateRange[{}]", billScheduleTypeE, atStartOfQuarter, atEndOfQuarter);

        // Compute actual utilization cost based on total number of gallons used and charge per 1,000 gallons on agreement
        long totalGallonsUsed = 0;
        double fixCostPer1000Galls = waterServiceAgreement.getEmbeddedAgreement().getFixedCost(); // Per agreement this will be the (fixed cost/1000 gallons)
        List<WaterUtilization> waterUtilizations = iWaterUtilizationRepository.findUtilizationInPeriod(waterServiceAgreement, atStartOfQuarter, atEndOfQuarter);
        for(WaterUtilization waterUtilization : waterUtilizations) {
            totalGallonsUsed = totalGallonsUsed + waterUtilization.getNumberOfGallonsUsed();
        }

        if(totalGallonsUsed > 0) {
            double waterChargeOnUtilization = (totalGallonsUsed * fixCostPer1000Galls) / 1000;
            LOGGER.info("Computed water charge of: {} on totalGallonsUsed: {} ", waterChargeOnUtilization, totalGallonsUsed);
            return waterChargeOnUtilization;
        }

        return 0;
    }

    @Transactional
    @Override
    public void createAgreement(WaterServiceAgreement waterServiceAgreement, ExecutionResult executionResult) {
        Assert.notNull(waterServiceAgreement, "WaterServiceAgreement cannot be null");
        Assert.isNull(waterServiceAgreement.getId(), "WaterServiceAgreement is already persisted");

        // Fetch persisted pieces to make sure that they are all valid
        Business business = iBusinessRepository.getOne(waterServiceAgreement.getEmbeddedUtilityService().getUtilityProvider().getId());
        Address serviceAddress = iAddressRepository.getOne(waterServiceAgreement.getEmbeddedUtilityService().getSvcCustomerAddress().getId());
        UserRecord userRecord = iUserRecordRepository.findByUserEmail(waterServiceAgreement.getEmbeddedAgreement().getUserRecord().getEmbeddedContactInfo().getEmail());

        // Validate to make sure all good to proceed
        validateBusiness(business, executionResult);
        validateServiceAddress(serviceAddress, executionResult);
        validateUserRecord(userRecord, executionResult);

        if(!executionResult.hasValidationAdvice()) {
            LOGGER.info("Creating and save new WaterServiceAgreement between User[{}] and Business[{}]", userRecord.getEmbeddedContactInfo().getEmail(), business.getName());

            // Lookup the Community if it has been passed
            Community community = null;
            if (waterServiceAgreement.getEmbeddedUtilityService().getSvcCustomerCommunity() != null) {
                community = iCommunityRepository.getOne(waterServiceAgreement.getEmbeddedUtilityService().getSvcCustomerCommunity().getId());
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
//                    embeddedAgreement.business(business)
//                    .serviceAddress(serviceAddress)
//                    .community(community)
//                    .businessCustomerAcctID(waterServiceAgreement.getBusinessCustomerAcctID())
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
