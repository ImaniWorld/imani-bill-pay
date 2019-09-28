package com.imani.bill.pay.domain.geographical.repository;

import com.imani.bill.pay.domain.geographical.Borough;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author manyce400
 */
@Repository
public interface IBoroughRepository extends JpaRepository<Borough, Long> {


    @Query("Select  borough From Borough borough Where borough.name =?1")
    public Optional<Borough> findByName(String name);

}
