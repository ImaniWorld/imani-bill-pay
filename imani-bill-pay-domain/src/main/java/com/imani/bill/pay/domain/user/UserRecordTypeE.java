package com.imani.bill.pay.domain.user;

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


    public boolean isInstitutionalUser() {
        if(this == PropertyManager || this == PropertyOwner || this == ServiceProvider) {
            return true;
        }

        return false;
    }

    public boolean isEndUser() {
        if(this == Tenant || this == HOAMember) {
            return true;
        }

        return false;
    }

}
