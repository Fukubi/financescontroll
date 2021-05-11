package com.learning.financescontroll.v1.dto;

import javax.validation.constraints.NotBlank;

import org.springframework.hateoas.RepresentationModel;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class CredentialDto extends RepresentationModel<CredentialDto> {
	
	@NotBlank(message = "É necessário incluir um usuário.")
	private String username;
	
	@NotBlank(message = "É necessário incluir uma senha.")
	private String password;
	
}
