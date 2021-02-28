package com.imani.bill.pay.xecservice.utility;

import com.imani.bill.pay.domain.agreement.EmbeddedAgreement;
import com.imani.bill.pay.domain.business.Business;
import com.imani.bill.pay.domain.contact.Address;
import com.imani.bill.pay.domain.execution.ExecutionResult;
import com.imani.bill.pay.domain.execution.ValidationAdvice;
import com.imani.bill.pay.domain.utility.SewerServiceAgreement;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

/**
 * @author manyce400
 */
@Aspect
@Component
public class SewerSvcAgreementExecServiceAdvisor {



    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(SewerSvcAgreementExecServiceAdvisor.class);

    @Before(value = "execution(* com.imani.bill.pay.xecservice.utility.SewerSvcAgreementExecService.processSewerSvcAgreement(..)) and args(executionResult)")
    public void beforeAdvice(JoinPoint joinPoint, ExecutionResult<SewerServiceAgreement> executionResult) {
        LOGGER.debug("Executing advice on waterServiceAgreementExecutionResult => {}", executionResult);

        SewerServiceAgreement sewerServiceAgreement = executionResult.getResult().get();
        validateEmbeddedAgreement(sewerServiceAgreement.getEmbeddedAgreement(), executionResult);
        validateUtilityServiceProvider(sewerServiceAgreement.getEmbeddedUtilityService().getUtilityProviderBusiness(), executionResult);
    }

    void validateEmbeddedAgreement(EmbeddedAgreement embeddedAgreement, ExecutionResult<SewerServiceAgreement> executionResult) {
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
                    && embeddedAgreement.getAgreementCommunity() == null) {
                executionResult.addValidationAdvice(ValidationAdvice.newInstance("EmbeddedAgreement is missing details on Agreement-Entity[User, Business, Property, Community]"));
            }
        }
    }

    void validateUtilityServiceProvider(Business business, ExecutionResult<SewerServiceAgreement> executionResult) {
        if(business == null || business.getId() == null) {
            executionResult.addValidationAdvice(ValidationAdvice.newInstance("Valid Utility Business is required for agreement."));
        }
    }

    void validateServiceAddress(Address address, ExecutionResult<SewerServiceAgreement> executionResult) {
        if(address == null || address.getId() == null) {
            executionResult.addValidationAdvice(ValidationAdvice.newInstance("Valid Service Address for Water Svc Agreement is missing."));
        }
    }

}