package com.imani.bill.pay.domain.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.imani.bill.pay.domain.AuditableRecord;
import com.imani.bill.pay.domain.contact.Address;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.annotations.Type;

import javax.persistence.*;

@Entity
@Table(name="UserToAddress")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserToAddress extends AuditableRecord {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ID", nullable=false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UserRecordID", nullable = false)
    private UserRecord userRecord;

    @Column(name="IsPrimaryAddress", nullable = true, columnDefinition = "TINYINT", length = 1)
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean primaryAddress;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "AddressID", nullable = true)
    private Address address;

    public UserToAddress() {

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

    public boolean isPrimaryAddress() {
        return primaryAddress;
    }

    public void setPrimaryAddress(boolean primaryAddress) {
        this.primaryAddress = primaryAddress;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("userRecord", userRecord)
                .append("primaryAddress", primaryAddress)
                .append("address", address)
                .toString();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private UserToAddress userToAddress = new UserToAddress();

        public Builder userRecord(UserRecord userRecord) {
            userToAddress.userRecord = userRecord;
            return this;
        }

        public Builder primaryAddress(boolean primaryAddress) {
            userToAddress.primaryAddress = primaryAddress;
            return this;
        }

        public Builder address(Address address) {
            userToAddress.address = address;
            return this;
        }

        public UserToAddress build() {
            return userToAddress;
        }
    }

}
