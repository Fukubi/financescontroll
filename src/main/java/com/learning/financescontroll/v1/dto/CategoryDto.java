package com.learning.financescontroll.v1.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.hateoas.RepresentationModel;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class CategoryDto extends RepresentationModel<CategoryDto> {

	private Long id;
	
	@NotBlank(message = "Informe o nome da categoria")
	private String nome;
	
	@NotNull(message = "Descrição não pode ser nula, use uma descrição vazia")
	private String descricao;
	
}
