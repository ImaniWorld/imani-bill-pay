package com.imani.bill.pay.domain.user.gateway;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.imani.bill.pay.domain.gateway.GenericAPIGatewayRequest;
import com.imani.bill.pay.domain.user.UserLoginStatistic;
import com.imani.bill.pay.domain.user.UserRecord;

import java.util.Optional;

/**
 * Domain object that identifies authentication status of an Imain BillPay user.
 *
 * @author manyce400
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserRecordRequest extends GenericAPIGatewayRequest {


    // Optional UserLoginStatistic only passed in as part of a login request
    private Optional<UserLoginStatistic> userLoginStatistic;


    public UserRecordRequest() {

    }

    public Optional<UserLoginStatistic> getUserLoginStatistic() {
        return userLoginStatistic;
    }

    public void setUserLoginStatistic(Optional<UserLoginStatistic> userLoginStatistic) {
        this.userLoginStatistic = userLoginStatistic;
    }

    public static Builder builder() {
        return new Builder();
    }


    public static final class Builder {

        private UserRecordRequest userRecordRequest = new UserRecordRequest();

        public Builder execUserRecord(UserRecord execUserRecord) {
            userRecordRequest.execUserRecord = Optional.of(execUserRecord);
            return this;
        }

        public Builder userLoginStatistic(UserLoginStatistic userLoginStatistic) {
            userRecordRequest.userLoginStatistic = Optional.of(userLoginStatistic);
            return this;
        }

        public UserRecordRequest build() {
            return userRecordRequest;
        }
    }

}
