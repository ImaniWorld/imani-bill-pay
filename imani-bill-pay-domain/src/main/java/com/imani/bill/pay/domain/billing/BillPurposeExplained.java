package com.imani.bill.pay.domain.billing;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.joda.time.DateTime;

/**
 * @author manyce400
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BillPurposeExplained {


    private BillServiceRenderedTypeE billServiceRenderedTypeE;

    private String student;

    private String property;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private DateTime billScheduleDate;

    private BillScheduleTypeE billScheduleTypeE;

    private Double fixedCost;

    public BillPurposeExplained() {

    }

    public BillServiceRenderedTypeE getBillServiceRenderedTypeE() {
        return billServiceRenderedTypeE;
    }

    public void setBillServiceRenderedTypeE(BillServiceRenderedTypeE billServiceRenderedTypeE) {
        this.billServiceRenderedTypeE = billServiceRenderedTypeE;
    }

    public String getStudent() {
        return student;
    }

    public void setStudent(String student) {
        this.student = student;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public DateTime getBillScheduleDate() {
        return billScheduleDate;
    }

    public void setBillScheduleDate(DateTime billScheduleDate) {
        this.billScheduleDate = billScheduleDate;
    }

    public BillScheduleTypeE getBillScheduleTypeE() {
        return billScheduleTypeE;
    }

    public void setBillScheduleTypeE(BillScheduleTypeE billScheduleTypeE) {
        this.billScheduleTypeE = billScheduleTypeE;
    }

    public Double getFixedCost() {
        return fixedCost;
    }

    public void setFixedCost(Double fixedCost) {
        this.fixedCost = fixedCost;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("billServiceRenderedTypeE", billServiceRenderedTypeE)
                .append("student", student)
                .append("property", property)
                .append("billScheduleDate", billScheduleDate)
                .append("billScheduleTypeE", billScheduleTypeE)
                .append("fixedCost", fixedCost)
                .toString();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private BillPurposeExplained billPurposeExplained = new BillPurposeExplained();

        public Builder billServiceRenderedTypeE(BillServiceRenderedTypeE billServiceRenderedTypeE) {
            billPurposeExplained.billServiceRenderedTypeE = billServiceRenderedTypeE;
            return this;
        }

        public Builder student(String student) {
            billPurposeExplained.student = student;
            return this;
        }

        public Builder property(String property) {
            billPurposeExplained.property = property;
            return this;
        }

        public Builder billScheduleDate(DateTime billScheduleDate) {
            billPurposeExplained.billScheduleDate = billScheduleDate;
            return this;
        }

        public Builder billScheduleTypeE(BillScheduleTypeE billScheduleTypeE) {
            billPurposeExplained.billScheduleTypeE = billScheduleTypeE;
            return this;
        }

        public Builder fixedCost(Double fixedCost) {
            billPurposeExplained.fixedCost = fixedCost;
            return this;
        }

        public BillPurposeExplained build() {
            return billPurposeExplained;
        }

    }

}
