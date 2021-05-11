package com.learning.financescontroll.v1.service;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;

import com.learning.financescontroll.entity.CategoryEntity;
import com.learning.financescontroll.repository.ICategoryRepository;
import com.learning.financescontroll.v1.dto.CategoryDto;

import lombok.extern.slf4j.Slf4j;

@CacheConfig(cacheNames = "categoria")
@Slf4j
@Service
public class CategoryService implements ICategoryService {
	
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
		} catch (Exception e) {
			log.error("Exception happened on listar: " + e.getCause().getMessage());
			throw e;
		}
	}

	@Override
	public CategoryDto consultar(Long id) {
		try {
			Optional<CategoryEntity> categoryOptional = this.categoryRepository.findById(id);
			if (categoryOptional.isPresent()) {
				return this.mapper.map(categoryOptional.get(), CategoryDto.class);
			}
			return null;
		} catch (Exception e) {
			log.error("Exception happened on consultar: " + e.getCause().getMessage());
			return null;
		}
	}

	@Override
	public Boolean cadastrar(CategoryDto category) {
		try {
			if (category.getId() != null) {
				return Boolean.FALSE;
			}
			
			CategoryEntity categoryEntity = this.mapper.map(category, CategoryEntity.class);
			this.categoryRepository.save(categoryEntity);
			return Boolean.TRUE;
		} catch (Exception e) {
			log.error("Exception happened on cadastrar: " + e.getCause().getMessage());
			return null;
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
		} catch (Exception e) {
			log.error("Exception happened on atualizar: " + e.getCause().getMessage());
			return Boolean.FALSE;
		}
	}

	@Override
	public Boolean excluir(Long id) {
		try {
			this.consultar(id);
			this.categoryRepository.deleteById(id);
			return Boolean.TRUE;
		} catch (Exception e) {
			log.error("Exception happened on excluir: " + e.getCause().getMessage());
			return Boolean.FALSE;
		}
	}

}
