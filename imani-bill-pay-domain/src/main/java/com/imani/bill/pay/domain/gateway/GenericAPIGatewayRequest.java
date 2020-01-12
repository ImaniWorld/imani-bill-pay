package com.imani.bill.pay.domain.gateway;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.imani.bill.pay.domain.user.UserRecord;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.util.Assert;

import java.util.Optional;

/**
 * @author manyce400
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GenericAPIGatewayRequest {


    // UserRecord that this Request applies to.  All actions will be executed for this user.
    protected Optional<UserRecord> execUserRecord;

    public GenericAPIGatewayRequest() {
    }

    public GenericAPIGatewayRequest(UserRecord execUserRecord) {
        Assert.notNull(execUserRecord, "UserRecord cannot be null");
        this.execUserRecord = Optional.of(execUserRecord);
    }

    public Optional<UserRecord> getExecUserRecord() {
        return execUserRecord;
    }

    public void setExecUserRecord(Optional<UserRecord> execUserRecord) {
        this.execUserRecord = execUserRecord;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("execUserRecord", execUserRecord)
                .toString();
    }

    public static GenericAPIGatewayRequest build(UserRecord execUserRecord) {
        return new GenericAPIGatewayRequest(execUserRecord);
    }

}
