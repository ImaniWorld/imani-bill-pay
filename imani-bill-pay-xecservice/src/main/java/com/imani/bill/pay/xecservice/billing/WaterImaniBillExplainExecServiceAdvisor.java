package com.imani.bill.pay.xecservice.billing;

import com.imani.bill.pay.domain.billing.ImaniBill;
import com.imani.bill.pay.domain.billing.ImaniBillExplained;
import com.imani.bill.pay.domain.billing.repository.IImaniBillRepository;
import com.imani.bill.pay.domain.execution.ExecutionResult;
import com.imani.bill.pay.domain.execution.ValidationAdvice;
import com.imani.bill.pay.domain.utility.WaterServiceAgreement;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @author manyce400
 */
@Aspect
@Component
public class WaterImaniBillExplainExecServiceAdvisor {



    @Autowired
    private IImaniBillRepository imaniBillRepository;


    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(WaterImaniBillExplainExecServiceAdvisor.class);


    /**
     * Executes a Before advice before {@linkplain com.imani.bill.pay.xecservice.billing.WaterImaniBillExplainExecService#explainImaniBill(Long, ExecutionResult)}    }
     * This Advice will validate that the community id passed as argument is valid and exists.
     */
    @Before(value = "execution(* com.imani.bill.pay.xecservice.billing.WaterImaniBillExplainExecService.explainImaniBill(..)) and args(imaniBillID, executionResult)")
    public void beforeAdvice(JoinPoint joinPoint, Long imaniBillID, ExecutionResult<ImaniBillExplained> executionResult) {
        LOGGER.debug("Executing beforeAdvice WaterImaniBillExplainExecServiceAdvisor#explainImaniBill() ...");

        Optional<ImaniBill> imaniBill = imaniBillRepository.findById(imaniBillID);
        WaterServiceAgreement waterServiceAgreement = imaniBill.get().getWaterServiceAgreement();

        if(!imaniBill.isPresent()) {
            StringBuffer sb = new StringBuffer("No valid ImaniBill found with ID[")
                    .append(imaniBillID)
                    .append("]");
            executionResult.addValidationAdvice(ValidationAdvice.newInstance(sb.toString()));
        }

        if(waterServiceAgreement == null) {
            StringBuffer sb = new StringBuffer("ImaniBill found with ID[")
                    .append(imaniBillID)
                    .append("] has no attached WaterService Agreement.");
            executionResult.addValidationAdvice(ValidationAdvice.newInstance(sb.toString()));
        }
    }

}
