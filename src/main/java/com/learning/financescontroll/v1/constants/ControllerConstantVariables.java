package com.learning.financescontroll.v1.constants;

import lombok.Getter;

@Getter
public enum ControllerConstantVariables {

	ATUALIZAR("UPDATE"),
	EXCLUIR("DELETE"),
	LISTAR("GET_ALL"),
	CONSULTAR("GET");
	
	private final String valor;
	
	private ControllerConstantVariables(String valor) {
		this.valor = valor;
	}
	
}
