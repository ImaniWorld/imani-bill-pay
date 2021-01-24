package com.imani.bill.pay.domain.agreement;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.imani.bill.pay.domain.billing.BillScheduleTypeE;
import com.imani.bill.pay.domain.user.UserRecord;
import com.imani.bill.pay.domain.user.UserRecordLite;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.joda.time.DateTime;

/**
 * @author manyce400
 */
public class EmbeddedAgreementLite {


    private Double fixedCost;

    private BillScheduleTypeE billScheduleTypeE;

    private boolean agreementInForce;

    private Integer numberOfDaysTillLate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private DateTime effectiveDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private DateTime terminationDate;

    private String agreementDocument;

    private UserRecordLite userBilled;


    public EmbeddedAgreementLite() {

    }

    public Double getFixedCost() {
        return fixedCost;
    }

    public void setFixedCost(Double fixedCost) {
        this.fixedCost = fixedCost;
    }

    public BillScheduleTypeE getBillScheduleTypeE() {
        return billScheduleTypeE;
    }

    public void setBillScheduleTypeE(BillScheduleTypeE billScheduleTypeE) {
        this.billScheduleTypeE = billScheduleTypeE;
    }

    public boolean isAgreementInForce() {
        return agreementInForce;
    }

    public void setAgreementInForce(boolean agreementInForce) {
        this.agreementInForce = agreementInForce;
    }

    public Integer getNumberOfDaysTillLate() {
        return numberOfDaysTillLate;
    }

    public void setNumberOfDaysTillLate(Integer numberOfDaysTillLate) {
        this.numberOfDaysTillLate = numberOfDaysTillLate;
    }

    public DateTime getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(DateTime effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public DateTime getTerminationDate() {
        return terminationDate;
    }

    public void setTerminationDate(DateTime terminationDate) {
        this.terminationDate = terminationDate;
    }

    public String getAgreementDocument() {
        return agreementDocument;
    }

    public void setAgreementDocument(String agreementDocument) {
        this.agreementDocument = agreementDocument;
    }

    public UserRecordLite getUserBilled() {
        return userBilled;
    }

    public void setUserBilled(UserRecordLite userBilled) {
        this.userBilled = userBilled;
    }

    public EmbeddedAgreement toEmbeddedAgreement(UserRecord billedUser) {
        EmbeddedAgreement embeddedAgreement = EmbeddedAgreement.builder()
                .fixedCost(fixedCost)
                .billScheduleTypeE(billScheduleTypeE)
                .agreementInForce(agreementInForce)
                .numberOfDaysTillLate(numberOfDaysTillLate)
                .effectiveDate(effectiveDate)
                .terminationDate(terminationDate)
                .agreementDocument(agreementDocument)
                .userRecord(billedUser)
                .build();
        return embeddedAgreement;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("fixedCost", fixedCost)
                .append("billScheduleTypeE", billScheduleTypeE)
                .append("agreementInForce", agreementInForce)
                .append("numberOfDaysTillLate", numberOfDaysTillLate)
                .append("effectiveDate", effectiveDate)
                .append("terminationDate", terminationDate)
                .append("agreementDocument", agreementDocument)
                .toString();
    }
}
