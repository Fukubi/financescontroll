package com.learning.financescontroll.v1.service;

import java.util.List;

import com.learning.financescontroll.v1.dto.EntryDto;

public interface IEntryService {

	public List<EntryDto> listar();
	
	public EntryDto consultar(final Long id);
	
	public Boolean cadastrar(final EntryDto entry);
	
	public Boolean atualizar(final EntryDto entry);
	
	public Boolean excluir(final Long id);
	
}
