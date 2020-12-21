package com.imani.bill.pay.service.billing;

import com.imani.bill.pay.domain.billing.ImaniBillExplained;
import com.imani.bill.pay.domain.execution.ExecutionResult;
import com.imani.bill.pay.domain.user.UserRecord;
import com.imani.bill.pay.domain.user.UserRecordLite;

import java.util.List;

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

}
