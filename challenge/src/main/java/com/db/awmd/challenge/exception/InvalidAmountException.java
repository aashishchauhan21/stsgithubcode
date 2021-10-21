package com.db.awmd.challenge.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class InvalidAmountException extends RuntimeException{

	private static final long serialVersionUID = 1L;

	public InvalidAmountException(String message) {
		super(message);
	}
}
