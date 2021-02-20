package com.imani.bill.pay.domain.utility;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.imani.bill.pay.domain.AuditableRecord;
import com.imani.bill.pay.domain.business.Business;
import com.imani.bill.pay.domain.contact.Address;
import com.imani.bill.pay.domain.geographical.Community;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.annotations.Type;

import javax.persistence.*;

/**
 * Domain object that models a service area designated for utility purposes that could exist for an
 * Address, Community or Business
 *
 * @author manyce400
 */
@Entity
@Table(name="UtilityServiceArea")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UtilityServiceArea extends AuditableRecord {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ID", nullable=false)
    private Long id;

    @Column(name="AreaName", length = 400)
    private String areaName;

    @Column(name="AreaDescription", length = 400)
    private String areaDescription;

    @Column(name="UtilityTypeE", nullable=false, length=25)
    @Enumerated(EnumType.STRING)
    private UtilityTypeE utilityTypeE;

    @Column(name="Active", nullable = true, columnDefinition = "TINYINT", length = 1)
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean active;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "AddressID")
    private Address address;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "BusinessID")
    private Business business;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "CommunityID")
    private Community community;


    public UtilityServiceArea() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getAreaDescription() {
        return areaDescription;
    }

    public void setAreaDescription(String areaDescription) {
        this.areaDescription = areaDescription;
    }

    public UtilityTypeE getUtilityTypeE() {
        return utilityTypeE;
    }

    public void setUtilityTypeE(UtilityTypeE utilityTypeE) {
        this.utilityTypeE = utilityTypeE;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Business getBusiness() {
        return business;
    }

    public void setBusiness(Business business) {
        this.business = business;
    }

    public Community getCommunity() {
        return community;
    }

    public void setCommunity(Community community) {
        this.community = community;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("areaName", areaName)
                .append("areaDescription", areaDescription)
                .append("utilityTypeE", utilityTypeE)
                .append("active", active)
                .append("address", address)
                .append("business", business)
                .append("community", community)
                .toString();
    }


}