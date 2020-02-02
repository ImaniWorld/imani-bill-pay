package com.imani.bill.pay.domain.payment.stripe;

import com.imani.bill.pay.domain.user.UserRecord;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;

/**
 * Models all fields required from ImaniBillPay to interact with Stripe Customer object.
 *
 * See Stripe Customer Object fields reference:  https://stripe.com/docs/api/customers/object
 * @author manyce400
 */
public enum  CustomerObjFieldsE {

    id,

    balance,

    currency,

    default_source,

    delinquent,

    description,

    discount,

    email,

    invoice_prefix,

    name,

    phone

    ;


    public static Map<String, Object> getCustomerCreateParams(UserRecord userRecord) {
        Assert.notNull(userRecord, "UserRecord cannot be null");
        Map<String, Object> params = new HashMap<>();
        params.put(name.name(), userRecord.getFullName());
        params.put(email.name(), userRecord.getEmbeddedContactInfo().getEmail());
        params.put(phone.name(), userRecord.getEmbeddedContactInfo().getMobilePhone());
        return params;
    }
}
