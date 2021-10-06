package com.bankapp.api.service;

import com.bankapp.api.model.CustomerAccount;

public interface CustomerAccountService {
	
	public void createCustomerAccount(CustomerAccount customerAccount);

	public CustomerAccount getCustomerAccountInfo(String acctNo);
	
	public double getBalance(String acctNo);

	public void depositAmount(String acctNo, double amount);
	
	public void transferAmount(String acctNo, String destAcctNo, double amount);
}
