package com.db.awmd.challenge.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import com.db.awmd.challenge.web.TransferAmountController;

@ControllerAdvice(assignableTypes = {TransferAmountController.class})
public class GlobalExceptionHandler {

	//handling custom exception
	@ExceptionHandler({ ResourceNotFoundException.class, AmountNotTransferException.class, InvalidAmountException.class,
			InvalidAccountIdException.class })
	public ResponseEntity<?> handleCustomException(RuntimeException ex, WebRequest request) {
		if (ex instanceof ResourceNotFoundException) {
			return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
		} else if (ex instanceof InvalidAmountException) {
			return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
		} else if (ex instanceof InvalidAccountIdException) {
			return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
		} else if (ex instanceof InvalidAccountIdException) {
			return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
		} else if (ex instanceof AmountNotTransferException) {
			return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
		} else {
			return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	// handling global exception
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<?> globalCustomExceptionHandling(MethodArgumentNotValidException ex, WebRequest request) {
		return new ResponseEntity<>(ex.getBindingResult().getFieldError().getDefaultMessage(), HttpStatus.BAD_REQUEST);
	}

	// handling global exception
	@ExceptionHandler(Exception.class)
	public ResponseEntity<?> globalExceptionHandling(Exception exception, WebRequest request) {
		return new ResponseEntity<>("Some Error occured", HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
