package com.imani.bill.pay.domain.education;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.imani.bill.pay.domain.AuditableRecord;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;

/**
 * @author manyce400
 */
@Entity
@Table(name="TuitionGrade")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TuitionGrade extends AuditableRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ID", nullable=false)
    private Long id;

    @Column(name="Grade", nullable = false, length = 100)
    private String grade;

    @Column(name="Description", length = 200)
    private String description;

    @Column(name="TuitionGradeTypeE", nullable=false, length=25)
    @Enumerated(EnumType.STRING)
    private TuitionGradeTypeE tuitionGradeTypeE;


    public TuitionGrade() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TuitionGradeTypeE getTuitionGradeTypeE() {
        return tuitionGradeTypeE;
    }

    public void setTuitionGradeTypeE(TuitionGradeTypeE tuitionGradeTypeE) {
        this.tuitionGradeTypeE = tuitionGradeTypeE;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("grade", grade)
                .append("description", description)
                .append("tuitionGradeTypeE", tuitionGradeTypeE)
                .toString();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private TuitionGrade tuitionGrade = new TuitionGrade();

        public Builder grade(String grade) {
            tuitionGrade.grade = grade;
            return this;
        }

        public Builder description(String description) {
            tuitionGrade.description = description;
            return this;
        }

        public Builder tuitionGradeTypeE(TuitionGradeTypeE tuitionGradeTypeE) {
            tuitionGrade.tuitionGradeTypeE = tuitionGradeTypeE;
            return this;
        }

        public TuitionGrade build() {
            return tuitionGrade;
        }
    }

}
