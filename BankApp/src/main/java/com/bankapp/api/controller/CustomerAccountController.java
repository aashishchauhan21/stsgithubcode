package com.bankapp.api.controller;

import java.util.Date;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.bankapp.api.exception.AmountNotTransferException;
import com.bankapp.api.exception.DublicateAccountNumberException;
import com.bankapp.api.exception.ErrorDetails;
import com.bankapp.api.exception.InvalidAmountException;
import com.bankapp.api.exception.ResourceNotFoundException;
import com.bankapp.api.model.CustomerAccount;
import com.bankapp.api.model.TransferAmount;
import com.bankapp.api.service.CustomerAccountService;
import com.bankapp.api.service.NotificationService;

@RestController
public class CustomerAccountController {
	
		private static final Logger logger = LoggerFactory.getLogger(CustomerAccountController.class);

		@Autowired
		private CustomerAccountService customerAccountService;		

		@Autowired
		private NotificationService notificationService;
		
		
		@GetMapping("/health")
		public String getHealthCheck() {
			return "OK";
		}

		CustomerAccount response = null;

		// method to create new customer account
		@PostMapping("/customer")
		public ResponseEntity<Object> createCustomer(@Valid @RequestBody CustomerAccount customerAccount) {
			logger.info("Receive request to create new customer account");
			response = customerAccountService.getCustomerAccountInfo(customerAccount.getAcctNo());
			if (response == null) {
				CustomerAccount cusAcctresponse = customerAccountService.createCustomerAccount(customerAccount);
				ErrorDetails errorDetails = new ErrorDetails(new Date(),
						"Customer account created successfully", "");
				return new ResponseEntity<>(errorDetails, HttpStatus.OK);
			} else {
				throw new DublicateAccountNumberException(
						"Customer already exist with given account number : " + customerAccount.getAcctNo());
			}
		}

		// method to get customer account details from account number
		@GetMapping("/customer/{acctNo}")
		public CustomerAccount getCustomerInfo(@PathVariable String acctNo) {
			logger.info("Receive request to get customer account info");
			response = customerAccountService.getCustomerAccountInfo(acctNo);
			if (response == null)
				throw new ResourceNotFoundException("Customer account number does not exist : " + acctNo);
			return response;
		}
		
		// method to get customer account balance from account number
		@GetMapping("/account/{acctNo}/balance")
		public Double getBalance(@PathVariable String acctNo) {
			logger.info("Receive request to get account balance");
			double balance = 0;
			CustomerAccount customerAccount = customerAccountService.getCustomerAccountInfo(acctNo);
			if (customerAccount != null) {
				balance = customerAccountService.getBalance(acctNo);
			} else {
				throw new ResourceNotFoundException("Customer account number does not exist : " + acctNo);
			}
			return balance;

		}

		// method to deposit Amount
		@PutMapping("/account/{acctNo}/deposit/{amount}")
		public Double depositAmount(@PathVariable String acctNo, @PathVariable double amount) {
			logger.info("Receive request to deposit amount in customer account");
			double updatedBalance = 0;
			CustomerAccount customerAccount = customerAccountService.getCustomerAccountInfo(acctNo);
			if (customerAccount != null) {
				if (amount > 0) {
					double balance = customerAccountService.getBalance(acctNo);
					customerAccountService.depositAmount(acctNo, amount);
					updatedBalance = balance + amount;
				} else {
					throw new ResourceNotFoundException("Deposit amount should be greator than 0 : " + amount);
				}
			} else {
				throw new ResourceNotFoundException("Customer account number does not exist : " + acctNo);
			}
			return updatedBalance;
		}

		// transfer Amount from one account to another account
		@PutMapping("/account/transfer")
		public ResponseEntity<Object> transferAmount(@Valid @RequestBody TransferAmount transferAmount) {
			logger.info("Receive request to tranfer amount");

			CustomerAccount customerAccount = customerAccountService.getCustomerAccountInfo(transferAmount.getAcctNo());
			if (customerAccount != null) {
				double initBalSender = customerAccountService.getBalance(transferAmount.getAcctNo());
				if (initBalSender >= transferAmount.getAmount()) {
					CustomerAccount destCustomerAccount = customerAccountService.getCustomerAccountInfo(transferAmount.getDestAcctNo());
					if (destCustomerAccount != null) {
						// double initBalReceiver = customerAccountService.getBalance(destAcctNo);
						if (transferAmount.getAmount() > 0) {
							customerAccountService.transferAmount(transferAmount.getAcctNo(), transferAmount.getDestAcctNo(),
									transferAmount.getAmount());
							notificationService.sendNotificationToCustomer(customerAccount, transferAmount.getAmount());
							notificationService.sendNotificationToCustomer(destCustomerAccount, transferAmount.getAmount());
							ErrorDetails errorDetails = new ErrorDetails(new Date(), "Amount transferred successfully", "");
							return new ResponseEntity<>(errorDetails, HttpStatus.OK);
						} else {
							throw new InvalidAmountException(
									"Amount to be transfer should be greator than 0 : " + transferAmount.getAmount());
						}
					} else {
						throw new ResourceNotFoundException(
								"Destination account number does not exist : " + transferAmount.getDestAcctNo());
					}
				} else {
					throw new AmountNotTransferException(
							"Insufficient Account Balance to transfer amount : " + initBalSender);
				}
			} else {
				throw new ResourceNotFoundException(
						"Customer account number does not exist : " + transferAmount.getAcctNo());
			}
		}
}
