package com.bankapp.api.test.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Date;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import com.bankapp.api.exception.ErrorDetails;
import com.bankapp.api.model.CustomerAccount;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
public class CustomerAccountControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;	
		
	
	@Test
	public void createCustomerAccountTest() throws JsonProcessingException, Exception {
		CustomerAccount customer = new CustomerAccount("101", "Savings", "Rahul Sharma", "Noida", "UP", "India", "9876541230", 500);
		ErrorDetails errorDetails = new ErrorDetails(new Date(),
				"Customer account created successfully with account number : " + customer.getAcctNo(), "");
		ResponseEntity<Object> response = new ResponseEntity<>(errorDetails, HttpStatus.OK);
		
		System.out.println("json::"+objectMapper.writeValueAsString(response));
		
		String url = "/customer";
		this.mockMvc.perform(post(url).contentType("application/json").content(objectMapper.writeValueAsString(customer)))
		.andExpect(status().isOk()).andExpect(content().json(objectMapper.writeValueAsString(response)));
		
		
	}
}
