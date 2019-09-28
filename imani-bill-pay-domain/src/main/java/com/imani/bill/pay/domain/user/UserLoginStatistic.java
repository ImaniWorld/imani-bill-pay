package com.imani.bill.pay.domain.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.imani.bill.pay.domain.AuditableRecord;
import com.imani.bill.pay.domain.device.DeviceTypeE;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.*;

/**
 * @author manyce400
 */
@Entity
@Table(name="UserLoginStatistic")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserLoginStatistic extends AuditableRecord {




    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ID", nullable=false)
    private Long id;


    @Column(name="DeviceType", nullable=true, length=20)
    @Enumerated(EnumType.STRING)
    private DeviceTypeE deviceTypeE;


    @Column(name="DeviceVersion", nullable=true, length=100)
    private String deviceVersion;


    @Column(name="DeviceOS", nullable=true, length=100)
    private String deviceOS;


    // Tracks the version of Imani on user's client
    @Column(name="ImaniClientVersion", nullable=true, length=100)
    private String iManiClientVersion;


    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "LastLoginDate", nullable = true)
    @Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime loginDate;


    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "LastLogoutDate", nullable = true)
    @Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime logoutDate;


    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UserRecordID", nullable = true)
    private UserRecord userRecord;


    public UserLoginStatistic() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DeviceTypeE getDeviceTypeE() {
        return deviceTypeE;
    }

    public void setDeviceTypeE(DeviceTypeE deviceTypeE) {
        this.deviceTypeE = deviceTypeE;
    }

    public String getDeviceVersion() {
        return deviceVersion;
    }

    public void setDeviceVersion(String deviceVersion) {
        this.deviceVersion = deviceVersion;
    }

    public String getDeviceOS() {
        return deviceOS;
    }

    public void setDeviceOS(String deviceOS) {
        this.deviceOS = deviceOS;
    }

    public String getiManiClientVersion() {
        return iManiClientVersion;
    }

    public void setiManiClientVersion(String iManiClientVersion) {
        this.iManiClientVersion = iManiClientVersion;
    }

    public DateTime getLoginDate() {
        return loginDate;
    }

    public void setLoginDate(DateTime loginDate) {
        this.loginDate = loginDate;
    }

    public DateTime getLogoutDate() {
        return logoutDate;
    }

    public void setLogoutDate(DateTime logoutDate) {
        this.logoutDate = logoutDate;
    }

    public UserRecord getUserRecord() {
        return userRecord;
    }

    public void setUserRecord(UserRecord userRecord) {
        this.userRecord = userRecord;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        UserLoginStatistic that = (UserLoginStatistic) o;

        return new EqualsBuilder()
                .append(id, that.id)
                .append(deviceTypeE, that.deviceTypeE)
                .append(deviceVersion, that.deviceVersion)
                .append(deviceOS, that.deviceOS)
                .append(iManiClientVersion, that.iManiClientVersion)
                .append(loginDate, that.loginDate)
                .append(logoutDate, that.logoutDate)
                .append(userRecord, that.userRecord)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .append(deviceTypeE)
                .append(deviceVersion)
                .append(deviceOS)
                .append(iManiClientVersion)
                .append(loginDate)
                .append(logoutDate)
                .append(userRecord)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("deviceTypeE", deviceTypeE)
                .append("deviceVersion", deviceVersion)
                .append("deviceOS", deviceOS)
                .append("iManiClientVersion", iManiClientVersion)
                .append("loginDate", loginDate)
                .append("logoutDate", logoutDate)
                .append("userRecord", userRecord)
                .toString();
    }


    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private UserLoginStatistic userLoginStatistic = new UserLoginStatistic();


        public Builder deviceTypeE(DeviceTypeE deviceTypeE) {
            userLoginStatistic.deviceTypeE = deviceTypeE;
            return this;
        }

        public Builder deviceVersion(String deviceVersion) {
            userLoginStatistic.deviceVersion = deviceVersion;
            return this;
        }

        public Builder deviceOS(String deviceOS) {
            userLoginStatistic.deviceOS = deviceOS;
            return this;
        }

        public Builder iManiClientVersion(String iManiClientVersion) {
            userLoginStatistic.iManiClientVersion = iManiClientVersion;
            return this;
        }

        public Builder loginDate(DateTime loginDate) {
            userLoginStatistic.loginDate = loginDate;
            return this;
        }

        public Builder logoutDate(DateTime logoutDate) {
            userLoginStatistic.logoutDate = logoutDate;
            return this;
        }

        public Builder userRecord(UserRecord userRecord) {
            userLoginStatistic.userRecord = userRecord;
            return this;
        }

        public UserLoginStatistic build() {
            return userLoginStatistic;
        }

    }


}
