package com.imani.bill.pay.domain.payment.stripe;

import com.imani.bill.pay.domain.property.PropertyManager;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Models all fields required from ImaniBillPay to interact with Stripe connected Account object.
 *
 * See Stripe Account Object fields reference:  https://stripe.com/docs/api/accounts/create
 * @author manyce400
 */
public enum AccountObjFieldsE {


    id,

    type, // can only be custom for custom accounts

    country,

    email,

    name,

    business_type,

    phone,

    company,

    requested_capabilities
    ;


    public static Map<String, Object> getAccountCreateParams(PropertyManager propertyManager) {
        Assert.notNull(propertyManager, "PropertyManager cannot be null");

        // Set requested capabilities
        List<Object> requestedCapabilities = new ArrayList<>();
        requestedCapabilities.add("card_payments"); // accept card payments
        requestedCapabilities.add("transfers"); // transfers allows for platform payments

        Map<String, Object> params = new HashMap<>();
        params.put(type.name(), "custom");
        params.put(email.name(), propertyManager.getEmbeddedContactInfo().getEmail());
        params.put(country.name(), "US");
        params.put(business_type.name(), StripeAcctHolderTypeE.Company.getHolderType());
        params.put(requested_capabilities.name(), requestedCapabilities);

        // Add Company details for this property manager
        Map<String, Object> companyParams = CompanyObjFieldsE.getCompanyCreateParams(propertyManager);
        params.put(company.name(), companyParams);
        return params;
    }




}
