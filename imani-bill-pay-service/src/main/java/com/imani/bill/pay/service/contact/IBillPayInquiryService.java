package com.imani.bill.pay.service.contact;

import com.imani.bill.pay.domain.contact.BillPayInquiry;

import java.util.List;

/**
 * @author manyce400
 */
public interface IBillPayInquiryService {

    public void save(BillPayInquiry billPayInquiry);

    public List<BillPayInquiry> findAll();
}
