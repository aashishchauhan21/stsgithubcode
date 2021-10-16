package com.bankapp.api.model;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class TransferAmount {

	@NotEmpty(message = "Account Number can not be blank. Please specify Account Number.")
	@NotNull(message = "Account Number can not be blank. Please specify Account Number.")
	private String acctNo;
	@NotEmpty(message = "Destination Account Number can not be blank. Please specify Destination Account Number.")
	@NotNull(message = "Destination Account Number can not be blank. Please specify Destination Account Number.")
	private String destAcctNo;
	@NotNull(message = "Amount to be transfer can not be blank. Please try with valid amount.")
	@Min(value = 1, message = "Amount to be transfer should be greator than 0. Please try with valid amount.")
	private double amount;

	public TransferAmount(String acctNo, String destAcctNo, double amount) {
		super();
		this.acctNo = acctNo;
		this.destAcctNo = destAcctNo;
		this.amount = amount;
	}

	public TransferAmount() {
	}

	public String getAcctNo() {
		return acctNo;
	}

	public void setAcctNo(String acctNo) {
		this.acctNo = acctNo;
	}

	public String getDestAcctNo() {
		return destAcctNo;
	}

	public void setDestAcctNo(String destAcctNo) {
		this.destAcctNo = destAcctNo;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

}
