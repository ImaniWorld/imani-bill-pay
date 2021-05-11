package com.imani.bill.pay.xecservice.billing;

import com.imani.bill.pay.domain.billing.ImaniBill;
import com.imani.bill.pay.domain.billing.ImaniBillExplained;
import com.imani.bill.pay.domain.execution.ExecutionResult;

import java.util.Optional;

/**
 * @author manyce400
 */
public interface IImaniBillExplanationService<O> {

    public ExecutionResult<ImaniBillExplained> getCurrentBillExplanation(O explanationTarget);

    // Default method that invokes functionality to create an ImanBillExplained object
    public default Optional<ImaniBillExplained> explainImaniBill(Optional<ImaniBill> imaniBill) {
        if(imaniBill.isPresent()) {
            ImaniBillExplained imaniBillExplained = imaniBill.get().toImaniBillExplained();
            return Optional.of(imaniBillExplained);
        }

        return Optional.empty();
    }

}
