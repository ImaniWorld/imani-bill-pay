package com.imani.bill.pay.domain.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.imani.bill.pay.domain.AuditableRecord;
import com.imani.bill.pay.domain.property.PropertyManager;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.annotations.Type;

import javax.persistence.*;

/**
 * Tracks all the Property management firms that a user is authorized to execute actions on behalf of.
 *
 * @author manyce400
 */
@Entity
@Table(name="PropertyManagerUser")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PropertyManagerUser extends AuditableRecord {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ID", nullable=false)
    private Long id;


    // Defines the access level granted to property manager user.
    @Column(name="UserRecordType", nullable=false, length=20)
    @Enumerated(EnumType.STRING)
    private PropertyManagerUserAccessE propertyManagerUserAccessE;


    @Column(name="authorized", nullable = true, columnDefinition = "TINYINT", length = 1)
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean authorized;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "UserRecordID", nullable = false)
    private UserRecord userRecord;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "PropertyManagerID", nullable = false)
    private PropertyManager propertyManager;


    public PropertyManagerUser() {

    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PropertyManagerUserAccessE getPropertyManagerUserAccessE() {
        return propertyManagerUserAccessE;
    }

    public void setPropertyManagerUserAccessE(PropertyManagerUserAccessE propertyManagerUserAccessE) {
        this.propertyManagerUserAccessE = propertyManagerUserAccessE;
    }

    public boolean isAuthorized() {
        return authorized;
    }

    public void setAuthorized(boolean authorized) {
        this.authorized = authorized;
    }

    public UserRecord getUserRecord() {
        return userRecord;
    }

    public void setUserRecord(UserRecord userRecord) {
        this.userRecord = userRecord;
    }

    public PropertyManager getPropertyManager() {
        return propertyManager;
    }

    public void setPropertyManager(PropertyManager propertyManager) {
        this.propertyManager = propertyManager;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("propertyManagerUserAccessE", propertyManagerUserAccessE)
                .append("authorized", authorized)
                .append("userRecord", userRecord)
                .append("propertyManager", propertyManager)
                .toString();
    }

    public static class Builder {

        private final PropertyManagerUser propertyManagerUser = new PropertyManagerUser();

        public Builder propertyManagerUserAccessE(PropertyManagerUserAccessE propertyManagerUserAccessE) {
            propertyManagerUser.propertyManagerUserAccessE = propertyManagerUserAccessE;
            return this;
        }

        public Builder authorized(boolean authorized) {
            propertyManagerUser.authorized = authorized;
            return this;
        }

        public Builder userRecord(UserRecord userRecord) {
            propertyManagerUser.userRecord = userRecord;
            return this;
        }

        public Builder propertyManager(PropertyManager propertyManager) {
            propertyManagerUser.propertyManager = propertyManager;
            return this;
        }

        public PropertyManagerUser build() {
            return propertyManagerUser;
        }

    }

}