package com.hn.exceptions;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.hn.model.ExceptionResponse;


@ControllerAdvice
public class HnApiExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(value = NoRecordFoundException.class)
	public ResponseEntity<Object> handleNoRecordFoundException(NoRecordFoundException ex) {
		return new ResponseEntity<>(new ExceptionResponse(new Date(), ex.getMessage()), HttpStatus.OK);
	}

}
