package com.imani.bill.pay.domain.property;

import com.google.common.collect.ImmutableSet;
import com.imani.bill.pay.domain.AuditableRecord;
import com.imani.bill.pay.domain.payment.ACHPaymentInfo;
import com.imani.bill.pay.domain.user.UserRecord;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.util.Assert;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * @author manyce400
 */
@Entity
@Table(name="PropertyOwner")
public class PropertyOwner extends AuditableRecord {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ID", nullable=false)
    private Long id;


    // Tracks the optional business name that this property owner is operating under if owner has a licensed business.
    @Column(name="BusinessName", nullable=true, length = 50)
    private String businessName;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ACHPaymentInfoID", nullable = true)
    private ACHPaymentInfo achPaymentInfo;


    // Tracks the UserRecord for this PropertyOwner allowing owner to login and access the Imani application.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UserRecordID", nullable = true)
    private UserRecord userRecord;


    // Tracks the residential address for this PropertyOwner
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PropertyID", nullable = true)
    private Property addressInfo;


    // Contains the portfolio of properties owned by the property owner which can be rented
    @OneToMany(cascade=CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "propertyOwner")
    private Set<Property> portfolio = new HashSet<>();


    public PropertyOwner() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public ACHPaymentInfo getAchPaymentInfo() {
        return achPaymentInfo;
    }

    public void setAchPaymentInfo(ACHPaymentInfo achPaymentInfo) {
        this.achPaymentInfo = achPaymentInfo;
    }

    public UserRecord getUserRecord() {
        return userRecord;
    }

    public void setUserRecord(UserRecord userRecord) {
        this.userRecord = userRecord;
    }

    public Property getAddressInfo() {
        return addressInfo;
    }

    public void setAddressInfo(Property addressInfo) {
        this.addressInfo = addressInfo;
    }

    public Set<Property> getPortfolio() {
        return ImmutableSet.copyOf(portfolio);
    }

    public void addToPortfolio(Property property) {
        Assert.notNull(property, "property cannot be null");
        this.portfolio.add(property);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        PropertyOwner that = (PropertyOwner) o;

        return new EqualsBuilder()
                .append(id, that.id)
                .append(businessName, that.businessName)
                .append(achPaymentInfo, that.achPaymentInfo)
                .append(userRecord, that.userRecord)
                .append(addressInfo, that.addressInfo)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .append(businessName)
                .append(achPaymentInfo)
                .append(userRecord)
                .append(addressInfo)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("businessName", businessName)
                .append("achPaymentInfo", achPaymentInfo)
                .append("userRecord", userRecord)
                .append("addressInfo", addressInfo)
                .toString();
    }
}