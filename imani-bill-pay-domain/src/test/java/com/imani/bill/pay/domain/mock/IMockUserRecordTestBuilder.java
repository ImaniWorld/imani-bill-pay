package com.imani.bill.pay.domain.mock;

import com.imani.bill.pay.domain.contact.EmbeddedContactInfo;
import com.imani.bill.pay.domain.user.UserRecord;

/**
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

    public default UserRecord buildUserRecord(String email, String firstName, String lastName) {
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
