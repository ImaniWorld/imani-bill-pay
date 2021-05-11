package com.imani.bill.pay.domain.utility;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.imani.bill.pay.domain.billing.ImaniBill;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.*;

/**
 * @author manyce400
 */
@Entity
@Table(name="WaterUtilizationCharge")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WaterUtilizationCharge {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ID", nullable=false)
    private Long id;

    @Column(name="Charge", nullable=true)
    private Double charge;

    @Column(name="TotalGallonsUsed", nullable=true)
    private Long totalGallonsUsed;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ImaniBillID", nullable = false)
    private ImaniBill imaniBill;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "UtilizationStartDate", nullable = false, updatable = false)
    @Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    protected DateTime utilizationStart;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "UtilizationEndDate", nullable = false, updatable = false)
    @Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    protected DateTime utilizationEnd;


    public WaterUtilizationCharge() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getCharge() {
        return charge;
    }

    public void setCharge(Double charge) {
        this.charge = charge;
    }

    public boolean hasCharge() {
        return charge != null && charge.doubleValue() > 0;
    }

    public Long getTotalGallonsUsed() {
        return totalGallonsUsed;
    }

    public void setTotalGallonsUsed(Long totalGallonsUsed) {
        this.totalGallonsUsed = totalGallonsUsed;
    }

    public ImaniBill getImaniBill() {
        return imaniBill;
    }

    public void setImaniBill(ImaniBill imaniBill) {
        this.imaniBill = imaniBill;
    }

    public DateTime getUtilizationStart() {
        return utilizationStart;
    }

    public void setUtilizationStart(DateTime utilizationStart) {
        this.utilizationStart = utilizationStart;
    }

    public DateTime getUtilizationEnd() {
        return utilizationEnd;
    }

    public void setUtilizationEnd(DateTime utilizationEnd) {
        this.utilizationEnd = utilizationEnd;
    }


    public WaterUtilizationChargeLite toWaterUtilizationChargeLite() {
        WaterUtilizationChargeLite waterUtilizationChargeLite = WaterUtilizationChargeLite.builder()
                .id(id)
                .charge(charge)
                .totalGallonsUsed(totalGallonsUsed)
                .utilizationStart(utilizationStart)
                .utilizationEnd(utilizationEnd)
                .build();
        return waterUtilizationChargeLite;
    }


    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("charge", charge)
                .append("totalGallonsUsed", totalGallonsUsed)
                .append("imaniBill", imaniBill)
                .append("utilizationStart", utilizationStart)
                .append("utilizationEnd", utilizationEnd)
                .toString();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final WaterUtilizationCharge waterUtilizationCharge = new WaterUtilizationCharge();

        public Builder charge(Double charge) {
            waterUtilizationCharge.charge = charge;
            return this;
        }

        public Builder totalGallonsUsed(Long totalGallonsUsed) {
            waterUtilizationCharge.totalGallonsUsed = totalGallonsUsed;
            return this;
        }

        public Builder imaniBill(ImaniBill imaniBill) {
            waterUtilizationCharge.imaniBill = imaniBill;
            return this;
        }

        public Builder utilizationStart(DateTime utilizationStart) {
            waterUtilizationCharge.utilizationStart = utilizationStart;
            return this;
        }

        public Builder utilizationEnd(DateTime utilizationEnd) {
            waterUtilizationCharge.utilizationEnd = utilizationEnd;
            return this;
        }

        public WaterUtilizationCharge build() {
            return waterUtilizationCharge;
        }

    }

}