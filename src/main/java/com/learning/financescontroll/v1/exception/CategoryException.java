package com.learning.financescontroll.v1.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class CategoryException extends RuntimeException {

	private static final long serialVersionUID = 8912570411380687502L;
	
	private final HttpStatus httpStatus;
	
	public CategoryException(final String message, final HttpStatus httpStatus) {
		super(message);
		this.httpStatus = httpStatus;
	}

}
