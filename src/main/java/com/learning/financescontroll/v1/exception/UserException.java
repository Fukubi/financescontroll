package com.learning.financescontroll.v1.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class UserException extends RuntimeException {

	private static final long serialVersionUID = -5679677195338050337L;
	
	private final HttpStatus httpStatus;
	
	public UserException(final String message, final HttpStatus httpStatus) {
		super(message);
		this.httpStatus = httpStatus;
	}

}
