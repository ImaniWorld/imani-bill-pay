package com.imani.bill.pay.domain.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.imani.bill.pay.domain.AuditableRecord;
import com.imani.bill.pay.domain.property.PropertyService;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.annotations.Type;

import javax.persistence.*;

/**
 * Domain object to track the Property Services that a user has signed up for.
 *
 * @author manyce400
 */
@Entity
@Table(name="UserResidencePropertyService")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserResidencePropertyService extends AuditableRecord {



    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ID", nullable=false)
    private Long id;


    @Column(name="ServiceActive", nullable = true, columnDefinition = "TINYINT", length = 1)
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean active;


    // Tracks additional service at the Property that this user has signed up for.  This will impact user billing.
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "PropertyServicesID", nullable = false)
    private PropertyService propertyService;


    // Tracks the User signed up for the PropertyService
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UserResidenceID", nullable = false)
    private UserResidence userResidence;


    public UserResidencePropertyService() {

    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public PropertyService getPropertyService() {
        return propertyService;
    }

    public void setPropertyService(PropertyService propertyService) {
        this.propertyService = propertyService;
    }

    public UserResidence getUserResidence() {
        return userResidence;
    }

    public void setUserResidence(UserResidence userResidence) {
        this.userResidence = userResidence;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        UserResidencePropertyService that = (UserResidencePropertyService) o;

        return new EqualsBuilder()
                .append(active, that.active)
                .append(id, that.id)
                .append(propertyService, that.propertyService)
                .append(userResidence, that.userResidence)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .append(active)
                .append(propertyService)
                .append(userResidence)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("active", active)
                .append("propertyService", propertyService)
                .append("userResidence", userResidence)
                .toString();
    }

    public static Builder builder() {
        return new Builder();
    }


    public static final class Builder {

        private UserResidencePropertyService userResidencePropertyService = new UserResidencePropertyService();

        public Builder active(boolean active) {
            userResidencePropertyService.active = active;
            return this;
        }

        public Builder propertyService(PropertyService propertyService) {
            userResidencePropertyService.propertyService = propertyService;
            return this;
        }

        public Builder userResidence(UserResidence userResidence) {
            userResidencePropertyService.userResidence = userResidence;
            return this;
        }

        public UserResidencePropertyService build() {
            return userResidencePropertyService;
        }
    }
}
