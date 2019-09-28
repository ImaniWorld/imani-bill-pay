package com.imani.bill.pay.domain.user;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @author manyce400
 */
public class UserRecordAuthentication {


    private Boolean authenticated;

    private Boolean loggedIn;

    private Boolean accountLocked;

    private Integer unsuccessfulLoginAttempts;

    private UserRecord userRecord;


    public UserRecordAuthentication() {

    }

    public Boolean getAuthenticated() {
        return authenticated;
    }

    public void setAuthenticated(Boolean authenticated) {
        this.authenticated = authenticated;
    }

    public Boolean getLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(Boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    public Boolean getAccountLocked() {
        return accountLocked;
    }

    public void setAccountLocked(Boolean accountLocked) {
        this.accountLocked = accountLocked;
    }

    public Integer getUnsuccessfulLoginAttempts() {
        return unsuccessfulLoginAttempts;
    }

    public void setUnsuccessfulLoginAttempts(Integer unsuccesfulLoginAttempts) {
        this.unsuccessfulLoginAttempts = unsuccesfulLoginAttempts;
    }

    public UserRecord getUserRecord() {
        return userRecord;
    }

    public void setUserRecord(UserRecord userRecord) {
        this.userRecord = userRecord;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
                .append("authenticated", authenticated)
                .append("loggedIn", loggedIn)
                .append("accountLocked", accountLocked)
                .append("unsuccessfulLoginAttempts", unsuccessfulLoginAttempts)
                .append("userRecord", userRecord)
                .toString();
    }


    public static Builder builder() {
        return new Builder();
    }


    public static final class Builder {

        private UserRecordAuthentication userRecordAuthentication = new UserRecordAuthentication();

        public Builder authenticated(Boolean authenticated) {
            userRecordAuthentication.authenticated = authenticated;
            return this;
        }

        public Builder loggedIn(Boolean loggedIn) {
            userRecordAuthentication.loggedIn = loggedIn;
            return this;
        }

        public Builder accountLocked(Boolean accountLocked) {
            userRecordAuthentication.accountLocked = accountLocked;
            return this;
        }

        public Builder unsuccessfulLoginAttempts(Integer unsuccessfulLoginAttempts) {
            userRecordAuthentication.unsuccessfulLoginAttempts = unsuccessfulLoginAttempts;
            return this;
        }

        public Builder userRecord(UserRecord userRecord) {
            userRecordAuthentication.userRecord = userRecord;
            return this;
        }

        public UserRecordAuthentication build() {
            return userRecordAuthentication;
        }
    }
}
