package com.learning.financescontroll.v1.service;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.learning.financescontroll.entity.UserEntity;
import com.learning.financescontroll.repository.IUserRepository;
import com.learning.financescontroll.v1.constants.ServiceConstantVariables;
import com.learning.financescontroll.v1.controller.UserController;
import com.learning.financescontroll.v1.dto.UserDto;
import com.learning.financescontroll.v1.exception.UserException;

@CacheConfig(cacheNames = "usuario")
@Service
public class UserService implements IUserService {

	private IUserRepository userRepository;
	private ModelMapper mapper;

	@Autowired
	public UserService(IUserRepository userRepository) {
		this.mapper = new ModelMapper();
		this.userRepository = userRepository;
	}

	@CachePut(unless = "#result.size()<3")
	@Override
	public List<UserDto> listar() {
		try {
			List<UserEntity> userEntityLisy = this.userRepository.findAll();
			List<UserDto> userDto = this.mapper.map(userEntityLisy, new TypeToken<List<UserDto>>() {
			}.getType());

			userDto.forEach(user -> user.add(WebMvcLinkBuilder
					.linkTo(WebMvcLinkBuilder.methodOn(UserController.class).consultarUsuario(user.getId()))
					.withSelfRel()));

			return userDto;
		} catch (UserException c) {
			throw c;
		} catch (Exception e) {
			throw new UserException(ServiceConstantVariables.ERRO_GENERICO.getValor(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@CachePut(key = "#id")
	@Override
	public UserDto consultar(Long id) {
		try {
			Optional<UserEntity> userOptional = this.userRepository.findById(id);
			if (userOptional.isPresent()) {
				return this.mapper.map(userOptional.get(), UserDto.class);
			}
			throw new UserException(ServiceConstantVariables.NOT_FOUND.getValor(), HttpStatus.NOT_FOUND);
		} catch (UserException c) {
			throw c;
		} catch (Exception e) {
			throw new UserException(ServiceConstantVariables.ERRO_GENERICO.getValor(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public Boolean cadastrar(UserDto user) {
		try {
			if (user.getId() != null) {
				throw new UserException(ServiceConstantVariables.ID_NOT_PERMITTED.getValor(), HttpStatus.BAD_REQUEST);
			}

			UserEntity userEntity = this.mapper.map(user, UserEntity.class);
			this.userRepository.save(userEntity);
			return Boolean.TRUE;
		} catch (UserException c) {
			throw c;
		} catch (Exception e) {
			throw new UserException(ServiceConstantVariables.ERRO_GENERICO.getValor(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public Boolean atualizar(UserDto user) {
		try {
			if (user.getId() == null) {
				throw new UserException(ServiceConstantVariables.MISSING_ID.getValor(), HttpStatus.BAD_REQUEST);
			}

			this.consultar(user.getId());

			UserEntity userEntity = this.mapper.map(user, UserEntity.class);
			this.userRepository.save(userEntity);
			return Boolean.TRUE;
		} catch (UserException c) {
			throw c;
		} catch (Exception e) {
			throw new UserException(ServiceConstantVariables.ERRO_GENERICO.getValor(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public Boolean excluir(Long id) {
		try {
			this.consultar(id);
			this.userRepository.deleteById(id);
			return Boolean.TRUE;
		} catch (UserException c) {
			throw c;
		} catch (Exception e) {
			throw new UserException(ServiceConstantVariables.ERRO_GENERICO.getValor(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
