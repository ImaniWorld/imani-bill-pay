package com.imani.bill.pay.domain.education;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.imani.bill.pay.domain.AuditableRecord;
import com.imani.bill.pay.domain.business.Business;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;

/**
 * Maps a School(Business) to the Tuition Grades that the School offers
 *
 * @author manyce400
 */
@Entity
@Table(name="SchoolToTuitionGrade")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SchoolToTuitionGrade extends AuditableRecord {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ID", nullable=false)
    private Long id;

    // Specifies the number of days past a payment due date for a payment to be considered late
    // This is exposed to allow customizing number of days till late payment for each TuitionGrade
    @Column(name="NumberOfDaysTillLate", nullable=true)
    private Integer numberOfDaysTillLate;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BusinessID", nullable = false)
    private Business school;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TuitionGradeID", nullable = false)
    private TuitionGrade tuitionGrade;

    public SchoolToTuitionGrade() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNumberOfDaysTillLate() {
        return numberOfDaysTillLate;
    }

    public void setNumberOfDaysTillLate(Integer numberOfDaysTillLate) {
        this.numberOfDaysTillLate = numberOfDaysTillLate;
    }

    public Business getSchool() {
        return school;
    }

    public void setSchool(Business school) {
        this.school = school;
    }

    public TuitionGrade getTuitionGrade() {
        return tuitionGrade;
    }

    public void setTuitionGrade(TuitionGrade tuitionGrade) {
        this.tuitionGrade = tuitionGrade;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("school", school)
                .append("tuitionGrade", tuitionGrade)
                .toString();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private SchoolToTuitionGrade schoolToTuitionGrade = new SchoolToTuitionGrade();

        public Builder school(Business school) {
            schoolToTuitionGrade.school = school;
            return this;
        }

        public Builder tuitionGrade(TuitionGrade tuitionGrade) {
            schoolToTuitionGrade.tuitionGrade = tuitionGrade;
            return this;
        }

        public SchoolToTuitionGrade build() {
            return schoolToTuitionGrade;
        }
    }
}
