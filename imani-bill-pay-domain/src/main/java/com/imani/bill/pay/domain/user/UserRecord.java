package com.imani.bill.pay.domain.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.imani.bill.pay.domain.AuditableRecord;
import com.imani.bill.pay.domain.contact.EmbeddedContactInfo;
import com.imani.bill.pay.domain.payment.ACHPaymentInfo;
import com.imani.bill.pay.domain.payment.IHasPaymentInfo;
import com.imani.bill.pay.domain.property.PropertyManager;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import javax.persistence.*;

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


    // Populated Only IF this user is a PropertyManager user.  This will identify the PropertyManager that the user can execute transactions on behalf of.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PropertyManagerID")
    private PropertyManager propertyManager;


    // Populated if this user has successfully setup a method of payment
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ACHPaymentInfoID")
    private ACHPaymentInfo achPaymentInfo;


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

    public PropertyManager getPropertyManager() {
        return propertyManager;
    }

    public void setPropertyManager(PropertyManager propertyManager) {
        this.propertyManager = propertyManager;
    }

    public ACHPaymentInfo getAchPaymentInfo() {
        return achPaymentInfo;
    }

    public void setAchPaymentInfo(ACHPaymentInfo achPaymentInfo) {
        this.achPaymentInfo = achPaymentInfo;
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
                .append("propertyManager", propertyManager)
                .append("achPaymentInfo", achPaymentInfo)
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

        public Builder propertyManager(PropertyManager propertyManager) {
            userRecord.propertyManager = propertyManager;
            return this;
        }

        public Builder achPaymentInfo(ACHPaymentInfo achPaymentInfo) {
            userRecord.achPaymentInfo = achPaymentInfo;
            return this;
        }

        public UserRecord build() {
            return userRecord;
        }
    }

}
