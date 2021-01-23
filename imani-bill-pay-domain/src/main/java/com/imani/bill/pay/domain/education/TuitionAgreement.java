package com.imani.bill.pay.domain.education;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.imani.bill.pay.domain.AuditableRecord;
import com.imani.bill.pay.domain.agreement.EmbeddedAgreement;
import com.imani.bill.pay.domain.agreement.IHasBillingAgreement;
import com.imani.bill.pay.domain.business.Business;
import com.imani.bill.pay.domain.user.UserRecord;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;

/**
 * @author manyce400
 */
@Entity
@Table(name="TuitionAgreement")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TuitionAgreement extends AuditableRecord implements IHasBillingAgreement {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ID", nullable=false)
    private Long id;

    // Tracks Student as a UserRecord for which tuition agreement is being made
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "StudentUserRecordID", nullable = false)
    private UserRecord student;

    // Tracks the TuitionGrade that this agreement has been made for
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "TuitionGradeID", nullable = false)
    private TuitionGrade tuitionGrade;

    @Embedded
    private EmbeddedAgreement embeddedAgreement;

    // Tracks the Business which agreement is made with
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "BusinessID", nullable = false)
    private Business business;


    public TuitionAgreement() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserRecord getStudent() {
        return student;
    }

    public void setStudent(UserRecord student) {
        this.student = student;
    }

    public TuitionGrade getTuitionGrade() {
        return tuitionGrade;
    }

    public void setTuitionGrade(TuitionGrade tuitionGrade) {
        this.tuitionGrade = tuitionGrade;
    }

    public EmbeddedAgreement getEmbeddedAgreement() {
        return embeddedAgreement;
    }

    public void setEmbeddedAgreement(EmbeddedAgreement embeddedAgreement) {
        this.embeddedAgreement = embeddedAgreement;
    }

    public Business getBusiness() {
        return business;
    }

    public void setBusiness(Business business) {
        this.business = business;
    }

    @Override
    public String describeAgreement() {
        StringBuffer sb = new StringBuffer("TuitionAgreement[Student: ")
                .append(student.getFullName())
                .append("; FixedCost: ")
                .append(embeddedAgreement.getFixedCost())
                .append("; School: ")
                .append(business.getName())
                .append("]");
        return sb.toString();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("student", student)
                .append("tuitionGrade", tuitionGrade)
                .append("embeddedAgreement", embeddedAgreement)
                .append("business", business)
                .append("createDate", createDate)
                .append("modifyDate", modifyDate)
                .toString();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private TuitionAgreement tuitionAgreement = new TuitionAgreement();

        public Builder student(UserRecord student) {
            tuitionAgreement.student = student;
            return this;
        }

        public Builder tuitionGrade(TuitionGrade tuitionGrade) {
            tuitionAgreement.tuitionGrade = tuitionGrade;
            return this;
        }

        public Builder embeddedAgreement(EmbeddedAgreement embeddedAgreement) {
            tuitionAgreement.embeddedAgreement = embeddedAgreement;
            return this;
        }

        public Builder business(Business business) {
            tuitionAgreement.business = business;
            return this;
        }

        public TuitionAgreement build() {
            return tuitionAgreement;
        }
    }

}