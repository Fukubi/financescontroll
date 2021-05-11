package com.learning.financescontroll.v1.service;

import java.util.List;

import com.learning.financescontroll.v1.dto.CategoryDto;

public interface ICategoryService {
	
	public List<CategoryDto> listar();
	
	public CategoryDto consultar(final Long id);
	
	public Boolean cadastrar(final CategoryDto category);
	
	public Boolean atualizar(final CategoryDto category);
	
	public Boolean excluir(final Long id);

}
