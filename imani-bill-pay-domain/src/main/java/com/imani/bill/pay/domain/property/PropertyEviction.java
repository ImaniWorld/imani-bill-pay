package com.imani.bill.pay.domain.property;

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
@Table(name="PropertyEviction", indexes = {@Index(name = "Property_Eviction_Info_IDX", columnList = "CourtIndexNumber, DocketNumber")})
public class PropertyEviction {



    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ID", nullable=false)
    private Long id;


    @Column(name="CourtIndexNumber", nullable=false, length=50)
    private String courtIndexNumber;


    @Column(name="DocketNumber", nullable=false, length=50)
    private String docketNumber;


    @Column(name="EvictionAptNumber", nullable=false, length=50)
    private String evictionAptNumber;


    @Column(name="EvictionStatus", nullable=false, length=50)
    private String evictionStatus;


    // Information on the Marshal who conducted the eviction
    @Column(name="MarshalFirstName", nullable=false, length=50)
    private String marshalFirstName;


    @Column(name="MarshalLastName", nullable=false, length=50)
    private String marshalLastName;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PropertyInfoID", nullable = true)
    private Property property;


    @Column(name = "ExecutionDate")
    @Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime executionDate;


    public PropertyEviction() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCourtIndexNumber() {
        return courtIndexNumber;
    }

    public void setCourtIndexNumber(String courtIndexNumber) {
        this.courtIndexNumber = courtIndexNumber;
    }

    public String getDocketNumber() {
        return docketNumber;
    }

    public void setDocketNumber(String docketNumber) {
        this.docketNumber = docketNumber;
    }

    public String getEvictionAptNumber() {
        return evictionAptNumber;
    }

    public void setEvictionAptNumber(String evictionAptNumber) {
        this.evictionAptNumber = evictionAptNumber;
    }

    public String getEvictionStatus() {
        return evictionStatus;
    }

    public void setEvictionStatus(String evictionStatus) {
        this.evictionStatus = evictionStatus;
    }

    public String getMarshalFirstName() {
        return marshalFirstName;
    }

    public void setMarshalFirstName(String marshalFirstName) {
        this.marshalFirstName = marshalFirstName;
    }

    public String getMarshalLastName() {
        return marshalLastName;
    }

    public void setMarshalLastName(String marshalLastName) {
        this.marshalLastName = marshalLastName;
    }

    public Property getProperty() {
        return property;
    }

    public void setProperty(Property property) {
        this.property = property;
    }

    public DateTime getExecutionDate() {
        return executionDate;
    }

    public void setExecutionDate(DateTime executionDate) {
        this.executionDate = executionDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        PropertyEviction that = (PropertyEviction) o;

        return new EqualsBuilder()
                .append(id, that.id)
                .append(courtIndexNumber, that.courtIndexNumber)
                .append(docketNumber, that.docketNumber)
                .append(evictionAptNumber, that.evictionAptNumber)
                .append(evictionStatus, that.evictionStatus)
                .append(marshalFirstName, that.marshalFirstName)
                .append(marshalLastName, that.marshalLastName)
                .append(property, that.property)
                .append(executionDate, that.executionDate)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .append(courtIndexNumber)
                .append(docketNumber)
                .append(evictionAptNumber)
                .append(evictionStatus)
                .append(marshalFirstName)
                .append(marshalLastName)
                .append(property)
                .append(executionDate)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("courtIndexNumber", courtIndexNumber)
                .append("docketNumber", docketNumber)
                .append("evictionAptNumber", evictionAptNumber)
                .append("evictionStatus", evictionStatus)
                .append("marshalFirstName", marshalFirstName)
                .append("marshalLastName", marshalLastName)
                .append("property", property)
                .append("executionDate", executionDate)
                .toString();
    }
}
