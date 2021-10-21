package com.db.awmd.challenge.web;

import com.db.awmd.challenge.domain.Account;
import com.db.awmd.challenge.domain.TransferAmountDTO;
import com.db.awmd.challenge.exception.AmountNotTransferException;
import com.db.awmd.challenge.exception.InvalidAmountException;
import com.db.awmd.challenge.exception.ResourceNotFoundException;
import com.db.awmd.challenge.service.AccountsService;
import com.db.awmd.challenge.service.TransferAmountService;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/accounts")
public class TransferAmountController {
	
	private static Logger logger = LoggerFactory.getLogger(TransferAmountController.class);

	@Autowired
	private TransferAmountService transferAmountService;

	@Autowired
	private AccountsService accountsService;

	// to transfer Amount from one account to another account
	@PutMapping("/transfer")
	public ResponseEntity<Object> transferAmount(@Valid @RequestBody TransferAmountDTO transferAmountDTO) {
		logger.info("Receive request to transfer amount");
		if (validateTransferAmount(transferAmountDTO)) {
			try {
				String response = transferAmountService.transferAmount(transferAmountDTO);
				if(response.contains("success"))
					return new ResponseEntity<>("Amount transferred successfully", HttpStatus.OK);
				else
					return new ResponseEntity<>("Some Error Occurred", HttpStatus.INTERNAL_SERVER_ERROR);
			} catch (Exception e) {
				logger.error("Exception in transferAmount :" + e.getMessage());
				return new ResponseEntity<>("Some Error Occurred", HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} else {
			return new ResponseEntity<>("Some Error Occurred", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	private boolean validateTransferAmount(TransferAmountDTO trAmountDTO) {
		logger.info("Entering in validateTransferAmount : " + System.currentTimeMillis());
		logger.info("TransferAmountDTO : " + trAmountDTO.toString());
		try {
			boolean response = false;
			if (trAmountDTO.getAmount().doubleValue() > 0.0) {
				Account srcAccount = accountsService.getAccount(trAmountDTO.getSrcAcctId());
				if (srcAccount != null) {
					logger.info("Source Account Balance:"+srcAccount.getBalance().doubleValue());
					logger.info("Amount:"+trAmountDTO.getAmount().doubleValue());
					if (trAmountDTO.getAmount().doubleValue() < srcAccount.getBalance().doubleValue()) {
						Account destAccount = accountsService.getAccount(trAmountDTO.getDestAcctId());
						if (destAccount != null) {
							response = true;
						} else {
							throw new ResourceNotFoundException(
									"Destination Customer Account Id does not exist : " + trAmountDTO.getDestAcctId());
						}
					} else {
						throw new AmountNotTransferException(
								"Insufficient Account Balance to transfer amount : " + srcAccount.getBalance());
					}
				} else {
					throw new ResourceNotFoundException(
							"Customer Account Id does not exist : " + trAmountDTO.getSrcAcctId());
				}
			} else {
				throw new InvalidAmountException("Amount to be transfer should be greator than zero");
			}
			return response;
		} finally {
			logger.info("Exiting from validateTransferAmount : " + System.currentTimeMillis());
		}
	}

}
