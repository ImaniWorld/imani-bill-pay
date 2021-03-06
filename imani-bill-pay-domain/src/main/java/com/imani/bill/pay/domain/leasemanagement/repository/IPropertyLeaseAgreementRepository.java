package com.imani.bill.pay.domain.leasemanagement.repository;

import com.imani.bill.pay.domain.leasemanagement.PropertyLeaseAgreement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author manyce400
 */
@Repository
public interface IPropertyLeaseAgreementRepository extends JpaRepository<PropertyLeaseAgreement, Long> {


}
