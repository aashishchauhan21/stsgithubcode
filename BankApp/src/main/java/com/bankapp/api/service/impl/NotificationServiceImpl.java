package com.bankapp.api.service.impl;

import org.springframework.stereotype.Service;

import com.bankapp.api.model.CustomerAccount;
import com.bankapp.api.service.NotificationService;

@Service
public class NotificationServiceImpl implements NotificationService{

	@Override
	public boolean sendNotificationToCustomer(CustomerAccount customerAccount, double amount) {
		return true;
	}

}
