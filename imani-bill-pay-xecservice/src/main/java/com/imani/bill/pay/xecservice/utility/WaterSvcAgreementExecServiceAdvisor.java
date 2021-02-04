package com.imani.bill.pay.xecservice.utility;

import com.imani.bill.pay.domain.agreement.EmbeddedAgreement;
import com.imani.bill.pay.domain.business.Business;
import com.imani.bill.pay.domain.contact.Address;
import com.imani.bill.pay.domain.execution.ExecutionResult;
import com.imani.bill.pay.domain.execution.ValidationAdvice;
import com.imani.bill.pay.domain.utility.WaterServiceAgreement;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

/**
 * @author manyce400
 */
@Aspect
@Component
public class WaterSvcAgreementExecServiceAdvisor {



    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(WaterSvcAgreementExecServiceAdvisor.class);

    @Before(value = "execution(* com.imani.bill.pay.xecservice.utility.WaterSvcAgreementExecService.processWaterSvcAgreement(..)) and args(executionResult)")
    public void beforeAdvice(JoinPoint joinPoint, ExecutionResult<WaterServiceAgreement> executionResult) {
        LOGGER.debug("Executing advice on waterServiceAgreementExecutionResult => {}", executionResult);

        WaterServiceAgreement waterServiceAgreement = executionResult.getResult().get();
        validateEmbeddedAgreement(waterServiceAgreement.getEmbeddedAgreement(), executionResult);
        validateBusiness(waterServiceAgreement.getBusiness(), executionResult);
        validateServiceAddress(waterServiceAgreement.getServiceAddress(), executionResult);
    }

    void validateEmbeddedAgreement(EmbeddedAgreement embeddedAgreement, ExecutionResult<WaterServiceAgreement> executionResult) {
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
            if(embeddedAgreement.getUserRecord() == null
                    || embeddedAgreement.getUserRecord().getEmbeddedContactInfo() == null
                    || embeddedAgreement.getUserRecord().getEmbeddedContactInfo().getEmail() == null) {
                executionResult.addValidationAdvice(ValidationAdvice.newInstance("EmbeddedAgreement is missing details of User[email] responsible for agreement."));
            }
        }
    }

    void validateBusiness(Business business, ExecutionResult<WaterServiceAgreement> executionResult) {
        if(business == null || business.getId() == null) {
            executionResult.addValidationAdvice(ValidationAdvice.newInstance("Valid Utility Business is required for agreement."));
        }
    }

    void validateServiceAddress(Address address, ExecutionResult<WaterServiceAgreement> executionResult) {
        if(address == null || address.getId() == null) {
            executionResult.addValidationAdvice(ValidationAdvice.newInstance("Valid Service Address for Water Svc Agreement is missing."));
        }
    }

}