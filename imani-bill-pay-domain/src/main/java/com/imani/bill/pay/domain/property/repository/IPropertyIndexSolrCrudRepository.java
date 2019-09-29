package com.imani.bill.pay.domain.property.repository;


import com.imani.bill.pay.domain.property.PropertyIndex;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.solr.repository.Query;
import org.springframework.data.solr.repository.SolrCrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author manyce400
 */
@Repository
public interface IPropertyIndexSolrCrudRepository extends SolrCrudRepository<PropertyIndex, Long> {

    @Query("streetName:*?0* AND borough:*?1*")
    public Page<PropertyIndex> findByStreetNameInBorough(String streetName, String borough, Pageable pageable);

}