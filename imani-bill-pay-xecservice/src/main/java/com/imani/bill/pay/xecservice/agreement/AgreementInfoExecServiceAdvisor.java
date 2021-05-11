package com.imani.bill.pay.xecservice.agreement;

import com.imani.bill.pay.domain.execution.ExecutionResult;
import com.imani.bill.pay.domain.execution.ValidationAdvice;
import com.imani.bill.pay.domain.geographical.Community;
import com.imani.bill.pay.domain.geographical.repository.ICommunityRepository;
import com.imani.bill.pay.domain.user.UserRecordLite;
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
public class AgreementInfoExecServiceAdvisor {



    @Autowired
    private ICommunityRepository iCommunityRepository;

    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(AgreementInfoExecServiceAdvisor.class);

    /**
     * Executes a Before advice before {@linkplain com.imani.bill.pay.xecservice.agreement.AgreementInfoExecService#findCommunityPropertiesWaterAgreements(Long, ExecutionResult)}   }
     * This Advice will validate that the community id passed as argument is valid and exists.
     */
    @Before(value = "execution(* com.imani.bill.pay.xecservice.agreement.AgreementInfoExecService.findCommunityPropertiesWaterAgreements(..)) and args(communityID, executionResult)")
    public void beforeAdvice(JoinPoint joinPoint, Long communityID, ExecutionResult<List<UserRecordLite>> executionResult) {
        LOGGER.debug("Executing beforeAdvice AgreementInfoExecServiceAdvisor#findCommunityPropertiesWaterAgreements ...");

        Optional<Community> community = iCommunityRepository.findById(communityID);
        if(!community.isPresent()) {
            StringBuffer sb = new StringBuffer("No valid Community found with ID[")
                    .append(communityID)
                    .append("]");
            executionResult.addValidationAdvice(ValidationAdvice.newInstance(sb.toString()));
        }
    }

}
