package com.learning.financescontroll.v1.dto;

import java.util.Date;

import javax.validation.constraints.NotNull;

import org.springframework.hateoas.RepresentationModel;

import com.learning.financescontroll.entity.CategoryEntity;
import com.learning.financescontroll.enumerators.TipoEnum;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class EntryDto extends RepresentationModel<EntryDto> {
		
	private Long id;

	@NotNull(message = "Tipo não pode ser nulo.")
	private TipoEnum tipo;

	@NotNull(message = "Data não pode ser nula.")
	private Date data;

	@NotNull(message = "Valor não pode ser nulo.")
	private int valor;

	@NotNull(message = "Categoria não pode ser nulo.")
	private CategoryEntity categoria;
	
}
