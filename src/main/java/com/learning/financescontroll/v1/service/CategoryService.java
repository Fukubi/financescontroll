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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.learning.financescontroll.entity.CategoryEntity;
import com.learning.financescontroll.entity.UserEntity;
import com.learning.financescontroll.repository.ICategoryRepository;
import com.learning.financescontroll.repository.IUserRepository;
import com.learning.financescontroll.v1.constants.ServiceConstantVariables;
import com.learning.financescontroll.v1.controller.CategoryController;
import com.learning.financescontroll.v1.dto.CategoryDto;
import com.learning.financescontroll.v1.exception.CategoryException;

@CacheConfig(cacheNames = "categoria")
@Service
public class CategoryService implements ICategoryService {

	private ICategoryRepository categoryRepository;
	private IUserRepository userRepository;
	private ModelMapper mapper;

	@Autowired
	public CategoryService(ICategoryRepository categoryRepository, IUserRepository userRepository) {
		this.mapper = new ModelMapper();
		this.categoryRepository = categoryRepository;
		this.userRepository = userRepository;
	}

	@CachePut(unless = "#result.size()<3")
	@Override
	public List<CategoryDto> listar() {
		try {
			List<CategoryDto> categoryDto = this.mapper.map(this.categoryRepository.findAll(),
					new TypeToken<List<CategoryDto>>() {
					}.getType());

			categoryDto.forEach(category -> category.add(WebMvcLinkBuilder
					.linkTo(WebMvcLinkBuilder.methodOn(CategoryController.class).consultarCategorias(category.getId()))
					.withSelfRel()));

			return categoryDto;
		} catch (CategoryException c) {
			throw c;
		} catch (Exception e) {
			throw new CategoryException(ServiceConstantVariables.ERRO_GENERICO.getValor(),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@CachePut(key = "#id")
	@Override
	public CategoryDto consultar(Long id) {
		try {
			Optional<CategoryEntity> categoryOptional = this.categoryRepository.findById(id);
			if (categoryOptional.isPresent()) {
				return this.mapper.map(categoryOptional.get(), CategoryDto.class);
			}
			throw new CategoryException(ServiceConstantVariables.NOT_FOUND.getValor(), HttpStatus.NOT_FOUND);
		} catch (CategoryException c) {
			throw c;
		} catch (Exception e) {
			throw new CategoryException(ServiceConstantVariables.ERRO_GENERICO.getValor(),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public Boolean cadastrar(CategoryDto category) {
		try {
			if (category.getId() != null) {
				throw new CategoryException(ServiceConstantVariables.ID_NOT_PERMITTED.getValor(),
						HttpStatus.BAD_REQUEST);
			}
			
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			Optional<UserEntity> usuario = this.userRepository.findByCredenciaisUsername(auth.getName());
			
			if(usuario.isEmpty()) {
				throw new CategoryException("Usuário não encontrado", HttpStatus.NOT_FOUND);
			}

			CategoryEntity categoryEntity = this.mapper.map(category, CategoryEntity.class);
			categoryEntity.setUser(usuario.get());
			this.categoryRepository.save(categoryEntity);
			return Boolean.TRUE;
		} catch (CategoryException c) {
			throw c;
		} catch (Exception e) {
			throw new CategoryException(ServiceConstantVariables.ERRO_GENERICO.getValor(),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public Boolean atualizar(CategoryDto category) {
		try {
			if (category.getId() == null) {
				throw new CategoryException(ServiceConstantVariables.MISSING_ID.getValor(), HttpStatus.BAD_REQUEST);
			}

			this.consultar(category.getId());
			
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			Optional<UserEntity> usuario = this.userRepository.findByCredenciaisUsername(auth.getName());
			
			if (usuario.isEmpty()) {
				throw new CategoryException("Usuário não encontrado", HttpStatus.NOT_FOUND);
			}

			CategoryEntity categoryEntity = this.mapper.map(category, CategoryEntity.class);
			categoryEntity.setUser(usuario.get());
			this.categoryRepository.save(categoryEntity);
			return Boolean.TRUE;
		} catch (CategoryException c) {
			throw c;
		} catch (Exception e) {
			throw new CategoryException(ServiceConstantVariables.ERRO_GENERICO.getValor(),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public Boolean excluir(Long id) {
		try {
			this.consultar(id);
			this.categoryRepository.deleteById(id);
			return Boolean.TRUE;
		} catch (CategoryException c) {
			throw c;
		} catch (Exception e) {
			throw new CategoryException(ServiceConstantVariables.ERRO_GENERICO.getValor(),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
