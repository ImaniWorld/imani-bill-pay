package com.imani.bill.pay.domain.business;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.imani.bill.pay.domain.contact.EmbeddedContactInfo;
import org.apache.commons.lang3.builder.ToStringBuilder;

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


    public BusinessLite() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public EmbeddedContactInfo getEmbeddedContactInfo() {
        return embeddedContactInfo;
    }

    public void setEmbeddedContactInfo(EmbeddedContactInfo embeddedContactInfo) {
        this.embeddedContactInfo = embeddedContactInfo;
    }

    public String getStripeAcctID() {
        return stripeAcctID;
    }

    public void setStripeAcctID(String stripeAcctID) {
        this.stripeAcctID = stripeAcctID;
    }

    public BusinessTypeE getBusinessTypeE() {
        return businessTypeE;
    }

    public void setBusinessTypeE(BusinessTypeE businessTypeE) {
        this.businessTypeE = businessTypeE;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("name", name)
                .append("embeddedContactInfo", embeddedContactInfo)
                .append("stripeAcctID", stripeAcctID)
                .append("businessTypeE", businessTypeE)
                .toString();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private final BusinessLite businessLite = new BusinessLite();

        public Builder id(Long id) {
            businessLite.id = id;
            return this;
        }

        public Builder name(String name) {
            businessLite.name = name;
            return this;
        }

        public Builder embeddedContactInfo(EmbeddedContactInfo embeddedContactInfo) {
            businessLite.embeddedContactInfo = embeddedContactInfo;
            return this;
        }

        public Builder stripeAcctID(String stripeAcctID) {
            businessLite.stripeAcctID = stripeAcctID;
            return this;
        }

        public Builder businessTypeE(BusinessTypeE businessTypeE) {
            businessLite.businessTypeE = businessTypeE;
            return this;
        }

        public BusinessLite build() {
            return businessLite;
        }

    }


}
