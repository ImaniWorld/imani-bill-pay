package com.imani.bill.pay.domain.geographical;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.common.collect.ImmutableSet;
import com.imani.bill.pay.domain.AuditableRecord;
import com.imani.bill.pay.domain.utility.UtilityServiceArea;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.util.Assert;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * @author manyce400
 */
@Entity
@Table(name="Community")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Community extends AuditableRecord {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ID", nullable=false)
    private Long id;

    @Column(name="CommunityName", length = 400)
    private String communityName;

    @Column(name="CommunityTypeE", nullable=false, length=25)
    @Enumerated(EnumType.STRING)
    private CommunityTypeE communityTypeE;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "CityID", nullable = false)
    private City city;

    @OneToMany(cascade=CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "community")
    private Set<UtilityServiceArea> utilityServiceAreas = new HashSet<>();

    public Community() {

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

    public void addBillableCommunityArea(UtilityServiceArea utilityServiceArea) {
        Assert.notNull(utilityServiceArea, "BillableCommunityArea cannot be null");
        utilityServiceAreas.add(utilityServiceArea);
    }

    public Set<UtilityServiceArea> getBillableCommunityAreas() {
        return ImmutableSet.copyOf(utilityServiceAreas);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("communityName", communityName)
                .append("communityTypeE", communityTypeE)
                .append("city", city)
                .toString();
    }


}