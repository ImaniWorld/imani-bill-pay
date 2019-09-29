package com.imani.bill.pay.domain.property.repository;

import com.imani.bill.pay.domain.property.PropertyEvictionIndex;
import org.springframework.data.solr.repository.SolrCrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author manyce400
 */
@Repository
public interface IPropertyEvictionSolrCrudRepository extends SolrCrudRepository<PropertyEvictionIndex, Long> {


}
