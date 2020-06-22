package com.imani.bill.pay.service.payment.plaid;

import com.imani.bill.pay.domain.execution.ExecutionResult;
import com.imani.bill.pay.domain.property.PropertyManager;
import com.imani.bill.pay.domain.user.UserRecord;

/**
 * @author manyce400
 */
public interface IPlaidAccountMasterService {


    public ExecutionResult linkPlaidBankAcct(String plaidPublicToken, String userID);

    /**
     * On successful retrieval of a Plaid public token for a UserRecord's bank account, this API will link that Plaid Account on our records for this user.
     *
     * @param plaidPublicToken Validated bank account token returned through Plaid's Link interface to allow loging and selecting a bank account on the web.
     * @param userRecord User for operation
     * @return ExecutionResult
     */
    public ExecutionResult linkPlaidBankAcct(String plaidPublicToken, UserRecord userRecord);

    /**
     * On successful retrieval of a Plaid public token for a Property manager's bank account, this API will link that Plaid Account on our records for this property manager.
     *
     * @param plaidPublicToken Validated bank account token returned through Plaid's Link interface to allow loging and selecting a bank account on the web.
     * @param propertyManager Property manager for operation
     * @return ExecutionResult
     */
    public ExecutionResult linkPlaidBankAcct(String plaidPublicToken, PropertyManager propertyManager);

    /**
     * Create a Linked Stripe Bank Account for this given Plaid account.  Plaid account should have already been linked with available access token
     * @param userRecord
     * @return ExecutionResult
     */
    public ExecutionResult createStripeAcctForPrimaryPlaidAcct(UserRecord userRecord);

}
