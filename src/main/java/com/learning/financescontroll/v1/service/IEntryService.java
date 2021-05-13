package com.learning.financescontroll.v1.service;

import java.util.List;

import com.learning.financescontroll.v1.dto.EntryDto;
import com.learning.financescontroll.v1.model.EntryModel;

public interface IEntryService {

	public List<EntryDto> listar();
	
	public EntryDto consultar(final Long id);
	
	public Boolean cadastrar(final EntryModel entry);
	
	public Boolean atualizar(final EntryModel entry);
	
	public Boolean excluir(final Long id);
	
}
