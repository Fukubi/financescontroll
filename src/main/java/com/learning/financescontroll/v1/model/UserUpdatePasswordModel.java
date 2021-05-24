package com.learning.financescontroll.v1.model;

import javax.validation.constraints.NotEmpty;

import lombok.Data;

@Data
public class UserUpdatePasswordModel {

	@NotEmpty(message = "Username não pode ser vazio")
	private String username;
	
	@NotEmpty(message = "Password não pode ser vazio")
	private String password;
	
}
