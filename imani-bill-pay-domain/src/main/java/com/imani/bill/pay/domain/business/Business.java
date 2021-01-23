package com.imani.bill.pay.domain.business;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.common.collect.ImmutableSet;
import com.imani.bill.pay.domain.AuditableRecord;
import com.imani.bill.pay.domain.contact.EmbeddedContactInfo;
import com.imani.bill.pay.domain.education.SchoolToTuitionGrade;
import com.imani.bill.pay.domain.education.TuitionGrade;
import com.imani.bill.pay.domain.property.Property;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.util.Assert;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="Business")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Business extends AuditableRecord {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ID", nullable=false)
    private Long id;

    @Column(name="Name", nullable=false, length = 100)
    private String name;
    
    @Embedded
    private EmbeddedContactInfo embeddedContactInfo;

    // Represents Stripe Account ID for Institutions - PropertyManager, PropertyOwner etc
    @Column(name="StripeAcctID", length=100)
    public String stripeAcctID;
    
    // Defines type of business
    @Column(name="BusinessTypeE", nullable=false, length=25)
    @Enumerated(EnumType.STRING)
    private BusinessTypeE businessTypeE;

    // Represents the physical property/address that the business operates in
    // This could be the businesses corporate HQ
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PropertyID", nullable = true)
    private Property businessProperty;

    // IF the Business is a School, this will link to all Tuition Grades offered by the school
    @OneToMany(cascade=CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "school")
    private Set<SchoolToTuitionGrade> schoolToTuitionGrades = new HashSet<>();

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

    public Property getBusinessProperty() {
        return businessProperty;
    }

    public void setBusinessProperty(Property businessProperty) {
        this.businessProperty = businessProperty;
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

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("name", name)
                .append("embeddedContactInfo", embeddedContactInfo)
                .append("stripeAcctID", stripeAcctID)
                .append("businessTypeE", businessTypeE)
                .append("businessProperty", businessProperty)
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

        public Builder businessProperty(Property businessProperty) {
            business.businessProperty = businessProperty;
            return this;
        }

        public Business build() {
            return business;
        }

    }
}
