package com.imani.bill.pay.domain.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.joda.time.DateTime;

/**
 * Light instance of UserRecord friendly for passing through API's request and response.
 *
 * @author manyce400
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserRecordLite {


    private String firstName;

    private String lastName;

    private String email;

    private Long mobilePhone;

    private UserRecordTypeE userRecordTypeE;

    private boolean loggedIn;

    private boolean accountLocked;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private DateTime lastLoginDate;

    // Critical: For security reasons this should never be set for outgoing purposes
    // TODO:  Find a way to ensure that this doesn't leak out.
    private String password;


    public UserRecordLite() {

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(Long mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public UserRecordTypeE getUserRecordTypeE() {
        return userRecordTypeE;
    }

    public void setUserRecordTypeE(UserRecordTypeE userRecordTypeE) {
        this.userRecordTypeE = userRecordTypeE;
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

    public DateTime getLastLoginDate() {
        return lastLoginDate;
    }

    public void setLastLoginDate(DateTime lastLoginDate) {
        this.lastLoginDate = lastLoginDate;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("firstName", firstName)
                .append("lastName", lastName)
                .append("email", email)
                .append("mobilePhone", mobilePhone)
                .append("userRecordTypeE", userRecordTypeE)
                .append("loggedIn", loggedIn)
                .append("accountLocked", accountLocked)
                .append("lastLoginDate", lastLoginDate)
                .append("password", password)
                .toString();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final UserRecordLite userRecordLite = new UserRecordLite();

        public Builder firstName(String firstName) {
            userRecordLite.firstName = firstName;
            return this;
        }

        public Builder lastName(String lastName) {
            userRecordLite.lastName = lastName;
            return this;
        }

        public Builder email(String email) {
            userRecordLite.email = email;
            return this;
        }

        public Builder mobilePhone(Long mobilePhone) {
            userRecordLite.mobilePhone = mobilePhone;
            return this;
        }

        public Builder userRecordTypeE(UserRecordTypeE userRecordTypeE) {
            userRecordLite.userRecordTypeE = userRecordTypeE;
            return this;
        }

        public Builder loggedIn(boolean loggedIn) {
            userRecordLite.loggedIn = loggedIn;
            return this;
        }

        public Builder accountLocked(boolean accountLocked) {
            userRecordLite.accountLocked = accountLocked;
            return this;
        }

        public Builder lastLoginDate(DateTime lastLoginDate) {
            userRecordLite.lastLoginDate = lastLoginDate;
            return this;
        }

        public UserRecordLite build() {
            return userRecordLite;
        }
    }
}
