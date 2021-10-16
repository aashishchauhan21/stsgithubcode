package com.bankapp.api.Entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import java.util.Date;

@Entity
public class AccountTransaction {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	private String acctNo;
	private int type;
	private double amount;
	private Date date;

	protected AccountTransaction() {
	}

	public AccountTransaction(String acctNo, int type, double amount, Date date) {
		this.acctNo = acctNo;
		this.type = type;
		this.amount = amount;
		this.date = date;
	}

	public long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAcctNo() {
		return acctNo;
	}

	public void setAcctNo(String acctNo) {
		this.acctNo = acctNo;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
}
