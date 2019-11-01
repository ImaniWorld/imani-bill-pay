package com.imani.bill.pay.domain.contact;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.imani.bill.pay.domain.AuditableRecord;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;

/**
 * @author manyce400
 */
@Entity
@Table(name="BillPayInquiry")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BillPayInquiry extends AuditableRecord {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ID", nullable=false)
    private Long id;

    @Column(name="ContactName", nullable=false, length = 100)
    private String contactName;

    @Column(name="ContactEmail", nullable=false, length = 50)
    private String contactEmail;

    @Column(name="ContactPhone", nullable=true)
    private Long contactPhone;

    @Column(name="BusinessType", nullable=false, length = 100)
    private String businessType;

    @Column(name="NumberOfUsers", nullable=false)
    private Integer numberOfUsers;

    @Column(name="ContactQuestion", nullable=false, length = 300)
    private String contactQuestion;


    public BillPayInquiry() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public Long getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(Long contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public Integer getNumberOfUsers() {
        return numberOfUsers;
    }

    public void setNumberOfUsers(Integer numberOfUsers) {
        this.numberOfUsers = numberOfUsers;
    }

    public String getContactQuestion() {
        return contactQuestion;
    }

    public void setContactQuestion(String contactQuestion) {
        this.contactQuestion = contactQuestion;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("contactName", contactName)
                .append("contactEmail", contactEmail)
                .append("contactPhone", contactPhone)
                .append("businessType", businessType)
                .append("numberOfUsers", numberOfUsers)
                .append("contactQuestion", contactQuestion)
                .toString();
    }

    public static Builder builder() {
        return new Builder();
    }


    public static class Builder {

        private BillPayInquiry billPayInquiry = new BillPayInquiry();

        public Builder contactName(String contactName) {
            billPayInquiry.contactName = contactName;
            return this;
        }

        public Builder contactEmail(String contactEmail) {
            billPayInquiry.contactEmail = contactEmail;
            return this;
        }

        public Builder contactPhone(Long contactPhone) {
            billPayInquiry.contactPhone = contactPhone;
            return this;
        }

        public Builder businessType(String businessType) {
            billPayInquiry.businessType = businessType;
            return this;
        }

        public Builder numberOfUsers(Integer numberOfUsers) {
            billPayInquiry.numberOfUsers = numberOfUsers;
            return this;
        }

        public Builder contactQuestion(String contactQuestion) {
            billPayInquiry.contactQuestion = contactQuestion;
            return this;
        }

        public BillPayInquiry build() {
            return billPayInquiry;
        }
    }
}
