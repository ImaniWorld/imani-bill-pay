package com.imani.bill.pay.domain.contact.repository;

import com.imani.bill.pay.domain.contact.BillPayInquiry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author manyce400
 */
@Repository
public interface IBillPayInquiryRepository extends JpaRepository<BillPayInquiry, Long> {


}
