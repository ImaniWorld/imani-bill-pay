package com.imani.bill.pay.domain.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.imani.bill.pay.domain.AuditableRecord;
import com.imani.bill.pay.domain.business.Business;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;

/**
 * Associates and links a UserRecord to a specific business
 *
 * @author manyce400
 */
@Entity
@Table(name="UserToBusiness")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserToBusiness extends AuditableRecord {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ID", nullable=false)
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UserRecordID", nullable = false)
    private UserRecord userRecord;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BusinessID", nullable = false)
    private Business business;


    public UserToBusiness() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserRecord getUserRecord() {
        return userRecord;
    }

    public void setUserRecord(UserRecord userRecord) {
        this.userRecord = userRecord;
    }

    public Business getBusiness() {
        return business;
    }

    public void setBusiness(Business business) {
        this.business = business;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", id)
                .append("userRecord", userRecord)
                .append("business", business)
                .toString();
    }

    public static Builder builder () {
        return new Builder();
    }

    public static class Builder {

        private UserToBusiness userToBusiness = new UserToBusiness();

        public Builder userRecord(UserRecord userRecord) {
            userToBusiness.userRecord = userRecord;
            return this;
        }

        public Builder business(Business business) {
            userToBusiness.business = business;
            return this;
        }

        public UserToBusiness build() {
            return userToBusiness;
        }
    }

}
