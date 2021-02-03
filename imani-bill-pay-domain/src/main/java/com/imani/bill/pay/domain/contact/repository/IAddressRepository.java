package com.imani.bill.pay.domain.contact.repository;

import com.imani.bill.pay.domain.contact.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author manyce400
 */
@Repository
public interface IAddressRepository extends JpaRepository<Address, Long> {


}