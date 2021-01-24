package com.imani.bill.pay.domain.education;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.imani.bill.pay.domain.agreement.EmbeddedAgreementLite;
import com.imani.bill.pay.domain.user.UserRecordLite;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * @author manyce400
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TuitionAgreementLite {


    private Long agreementID;

    private Long schoolID;

    private Long tuitionGradeID;

    private UserRecordLite student;

    private EmbeddedAgreementLite embeddedAgreementLite;


    public TuitionAgreementLite() {

    }

    public Long getAgreementID() {
        return agreementID;
    }

    public void setAgreementID(Long agreementID) {
        this.agreementID = agreementID;
    }

    public Long getSchoolID() {
        return schoolID;
    }

    public void setSchoolID(Long schoolID) {
        this.schoolID = schoolID;
    }

    public Long getTuitionGradeID() {
        return tuitionGradeID;
    }

    public void setTuitionGradeID(Long tuitionGradeID) {
        this.tuitionGradeID = tuitionGradeID;
    }

    public UserRecordLite getStudent() {
        return student;
    }

    public void setStudent(UserRecordLite student) {
        this.student = student;
    }

    public EmbeddedAgreementLite getEmbeddedAgreementLite() {
        return embeddedAgreementLite;
    }

    public void setEmbeddedAgreementLite(EmbeddedAgreementLite embeddedAgreementLite) {
        this.embeddedAgreementLite = embeddedAgreementLite;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("agreementID", agreementID)
                .append("schoolID", schoolID)
                .append("tuitionGradeID", tuitionGradeID)
                .append("student", student)
                .append("embeddedAgreementLite", embeddedAgreementLite)
                .toString();
    }

}
