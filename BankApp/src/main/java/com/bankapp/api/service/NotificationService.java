package com.bankapp.api.service;

import com.bankapp.api.model.CustomerAccount;

public interface NotificationService {
	
	public boolean sendNotificationToCustomer(CustomerAccount customerAccount, double amount);
	
}
