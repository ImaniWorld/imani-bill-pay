package com.imani.bill.pay.domain.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.apache.commons.lang3.builder.ToStringBuilder;

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

    // For security reasons this should never be set for outgoing purposes
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

        public Builder password(String password) {
            userRecordLite.password = password;
            return this;
        }

        public UserRecordLite build() {
            return userRecordLite;
        }
    }
}
