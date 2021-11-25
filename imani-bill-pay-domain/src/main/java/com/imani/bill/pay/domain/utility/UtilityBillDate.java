package com.imani.bill.pay.domain.utility;

import com.imani.bill.pay.domain.billing.BillScheduleTypeE;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.joda.time.DateTime;

/**
 * @author manyce400
 */
public class UtilityBillDate {


    private DateTime due;

    // Tracks utilization start
    private DateTime utilStart;

    // Tracks utilization end
    private DateTime utilEnd;

    private BillScheduleTypeE billScheduleTypeE;

    public UtilityBillDate() {

    }

    public DateTime getDue() {
        return due;
    }

    public void setDue(DateTime due) {
        this.due = due;
    }

    public DateTime getUtilStart() {
        return utilStart;
    }

    public void setUtilStart(DateTime utilStart) {
        this.utilStart = utilStart;
    }

    public DateTime getUtilEnd() {
        return utilEnd;
    }

    public void setUtilEnd(DateTime utilEnd) {
        this.utilEnd = utilEnd;
    }

    public BillScheduleTypeE getBillScheduleTypeE() {
        return billScheduleTypeE;
    }

    public void setBillScheduleTypeE(BillScheduleTypeE billScheduleTypeE) {
        this.billScheduleTypeE = billScheduleTypeE;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("due", due)
                .append("utilStart", utilStart)
                .append("utilEnd", utilEnd)
                .append("billScheduleTypeE", billScheduleTypeE)
                .toString();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private final UtilityBillDate utilityBillDate = new UtilityBillDate();

        public Builder due(DateTime due) {
            utilityBillDate.due = due;
            return this;
        }

        public Builder utilStart(DateTime utilStart) {
            utilityBillDate.utilStart = utilStart;
            return this;
        }

        public Builder utilEnd(DateTime utilEnd) {
            utilityBillDate.utilEnd = utilEnd;
            return this;
        }

        public Builder billScheduleTypeE(BillScheduleTypeE billScheduleTypeE) {
            utilityBillDate.billScheduleTypeE = billScheduleTypeE;
            return this;
        }

        public UtilityBillDate build() {
            return utilityBillDate;
        }
    }
}
