package com.imani.bill.pay.xecservice.utility;

import com.imani.bill.pay.domain.agreement.EmbeddedAgreement;
import com.imani.bill.pay.domain.billing.BillPayFee;
import com.imani.bill.pay.domain.business.Business;
import com.imani.bill.pay.domain.contact.Address;
import com.imani.bill.pay.domain.execution.ExecutionResult;
import com.imani.bill.pay.domain.execution.ValidationAdvice;
import com.imani.bill.pay.domain.utility.UtilityServiceArea;
import com.imani.bill.pay.domain.utility.WaterServiceAgreement;
import com.imani.bill.pay.domain.utility.WaterUtilization;
import com.imani.bill.pay.domain.utility.WaterUtilizationLite;
import com.imani.bill.pay.domain.utility.repository.IWaterServiceAgreementRepository;
import com.imani.bill.pay.domain.utility.repository.IWaterUtilizationRepository;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * @author manyce400
 */
@Aspect
@Component
public class WaterSvcAgreementExecServiceAdvisor {


    @Autowired
    private IWaterUtilizationRepository iWaterUtilizationRepository;

    @Autowired
    private IWaterServiceAgreementRepository iWaterServiceAgreementRepository;

    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(WaterSvcAgreementExecServiceAdvisor.class);

    @Before(value = "execution(* com.imani.bill.pay.xecservice.utility.WaterSvcAgreementExecService.processWaterSvcAgreement(..)) and args(executionResult, billPayFees)")
    public void beforeAdvice(JoinPoint joinPoint, ExecutionResult<WaterServiceAgreement> executionResult, List<BillPayFee> billPayFees) {
        LOGGER.info("Executing AOP advice on new WaterServiceAgreement creation request");

        // Validate the billpay fees
        validateBillPayFees(billPayFees, executionResult);

        // Get agreement and perform basic validations on the data that we have been given
        WaterServiceAgreement waterServiceAgreement = executionResult.getResult().get();
        validateUtilityServiceProvider(waterServiceAgreement.getEmbeddedUtilityService().getUtilityProviderBusiness(), executionResult);
        validateEmbeddedAgreement(waterServiceAgreement.getEmbeddedAgreement(), waterServiceAgreement.getEmbeddedUtilityService().getUtilityServiceArea(), executionResult);
    }

    @Before(value = "execution(* com.imani.bill.pay.xecservice.utility.WaterSvcAgreementExecService.processWaterUtilization(..)) and args(waterUtilizationLite, executionResult)")
    public void beforeAdvice(JoinPoint joinPoint, WaterUtilizationLite waterUtilizationLite, ExecutionResult<WaterUtilizationLite> executionResult) {
        LOGGER.info("Executing AOP advice on new water utilization");

        // Verify that there is an existing WaterServiceAgreement for this new utilization reques
        Optional<WaterServiceAgreement> waterServiceAgreement = iWaterServiceAgreementRepository.findById(waterUtilizationLite.getWaterServiceAgreementLite().getId());
        if(!waterServiceAgreement.isPresent()) {
            StringBuffer sb = new StringBuffer("No WaterServiceAgeement found for => ID[")
                    .append(waterUtilizationLite.getWaterServiceAgreementLite().getId())
                    .append("]");
            executionResult.addValidationAdvice(ValidationAdvice.newInstance(sb.toString()));
        }

        // Verify that a utilization hasn't already been logged for the specified utilization date.
        Optional<WaterUtilization> existingUtilization = iWaterUtilizationRepository.findUtilizationByDate(waterServiceAgreement.get(), waterUtilizationLite.getUtilizationDate());
        if(existingUtilization.isPresent()) {
            StringBuffer sb = new StringBuffer("An existing water utilization found [Utilization: ")
                    .append(existingUtilization.get().getNumberOfGallonsUsed()).append("gallons")
                    .append(" | Date: ").append(existingUtilization.get().getUtilizationDate()).append("]");
            executionResult.addValidationAdvice(ValidationAdvice.newInstance(sb.toString()));
        }

        if(waterUtilizationLite.getWaterServiceAgreementLite() == null
                || waterUtilizationLite.getWaterServiceAgreementLite().getId() == null) {
            executionResult.addValidationAdvice(ValidationAdvice.newInstance("WaterUtilization is missing a WaterServiceAgreement"));
        }

        if(waterUtilizationLite.getNumberOfGallonsUsed() == null) {
            executionResult.addValidationAdvice(ValidationAdvice.newInstance("WaterUtilization is missing number of gallons used"));
        }

        if(waterUtilizationLite.getUtilizationDate() == null) {
            executionResult.addValidationAdvice(ValidationAdvice.newInstance("WaterUtilization is missing a utilization date"));
        }
    }

    void validateEmbeddedAgreement(EmbeddedAgreement embeddedAgreement, UtilityServiceArea utilityServiceArea, ExecutionResult<WaterServiceAgreement> executionResult) {
        if(embeddedAgreement == null) {
            executionResult.addValidationAdvice(ValidationAdvice.newInstance("EmbeddedAgreement with details is required."));
        } else {
            if(embeddedAgreement.getBillScheduleTypeE() == null) {
                executionResult.addValidationAdvice(ValidationAdvice.newInstance("EmbeddedAgreement is missing a BillScheduleType"));
            }
            if(embeddedAgreement.getFixedCost() == null) {
                executionResult.addValidationAdvice(ValidationAdvice.newInstance("EmbeddedAgreement is missing a Fixed Cost"));
            }
            if(embeddedAgreement.getNumberOfDaysTillLate() == null) {
                executionResult.addValidationAdvice(ValidationAdvice.newInstance("EmbeddedAgreement is missing number of days till a late payment is applied"));
            }
            if(embeddedAgreement.getEffectiveDate() == null) {
                executionResult.addValidationAdvice(ValidationAdvice.newInstance("EmbeddedAgreement is missing Effective Date."));
            }
            if(embeddedAgreement.getAgreementUserRecord() == null
                    && embeddedAgreement.getAgreementBusiness() == null
                    && embeddedAgreement.getAgreementProperty() == null
                    && embeddedAgreement.getAgreementCommunity() == null
                    && utilityServiceArea == null) {
                executionResult.addValidationAdvice(ValidationAdvice.newInstance("EmbeddedAgreement is missing details on Agreement-Entity[User, Business, Property, Community, UtilityServiceArea]"));
            }
        }
    }

    void validateUtilityServiceProvider(Business business, ExecutionResult<WaterServiceAgreement> executionResult) {
        if(business == null || business.getId() == null) {
            executionResult.addValidationAdvice(ValidationAdvice.newInstance("Valid Utility Business is required for agreement."));
        }
    }

    void validateServiceAddress(Address address, ExecutionResult<WaterServiceAgreement> executionResult) {
        if(address == null || address.getId() == null) {
            executionResult.addValidationAdvice(ValidationAdvice.newInstance("Valid Service Address for Water Svc Agreement is missing."));
        }
    }

    void validateBillPayFees(List<BillPayFee> billPayFees, ExecutionResult<WaterServiceAgreement> executionResult) {
        if(billPayFees == null || billPayFees.size() == 0) {
            executionResult.addValidationAdvice(ValidationAdvice.newInstance("Water Svc Agreement is missing required bill pay fees."));
        }
    }

}