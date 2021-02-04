package com.imani.bill.pay.domain.business;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.imani.bill.pay.domain.contact.EmbeddedContactInfo;

/**
 * @author manyce400
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BusinessLite {


    private Long id;

    private String name;

    private EmbeddedContactInfo embeddedContactInfo;

    public String stripeAcctID;

    private BusinessTypeE businessTypeE;


}
