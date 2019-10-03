package com.imani.bill.pay.domain.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.imani.bill.pay.domain.gateway.APIGatewayEvent;
import com.imani.bill.pay.domain.gateway.APIGatewayEventStatusE;

import java.util.Optional;

/**
 * Domain object that identifies authentication status of an Imain BillPay user.
 *
 * @author manyce400
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserRecordEvent extends APIGatewayEvent {



    private UserRecord userRecord;

    // Optional UserLoginStatistic only passed in as part of a login request
    private Optional<UserLoginStatistic> userLoginStatistic;


    public UserRecordEvent() {

    }


    public UserRecord getUserRecord() {
        return userRecord;
    }

    public void setUserRecord(UserRecord userRecord) {
        this.userRecord = userRecord;
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

        private UserRecordEvent userRecordEvent = new UserRecordEvent();

        public Builder apiGatewayEventStatusE(APIGatewayEventStatusE apiGatewayEventStatusE) {
            userRecordEvent.apiGatewayEventStatusE = apiGatewayEventStatusE;
            return this;
        }

        public Builder userRecord(UserRecord userRecord) {
            userRecordEvent.userRecord = userRecord;
            return this;
        }

        public Builder userLoginStatistic(UserLoginStatistic userLoginStatistic) {
            userRecordEvent.userLoginStatistic = Optional.of(userLoginStatistic);
            return this;
        }

        public UserRecordEvent build() {
            userRecordEvent.addEventTime();
            return userRecordEvent;
        }
    }
}
