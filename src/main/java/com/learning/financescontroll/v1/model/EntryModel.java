package com.learning.financescontroll.v1.model;

import java.util.Date;

import javax.validation.constraints.NotNull;

import com.learning.financescontroll.enumerators.TipoEnum;

import lombok.Data;

@Data
public class EntryModel {
	private Long id;

	@NotNull(message = "Tipo não pode ser nulo.")
	private TipoEnum tipo;

	@NotNull(message = "Data não pode ser nula.")
	private Date data;

	@NotNull(message = "Valor não pode ser nulo.")
	private int valor;

	@NotNull(message = "Categoria não pode ser nulo.")
	private Long categoriaId;
}
