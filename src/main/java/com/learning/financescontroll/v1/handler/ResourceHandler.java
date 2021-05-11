package com.learning.financescontroll.v1.handler;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.learning.financescontroll.v1.exception.CategoryException;
import com.learning.financescontroll.v1.exception.EntryException;
import com.learning.financescontroll.v1.exception.UserException;
import com.learning.financescontroll.v1.model.ResponseModel;

@ControllerAdvice
public class ResourceHandler {
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ResponseModel<Map<String, String>>> handlerMethodArgumentNotValidException(MethodArgumentNotValidException m) {
		Map<String, String> erros = new HashMap<>();
		m.getBindingResult().getAllErrors().forEach(erro -> {
			String campo = ((FieldError) erro).getField();
			String mensagem = erro.getDefaultMessage();
			erros.put(campo, mensagem);
		});
		
		ResponseModel<Map<String, String>> response = new ResponseModel<>();
		response.setData(erros);
		response.setStatusCode(HttpStatus.BAD_REQUEST.value());
		
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	}
	
	@ExceptionHandler(CategoryException.class)
	public ResponseEntity<ResponseModel<String>> handlerCategoryException(CategoryException c) {
		ResponseModel<String> response = new ResponseModel<>();
		response.setStatusCode(c.getHttpStatus().value());
		response.setData(c.getMessage());
		return ResponseEntity.status(c.getHttpStatus()).body(response);
	}
	
	@ExceptionHandler(EntryException.class)
	public ResponseEntity<ResponseModel<String>> handlerEntryException(EntryException e) {
		ResponseModel<String> response = new ResponseModel<>();
		response.setStatusCode(e.getHttpStatus().value());
		response.setData(e.getMessage());
		return ResponseEntity.status(e.getHttpStatus()).body(response);
	}
	
	@ExceptionHandler(UserException.class)
	public ResponseEntity<ResponseModel<String>> handlerUserException(UserException u) {
		ResponseModel<String> response = new ResponseModel<>();
		response.setStatusCode(u.getHttpStatus().value());
		response.setData(u.getMessage());
		return ResponseEntity.status(u.getHttpStatus()).body(response);
	}
}
