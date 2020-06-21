package com.imani.bill.pay.service.mock;

import com.imani.bill.pay.domain.contact.EmbeddedContactInfo;
import com.imani.bill.pay.domain.user.UserRecord;

/**
 * Simple interface that builds a default UserRecord to make it easy to run tests that require UserRecord
 *
 * @author manyce400
 */
public interface IMockUserRecordTestBuilder {

    public default UserRecord buildUserRecord() {
        EmbeddedContactInfo embeddedContactInfo = EmbeddedContactInfo.builder()
                .email("test.user@imani.com")
                .build();

        UserRecord userRecord = UserRecord.builder()
                .firstName("Test")
                .lastName("User")
                .embeddedContactInfo(embeddedContactInfo)
                .build();
        return userRecord;
    }

}
