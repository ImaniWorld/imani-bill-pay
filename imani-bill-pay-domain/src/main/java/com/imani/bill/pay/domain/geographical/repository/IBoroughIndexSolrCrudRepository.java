package com.imani.bill.pay.domain.geographical.repository;


import com.imani.bill.pay.domain.geographical.BoroughIndex;
import org.springframework.data.solr.repository.SolrCrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author manyce400
 */
@Repository
public interface IBoroughIndexSolrCrudRepository extends SolrCrudRepository<BoroughIndex, Long> {

}