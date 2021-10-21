package com.db.awmd.challenge.domain;

import java.math.BigDecimal;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class TransferAmountDTO {	

	@NotNull
	@NotEmpty(message = "Account Id can not be blank. Please specify Account Number.")
	private String srcAcctId;

	@NotNull
	@NotEmpty(message = "Destination Account Number can not be blank. Please specify Destination Account Number.")
	private String destAcctId;
	@NotNull(message = "Amount to be transfer can not be blank. Please try with valid amount.")
	@Min(value = 1, message = "Amount to be transfer should be greator than 0. Please try with valid amount.")
	private BigDecimal amount;

	@JsonCreator
	public TransferAmountDTO(@JsonProperty("srcAcctId") String srcAccountId,
			@JsonProperty("destAcctId") String destAccountId, @JsonProperty("amount") BigDecimal amount) {
		this.srcAcctId = srcAccountId;
		this.destAcctId = destAccountId;
		this.amount = amount;
	}
}
