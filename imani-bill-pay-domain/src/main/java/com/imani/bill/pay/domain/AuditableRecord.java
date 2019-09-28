package com.imani.bill.pay.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

/**
 * Parent class for tracking all audit changes automatically.  Subclasses can enable this feature by extending this class
 *
 * @author manyce400
 */
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class AuditableRecord {



    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "CreateDate", nullable = false, updatable = false)
    @Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @CreatedDate
    protected DateTime createDate;


    // For security reasons dont expose audit field values in JSON
    @JsonIgnore
    @Column(name = "ModifyDate")
    @Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @LastModifiedDate
    protected DateTime modifyDate;


    public DateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(DateTime createDate) {
        this.createDate = createDate;
    }

    public DateTime getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(DateTime modifyDate) {
        this.modifyDate = modifyDate;
    }

}
