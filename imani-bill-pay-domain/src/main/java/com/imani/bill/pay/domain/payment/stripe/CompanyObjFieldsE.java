package com.imani.bill.pay.domain.payment.stripe;

import com.imani.bill.pay.domain.business.Business;

import java.util.HashMap;
import java.util.Map;

/**
 * Models all fields required from ImaniBillPay to interact with Stripe connected Account Company object.
 *
 * See Stripe Account Object fields reference:  https://stripe.com/docs/api/accounts/create
 *
 * @author manyce400
 */
public enum CompanyObjFieldsE {


    name,

    email,

    phone,

    tax_id,

    ;


    public static Map<String, Object> getCompanyCreateParams(Business business) {
        Map<String, Object> params = new HashMap<>();
        params.put(name.name(), business.getName());
        params.put(email.name(), business.getEmbeddedContactInfo().getEmail());
        params.put(phone.name(), business.getEmbeddedContactInfo().getPhone());
        return params;
    }

}
