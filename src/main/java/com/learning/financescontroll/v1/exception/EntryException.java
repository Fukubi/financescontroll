package com.learning.financescontroll.v1.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class EntryException extends RuntimeException {

	private static final long serialVersionUID = 3027914821721299937L;

	private final HttpStatus httpStatus;
	
	public EntryException(final String message, final HttpStatus httpStatus) {
		super(message);
		this.httpStatus = httpStatus;
	}
	
}
