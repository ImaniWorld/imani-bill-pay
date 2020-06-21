package com.imani.bill.pay.domain;

import com.imani.bill.pay.domain.user.UserRecord;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * @author manyce400
 */
public class UserAuditableRecord extends AuditableRecord {


    // Tracks the user responsible for creating this auditable record
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CreatedByUserRecordID", nullable = true)
    protected UserRecord createdBy;


    public UserRecord getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(UserRecord createdBy) {
        this.createdBy = createdBy;
    }
}
