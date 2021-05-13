package com.learning.financescontroll.v1.model;

import java.util.Date;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.learning.financescontroll.enumerators.TipoEnum;

import lombok.Data;

@Data
public class EntryModel {
	private Long id;

	@NotNull(message = "Tipo não pode ser nulo.")
	@Digits(integer = 1, fraction = 0, message = "Apenas números inteiros permitidos")
	@Max(value = 1, message = "O tipo só suporta os valores 1 e 0")
	@Min(value = 0, message = "O tipo só suporta os valores 1 e 0")
	private TipoEnum tipo;

	@NotNull(message = "Data não pode ser nula.")
	private Date data;

	@NotNull(message = "Valor não pode ser nulo.")
	private int valor;

	@NotNull(message = "Categoria não pode ser nulo.")
	private Long categoriaId;
}
