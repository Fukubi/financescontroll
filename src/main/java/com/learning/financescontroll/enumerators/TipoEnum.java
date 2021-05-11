package com.learning.financescontroll.enumerators;

import lombok.Getter;

@Getter
public enum TipoEnum {
	RECEITA(1, "Receita"),
	DESPESA(1, "Despesa");
	
	private final int code;
	private final String name;
	
	TipoEnum(int code, String name) {
		this.code = code;
		this.name = name;
	}
}
