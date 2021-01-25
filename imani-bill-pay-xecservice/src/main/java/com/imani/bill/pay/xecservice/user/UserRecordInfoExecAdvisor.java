package com.imani.bill.pay.xecservice.user;

import com.imani.bill.pay.domain.business.Business;
import com.imani.bill.pay.domain.business.repository.IBusinessRepository;
import com.imani.bill.pay.domain.execution.ExecutionResult;
import com.imani.bill.pay.domain.execution.ValidationAdvice;
import com.imani.bill.pay.domain.user.UserRecordLite;
import com.imani.bill.pay.domain.user.UserRecordTypeE;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * Uses AspectJ to verify all request to execute UserRecord Information
 *
 * @author manyce400
 */
@Aspect
@Component
public class UserRecordInfoExecAdvisor {


    @Autowired
    private IBusinessRepository iBusinessRepository;

    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(UserRecordInfoExecAdvisor.class);

    /**
     * Executes a Before advice before {@linkplain com.imani.bill.pay.xecservice.user.UserRecordInfoExecService#findUsersWithBusinessAffiliation(Long, String, ExecutionResult)}  }
     * This Advice will validate that the Business passed as argument is valid as well as UserRecordTypeE.
     */
    @Before(value = "execution(* com.imani.bill.pay.xecservice.user.UserRecordInfoExecService.findUsersWithBusinessAffiliation(..)) and args(businessID, userType, executionResult)")
    public void beforeAdvice(JoinPoint joinPoint, Long businessID, String userType, ExecutionResult<List<UserRecordLite>> executionResult) {
        LOGGER.debug("Executing beforeAdvice UserRecordInfoExecService#findUsersWithBusinessAffiliation ...");

        // Validate the business
        Optional<Business> business = iBusinessRepository.findById(businessID);
        if(!business.isPresent()) {
            StringBuffer sb = new StringBuffer("No valid Business found with ID[")
                    .append(businessID)
                    .append("]");
            executionResult.addValidationAdvice(ValidationAdvice.newInstance(sb.toString()));
        }

        // Validate the userRecordTypeE
        Optional<UserRecordTypeE> userRecordTypeE = UserRecordTypeE.findByType(userType);
        if(!userRecordTypeE.isPresent()) {
            StringBuffer sb = new StringBuffer("No valid UserRecordTypeE found [")
                    .append(userType)
                    .append("]");
            executionResult.addValidationAdvice(ValidationAdvice.newInstance(sb.toString()));
        }
    }

}
