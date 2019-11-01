package com.imani.bill.pay.zgateway.inquiry;

import com.imani.bill.pay.domain.contact.BillPayInquiry;
import com.imani.bill.pay.service.contact.BillPayInquiryService;
import com.imani.bill.pay.service.contact.IBillPayInquiryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author manyce400
 */
@RestController
@RequestMapping("/billpay/inquiry")
public class BillPayInquiryController {



    @Autowired
    @Qualifier(BillPayInquiryService.SPRING_BEAN)
    private IBillPayInquiryService iBillPayInquiryService;

    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(BillPayInquiryController.class);


    @PostMapping("/new")
    public void processBillPayInquiry(@RequestBody BillPayInquiry billPayInquiry) {
        LOGGER.info("Processing new Imani BillPay inquiry request for.....");
        iBillPayInquiryService.save(billPayInquiry);
    }
}
