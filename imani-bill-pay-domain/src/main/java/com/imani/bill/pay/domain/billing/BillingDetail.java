package com.imani.bill.pay.domain.billing;

import com.imani.bill.pay.domain.payment.ACHPaymentInfo;
import com.imani.bill.pay.domain.user.UserRecord;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @author manyce400
 */
public class BillingDetail {


    private UserRecord userRecord;

    private ImaniBill imaniBill;

    private ACHPaymentInfo achPaymentInfo;

    private ImaniBillExplained imaniBillExplained;

    public BillingDetail() {

    }

    public UserRecord getUserRecord() {
        return userRecord;
    }

    public void setUserRecord(UserRecord userRecord) {
        this.userRecord = userRecord;
    }

    public ImaniBill getImaniBill() {
        return imaniBill;
    }

    public void setImaniBill(ImaniBill imaniBill) {
        this.imaniBill = imaniBill;
    }

    public ACHPaymentInfo getAchPaymentInfo() {
        return achPaymentInfo;
    }

    public void setAchPaymentInfo(ACHPaymentInfo achPaymentInfo) {
        this.achPaymentInfo = achPaymentInfo;
    }

    public ImaniBillExplained getImaniBillExplained() {
        return imaniBillExplained;
    }

    public void setImaniBillExplained(ImaniBillExplained imaniBillExplained) {
        this.imaniBillExplained = imaniBillExplained;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("userRecord", userRecord)
                .append("imaniBill", imaniBill)
                .append("achPaymentInfo", achPaymentInfo)
                .append("achPaymentInfo", imaniBillExplained)
                .toString();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private BillingDetail billingDetail = new BillingDetail();

        public Builder userRecord(UserRecord userRecord) {
            billingDetail.userRecord = userRecord;
            return this;
        }

        public Builder imaniBill(ImaniBill imaniBill) {
            billingDetail.imaniBill = imaniBill;
            return this;
        }

        public Builder achPaymentInfo(ACHPaymentInfo achPaymentInfo) {
            billingDetail.achPaymentInfo = achPaymentInfo;
            return this;
        }

        public Builder imaniBillExplained(ImaniBillExplained imaniBillExplained) {
            billingDetail.imaniBillExplained = imaniBillExplained;
            return this;
        }

        public BillingDetail build() {
            return billingDetail;
        }
    }

}
