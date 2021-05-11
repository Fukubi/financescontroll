package com.learning.financescontroll.v1.dto;

import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.hateoas.RepresentationModel;

import com.learning.financescontroll.entity.CredentialEntity;
import com.learning.financescontroll.entity.EntryEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class UserDto extends RepresentationModel<UserDto> {

	private Long id;
	
	@NotEmpty(message = "Usuário precisa de nome")
	private String nome;
	
	@NotNull(message = "Credencias não pode ser nula.")
	private CredentialEntity credenciais;
	
	@NotNull(message = "Lançamento não pode ser nulo.")
	private List<EntryEntity> entries;
	
}
