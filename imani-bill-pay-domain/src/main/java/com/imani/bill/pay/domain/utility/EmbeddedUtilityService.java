package com.imani.bill.pay.domain.utility;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.imani.bill.pay.domain.business.Business;
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
    @JoinColumn(name = "UtilityProviderBusinessID", nullable = false)
    private Business utilityProviderBusiness;


    // Unique Service Account that the specific business uses to track this customer agreement
    @Column(name="SvcCustomerAcctID", length = 100)
    private String svcCustomerAcctID;


    // Optional field to provide additional context and details for service
    @Column(name="SvcDescription", length = 300)
    private String svcDescription;

    // Optional UtilityServiceArea for which the service is being rendered.
    // UtilityServiceArea will be attached to either an Address, Business, or Community.
    // IF set business, serviceAddress and community should all be null.
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "UtilityServiceAreaID")
    private UtilityServiceArea utilityServiceArea;


    public EmbeddedUtilityService() {

    }

    public Business getUtilityProviderBusiness() {
        return utilityProviderBusiness;
    }

    public void setUtilityProviderBusiness(Business utilityProviderBusiness) {
        this.utilityProviderBusiness = utilityProviderBusiness;
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

    public UtilityServiceArea getUtilityServiceArea() {
        return utilityServiceArea;
    }

    public void setUtilityServiceArea(UtilityServiceArea utilityServiceArea) {
        this.utilityServiceArea = utilityServiceArea;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("utilityProviderBusiness", utilityProviderBusiness)
                .append("svcCustomerAcctID", svcCustomerAcctID)
                .append("svcDescription", svcDescription)
                .append("utilityServiceArea", utilityServiceArea)
                .toString();
    }


    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private EmbeddedUtilityService embeddedUtilityService = new EmbeddedUtilityService();

        public Builder utilityProviderBusiness(Business utilityProviderBusiness) {
            embeddedUtilityService.utilityProviderBusiness = utilityProviderBusiness;
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

        public Builder utilityServiceArea(UtilityServiceArea utilityServiceArea) {
            embeddedUtilityService.utilityServiceArea = utilityServiceArea;
            return this;
        }

        public EmbeddedUtilityService build() {
            return embeddedUtilityService;
        }

    }

}