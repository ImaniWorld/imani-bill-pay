package com.imani.bill.pay.service.billing;

import com.imani.bill.pay.domain.billing.ImaniBill;
import com.imani.bill.pay.domain.billing.ImaniBillExplained;
import com.imani.bill.pay.domain.execution.ExecutionResult;
import com.imani.bill.pay.domain.user.UserRecord;
import com.imani.bill.pay.domain.user.UserRecordLite;

import java.util.List;
import java.util.Optional;

/**
 * @author manyce400
 */
public interface IBillExplanationService {

    // Get the explanation for the current and most recent user bill from this implementation
    public ExecutionResult<ImaniBillExplained> getCurrentBillExplanation(UserRecord userRecord);

    // Get the explanation for the current and most recent user bill from this implementation
    public ExecutionResult<ImaniBillExplained> getCurrentBillExplanation(UserRecordLite userRecordLite);

    // Get the explanation for the current and most recent user bill from this implementation
    public ExecutionResult<List<ImaniBillExplained>> getYTDBillsExplanation(UserRecord userRecord);

    // Get the explanation for the current and most recent user bill from this implementation
    public ExecutionResult<List<ImaniBillExplained>> getYTDBillsExplanation(UserRecordLite userRecordLite);


    public default Optional<ImaniBillExplained> explainImaniBill(Optional<ImaniBill> imaniBill) {
        ExecutionResult<ImaniBillExplained> executionResult = new ExecutionResult<>();

        if(imaniBill.isPresent()) {
            ImaniBillExplained imaniBillExplained = imaniBill.get().toImaniBillExplained();
            return Optional.of(imaniBillExplained);
        }

        return Optional.empty();
    }

}
