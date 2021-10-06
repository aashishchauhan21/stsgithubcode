package com.bankapp.api.exception;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

	// handling specific exception
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<?> resourceNotFoundHandling(ResourceNotFoundException exception, WebRequest request){
		ErrorDetails errorDetails = 
				new ErrorDetails(new Date(), exception.getMessage(), request.getDescription(false));
		return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
	}

	// handling global exception	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<?> globalExceptionHandling(Exception exception, WebRequest request){
		ErrorDetails errorDetails = 
				new ErrorDetails(new Date(), exception.getMessage(), request.getDescription(false));
		return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	// handling custom validation exception
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<?> globalCustomExceptionHandling(MethodArgumentNotValidException mNotValidException,
			WebRequest request) {
		ErrorDetails errorDetails = new ErrorDetails(new Date(), "Request Payload Validation Error",
				mNotValidException.getBindingResult().getFieldError().getDefaultMessage());
		return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
	}
	
	// handling custom exception
	@ExceptionHandler(DublicateAccountNumberException.class)
	public ResponseEntity<?> dublicateAccountNumberExceptionHandling(DublicateAccountNumberException exception, WebRequest request){
		ErrorDetails errorDetails = 
				new ErrorDetails(new Date(), exception.getMessage(), request.getDescription(false));
		return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
	}
	
	// handling custom exception
	@ExceptionHandler(AmountNotTransferException.class)
	public ResponseEntity<?> amountNotTransferExceptionHandling(AmountNotTransferException exception, WebRequest request){
		ErrorDetails errorDetails = 
				new ErrorDetails(new Date(), exception.getMessage(), request.getDescription(false));
		return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
	}
	
	// handling custom exception
	@ExceptionHandler(InvalidAmountException.class)
	public ResponseEntity<?> invalidAmountExceptionlHandling(InvalidAmountException exception, WebRequest request){
		ErrorDetails errorDetails = 
				new ErrorDetails(new Date(), exception.getMessage(), request.getDescription(false));
		return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
	}
}
