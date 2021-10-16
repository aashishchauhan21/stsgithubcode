package com.bankapp.api.test.controller;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.bankapp.api.Entity.CustomerAccount;
import com.bankapp.api.dao.AccountTransactionDAO;
import com.bankapp.api.dao.CustomerAccountDAO;
import com.bankapp.api.service.CustomerAccountService;

@SpringBootTest
@AutoConfigureMockMvc
public class CustomerAccountServiceTest {

	@MockBean
	private CustomerAccountDAO customerAccountDAO;

	@Autowired
	private CustomerAccountService customerAccountService;

	@MockBean
	private AccountTransactionDAO accountTransactionDAO;

	@Test
	public void createCustomerAccountTest() throws Exception {
		CustomerAccount customerAccount = new CustomerAccount();
		customerAccount.setAcctNo("101");
		customerAccount.setAcctType("Savings");
		customerAccount.setCustName("Aashish");
		customerAccount.setCity("Faridabad");
		customerAccount.setState("Haryana");
		customerAccount.setCountry("India");
		customerAccount.setMobno("9999847134");
		customerAccount.setBalance(1000);

		when(customerAccountDAO.save(customerAccount)).thenReturn(customerAccount);
		CustomerAccount resAccount = customerAccountService.createCustomerAccount(customerAccount);
		Assert.assertTrue(resAccount.getAcctNo().equals(customerAccount.getAcctNo()));
	}

	@Test
	public void getCustomerAccountInfoTest() {
		CustomerAccount customerAccount = new CustomerAccount();
		customerAccount.setAcctNo("101");
		customerAccount.setAcctType("Savings");
		customerAccount.setCustName("Aashish");
		customerAccount.setCity("Faridabad");
		customerAccount.setState("Haryana");
		customerAccount.setCountry("India");
		customerAccount.setMobno("9999847134");
		customerAccount.setBalance(1000);

		String acctNo = "101";
		when(customerAccountDAO.findById(acctNo)).thenReturn(Optional.of(customerAccount));
		CustomerAccount response = customerAccountService.getCustomerAccountInfo(acctNo);
		Assert.assertTrue(response.getCustName().equals(customerAccount.getCustName()));
	}

	@Test
	public void transferAmountTest() {
		String acctNo = "101";
		String destAcctNo = "102";
		double amount = 500;
		doNothing().when(customerAccountDAO).withdrawAmountByAcctNo(acctNo, amount);
		doNothing().when(customerAccountDAO).saveBalanceByAcctNo(destAcctNo, amount);

		customerAccountService.transferAmount(acctNo, destAcctNo, amount);
	}
}
