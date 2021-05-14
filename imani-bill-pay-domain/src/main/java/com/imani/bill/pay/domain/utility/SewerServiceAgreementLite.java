package com.imani.bill.pay.domain.utility;

import com.imani.bill.pay.domain.agreement.EmbeddedAgreementLite;
import com.imani.bill.pay.domain.business.BusinessLite;
import com.imani.bill.pay.domain.contact.AddressLite;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * @author manyce400 
 */
public class SewerServiceAgreementLite {



    private Long id;

    private String businessCustomerAcctID;

    private EmbeddedAgreementLite embeddedAgreement;

    private BusinessLite business;

    private AddressLite serviceAddress;
    
    

    public SewerServiceAgreementLite() {
        
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBusinessCustomerAcctID() {
        return businessCustomerAcctID;
    }

    public void setBusinessCustomerAcctID(String businessCustomerAcctID) {
        this.businessCustomerAcctID = businessCustomerAcctID;
    }

    public EmbeddedAgreementLite getEmbeddedAgreement() {
        return embeddedAgreement;
    }

    public void setEmbeddedAgreement(EmbeddedAgreementLite embeddedAgreement) {
        this.embeddedAgreement = embeddedAgreement;
    }

    public BusinessLite getBusiness() {
        return business;
    }

    public void setBusiness(BusinessLite business) {
        this.business = business;
    }

    public AddressLite getServiceAddress() {
        return serviceAddress;
    }

    public void setServiceAddress(AddressLite serviceAddress) {
        this.serviceAddress = serviceAddress;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("businessCustomerAcctID", businessCustomerAcctID)
                .append("embeddedAgreement", embeddedAgreement)
                .append("business", business)
                .append("serviceAddress", serviceAddress)
                .toString();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private SewerServiceAgreementLite sewerServiceAgreementLite = new SewerServiceAgreementLite();

        public Builder id(Long id) {
            sewerServiceAgreementLite.id = id;
            return this;
        }

        public Builder businessCustomerAcctID(String businessCustomerAcctID) {
            sewerServiceAgreementLite.businessCustomerAcctID = businessCustomerAcctID;
            return this;
        }

        public Builder embeddedAgreement(EmbeddedAgreementLite embeddedAgreement) {
            sewerServiceAgreementLite.embeddedAgreement = embeddedAgreement;
            return this;
        }

        public Builder business(BusinessLite business) {
            sewerServiceAgreementLite.business = business;
            return this;
        }

        public Builder serviceAddress(AddressLite serviceAddress) {
            sewerServiceAgreementLite.serviceAddress = serviceAddress;
            return this;
        }

        public SewerServiceAgreementLite build() {
            return sewerServiceAgreementLite;
        }

    }
}
