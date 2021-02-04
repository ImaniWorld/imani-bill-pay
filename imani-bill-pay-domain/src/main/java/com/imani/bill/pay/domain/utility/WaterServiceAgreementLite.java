package com.imani.bill.pay.domain.utility;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.imani.bill.pay.domain.agreement.EmbeddedAgreementLite;
import com.imani.bill.pay.domain.business.BusinessLite;
import com.imani.bill.pay.domain.contact.AddressLite;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * @author manyce400
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WaterServiceAgreementLite {


    private Long id;

    private String businessCustomerAcctID;

    private Long numberOfGallonsPerFixedCost;

    private EmbeddedAgreementLite embeddedAgreement;

    private BusinessLite business;

    private AddressLite serviceAddress;


    public WaterServiceAgreementLite() {

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

    public Long getNumberOfGallonsPerFixedCost() {
        return numberOfGallonsPerFixedCost;
    }

    public void setNumberOfGallonsPerFixedCost(Long numberOfGallonsPerFixedCost) {
        this.numberOfGallonsPerFixedCost = numberOfGallonsPerFixedCost;
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
                .append("numberOfGallonsPerFixedCost", numberOfGallonsPerFixedCost)
                .append("embeddedAgreement", embeddedAgreement)
                .append("business", business)
                .append("serviceAddress", serviceAddress)
                .toString();
    }
}
