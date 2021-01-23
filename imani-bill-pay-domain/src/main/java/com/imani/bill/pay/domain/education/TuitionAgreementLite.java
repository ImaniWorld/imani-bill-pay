package com.imani.bill.pay.domain.education;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.imani.bill.pay.domain.agreement.EmbeddedAgreement;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * @author manyce400
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TuitionAgreementLite {


    private Long agreementID;

    private Long businessID;

    private String studentFirstName;

    private String studentLastName;

    private EmbeddedAgreement embeddedAgreement;


    public TuitionAgreementLite() {

    }

    public Long getAgreementID() {
        return agreementID;
    }

    public void setAgreementID(Long agreementID) {
        this.agreementID = agreementID;
    }

    public Long getBusinessID() {
        return businessID;
    }

    public void setBusinessID(Long businessID) {
        this.businessID = businessID;
    }

    public String getStudentFirstName() {
        return studentFirstName;
    }

    public void setStudentFirstName(String studentFirstName) {
        this.studentFirstName = studentFirstName;
    }

    public String getStudentLastName() {
        return studentLastName;
    }

    public void setStudentLastName(String studentLastName) {
        this.studentLastName = studentLastName;
    }

    public EmbeddedAgreement getEmbeddedAgreement() {
        return embeddedAgreement;
    }

    public void setEmbeddedAgreement(EmbeddedAgreement embeddedAgreement) {
        this.embeddedAgreement = embeddedAgreement;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("agreementID", agreementID)
                .append("businessID", businessID)
                .append("studentFirstName", studentFirstName)
                .append("studentLastName", studentLastName)
                .append("embeddedAgreement", embeddedAgreement)
                .toString();
    }

}
