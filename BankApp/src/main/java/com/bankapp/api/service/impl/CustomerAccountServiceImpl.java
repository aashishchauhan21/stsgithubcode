package com.bankapp.api.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bankapp.api.dao.AccountTransactionDAO;
import com.bankapp.api.dao.CustomerAccountDAO;
import com.bankapp.api.model.AccountTransaction;
import com.bankapp.api.model.CustomerAccount;
import com.bankapp.api.model.TransactionType;
import com.bankapp.api.service.CustomerAccountService;

@Service
public class CustomerAccountServiceImpl implements CustomerAccountService{
	
	@Autowired
	private CustomerAccountDAO customerAccountDAO;
	
	@Autowired
	private AccountTransactionDAO accountTransactionDAO;

	@Override
	public void createCustomerAccount(CustomerAccount customerAccount) {
		customerAccountDAO.save(customerAccount);
	}

	@Override
	public CustomerAccount getCustomerAccountInfo(String acctNo) {
		return customerAccountDAO.findById(acctNo).orElse(null);
	}	

	@Override
	public double getBalance(String acctNo) {
		return customerAccountDAO.findBalanceByAcctNo(acctNo);	
	}
	
	@Transactional
	public void depositAmount(String acctNo, double amount) {
		customerAccountDAO.saveBalanceByAcctNo(acctNo, amount);
		accountTransactionDAO.save(new AccountTransaction(acctNo, TransactionType.DEPOSIT.getId(), amount, new Date()));
	}

	@Transactional
	public void transferAmount(String acctNo, String destAcctNo, double amount) {
		customerAccountDAO.withdrawAmountByAcctNo(acctNo, amount);		
		customerAccountDAO.saveBalanceByAcctNo(destAcctNo, amount);
		accountTransactionDAO.save(new AccountTransaction(acctNo, TransactionType.WITHDRAWAL.getId(), amount, new Date()));
		accountTransactionDAO.save(new AccountTransaction(destAcctNo, TransactionType.DEPOSIT.getId(), amount, new Date()));		
	}

}
