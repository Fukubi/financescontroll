package com.learning.financescontroll.entity;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CredentialEntity implements Serializable {

	private static final long serialVersionUID = -8710766553860617257L;

	@JsonInclude(Include.NON_NULL)
	private String username;
	
	@JsonInclude(Include.NON_NULL)
	private String password;
}
