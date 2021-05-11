package com.learning.financescontroll.v1.model;

import org.springframework.hateoas.RepresentationModel;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class ResponseModel<T> extends RepresentationModel<ResponseModel<T>> {
	
	private int statusCode;
	private T data;
	private Long timestamp;
	
	public ResponseModel() {
		this.timestamp = System.currentTimeMillis();
	}

}
