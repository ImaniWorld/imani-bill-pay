package com.imani.bill.pay.domain.business;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.common.collect.ImmutableSet;
import com.imani.bill.pay.domain.AuditableRecord;
import com.imani.bill.pay.domain.contact.Address;
import com.imani.bill.pay.domain.contact.EmbeddedContactInfo;
import com.imani.bill.pay.domain.education.SchoolToTuitionGrade;
import com.imani.bill.pay.domain.education.TuitionGrade;
import com.imani.bill.pay.domain.payment.IHasPaymentInfo;
import com.imani.bill.pay.domain.utility.UtilityServiceArea;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.util.Assert;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="Business")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Business extends AuditableRecord implements IHasPaymentInfo {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ID", nullable=false)
    private Long id;

    @Column(name="Name", nullable=false, length = 100)
    private String name;
    
    @Embedded
    private EmbeddedContactInfo embeddedContactInfo;

    // Represents Stripe Account ID for this business.  This gets created as part of creating a new business
    @Column(name="StripeAcctID", length=100)
    public String stripeAcctID;
    
    // Defines type of business
    @Column(name="BusinessTypeE", nullable=false, length=25)
    @Enumerated(EnumType.STRING)
    private BusinessTypeE businessTypeE;

    // Represents the physical property/address that the business operates in
    // This could be the businesses corporate HQ
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "AddressID", nullable = true)
    private Address address;

    // IF the Business is a School, this will link to all Tuition Grades offered by the school
    @OneToMany(cascade=CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "school")
    private Set<SchoolToTuitionGrade> schoolToTuitionGrades = new HashSet<>();

    @OneToMany(cascade=CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "business")
    private Set<UtilityServiceArea> utilityServiceAreas = new HashSet<>();

    public Business() {
        
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

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Set<SchoolToTuitionGrade> getSchoolToTuitionGrades() {
        return ImmutableSet.copyOf(schoolToTuitionGrades);
    }

    public void addSchoolToTuitionGrade(TuitionGrade tuitionGrade) {
        Assert.notNull(tuitionGrade, "TuitionGrade cannot be null");
        SchoolToTuitionGrade schoolToTuitionGrade = SchoolToTuitionGrade.builder()
                .school(this)
                .tuitionGrade(tuitionGrade)
                .build();
        schoolToTuitionGrades.add(schoolToTuitionGrade);
    }

    public void addBillableCommunityArea(UtilityServiceArea utilityServiceArea) {
        Assert.notNull(utilityServiceArea, "BillableCommunityArea cannot be null");
        utilityServiceAreas.add(utilityServiceArea);
    }

    public Set<UtilityServiceArea> getBillableCommunityAreas() {
        return ImmutableSet.copyOf(utilityServiceAreas);
    }

    public BusinessLite toBusinessLite() {
        BusinessLite businessLite = BusinessLite.builder()
                .id(id)
                .businessTypeE(businessTypeE)
                .name(name)
                .embeddedContactInfo(embeddedContactInfo)
                .build();
        return businessLite;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Business business = (Business) o;

        return new EqualsBuilder()
                .append(id, business.id)
                .append(name, business.name)
                .append(embeddedContactInfo, business.embeddedContactInfo)
                .append(stripeAcctID, business.stripeAcctID)
                .append(businessTypeE, business.businessTypeE)
                .append(address, business.address)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .append(name)
                .append(embeddedContactInfo)
                .append(stripeAcctID)
                .append(businessTypeE)
                .append(address)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("name", name)
                .append("embeddedContactInfo", embeddedContactInfo)
                .append("stripeAcctID", stripeAcctID)
                .append("businessTypeE", businessTypeE)
                .append("address", address)
                .toString();
    }

    public static Builder builder() {
        return new Builder();
    }
    
    public static class Builder  {
        private Business business = new Business();

        public Builder name(String name) {
            business.name = name;
            return this;
        }

        public Builder embeddedContactInfo(EmbeddedContactInfo embeddedContactInfo) {
            business.embeddedContactInfo = embeddedContactInfo;
            return this;
        }

        public Builder stripeAcctID(String stripeAcctID) {
            business.stripeAcctID = stripeAcctID;
            return this;
        }

        public Builder businessTypeE(BusinessTypeE businessTypeE) {
            business.businessTypeE = businessTypeE;
            return this;
        }

        public Builder address(Address address) {
            business.address = address;
            return this;
        }

        public Business build() {
            return business;
        }

    }
}
