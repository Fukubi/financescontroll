package com.learning.financescontroll.v1.service;

import java.util.Date;
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
import com.learning.financescontroll.entity.EntryEntity;
import com.learning.financescontroll.entity.UserEntity;
import com.learning.financescontroll.repository.ICategoryRepository;
import com.learning.financescontroll.repository.IEntryRepository;
import com.learning.financescontroll.repository.IUserRepository;
import com.learning.financescontroll.v1.constants.ServiceConstantVariables;
import com.learning.financescontroll.v1.controller.EntryController;
import com.learning.financescontroll.v1.dto.EntryDto;
import com.learning.financescontroll.v1.exception.EntryException;
import com.learning.financescontroll.v1.model.EntryModel;
import com.learning.financescontroll.v1.utils.ConverterUtils;

@CacheConfig(cacheNames = "entries")
@Service
public class EntryService implements IEntryService {

	private IEntryRepository entryRepository;
	private ICategoryRepository categoryRepository;
	private IUserRepository userRepository;
	private ModelMapper mapper;

	@Autowired
	public EntryService(IEntryRepository entryRepository, ICategoryRepository categoryRepository,
			IUserRepository userRepository) {
		this.mapper = new ModelMapper();
		this.categoryRepository = categoryRepository;
		this.entryRepository = entryRepository;
		this.userRepository = userRepository;
	}

	@CachePut("#result.size()<3")
	@Override
	public List<EntryDto> listar() {
		try {
			UserEntity user = this.findAuthUser();
			List<EntryDto> entryDto = this.mapper.map(this.entryRepository.findAllByUserId(user.getId()),
					new TypeToken<List<EntryDto>>() {
					}.getType());

			entryDto.forEach(entry -> entry.add(WebMvcLinkBuilder
					.linkTo(WebMvcLinkBuilder.methodOn(EntryController.class).consultarLancamento(entry.getId()))
					.withSelfRel()));

			return entryDto;
		} catch (EntryException c) {
			throw c;
		} catch (Exception e) {
			throw new EntryException(ServiceConstantVariables.ERRO_GENERICO.getValor(),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@CachePut(key = "#id")
	@Override
	public EntryDto consultar(Long id) {
		try {
			Optional<EntryEntity> entryOptional = this.entryRepository.findById(id);
			if (entryOptional.isPresent()) {
				return this.mapper.map(entryOptional.get(), EntryDto.class);
			}
			throw new EntryException(ServiceConstantVariables.NOT_FOUND.getValor(), HttpStatus.NOT_FOUND);
		} catch (EntryException c) {
			throw c;
		} catch (Exception e) {
			throw new EntryException(ServiceConstantVariables.ERRO_GENERICO.getValor(),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public Boolean cadastrar(EntryModel entry) {
		try {
			if (entry.getId() != null) {
				throw new EntryException(ServiceConstantVariables.ID_NOT_PERMITTED.getValor(), HttpStatus.BAD_REQUEST);
			}

			if (entry.getCategoriaId() == null) {
				throw new EntryException(ServiceConstantVariables.MISSING_CATEGORY.getValor(), HttpStatus.BAD_REQUEST);
			}

			if (entry.getData() == null) {
				entry.setData(new Date());
			}

			UserEntity user = this.findAuthUser();
			
			Optional<CategoryEntity> categoryOptional = this.categoryRepository.findById(entry.getCategoriaId());
			if (categoryOptional.isEmpty()) {
				throw new EntryException(ServiceConstantVariables.NOT_FOUND.getValor(), HttpStatus.BAD_REQUEST);
			}
			
			if (!categoryOptional.get().getUser().getId().equals(user.getId())) {
				throw new EntryException(ServiceConstantVariables.NOT_FOUND.getValor(), HttpStatus.NOT_FOUND);
			}
			
			EntryDto entryDto = ConverterUtils.converterEntryModelParaDto(entry);
			entryDto.setCategoria(categoryOptional.get());

			EntryEntity entryEntity = this.mapper.map(entryDto, EntryEntity.class);
			entryEntity.setUser(user);
			this.entryRepository.save(entryEntity);
			return Boolean.TRUE;
		} catch (EntryException c) {
			throw c;
		} catch (Exception e) {
			throw new EntryException(ServiceConstantVariables.ERRO_GENERICO.getValor(),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public Boolean atualizar(EntryModel entry) {
		try {
			if (entry.getId() == null) {
				throw new EntryException(ServiceConstantVariables.MISSING_ID.getValor(), HttpStatus.BAD_REQUEST);
			}

			this.consultar(entry.getId());
			Optional<EntryEntity> entryOptional = this.entryRepository.findById(entry.getId());

			Optional<CategoryEntity> categoryOptional = this.categoryRepository.findById(entry.getCategoriaId());
			if (categoryOptional.isEmpty()) {
				throw new EntryException(ServiceConstantVariables.NOT_FOUND.getValor(), HttpStatus.NOT_FOUND);
			}

			UserEntity user = this.findAuthUser();
			
			if (!categoryOptional.get().getUser().getId().equals(user.getId())) {
				throw new EntryException(ServiceConstantVariables.NOT_FOUND.getValor(), HttpStatus.NOT_FOUND);
			}

			if (entryOptional.isPresent() && entryOptional.get().getUser().getId().equals(user.getId())) {
				EntryDto entryDto = ConverterUtils.converterEntryModelParaDto(entry);
				entryDto.setCategoria(categoryOptional.get());

				EntryEntity entryEntity = this.mapper.map(entryDto, EntryEntity.class);
				entryEntity.setUser(user);
				this.entryRepository.save(entryEntity);
				return Boolean.TRUE;
			}
			throw new EntryException(ServiceConstantVariables.NOT_FOUND.getValor(), HttpStatus.NOT_FOUND);
		} catch (EntryException c) {
			throw c;
		} catch (Exception e) {
			throw new EntryException(ServiceConstantVariables.ERRO_GENERICO.getValor(),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public Boolean excluir(Long id) {
		try {
			this.consultar(id);
			this.entryRepository.deleteById(id);
			return Boolean.TRUE;
		} catch (EntryException c) {
			throw c;
		} catch (Exception e) {
			throw new EntryException(ServiceConstantVariables.ERRO_GENERICO.getValor(),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	private UserEntity findAuthUser() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Optional<UserEntity> user = this.userRepository.findByCredenciaisUsername(auth.getName());

		if (user.isEmpty()) {
			throw new EntryException(ServiceConstantVariables.NOT_FOUND.getValor(), HttpStatus.NOT_FOUND);
		}

		return user.get();
	}

}
