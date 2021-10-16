package com.bankapp.api.test.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Assert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.bankapp.api.Entity.CustomerAccount;
import com.bankapp.api.exception.ErrorDetails;
import com.bankapp.api.model.TransferAmount;
import com.bankapp.api.service.CustomerAccountService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
public class CustomerAccountControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private CustomerAccountService customerAccountService;

	@Test
	@DisplayName("Test for create customer account")
	public void createCustomerAccountTest() throws JsonProcessingException, Exception {
		
		CustomerAccount customer = new CustomerAccount("101", "Savings", "Rahul Sharma", "Noida", "UP", "India",
				"9876541230", 500);

		when(customerAccountService.getCustomerAccountInfo("101")).thenReturn(null);
		when(customerAccountService.createCustomerAccount(customer)).thenReturn(customer);

		String jsonValue = objectMapper.writeValueAsString(customer);
		RequestBuilder build = MockMvcRequestBuilders.post("/customer").headers(getHttpHeaders())
				.contentType(MediaType.APPLICATION_JSON).content(jsonValue);
		MvcResult mvcResult = mockMvc.perform(build).andExpect(status().isOk()).andReturn();
		ErrorDetails resultContent = objectMapper.readValue(mvcResult.getResponse().getContentAsString(),
				ErrorDetails.class);
		String jsonValue1 = objectMapper.writeValueAsString(resultContent);
		Assert.assertTrue(jsonValue1.contains("Customer account created successfully"));
	}

	@Test
	@DisplayName("Test for getting customer info")
	public void getCustomerInfoTest() throws Exception {

		CustomerAccount customerAccount = new CustomerAccount();
		customerAccount.setAcctNo("101");
		customerAccount.setAcctType("Savings");
		customerAccount.setCustName("Aashish");
		customerAccount.setCity("Faridabad");
		customerAccount.setState("Haryana");
		customerAccount.setCountry("India");
		customerAccount.setMobno("9999847134");
		customerAccount.setBalance(1000);

		// String jsonValue = objectMapper.writeValueAsString(customerAccount);
		when(customerAccountService.getCustomerAccountInfo("101")).thenReturn(customerAccount);

		RequestBuilder build = MockMvcRequestBuilders.get("/customer/{acctNo}", "101").headers(getHttpHeaders())
				.contentType(MediaType.APPLICATION_JSON);

		MvcResult mvcResult = mockMvc.perform(build).andExpect(status().isOk()).andReturn();

		CustomerAccount resultContent = objectMapper.readValue(mvcResult.getResponse().getContentAsString(),
				CustomerAccount.class);
		String jsonValue1 = objectMapper.writeValueAsString(resultContent);
		Assert.assertTrue(jsonValue1.contains("101"));
	}

	@Test
	@DisplayName("Test for getting customer info")
	public void getCustomerAccountBalanceTest() throws Exception {

		CustomerAccount customerAccount = new CustomerAccount();

		customerAccount.setAcctNo("101");
		customerAccount.setAcctType("Savings");
		customerAccount.setCustName("Aashish");
		customerAccount.setCity("Faridabad");
		customerAccount.setState("Haryana");
		customerAccount.setCountry("India");
		customerAccount.setMobno("9999847134");
		customerAccount.setBalance(1000);

		when(customerAccountService.getCustomerAccountInfo("101")).thenReturn(customerAccount);
		when(customerAccountService.getBalance("101")).thenReturn((double) 1000);

		RequestBuilder build = MockMvcRequestBuilders.get("/account/{acctNo}/balance", "101").headers(getHttpHeaders())
				.contentType(MediaType.APPLICATION_JSON);

		MvcResult mvcResult = mockMvc.perform(build).andExpect(status().isOk()).andReturn();

		Double balance = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Double.class);
		String jsonValue1 = objectMapper.writeValueAsString(balance);
		Assert.assertTrue(jsonValue1.contains("1000"));
	}

	@Test
	@DisplayName("Test for tansfer balance")
	public void transferAmountTest() throws Exception {

		TransferAmount transferAmount = new TransferAmount();
		transferAmount.setAcctNo("101");
		transferAmount.setAmount(500);
		transferAmount.setDestAcctNo("102");
		String jsonValue = objectMapper.writeValueAsString(transferAmount);

		CustomerAccount customerAccount = new CustomerAccount();
		customerAccount.setAcctNo("101");
		customerAccount.setAcctType("Savings");
		customerAccount.setCustName("Aashish");
		customerAccount.setCity("Faridabad");
		customerAccount.setState("Haryana");
		customerAccount.setCountry("India");
		customerAccount.setMobno("9999847134");
		customerAccount.setBalance(1000);

		CustomerAccount destCustomerAccount = new CustomerAccount();
		destCustomerAccount.setAcctNo("102");
		destCustomerAccount.setAcctType("Savings");
		destCustomerAccount.setCustName("Yash");
		destCustomerAccount.setCity("Faridabad");
		destCustomerAccount.setState("Haryana");
		destCustomerAccount.setCountry("India");
		destCustomerAccount.setMobno("9999847144");
		destCustomerAccount.setBalance(1000);

		when(customerAccountService.getCustomerAccountInfo("101")).thenReturn(customerAccount);
		when(customerAccountService.getBalance("101")).thenReturn((double) 1000);
		when(customerAccountService.getCustomerAccountInfo("102")).thenReturn(customerAccount);

		RequestBuilder build = MockMvcRequestBuilders.put("/account/transfer").headers(getHttpHeaders())
				.contentType(MediaType.APPLICATION_JSON).content(jsonValue);

		MvcResult mvcResult = mockMvc.perform(build).andExpect(status().isOk()).andReturn();

		ErrorDetails resultContent = objectMapper.readValue(mvcResult.getResponse().getContentAsString(),
				ErrorDetails.class);
		String jsonValue1 = objectMapper.writeValueAsString(resultContent);
		Assert.assertTrue(jsonValue1.contains("Amount transferred successfully"));
	}

	public HttpHeaders getHttpHeaders() {
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.add("Content-Type", "application/json");
		httpHeaders.add("Accept", "application/json");
		return httpHeaders;
	}
}
