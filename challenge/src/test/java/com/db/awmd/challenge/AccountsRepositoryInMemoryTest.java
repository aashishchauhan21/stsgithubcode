package com.db.awmd.challenge;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.db.awmd.challenge.domain.Account;
import com.db.awmd.challenge.repository.AccountsRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AccountsRepositoryInMemoryTest {

	@Autowired
	private AccountsRepository accountsRepository;

	@Before
	public void prepareMockMvc() {
	   	// Reset the existing accounts before each test.
		accountsRepository.clearAccounts();
	}

	@Test
	public void createAccountTest() {
		Account srcAccount = new Account("id-123", new BigDecimal("1000.00"));
		accountsRepository.createAccount(srcAccount);
		assertThat(accountsRepository.getAccount(srcAccount.getAccountId()).getAccountId().equals("Id-123"));
		assertThat(accountsRepository.getAccount(srcAccount.getAccountId()).getBalance())
				.isEqualTo(new BigDecimal("1000.00"));
	}

	@Test
	public void updateAccountTest() {
		String accountId = "id-123";
		accountsRepository.createAccount(new Account(accountId, new BigDecimal("1000.00")));
		Account srcAccount = new Account(accountId, new BigDecimal("700.00"));
		accountsRepository.updateAccount(srcAccount);
		assertThat(accountsRepository.getAccount(accountId).getBalance()).isEqualTo(new BigDecimal("700.00"));

	}
	
	@Test
	public void getAccountTest() {
		String sourceAccountId = "id-456";
		accountsRepository.createAccount(new Account(sourceAccountId, new BigDecimal("500.00")));
		Account accountObj = accountsRepository.getAccount(sourceAccountId);;
		assertNotNull(accountObj);
	}

}