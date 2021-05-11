package com.learning.financescontroll.v1.service;

import java.util.List;

import com.learning.financescontroll.v1.dto.UserDto;

public interface IUserService {

	public List<UserDto> listar();
	
	public UserDto consultar(final Long id);
	
	public Boolean cadastrar(final UserDto user);
	
	public Boolean atualizar(final UserDto user);
	
	public Boolean excluir(final Long id);
	
}
