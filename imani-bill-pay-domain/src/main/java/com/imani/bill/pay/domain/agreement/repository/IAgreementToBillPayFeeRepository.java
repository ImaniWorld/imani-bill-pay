package com.imani.bill.pay.domain.agreement.repository;

import com.imani.bill.pay.domain.agreement.AgreementToScheduleBillPayFee;
import com.imani.bill.pay.domain.utility.SewerServiceAgreement;
import com.imani.bill.pay.domain.utility.WaterServiceAgreement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author manyce400
 */
public interface IAgreementToBillPayFeeRepository extends JpaRepository<AgreementToScheduleBillPayFee, Long> {


    @Query("Select agreementToScheduleBillPayFee From AgreementToScheduleBillPayFee agreementToScheduleBillPayFee Where agreementToScheduleBillPayFee.waterServiceAgreement = ?1 and agreementToScheduleBillPayFee.enforced = 1")
    public List<AgreementToScheduleBillPayFee> findWaterServiceAgreementSchdFees(WaterServiceAgreement waterServiceAgreement);

    @Query("Select agreementToScheduleBillPayFee From AgreementToScheduleBillPayFee agreementToScheduleBillPayFee Where agreementToScheduleBillPayFee.sewerServiceAgreement = ?1 and agreementToScheduleBillPayFee.enforced = 1")
    public List<AgreementToScheduleBillPayFee> findSewerServiceAgreementSchdFees(SewerServiceAgreement sewerServiceAgreement);

}