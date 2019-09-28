package com.imani.bill.pay.domain.user.repository;

import com.imani.bill.pay.domain.user.UserRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * @author manyce400
 */
@Repository
public interface IUserRecordRepository extends JpaRepository<UserRecord, Long> {

    @Query("Select userRecord From UserRecord userRecord Where userRecord.embeddedContactInfo.email = ?1")
    public UserRecord findByUserEmail(String email);

    @Query("Select userRecord From UserRecord userRecord Where userRecord.embeddedContactInfo.email = ?1 and userRecord.embeddedContactInfo.mobilePhone = ?2")
    public UserRecord findByUserEmailAndMobilePhone(String email, Long mobilePhone);

}
