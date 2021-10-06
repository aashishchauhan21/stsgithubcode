package com.bankapp.api.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
public class CustomerAccount {
	
	@Id
	@NotEmpty(message = "Account no can not be blank. Please specify account no in payload.")
	private String acctNo;
	@NotEmpty(message = "Account type can not be blank. Please specify account type in payload.")
	private String acctType;
	@NotEmpty(message = "Customer Name can not be blank. Please specify customer name in payload.")
	private String custName;
	@NotEmpty(message = "City can not be blank. Please specify City in payload.")
	private String city;
	@NotEmpty(message = "State can not be blank. Please specify state in payload.")
	private String state;
	@NotEmpty(message = "Country can not be blank. Please specify country in payload.")
	private String country;
	@NotNull(message = "Mobile No can not be blank. Please specify mobile no in payload.")
	@Size(min = 10, max=10, message = "Invalid Mobile No, Please specify valid mobile no in payload.")
	private String mobno;	
	@NotNull(message = "Opening Account Balance can not be blank. Please provide the value and try again.")
	@Min(value=500, message = "Opening Account Balance can not be less than 500. Please provide the valid amount and try again.")
	private double balance;

	public CustomerAccount() {

	}

	public CustomerAccount(String acctNo, String acctType, String custName, String city, String state, String country,
			String mobno, double balance) {
		super();
		this.acctNo = acctNo;
		this.acctType = acctType;
		this.custName = custName;
		this.city = city;
		this.state = state;
		this.country = country;
		this.mobno = mobno;
		this.balance = balance;
	}

	public String getAcctNo() {
		return acctNo;
	}

	public void setAcctNo(String acctNo) {
		this.acctNo = acctNo;
	}

	public String getAcctType() {
		return acctType;
	}

	public void setAcctType(String acctType) {
		this.acctType = acctType;
	}

	public String getCustName() {
		return custName;
	}

	public void setCustName(String custName) {
		this.custName = custName;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getMobno() {
		return mobno;
	}

	public void setMobno(String mobno) {
		this.mobno = mobno;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}
}
