package com.imani.bill.pay.domain.user;

import java.util.Optional;

/**
 * @author manyce400
 */
public enum UserRecordTypeE {

    APIUser,

    BillPayer,

    Student,

    BusinessUser,

    BusinessAdmin,

    PropertyOwner,

    PropertyManager,

    ServiceProvider,

    Tenant,

    HOAMember,

    PlatformSupport,
    ;


    public static Optional<UserRecordTypeE> findByType(String type) {
        for(UserRecordTypeE userRecordTypeE : UserRecordTypeE.values()) {
            if(userRecordTypeE.toString().equals(type)) {
                return Optional.of(userRecordTypeE);
            }
        }

        return Optional.empty();
    }

}
