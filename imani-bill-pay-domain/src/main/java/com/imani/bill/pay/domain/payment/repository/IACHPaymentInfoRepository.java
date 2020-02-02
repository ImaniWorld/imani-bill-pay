package com.imani.bill.pay.domain.payment.repository;

import com.imani.bill.pay.domain.payment.ACHPaymentInfo;
import com.imani.bill.pay.domain.property.PropertyManager;
import com.imani.bill.pay.domain.property.PropertyOwner;
import com.imani.bill.pay.domain.user.UserRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * @author manyce400
 */
@Repository
public interface IACHPaymentInfoRepository extends JpaRepository<ACHPaymentInfo, Long> {


    @Query("Select aCHPaymentInfo From ACHPaymentInfo aCHPaymentInfo Where aCHPaymentInfo.userRecord =?1 and aCHPaymentInfo.isPrimary = true")
    public ACHPaymentInfo findPrimaryUserACHPaymentInfo(UserRecord userRecord);

    @Query("Select aCHPaymentInfo From ACHPaymentInfo aCHPaymentInfo Where aCHPaymentInfo.propertyManager =?1")
    public ACHPaymentInfo findPropertyManagerACHPaymentInfo(PropertyManager propertyManager);

    @Query("Select aCHPaymentInfo From ACHPaymentInfo aCHPaymentInfo Where aCHPaymentInfo.propertyOwner =?1")
    public ACHPaymentInfo findPropertyOwnerACHPaymentInfo(PropertyOwner propertyOwner);

}
