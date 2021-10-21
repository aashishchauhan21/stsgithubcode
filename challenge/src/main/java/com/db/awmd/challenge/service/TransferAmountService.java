package com.db.awmd.challenge.service;

import com.db.awmd.challenge.domain.Account;
import com.db.awmd.challenge.domain.TransferAmountDTO;
import com.db.awmd.challenge.repository.AccountsRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransferAmountService {
	
	private static Logger logger = LoggerFactory.getLogger(TransferAmountService.class);
	
	@Autowired
	private AccountsRepository accountsRepository;

	@Autowired
	private EmailNotificationService emailNotificationService;
	
	public String transferAmount(TransferAmountDTO transferAmtDto) {
		logger.info("Entering in transferAmount :"+ transferAmtDto.toString());
		String response = "failure";
		Account srcAccount = accountsRepository.getAccount(transferAmtDto.getSrcAcctId());
		Account destAccount = accountsRepository.getAccount(transferAmtDto.getDestAcctId());
		synchronized (this) {
			try {
				srcAccount.setBalance(srcAccount.getBalance().subtract(transferAmtDto.getAmount()));
				accountsRepository.updateAccount(srcAccount);
				destAccount.setBalance(destAccount.getBalance().add(transferAmtDto.getAmount()));
				accountsRepository.updateAccount(destAccount);
				
				//Sending notification to both accounts
				emailNotificationService.notifyAboutTransfer(srcAccount, "Amount debited with : " + transferAmtDto.getAmount());
				emailNotificationService.notifyAboutTransfer(destAccount, "Amount credited with : " + transferAmtDto.getAmount());
				response = "success";
			} finally {
				logger.info("Exiting from transferAmount :"+ System.currentTimeMillis());
			}
		}
		return response;
	}
}
