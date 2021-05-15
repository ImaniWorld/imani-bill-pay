package com.imani.bill.pay.xecservice.billing;

import com.imani.bill.pay.domain.billing.ImaniBill;
import com.imani.bill.pay.domain.billing.ImaniBillExplained;
import com.imani.bill.pay.domain.billing.repository.IImaniBillRepository;
import com.imani.bill.pay.domain.execution.ExecutionResult;
import com.imani.bill.pay.domain.utility.SewerServiceAgreement;
import com.imani.bill.pay.service.util.DateTimeUtil;
import com.imani.bill.pay.service.util.IDateTimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Optional;

/**
 * @author manyce400
 */
@Service(SewerImaniBillExplainExecService.SPRING_BEAN)
public class SewerImaniBillExplainExecService implements IImaniBillExplainExecService<SewerServiceAgreement> {


    @Autowired
    private IImaniBillRepository imaniBillRepository;

    @Autowired
    @Qualifier(DateTimeUtil.SPRING_BEAN)
    private IDateTimeUtil iDateTimeUtil;

    public static final String SPRING_BEAN = "com.imani.bill.pay.xecservice.billing.SewerImaniBillExplainExecService";

    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(SewerImaniBillExplainExecService.class);


    @Override
    public void explainImaniBill(Long imaniBillID, ExecutionResult<ImaniBillExplained> executionResult) {
        Assert.notNull(imaniBillID, "ImaniBillID cannot be null");
        Assert.notNull(executionResult, "ExecutionResult cannot be null");

        LOGGER.info("Call #explainImaniBill() validation errors found=> [{}]", executionResult.hasValidationAdvice());

        if(!executionResult.hasValidationAdvice()
                && !executionResult.hasExecutionError()) {
            Optional<ImaniBill> imaniBill = imaniBillRepository.findById(imaniBillID);
            ImaniBillExplained imaniBillExplained = imaniBill.get().toImaniBillExplained();
            executionResult.setResult(imaniBillExplained);
        }
    }

}
