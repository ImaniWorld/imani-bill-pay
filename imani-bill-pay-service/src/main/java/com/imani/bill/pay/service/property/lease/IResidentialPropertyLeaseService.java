package com.imani.bill.pay.service.property.lease;

import com.imani.bill.pay.domain.execution.ExecutionResult;
import com.imani.bill.pay.domain.leasemanagement.PropertyLeaseAgreementLite;
import com.imani.bill.pay.domain.user.UserRecord;
import com.imani.bill.pay.domain.user.UserRecordLite;

/**
 * @author manyce400
 */
public interface IResidentialPropertyLeaseService {

    public ExecutionResult<PropertyLeaseAgreementLite> leaseProperty(UserRecord userRecord, PropertyLeaseAgreementLite propertyLeaseAgreementLite);

    public ExecutionResult<PropertyLeaseAgreementLite> leaseProperty(UserRecordLite userRecordLite, PropertyLeaseAgreementLite propertyLeaseAgreementLite);

}
