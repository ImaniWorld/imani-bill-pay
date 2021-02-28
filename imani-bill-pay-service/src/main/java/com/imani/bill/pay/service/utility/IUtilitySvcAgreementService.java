package com.imani.bill.pay.service.utility;

import com.imani.bill.pay.domain.agreement.EmbeddedAgreement;
import com.imani.bill.pay.domain.business.Business;
import com.imani.bill.pay.domain.contact.Address;
import com.imani.bill.pay.domain.execution.ExecutionResult;
import com.imani.bill.pay.domain.execution.ValidationAdvice;
import com.imani.bill.pay.domain.geographical.Community;
import com.imani.bill.pay.domain.property.Property;
import com.imani.bill.pay.domain.user.UserRecord;

import java.util.Optional;

/**
 * @author manyce400
 */
public interface IUtilitySvcAgreementService {

    default void validateBusiness(Business utilityProviderBusiness, ExecutionResult executionResult) {
        if(utilityProviderBusiness == null) {
            executionResult.addValidationAdvice(ValidationAdvice.newInstance("Failed to find the Water Utility Business provided for this agreement."));
        }
    }

    default void validateServiceAddress(Address serviceAddress, ExecutionResult executionResult) {
        if(serviceAddress == null) {
            executionResult.addValidationAdvice(ValidationAdvice.newInstance("Failed to find the Service Address provided for agreement."));
        }
    }

    default void enrichAgreement(Optional<UserRecord> userRecord, Optional<Property> property, Optional<Business> agreementBusiness, Optional<Community> community, EmbeddedAgreement embeddedAgreement, ExecutionResult executionResult) {
        if(userRecord.isPresent()) {
            // We expect a specific Property to be specified here for this kind of agreement
            if(property.isPresent()) {
                embeddedAgreement.setAgreementUserRecord(userRecord.get());
                embeddedAgreement.setAgreementProperty(property.get());
            } else {
                executionResult.addValidationAdvice(ValidationAdvice.newInstance("Failed to find the Service Address provided for agreement."));
            }
        } else {
            if(agreementBusiness.isPresent()) {
                embeddedAgreement.setAgreementBusiness(agreementBusiness.get());
            }
            if(community.isPresent()) {
                embeddedAgreement.setAgreementCommunity(community.get());
            }
        }
    }


}
