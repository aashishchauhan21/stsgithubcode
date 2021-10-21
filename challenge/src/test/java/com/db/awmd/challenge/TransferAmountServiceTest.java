package com.db.awmd.challenge;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import com.db.awmd.challenge.domain.Account;
import com.db.awmd.challenge.domain.TransferAmountDTO;
import com.db.awmd.challenge.repository.AccountsRepository;
import com.db.awmd.challenge.service.AccountsService;
import com.db.awmd.challenge.service.TransferAmountService;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TransferAmountServiceTest {

	@Autowired
	private TransferAmountService transferAmountService;

	@Autowired
	private AccountsService accountsService;

	@Mock
	private AccountsRepository accountsRepository;

	private static Logger loggerTest = LoggerFactory.getLogger(TransferAmountServiceTest.class);

	@Before
	public void setUp() throws Exception {
		accountsService.getAccountsRepository().clearAccounts();
		accountsService.createAccount(new Account("Id-123", new BigDecimal("1000.00")));
		accountsService.createAccount(new Account("Id-456", new BigDecimal("500.00")));
	}

	@Test
	public void transferAmountConcurrentTest() throws InterruptedException {
		loggerTest.info("Entering into transferAmountConcurrentTest : " + System.currentTimeMillis());

		Map<String, Account> firstThreadMap = new ConcurrentHashMap<>();
		Map<String, Account> secondThreadMap = new ConcurrentHashMap<>();
		String sourceAccountId = "Id-123";
		String destAccountId = "Id-456";

		when(accountsRepository.getAccount(sourceAccountId))
				.thenReturn(new Account(sourceAccountId, new BigDecimal("1000.00")));
		when(accountsRepository.getAccount(destAccountId))
				.thenReturn(new Account(destAccountId, new BigDecimal("500.00")));

		Thread t1 = new Thread(new Runnable() {
			public void run() {
				for (int i = 1; i <= 10; i++) {
					String resultObj = transferAmountService.transferAmount(
							new TransferAmountDTO(sourceAccountId, destAccountId, new BigDecimal("50.00")));
					if (resultObj.contains("success")) {
						Account modifiedDestAccObj = accountsService.getAccount(destAccountId);
						if (firstThreadMap.containsKey(destAccountId)) {
							firstThreadMap.replace(destAccountId, modifiedDestAccObj);
						} else {
							firstThreadMap.putIfAbsent(destAccountId, modifiedDestAccObj);
						}
					}
				}
				assertThat(firstThreadMap.get(destAccountId).getBalance()).isEqualTo(new BigDecimal("1000.00"));

			}
		});

		Thread t2 = new Thread(new Runnable() {
			public void run() {
				for (int i = 1; i <= 10; i++) {
					String resultObj2 = transferAmountService.transferAmount(
							new TransferAmountDTO(sourceAccountId, destAccountId, new BigDecimal("20.00")));
					if (resultObj2.contains("success")) {
						Account destAccObj = accountsService.getAccount(destAccountId);
						if (secondThreadMap.containsKey(destAccountId)) {
							secondThreadMap.replace(destAccountId, destAccObj);
						} else {
							secondThreadMap.putIfAbsent(destAccountId, destAccObj);
						}

					}

				}
				assertThat(secondThreadMap.get(destAccountId).getBalance()).isEqualTo(new BigDecimal("1200.00"));
			}
		});
		t1.start();
		t2.start();
		t1.join();
		t2.join();

		loggerTest.info("Exiting from transferAmountConcurrentTest : " + System.currentTimeMillis());
	}

	@Test
	public void transferAmountTest() {
		loggerTest.info("Entering into transferAmountTest : " + System.currentTimeMillis());

		String sourceAccountId = "Id-123";
		String destAccountId = "Id-456";
		when(accountsRepository.getAccount(sourceAccountId))
				.thenReturn(new Account(sourceAccountId, new BigDecimal("1000.00")));
		when(accountsRepository.getAccount(destAccountId))
				.thenReturn(new Account(destAccountId, new BigDecimal("500.00")));
		transferAmountService
				.transferAmount(new TransferAmountDTO(sourceAccountId, destAccountId, new BigDecimal("300.00")));
		assertThat(accountsService.getAccount(destAccountId).getBalance()).isEqualTo((new BigDecimal("800.00")));

		loggerTest.info("Exiting from transferAmountTest : " + System.currentTimeMillis());
	}

	@Test
	public void testEmailSentNotification() {
		String srcAccountId = "Id-123";
		String destAccountId = "Id-456";
		boolean emailSent = false;

		when(accountsRepository.getAccount(srcAccountId))
				.thenReturn(new Account(srcAccountId, new BigDecimal("1000.00")));
		when(accountsRepository.getAccount(destAccountId))
				.thenReturn(new Account(destAccountId, new BigDecimal("500.00")));

		// if resObj contains "success" then it means email is sent to both accounts
		String resObj = transferAmountService
				.transferAmount(new TransferAmountDTO(srcAccountId, destAccountId, new BigDecimal("200.00")));

		if (resObj.contains("success")) {
			emailSent = true;
		}
		assertTrue(emailSent);
	}

}
