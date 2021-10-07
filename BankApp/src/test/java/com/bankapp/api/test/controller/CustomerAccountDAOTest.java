package com.bankapp.api.test.controller;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import com.bankapp.api.dao.CustomerAccountDAO;
import com.bankapp.api.model.CustomerAccount;

@DataJpaTest
@AutoConfigureMockMvc
public class CustomerAccountDAOTest {

	@Autowired
	private CustomerAccountDAO customerAccountDAO;

	@Test
	public void saveCustomerAccountTest() throws Exception {
		CustomerAccount customerAccount = new CustomerAccount();
		customerAccount.setAcctNo("101");
		customerAccount.setAcctType("Savings");
		customerAccount.setCustName("Aashish");
		customerAccount.setCity("Faridabad");
		customerAccount.setState("Haryana");
		customerAccount.setCountry("India");
		customerAccount.setMobno("9999847134");
		customerAccount.setBalance(1000);

		CustomerAccount resAccount = customerAccountDAO.save(customerAccount);
		Assert.assertTrue(resAccount.getAcctNo().equals(customerAccount.getAcctNo()));
	}

	@Test
	public void depositAmountTest() throws Exception {
		String acctNo = "101";
		double amount = 500;
		double openingBalance = 1000;
		CustomerAccount customerAccount = new CustomerAccount();
		customerAccount.setAcctNo("101");
		customerAccount.setAcctType("Savings");
		customerAccount.setCustName("Aashish");
		customerAccount.setCity("Faridabad");
		customerAccount.setState("Haryana");
		customerAccount.setCountry("India");
		customerAccount.setMobno("9999847134");
		customerAccount.setBalance(openingBalance);
		customerAccountDAO.save(customerAccount);

		customerAccountDAO.saveBalanceByAcctNo(acctNo, amount);
		CustomerAccount response = customerAccountDAO.getById(acctNo);
		Assert.assertTrue(response.getBalance() == openingBalance+amount);
	}

	@Test
	public void withdrawAmountByAcctNoTest() throws Exception {
		String acctNo = "101";
		double amount = 350;
		double openingBalance = 1000;
		CustomerAccount customerAccount = new CustomerAccount();
		customerAccount.setAcctNo("101");
		customerAccount.setAcctType("Savings");
		customerAccount.setCustName("Aashish");
		customerAccount.setCity("Faridabad");
		customerAccount.setState("Haryana");
		customerAccount.setCountry("India");
		customerAccount.setMobno("9999847134");
		customerAccount.setBalance(openingBalance);
		customerAccountDAO.save(customerAccount);

		customerAccountDAO.withdrawAmountByAcctNo(acctNo, amount);
		CustomerAccount customerAccountInfo = customerAccountDAO.getById(acctNo);
		Assert.assertTrue(openingBalance - amount == customerAccountInfo.getBalance());
	}

}
