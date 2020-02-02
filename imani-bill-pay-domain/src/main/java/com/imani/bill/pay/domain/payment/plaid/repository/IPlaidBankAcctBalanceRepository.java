package com.imani.bill.pay.domain.payment.plaid.repository;

import com.imani.bill.pay.domain.payment.ACHPaymentInfo;
import com.imani.bill.pay.domain.payment.plaid.PlaidBankAcctBalance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author manyce400
 */
@Repository
public interface IPlaidBankAcctBalanceRepository extends JpaRepository<PlaidBankAcctBalance, Long> {


    @Query("Select plaidBankAcctBalance From PlaidBankAcctBalance plaidBankAcctBalance Where plaidBankAcctBalance.achPaymentInfo =?1")
    public List<PlaidBankAcctBalance> findAccountBalances(ACHPaymentInfo achPaymentInfo);

}
