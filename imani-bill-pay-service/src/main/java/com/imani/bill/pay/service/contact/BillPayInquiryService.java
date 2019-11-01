package com.imani.bill.pay.service.contact;

import com.imani.bill.pay.domain.contact.BillPayInquiry;
import com.imani.bill.pay.domain.contact.repository.IBillPayInquiryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.transaction.Transactional;
import java.util.List;

/**
 * @author manyce400
 */
@Service(BillPayInquiryService.SPRING_BEAN)
public class BillPayInquiryService implements IBillPayInquiryService {



    @Autowired
    private IBillPayInquiryRepository iBillPayInquiryRepository;

    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(BillPayInquiryService.class);

    public static final String SPRING_BEAN = "com.imani.bill.pay.service.contact.BillPayInquiryService";


    @Transactional
    @Override
    public void save(BillPayInquiry billPayInquiry) {
        Assert.notNull(billPayInquiry, "BillPayInquiry cannot be null");
        LOGGER.info("Saving BillPayInquiry:=> {}", billPayInquiry);
        iBillPayInquiryRepository.save(billPayInquiry);
    }

    @Override
    public List<BillPayInquiry> findAll() {
        LOGGER.info("Finding and returning all BillPay Inquiry data.....");
        return iBillPayInquiryRepository.findAll();
    }
}
