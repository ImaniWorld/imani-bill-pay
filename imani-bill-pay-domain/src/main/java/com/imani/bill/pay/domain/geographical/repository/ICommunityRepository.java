package com.imani.bill.pay.domain.geographical.repository;

import com.imani.bill.pay.domain.geographical.Community;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author manyce400
 */
@Repository
public interface ICommunityRepository extends JpaRepository<Community, Long> {


}
