package com.imani.bill.pay.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * Embeddable class that tracks and persist details about when an API call starts and ends
 *
 * @author manyce400
 */
@Embeddable
@JsonInclude(JsonInclude.Include.NON_NULL)
public class APICallTimeRecord {



    // Start Datetime for when API call was made
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "ApiCallStart", nullable = false, updatable = false)
    @Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @CreatedDate
    private DateTime apiCallStart;

    // End Datetime for when API call completed
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "ApiCallEnd", nullable = false, updatable = false)
    @Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @CreatedDate
    private DateTime apiCallEnd;


    public APICallTimeRecord() {

    }

    public DateTime getApiCallStart() {
        return apiCallStart;
    }

    public void setApiCallStart(DateTime apiCallStart) {
        this.apiCallStart = apiCallStart;
    }

    public void startApiCall() {
        this.apiCallStart = DateTime.now();
    }

    public DateTime getApiCallEnd() {
        return apiCallEnd;
    }

    public void setApiCallEnd(DateTime apiCallEnd) {
        this.apiCallEnd = apiCallEnd;
    }

    public void endApiCall() {
        this.apiCallEnd = DateTime.now();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE)
                .append("apiCallStart", apiCallStart)
                .append("apiCallEnd", apiCallEnd)
                .toString();
    }

    public static APICallTimeRecord start() {
        APICallTimeRecord apiCallTimeRecord = new APICallTimeRecord();
        apiCallTimeRecord.startApiCall();
        return apiCallTimeRecord;
    }

}
