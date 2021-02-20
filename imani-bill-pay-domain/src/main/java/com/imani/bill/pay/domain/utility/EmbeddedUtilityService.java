package com.imani.bill.pay.domain.utility;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.imani.bill.pay.domain.business.Business;
import com.imani.bill.pay.domain.contact.Address;
import com.imani.bill.pay.domain.geographical.Community;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;

/**
 * @author manyce400
 */
@Embeddable
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EmbeddedUtilityService {


    // Tracks the Business which is responsible for providing the utility servie
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "UtilityProviderID", nullable = false)
    private Business utilityProvider;


    // Unique Service Account that the specific business uses to track this customer agreement
    @Column(name="SvcCustomerAcctID", length = 100)
    private String svcCustomerAcctID;


    // Optional field to provide additional context and details for service
    @Column(name="SvcDescription", length = 300)
    private String svcDescription;


    // Optional Customer Address for which the service is being rendered.
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "SvcCustomerAddressID")
    private Address svcCustomerAddress;


    // Optional Service customer Business for which the service is being rendered.
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "SvcCustomerBusinessID", nullable = false)
    private Business svcCustomerBusiness;


    // Optional Community  for which the service is being rendered.
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "SvcCustomerCommunityID")
    private Community svcCustomerCommunity;


    // Optional UtilityServiceArea for which the service is being rendered.
    // UtilityServiceArea will be attached to either an Address, Business, or Community.
    // IF set business, serviceAddress and community should all be null.
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "UtilityServiceAreaID")
    private UtilityServiceArea utilityServiceArea;


    public EmbeddedUtilityService() {

    }

    public Business getUtilityProvider() {
        return utilityProvider;
    }

    public void setUtilityProvider(Business utilityProvider) {
        this.utilityProvider = utilityProvider;
    }

    public String getSvcCustomerAcctID() {
        return svcCustomerAcctID;
    }

    public void setSvcCustomerAcctID(String svcCustomerAcctID) {
        this.svcCustomerAcctID = svcCustomerAcctID;
    }

    public String getSvcDescription() {
        return svcDescription;
    }

    public void setSvcDescription(String svcDescription) {
        this.svcDescription = svcDescription;
    }

    public Address getSvcCustomerAddress() {
        return svcCustomerAddress;
    }

    public void setSvcCustomerAddress(Address svcCustomerAddress) {
        this.svcCustomerAddress = svcCustomerAddress;
    }

    public Business getSvcCustomerBusiness() {
        return svcCustomerBusiness;
    }

    public void setSvcCustomerBusiness(Business svcCustomerBusiness) {
        this.svcCustomerBusiness = svcCustomerBusiness;
    }

    public Community getSvcCustomerCommunity() {
        return svcCustomerCommunity;
    }

    public void setSvcCustomerCommunity(Community svcCustomerCommunity) {
        this.svcCustomerCommunity = svcCustomerCommunity;
    }

    public UtilityServiceArea getUtilityServiceArea() {
        return utilityServiceArea;
    }

    public void setUtilityServiceArea(UtilityServiceArea utilityServiceArea) {
        this.utilityServiceArea = utilityServiceArea;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("utilityProvider", utilityProvider)
                .append("svcCustomerAcctID", svcCustomerAcctID)
                .append("svcDescription", svcDescription)
                .append("svcCustomerAddress", svcCustomerAddress)
                .append("svcCustomerBusiness", svcCustomerBusiness)
                .append("svcCustomerCommunity", svcCustomerCommunity)
                .append("utilityServiceArea", utilityServiceArea)
                .toString();
    }

    public static class Builder {

        private EmbeddedUtilityService embeddedUtilityService = new EmbeddedUtilityService();

        public Builder utilityProvider(Business utilityProvider) {
            embeddedUtilityService.utilityProvider = utilityProvider;
            return this;
        }

        public Builder svcCustomerAcctID(String svcCustomerAcctID) {
            embeddedUtilityService.svcCustomerAcctID = svcCustomerAcctID;
            return this;
        }

        public Builder svcDescription(String svcDescription) {
            embeddedUtilityService.svcDescription = svcDescription;
            return this;
        }

        public Builder svcCustomerAddress(Address svcCustomerAddress) {
            embeddedUtilityService.svcCustomerAddress = svcCustomerAddress;
            return this;
        }

        public Builder svcCustomerBusiness(Business svcCustomerBusiness) {
            embeddedUtilityService.svcCustomerBusiness = svcCustomerBusiness;
            return this;
        }

        public Builder svcCustomerCommunity(Community svcCustomerCommunity) {
            embeddedUtilityService.svcCustomerCommunity = svcCustomerCommunity;
            return this;
        }

        public Builder utilityServiceArea(UtilityServiceArea utilityServiceArea) {
            embeddedUtilityService.utilityServiceArea = utilityServiceArea;
            return this;
        }

        public EmbeddedUtilityService build() {
            return embeddedUtilityService;
        }

    }

}