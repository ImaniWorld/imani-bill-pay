package com.imani.bill.pay.domain.geographical;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.imani.bill.pay.domain.business.BusinessLite;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * @author manyce400
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommunityLite {


    private Long id;

    private String communityName;

    private CommunityTypeE communityTypeE;

    private City city;

    private BusinessLite managedByBusiness;

    public CommunityLite() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCommunityName() {
        return communityName;
    }

    public void setCommunityName(String communityName) {
        this.communityName = communityName;
    }

    public CommunityTypeE getCommunityTypeE() {
        return communityTypeE;
    }

    public void setCommunityTypeE(CommunityTypeE communityTypeE) {
        this.communityTypeE = communityTypeE;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public BusinessLite getManagedByBusiness() {
        return managedByBusiness;
    }

    public void setManagedByBusiness(BusinessLite managedByBusiness) {
        this.managedByBusiness = managedByBusiness;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("communityName", communityName)
                .append("communityTypeE", communityTypeE)
                .append("city", city)
                .append("managedByBusiness", managedByBusiness)
                .toString();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private final CommunityLite communityLite = new CommunityLite();

        public Builder id(Long id) {
            communityLite.id = id;
            return this;
        }

        public Builder communityName(String communityName) {
            communityLite.communityName = communityName;
            return this;
        }

        public Builder communityTypeE(CommunityTypeE communityTypeE) {
            communityLite.communityTypeE = communityTypeE;
            return this;
        }

        public Builder city(City city) {
            communityLite.city = city;
            return this;
        }

        public Builder managedByBusiness(BusinessLite managedByBusiness) {
            communityLite.managedByBusiness = managedByBusiness;
            return this;
        }

        public CommunityLite build() {
            return communityLite;
        }

    }


}
