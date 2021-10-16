package com.bankapp.api.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.bankapp.api.Entity.CustomerAccount;

@Repository
public interface CustomerAccountDAO
		extends CrudRepository<CustomerAccount, String>, JpaRepository<CustomerAccount, String> {

	@Query("select balance from CustomerAccount where acctNo = ?1")
	public double findBalanceByAcctNo(String acctNo);

	@Modifying(clearAutomatically = true)
	@Query("update CustomerAccount set balance = balance+?2 where acctNo=?1")
	public void saveBalanceByAcctNo(String acctNo, double balance);

	@Modifying(clearAutomatically = true)
	@Query("update CustomerAccount set balance = balance-?2 where acctNo=?1")
	public void withdrawAmountByAcctNo(String acctNo, double balance);
}
