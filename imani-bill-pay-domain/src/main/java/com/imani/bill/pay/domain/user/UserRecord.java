package com.imani.bill.pay.domain.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.common.collect.ImmutableSet;
import com.imani.bill.pay.domain.AuditableRecord;
import com.imani.bill.pay.domain.business.Business;
import com.imani.bill.pay.domain.contact.Address;
import com.imani.bill.pay.domain.contact.EmbeddedContactInfo;
import com.imani.bill.pay.domain.payment.IHasPaymentInfo;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * UserRecord is the domain model for all users that can access Imani Cash to transact.
 *
 * @author manyce400
 */
@Entity
@Table(name="UserRecord")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserRecord extends AuditableRecord implements IHasPaymentInfo {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ID", nullable=false)
    private Long id;

    @Column(name="FirstName", nullable=false, length = 50)
    private String firstName;

    @Column(name="LastName", nullable=false, length = 50)
    private String lastName;

    @Embedded
    private EmbeddedContactInfo embeddedContactInfo;


    // For security reasons, this field will not be returned in JSON of this object.
    //@JsonIgnore
    @Column(name="Password", nullable=false, length = 200)
    private String password;


    // Represents a Stripe Customer ID if this entry is for an End-User Customer(UserRecord)
    @Column(name="StripeCustomerID", nullable=true, length=100)
    public String stripeCustomerID;


    @Column(name="UserRecordType", nullable=false, length=20)
    @Enumerated(EnumType.STRING)
    private UserRecordTypeE userRecordTypeE;


    // Track the number of unsuccessful login attempts. This should be tracked by security protocols and appropriate actions taken
    // For security reasons, this field will not be returned in JSON of this object.
    //@JsonIgnore
    @Column(name="UnsuccessfulLoginAttempts", nullable=true)
    private Integer unsuccessfulLoginAttempts;


    // For security reasons, this field will not be returned in JSON of this object.
    @Column(name="LoggedIn", nullable = true, columnDefinition = "TINYINT", length = 1)
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean loggedIn;


    // For security reasons, this field will not be returned in JSON of this object.
    //@JsonIgnore
    @Column(name="ResetPassword", nullable = true, columnDefinition = "TINYINT", length = 1)
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean resetPassword;


    // IF set to true then user is not allowed to access QPalX application
    @Column(name="AccountLocked", nullable = true, columnDefinition = "TINYINT", length = 1)
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean accountLocked;


    // Only users that have accepted terms and conditions should be allowed to use the platform.  Date of acceptance will be create date
    @Column(name="AcceptedTermsAndConditions", nullable = false, columnDefinition = "TINYINT", length = 1)
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean acceptedTermsAndConditions;


    // Track and update the last time this user was logged in to our system
    // For security reasons, this field will not be returned in JSON of this object.
    @JsonIgnore
    @Column(name = "LastLoginDate", nullable = true)
    @Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime lastLoginDate;


    // Track and update the last time this user logged out
    // For security reasons, this field will not be returned in JSON of this object.
    @JsonIgnore
    @Column(name = "LastLogoutDate")
    @Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime lastLogoutDate;


    // Tracks and maps a user to Businesses that this user is affiliated with.  Actualy business relationship
    // Will be determined by the u
    @OneToMany(cascade=CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "userRecord")
    private Set<UserToBusiness> userToBusinesses = new HashSet<>();

    @OneToMany(cascade=CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "userRecord")
    private Set<UserToAddress> userToAddresses = new HashSet<>();


    public UserRecord() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @JsonIgnore
    public String getFullName() {
        StringBuffer sb = new StringBuffer(firstName)
                .append(" ")
                .append(lastName);
        return sb.toString();
    }

    public EmbeddedContactInfo getEmbeddedContactInfo() {
        return embeddedContactInfo;
    }

    public void setEmbeddedContactInfo(EmbeddedContactInfo embeddedContactInfo) {
        this.embeddedContactInfo = embeddedContactInfo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getStripeCustomerID() {
        return stripeCustomerID;
    }

    public void setStripeCustomerID(String stripeCustomerID) {
        this.stripeCustomerID = stripeCustomerID;
    }

    public UserRecordTypeE getUserRecordTypeE() {
        return userRecordTypeE;
    }

    public void setUserRecordTypeE(UserRecordTypeE userRecordTypeE) {
        this.userRecordTypeE = userRecordTypeE;
    }

    public Integer getUnsuccessfulLoginAttempts() {
        return unsuccessfulLoginAttempts;
    }

    public void setUnsuccessfulLoginAttempts(Integer unsuccessfulLoginAttempts) {
        this.unsuccessfulLoginAttempts = unsuccessfulLoginAttempts;
    }

    public boolean isResetPassword() {
        return resetPassword;
    }

    public void setResetPassword(boolean resetPassword) {
        this.resetPassword = resetPassword;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    public boolean isAccountLocked() {
        return accountLocked;
    }

    public void setAccountLocked(boolean accountLocked) {
        this.accountLocked = accountLocked;
    }

    public boolean isAcceptedTermsAndConditions() {
        return acceptedTermsAndConditions;
    }

    public void setAcceptedTermsAndConditions(boolean acceptedTermsAndConditions) {
        this.acceptedTermsAndConditions = acceptedTermsAndConditions;
    }

    public DateTime getLastLoginDate() {
        return lastLoginDate;
    }

    public void setLastLoginDate(DateTime lastLoginDate) {
        this.lastLoginDate = lastLoginDate;
    }

    public DateTime getLastLogoutDate() {
        return lastLogoutDate;
    }

    public void setLastLogoutDate(DateTime lastLogoutDate) {
        this.lastLogoutDate = lastLogoutDate;
    }

    public Set<UserToBusiness> getUserToBusinesses() {
        return ImmutableSet.copyOf(userToBusinesses);
    }

    public void addUserToBusiness(Business business) {
        Assert.notNull(business, "Business cannot be null");
        UserToBusiness userToBusiness = UserToBusiness.builder()
                .userRecord(this)
                .business(business)
                .build();
        this.userToBusinesses.add(userToBusiness);
    }

    public Set<UserToAddress> getUserToAddresses() {
        return ImmutableSet.copyOf(userToAddresses);
    }

    public void addUserToAddress(Address address, boolean isPrimary) {
        Assert.notNull(address, "Address cannot be null");
        UserToAddress userToAddress = UserToAddress.builder()
                .userRecord(this)
                .address(address)
                .primaryAddress(isPrimary)
                .build();
        this.userToAddresses.add(userToAddress);
    }

    public boolean hasAddress(Address address) {
        Assert.notNull(address, "Address cannot be null");
        return userToAddresses.contains(address);
    }

    public void updateSafeFieldsWherePresent(UserRecord userRecordToCopy) {
        Assert.notNull(userRecordToCopy, "userRecordToCopy cannot be null");

        if(StringUtils.hasLength(userRecordToCopy.getFirstName())) {
            this.setFirstName(userRecordToCopy.getFirstName());
        }

        if(StringUtils.hasLength(userRecordToCopy.getLastName())) {
            this.setLastName(userRecordToCopy.getLastName());
        }

        if(userRecordToCopy.getEmbeddedContactInfo().getPhone() != null) {
            this.getEmbeddedContactInfo().setPhone(userRecordToCopy.getEmbeddedContactInfo().getPhone());
        }
    }

    public UserRecordLite toUserRecordLite() {
        UserRecordLite userRecordLite = UserRecordLite.builder()
                .id(id)
                .firstName(firstName)
                .lastName(lastName)
                .email(embeddedContactInfo.getEmail())
                .mobilePhone(embeddedContactInfo.getMobilePhone())
                .userRecordTypeE(userRecordTypeE)
                .loggedIn(loggedIn)
                .accountLocked(accountLocked)
                .lastLoginDate(lastLoginDate)
                .build();
        return userRecordLite;
    }


    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("firstName", firstName)
                .append("lastName", lastName)
                .append("embeddedContactInfo", embeddedContactInfo)
                .append("password", password)
                .append("stripeCustomerID", stripeCustomerID)
                .append("userRecordTypeE", userRecordTypeE)
                .append("unsuccessfulLoginAttempts", unsuccessfulLoginAttempts)
                .append("loggedIn", loggedIn)
                .append("resetPassword", resetPassword)
                .append("accountLocked", accountLocked)
                .append("acceptedTermsAndConditions", acceptedTermsAndConditions)
                .append("lastLoginDate", lastLoginDate)
                .append("lastLogoutDate", lastLogoutDate)
                .toString();
    }


    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private UserRecord userRecord = new UserRecord();

        public Builder firstName(String firstName) {
            userRecord.firstName = firstName;
            return this;
        }

        public Builder lastName(String lastName) {
            userRecord.lastName = lastName;
            return this;
        }

        public Builder embeddedContactInfo(EmbeddedContactInfo embeddedContactInfo) {
            userRecord.embeddedContactInfo = embeddedContactInfo;
            return this;
        }

        public Builder password(String password) {
            userRecord.password = password;
            return this;
        }

        public Builder stripeCustomerID(String stripeCustomerID) {
            userRecord.stripeCustomerID = stripeCustomerID;
            return this;
        }

        public Builder userRecordTypeE(UserRecordTypeE userRecordTypeE) {
            userRecord.userRecordTypeE = userRecordTypeE;
            return this;
        }

        public Builder unsuccessfulLoginAttempts(Integer unsuccessfulLoginAttempts) {
            userRecord.unsuccessfulLoginAttempts = unsuccessfulLoginAttempts;
            return this;
        }

        public Builder loggedIn(boolean loggedIn) {
            userRecord.loggedIn = loggedIn;
            return this;
        }

        public Builder resetPassword(boolean resetPassword) {
            userRecord.resetPassword = resetPassword;
            return this;
        }

        public Builder accountLocked(boolean accountLocked) {
            userRecord.accountLocked = accountLocked;
            return this;
        }

        public Builder acceptedTermsAndConditions(boolean acceptedTermsAndConditions) {
            userRecord.acceptedTermsAndConditions = acceptedTermsAndConditions;
            return this;
        }

        public Builder lastLoginDate(DateTime lastLoginDate) {
            userRecord.lastLoginDate = lastLoginDate;
            return this;
        }

        public Builder lastLogoutDate(DateTime lastLogoutDate) {
            userRecord.lastLogoutDate = lastLogoutDate;
            return this;
        }

        public Builder business(Business business) {
            userRecord.addUserToBusiness(business);
            return this;
        }

        public Builder address(Address address, boolean isPrimary) {
            userRecord.addUserToAddress(address, isPrimary);
            return this;
        }

        public UserRecord build() {
            return userRecord;
        }
    }

}
