package com.learning.financescontroll.v1.constants;

import lombok.Getter;

@Getter
public enum ServiceConstantVariables {
	ERRO_GENERICO("Erro interno identificado, Contate o suporte"),
	NOT_FOUND("Entidade não encontrada"),
	ID_NOT_PERMITTED("Não pode existir um ID nessa requisição"),
	MISSING_ID("ID não existe na requisição"),
	MISSING_CATEGORY("É necessário que exista uma categoria");
	
	private final String valor;
	
	private ServiceConstantVariables(String valor) {
		this.valor = valor;
	}
}
