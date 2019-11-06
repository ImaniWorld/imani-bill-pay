package com.imani.bill.pay.domain.property;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.common.collect.ImmutableSet;
import com.imani.bill.pay.domain.AuditableRecord;
import com.imani.bill.pay.domain.contact.EmbeddedContactInfo;
import com.imani.bill.pay.domain.payment.ACHPaymentInfo;
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
@Table(name="PropertyManager")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PropertyManager extends AuditableRecord {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ID", nullable=false)
    private Long id;


    @Column(name="name", nullable=false, length = 50)
    private String name;


    @Embedded
    private EmbeddedContactInfo embeddedContactInfo;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ACHPaymentInfoID", nullable = true)
    private ACHPaymentInfo achPaymentInfo;


    // Tracks the Business Property Address.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PropertyInfoID", nullable = true)
    private Property businessAddressInfo;


    // Contains the portfolio of properties managed by the property management firm
    @OneToMany(cascade=CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "propertyManager")
    private Set<Property> portfolio = new HashSet<>();


    public PropertyManager() {

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

    public ACHPaymentInfo getAchPaymentInfo() {
        return achPaymentInfo;
    }

    public void setAchPaymentInfo(ACHPaymentInfo achPaymentInfo) {
        this.achPaymentInfo = achPaymentInfo;
    }

    public Property getBusinessAddressInfo() {
        return businessAddressInfo;
    }

    public void setBusinessAddressInfo(Property businessAddressInfo) {
        this.businessAddressInfo = businessAddressInfo;
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

        PropertyManager that = (PropertyManager) o;

        return new EqualsBuilder()
                .append(id, that.id)
                .append(name, that.name)
                .append(embeddedContactInfo, that.embeddedContactInfo)
                .append(achPaymentInfo, that.achPaymentInfo)
                .append(businessAddressInfo, that.businessAddressInfo)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .append(name)
                .append(embeddedContactInfo)
                .append(achPaymentInfo)
                .append(businessAddressInfo)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("name", name)
                .append("embeddedContactInfo", embeddedContactInfo)
                .append("achPaymentInfo", achPaymentInfo)
                .append("businessAddressInfo", businessAddressInfo)
                .toString();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private PropertyManager propertyManager = new PropertyManager();

        public Builder name(String name) {
            propertyManager.name = name;
            return this;
        }

        public Builder embeddedContactInfo(EmbeddedContactInfo embeddedContactInfo) {
            propertyManager.embeddedContactInfo = embeddedContactInfo;
            return this;
        }

        public Builder achPaymentInfo(ACHPaymentInfo achPaymentInfo) {
            propertyManager.achPaymentInfo = achPaymentInfo;
            return this;
        }

        public Builder businessAddressInfo(Property businessAddressInfo) {
            propertyManager.businessAddressInfo = businessAddressInfo;
            return this;
        }

        public PropertyManager build() {
            return propertyManager;
        }
    }
    
}