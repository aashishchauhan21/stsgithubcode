package com.db.awmd.challenge.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class AmountNotTransferException extends RuntimeException{

	private static final long serialVersionUID = 1L;

	public AmountNotTransferException(String message) {
		super(message);
	}
}
