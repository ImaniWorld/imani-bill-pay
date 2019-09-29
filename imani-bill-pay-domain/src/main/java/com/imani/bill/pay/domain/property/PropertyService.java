package com.imani.bill.pay.domain.property;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.imani.bill.pay.domain.AuditableRecord;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.*;

/**
 * Domain object for the additional services that can be provided on top of rentals at a Property.
 *
 * @author manyce400
 */
@Entity
@Table(name="PropertyService")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PropertyService extends AuditableRecord {



    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ID", nullable=false)
    private Long id;


    @Column(name="ServiceName", nullable=false, length = 100)
    private String serviceName;


    @Column(name="ServiceMonthlyCost", nullable=false)
    private Double serviceMonthlyCost;


    @Column(name="ServiceActive", nullable = true, columnDefinition = "TINYINT", length = 1)
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean serviceActive;


    // Tracks the Property that this service is offered at
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "PropertyID", nullable = false)
    private Property property;

    public PropertyService() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public Double getServiceMonthlyCost() {
        return serviceMonthlyCost;
    }

    public void setServiceMonthlyCost(Double serviceMonthlyCost) {
        this.serviceMonthlyCost = serviceMonthlyCost;
    }

    public Property getProperty() {
        return property;
    }

    public void setProperty(Property property) {
        this.property = property;
    }

    public boolean isServiceActive() {
        return serviceActive;
    }

    public void setServiceActive(boolean serviceActive) {
        this.serviceActive = serviceActive;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("serviceName", serviceName)
                .append("serviceActive", serviceActive)
                .append("property", property)
                .toString();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private PropertyService propertyService = new PropertyService();

        public Builder serviceName(String serviceName) {
            propertyService.serviceName = serviceName;
            return this;
        }

        public Builder serviceMonthlyCost(Double serviceMonthlyCost) {
            propertyService.serviceMonthlyCost = serviceMonthlyCost;
            return this;
        }

        public Builder serviceActive(boolean serviceActive) {
            propertyService.serviceActive = serviceActive;
            return this;
        }

        public Builder property(Property property) {
            propertyService.property = property;
            return this;
        }

        public Builder createDate(DateTime createDate) {
            propertyService.createDate = createDate;
            return this;
        }

        public PropertyService build() {
            return propertyService;
        }

    }

}
