package com.learning.financescontroll.entity;

import javax.persistence.Embeddable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
public class CredentialEntity {

	@JsonInclude(Include.NON_NULL)
	private String username;
	
	@JsonInclude(Include.NON_NULL)
	private String password;
}
