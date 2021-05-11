package com.learning.financescontroll.v1.service;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.learning.financescontroll.entity.CategoryEntity;
import com.learning.financescontroll.repository.ICategoryRepository;
import com.learning.financescontroll.v1.dto.CategoryDto;
import com.learning.financescontroll.v1.exception.CategoryException;

@CacheConfig(cacheNames = "categoria")
@Service
public class CategoryService implements ICategoryService {
	
	private String ERRO_GENERICO = "Erro interno identificado, Contate o suporte";
	
	private ICategoryRepository categoryRepository;
	private ModelMapper mapper;
	
	@Autowired
	public CategoryService(ICategoryRepository categoryRepository) {
		this.mapper = new ModelMapper();
		this.categoryRepository = categoryRepository;
	}

	@CachePut(unless = "#result.size()<3")
	@Override
	public List<CategoryDto> listar() {
		try {
			return this.mapper.map(this.categoryRepository.findAll(), new TypeToken<List<CategoryDto>>() {}.getType());
		} catch (CategoryException c) {
			throw c;
		} catch (Exception e) {
			throw new CategoryException(ERRO_GENERICO, HttpStatus.INTERNAL_SERVER_ERROR);
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
			throw new CategoryException("Categoria não encontrada", HttpStatus.NOT_FOUND);
		} catch (CategoryException c) {
			throw c;
		} catch (Exception e) {
			throw new CategoryException(ERRO_GENERICO, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public Boolean cadastrar(CategoryDto category) {
		try {
			if (category.getId() != null) {
				throw new CategoryException("Não pode existir ID em cadastrar.", HttpStatus.BAD_REQUEST);
			}
			
			CategoryEntity categoryEntity = this.mapper.map(category, CategoryEntity.class);
			this.categoryRepository.save(categoryEntity);
			return Boolean.TRUE;
		} catch (CategoryException c) {
			throw c;
		} catch (Exception e) {
			throw new CategoryException(ERRO_GENERICO, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public Boolean atualizar(CategoryDto category) {
		try {
			if (category.getId() == null) {
				return Boolean.FALSE;
			}
			
			this.consultar(category.getId());
			
			CategoryEntity categoryEntity = this.mapper.map(category, CategoryEntity.class);
			this.categoryRepository.save(categoryEntity);
			return Boolean.TRUE;
		} catch (CategoryException c) {
			throw c;
		} catch (Exception e) {
			throw new CategoryException(ERRO_GENERICO, HttpStatus.INTERNAL_SERVER_ERROR);
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
			throw new CategoryException(ERRO_GENERICO, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
