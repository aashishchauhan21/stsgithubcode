package com.bankapp.api.service.impl;

import java.util.Date;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
	
	private static final Logger logger = LoggerFactory.getLogger(CustomerAccountServiceImpl.class);
	
	@Autowired
	private CustomerAccountDAO customerAccountDAO;
	
	@Autowired
	private AccountTransactionDAO accountTransactionDAO;

	@Override
	public CustomerAccount createCustomerAccount(CustomerAccount customerAccount) {
		logger.info("Entering in createCustomerAccount :", System.currentTimeMillis());	
		CustomerAccount savedCustomer = null;
		savedCustomer = customerAccountDAO.save(customerAccount);
		return savedCustomer;
	}

	@Override
	public CustomerAccount getCustomerAccountInfo(String acctNo) {
		logger.info("Entering in getCustomerAccountInfo :", System.currentTimeMillis());	
		return customerAccountDAO.findById(acctNo).orElse(null);
	}	

	@Override
	public double getBalance(String acctNo) {
		logger.info("Entering in getBalance :", System.currentTimeMillis());	
		return customerAccountDAO.findBalanceByAcctNo(acctNo);
	}
	
	@Override
	@Transactional
	public void depositAmount(String acctNo, double amount) {
		logger.info("Entering in depositAmount :", System.currentTimeMillis());	
		customerAccountDAO.saveBalanceByAcctNo(acctNo, amount);
		accountTransactionDAO.save(new AccountTransaction(acctNo, TransactionType.DEPOSIT.getId(), amount, new Date()));
		logger.info("Exiting from depositAmount :", System.currentTimeMillis());	
	}
	
	@Override
	@Transactional
	public void transferAmount(String acctNo, String destAcctNo, double amount) {
		logger.info("Entering in transferAmount :", System.currentTimeMillis());
		ReentrantLock lock = new ReentrantLock();
		try {
			lock.lock();
			if(lock.tryLock()) {
				customerAccountDAO.withdrawAmountByAcctNo(acctNo, amount);		
				customerAccountDAO.saveBalanceByAcctNo(destAcctNo, amount);
			}
			//accountTransactionDAO.save(new AccountTransaction(acctNo, TransactionType.WITHDRAWAL.getId(), amount, new Date()));
			//accountTransactionDAO.save(new AccountTransaction(destAcctNo, TransactionType.DEPOSIT.getId(), amount, new Date()));
		} finally {
			lock.unlock();
		}
		logger.info("Exiting from transferAmount :", System.currentTimeMillis());		
	}
}
