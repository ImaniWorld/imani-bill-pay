package com.imani.bill.pay.domain.daycare;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.imani.bill.pay.domain.AuditableRecord;
import com.imani.bill.pay.domain.business.BusinessTypeE;
import com.imani.bill.pay.domain.contact.EmbeddedContactInfo;
import com.imani.bill.pay.domain.property.Property;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;

/**
 * @author manyce400
 */
@Entity
@Table(name="DayCare")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DayCare extends AuditableRecord {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ID", nullable=false)
    private Long id;


    @Column(name="DayCareName", nullable=false, length = 100)
    private String dayCareName;


    @Embedded
    private EmbeddedContactInfo embeddedContactInfo;


    // Represents Stripe Account ID for Institutions - PropertyManager, PropertyOwner etc
    @Column(name="StripeAcctID", length=100)
    public String stripeAcctID;


    // Defines type of business
    @Column(name="BusinessTypeE", nullable=false, length=25)
    @Enumerated(EnumType.STRING)
    private BusinessTypeE businessTypeE;

    // Represents the physical property/address that the day care operates in
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PropertyID", nullable = true)
    private Property businessAddressInfo;

    public DayCare() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDayCareName() {
        return dayCareName;
    }

    public void setDayCareName(String dayCareName) {
        this.dayCareName = dayCareName;
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

    public Property getBusinessAddressInfo() {
        return businessAddressInfo;
    }

    public void setBusinessAddressInfo(Property businessAddressInfo) {
        this.businessAddressInfo = businessAddressInfo;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("dayCareName", dayCareName)
                .append("embeddedContactInfo", embeddedContactInfo)
                .append("stripeAcctID", stripeAcctID)
                .append("businessTypeE", businessTypeE)
                .append("businessAddressInfo", businessAddressInfo)
                .toString();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final DayCare dayCare = new DayCare();

        public Builder dayCareName(String dayCareName) {
            dayCare.dayCareName = dayCareName;
            return this;
        }

        public Builder embeddedContactInfo(EmbeddedContactInfo embeddedContactInfo) {
            dayCare.embeddedContactInfo = embeddedContactInfo;
            return this;
        }

        public Builder stripeAcctID(String stripeAcctID) {
            dayCare.stripeAcctID = stripeAcctID;
            return this;
        }

        public Builder businessTypeE(BusinessTypeE businessTypeE) {
            dayCare.businessTypeE = businessTypeE;
            return this;
        }

        public Builder businessAddressInfo(Property businessAddressInfo) {
            dayCare.businessAddressInfo = businessAddressInfo;
            return this;
        }

        public DayCare build() {
            return dayCare;
        }
    }

}
