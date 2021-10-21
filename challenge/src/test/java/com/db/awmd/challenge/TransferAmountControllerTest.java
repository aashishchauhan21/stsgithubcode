package com.db.awmd.challenge;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import com.db.awmd.challenge.domain.Account;
import com.db.awmd.challenge.repository.AccountsRepository;
import com.db.awmd.challenge.service.AccountsService;
import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
public class TransferAmountControllerTest {

	private MockMvc mockMvc;

	@Autowired
	private AccountsRepository accountsRepository;

	@Autowired
	private AccountsService accountsService;

	@Autowired
	private WebApplicationContext webApplicationContext;

	@Before
	public void prepareMockMvc() {
		this.mockMvc = webAppContextSetup(this.webApplicationContext).build();

		// Reset the existing accounts before each test.
		accountsService.getAccountsRepository().clearAccounts();
		String srcAccId = "Id-123";
		String destAccId = "Id-456";
		accountsRepository.createAccount(new Account(srcAccId, new BigDecimal("1500.00")));
		accountsRepository.createAccount(new Account(destAccId, new BigDecimal("500.00")));

	}

	@Test
	public void transferAmount() throws Exception {
		MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.put("/v1/accounts/transfer")
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content("{\"srcAcctId\":\"Id-123\",\"amount\":500.0,\"destAcctId\":\"Id-456\"}")
				.accept(MediaType.APPLICATION_JSON).characterEncoding("UTF-8");

		this.mockMvc.perform(builder).andExpect(status().isOk());

	}

	@Test
	public void createInvalidSourceAccountId() throws Exception {
		MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.put("/v1/accounts/transfer")
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content("{\"srcAcctId\":\"Id-111\",\"amount\":500.0,\"destAcctId\":\"Id-456\"}")
				.accept(MediaType.APPLICATION_JSON).characterEncoding("UTF-8");

		this.mockMvc.perform(builder).andExpect(status().isNotFound());
	}
	
	@Test
	public void createInvalidDestinationAccountId() throws Exception {
		MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.put("/v1/accounts/transfer")
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content("{\"srcAcctId\":\"Id-123\",\"amount\":500.0,\"destAcctId\":\"Id-112\"}")
				.accept(MediaType.APPLICATION_JSON).characterEncoding("UTF-8");

		this.mockMvc.perform(builder).andExpect(status().isNotFound());
	}

	@Test
	public void createInvalidTransferAmount() throws Exception {
		MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.put("/v1/accounts/transfer")
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content("{\"srcAcctId\":\"Id-123\",\"amount\":-100.0,\"destAcctId\":\"Id-456\"}")
				.accept(MediaType.APPLICATION_JSON).characterEncoding("UTF-8");

		this.mockMvc.perform(builder).andExpect(status().isBadRequest());
	}

	@Test
	public void createInsufficientTransferAmount() throws Exception {
		MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.put("/v1/accounts/transfer")
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content("{\"srcAcctId\":\"Id-123\",\"amount\":3000.0,\"destAcctId\":\"Id-456\"}")
				.accept(MediaType.APPLICATION_JSON).characterEncoding("UTF-8");

		this.mockMvc.perform(builder).andExpect(status().isBadRequest());
	}
	
}
