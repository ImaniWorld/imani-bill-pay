package com.imani.bill.pay.domain.geographical.repository;

import com.imani.bill.pay.domain.geographical.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author manyce400
 */
@Repository
public interface ICountryRepository extends JpaRepository<Country, Long> {


}
