package com.learning.financescontroll.service.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import com.learning.financescontroll.entity.CategoryEntity;
import com.learning.financescontroll.entity.EntryEntity;
import com.learning.financescontroll.enumerators.TipoEnum;
import com.learning.financescontroll.repository.ICategoryRepository;
import com.learning.financescontroll.repository.IEntryRepository;
import com.learning.financescontroll.v1.constants.ServiceConstantVariables;
import com.learning.financescontroll.v1.dto.EntryDto;
import com.learning.financescontroll.v1.exception.EntryException;
import com.learning.financescontroll.v1.model.EntryModel;
import com.learning.financescontroll.v1.service.EntryService;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
class EntryServiceUnitTest {

	@Mock
	private IEntryRepository entryRepository;

	@Mock
	private ICategoryRepository categoryRepository;

	@InjectMocks
	private EntryService entryService;

	private static EntryEntity entryEntity;

	@BeforeAll
	public static void init() {
		entryEntity = new EntryEntity();
		entryEntity.setId(1L);
		entryEntity.setData(new Date());
		entryEntity.setValor(100);
		entryEntity.setTipo(TipoEnum.RECEITA);

		CategoryEntity category = new CategoryEntity();
		category.setId(1L);
		category.setNome("Trabalho");
		category.setDescricao("Emprego na rasmoo e freelas");
		entryEntity.setCategoria(category);
	}

	@Test
	void testListarSucesso() {
		List<EntryEntity> listEntry = new ArrayList<>();
		listEntry.add(entryEntity);

		Mockito.when(this.entryRepository.findAll()).thenReturn(listEntry);

		List<EntryDto> listEntryDto = this.entryService.listar();

		assertNotNull(listEntryDto);
		assertEquals(1L, listEntryDto.get(0).getId());
		assertEquals(100, listEntryDto.get(0).getValor());
		assertEquals(TipoEnum.RECEITA, listEntryDto.get(0).getTipo());
		assertEquals("/v1/entry/1", listEntryDto.get(0).getLinks().getRequiredLink("self").getHref());
		assertEquals(1, listEntryDto.size());

		Mockito.verify(this.entryRepository, times(1)).findAll();
	}

	@Test
	void testConsultarSucesso() {
		Mockito.when(this.entryRepository.findById(1L)).thenReturn(Optional.of(entryEntity));
		EntryDto entryDto = this.entryService.consultar(1L);

		assertNotNull(entryDto);
		assertEquals(1L, entryDto.getId());
		assertEquals(100, entryDto.getValor());
		assertEquals(TipoEnum.RECEITA, entryDto.getTipo());

		Mockito.verify(this.entryRepository, times(1)).findById(1L);
	}

	@Test
	void testCadastrarSucesso() {
		EntryModel entryModel = new EntryModel();
		entryModel.setData(entryEntity.getData());
		entryModel.setValor(100);
		entryModel.setTipo(TipoEnum.RECEITA);

		CategoryEntity category = new CategoryEntity();
		category.setId(1L);
		category.setNome("Trabalho");
		category.setDescricao("Emprego na rasmoo e freelas");
		entryModel.setCategoriaId(category.getId());

		entryEntity.setId(null);

		Mockito.when(this.categoryRepository.findById(1L)).thenReturn(Optional.of(category));
		Mockito.when(this.entryRepository.save(entryEntity)).thenReturn(entryEntity);

		Boolean sucesso = this.entryService.cadastrar(entryModel);

		assertTrue(sucesso);

		Mockito.verify(this.entryRepository, times(1)).save(entryEntity);

		entryEntity.setId(1L);
	}

	@Test
	void testAtualizarSucesso() {
		EntryModel entryModel = new EntryModel();
		entryModel.setId(1L);
		entryModel.setData(entryEntity.getData());
		entryModel.setValor(100);
		entryModel.setTipo(TipoEnum.RECEITA);

		CategoryEntity category = new CategoryEntity();
		category.setId(1L);
		category.setNome("Trabalho");
		category.setDescricao("Emprego na rasmoo e freelas");
		entryModel.setCategoriaId(category.getId());

		Mockito.when(this.categoryRepository.findById(1L)).thenReturn(Optional.of(category));
		Mockito.when(this.entryRepository.findById(1L)).thenReturn(Optional.of(entryEntity));
		Mockito.when(this.entryRepository.save(entryEntity)).thenReturn(entryEntity);

		Boolean sucesso = this.entryService.atualizar(entryModel);

		assertTrue(sucesso);

		Mockito.verify(this.entryRepository, times(1)).findById(1L);
		Mockito.verify(this.entryRepository, times(1)).save(entryEntity);
	}

	@Test
	void testExcluirSucesso() {
		Mockito.when(this.entryRepository.findById(1L)).thenReturn(Optional.of(entryEntity));

		Boolean sucesso = this.entryService.excluir(1L);

		assertTrue(sucesso);

		Mockito.verify(this.entryRepository, times(1)).findById(1L);
		Mockito.verify(this.entryRepository, times(1)).deleteById(1L);
	}

	@Test
	void testAtualizarThrowEntryException() {
		EntryModel entryModel = new EntryModel();
		entryModel.setId(1L);
		entryModel.setData(entryEntity.getData());
		entryModel.setValor(100);
		entryModel.setTipo(TipoEnum.RECEITA);

		CategoryEntity category = new CategoryEntity();
		category.setId(1L);
		category.setNome("Trabalho");
		category.setDescricao("Emprego na rasmoo e freelas");
		entryModel.setCategoriaId(category.getId());

		Mockito.when(this.entryRepository.findById(1L)).thenReturn(Optional.empty());

		EntryException entryException;

		entryException = assertThrows(EntryException.class, () -> {
			this.entryService.atualizar(entryModel);
		});

		assertEquals(HttpStatus.NOT_FOUND, entryException.getHttpStatus());
		assertEquals(ServiceConstantVariables.NOT_FOUND.getValor(), entryException.getMessage());

		Mockito.verify(this.entryRepository, times(1)).findById(1L);
		Mockito.verify(this.entryRepository, times(0)).save(entryEntity);
	}

	@Test
	void testExcluirThrowEntryException() {
		Mockito.when(this.entryRepository.findById(1L)).thenReturn(Optional.empty());

		EntryException entryException;

		entryException = assertThrows(EntryException.class, () -> {
			this.entryService.excluir(1L);
		});

		assertEquals(HttpStatus.NOT_FOUND, entryException.getHttpStatus());
		assertEquals(ServiceConstantVariables.NOT_FOUND.getValor(), entryException.getMessage());

		Mockito.verify(this.entryRepository, times(1)).findById(1L);
		Mockito.verify(this.entryRepository, times(0)).deleteById(1L);
	}

	@Test
	void testCadastrarComIdThrowsEntryException() {
		EntryModel entryModel = new EntryModel();
		entryModel.setId(1L);
		entryModel.setData(entryEntity.getData());
		entryModel.setValor(100);
		entryModel.setTipo(TipoEnum.RECEITA);

		CategoryEntity category = new CategoryEntity();
		category.setId(1L);
		category.setNome("Trabalho");
		category.setDescricao("Emprego na rasmoo e freelas");
		entryModel.setCategoriaId(category.getId());

		EntryException entryException;

		entryException = assertThrows(EntryException.class, () -> {
			this.entryService.cadastrar(entryModel);
		});

		assertEquals(HttpStatus.BAD_REQUEST, entryException.getHttpStatus());
		assertEquals(ServiceConstantVariables.ID_NOT_PERMITTED.getValor(), entryException.getMessage());

		Mockito.verify(this.entryRepository, times(0)).save(entryEntity);
	}

	@Test
	void testConsultarThrowsEntryException() {
		Mockito.when(this.entryRepository.findById(1L)).thenReturn(Optional.empty());

		EntryException entryException;

		entryException = assertThrows(EntryException.class, () -> {
			this.entryService.consultar(1L);
		});

		assertEquals(HttpStatus.NOT_FOUND, entryException.getHttpStatus());
		assertEquals(ServiceConstantVariables.NOT_FOUND.getValor(), entryException.getMessage());

		Mockito.verify(this.entryRepository, times(1)).findById(1L);
	}

	@Test
	void testAtualizarThrowException() {
		EntryModel entryModel = new EntryModel();
		entryModel.setId(1L);
		entryModel.setData(entryEntity.getData());
		entryModel.setValor(100);
		entryModel.setTipo(TipoEnum.RECEITA);

		CategoryEntity category = new CategoryEntity();
		category.setId(1L);
		category.setNome("Trabalho");
		category.setDescricao("Emprego na rasmoo e freelas");
		entryModel.setCategoriaId(category.getId());

		Mockito.when(this.categoryRepository.findById(1L)).thenReturn(Optional.of(category));
		Mockito.when(this.entryRepository.findById(1L)).thenReturn(Optional.of(entryEntity));
		Mockito.when(this.entryRepository.save(entryEntity)).thenThrow(IllegalStateException.class);

		EntryException entryException;

		entryException = assertThrows(EntryException.class, () -> {
			this.entryService.atualizar(entryModel);
		});

		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, entryException.getHttpStatus());
		assertEquals(ServiceConstantVariables.ERRO_GENERICO.getValor(), entryException.getMessage());

		Mockito.verify(this.entryRepository, times(1)).findById(1L);
		Mockito.verify(this.entryRepository, times(1)).save(entryEntity);
	}

	@Test
	void testCadastrarThrowsException() {
		EntryModel entryModel = new EntryModel();
		entryModel.setData(entryEntity.getData());
		entryModel.setValor(100);
		entryModel.setTipo(TipoEnum.RECEITA);

		CategoryEntity category = new CategoryEntity();
		category.setId(1L);
		category.setNome("Trabalho");
		category.setDescricao("Emprego na rasmoo e freelas");
		entryModel.setCategoriaId(category.getId());

		entryEntity.setId(null);

		Mockito.when(this.categoryRepository.findById(1L)).thenReturn(Optional.of(category));
		Mockito.when(this.entryRepository.save(entryEntity)).thenThrow(IllegalStateException.class);

		EntryException entryException;

		entryException = assertThrows(EntryException.class, () -> {
			this.entryService.cadastrar(entryModel);
		});

		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, entryException.getHttpStatus());
		assertEquals(ServiceConstantVariables.ERRO_GENERICO.getValor(), entryException.getMessage());

		Mockito.verify(this.entryRepository, times(1)).save(entryEntity);
		entryEntity.setId(1L);
	}

	@Test
	void testConsultarThrowsException() {
		Mockito.when(this.entryRepository.findById(1L)).thenThrow(IllegalStateException.class);

		EntryException entryException;

		entryException = assertThrows(EntryException.class, () -> {
			this.entryService.consultar(1L);
		});

		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, entryException.getHttpStatus());
		assertEquals(ServiceConstantVariables.ERRO_GENERICO.getValor(), entryException.getMessage());

		Mockito.verify(this.entryRepository, times(1)).findById(1L);
	}

	@Test
	void testListarThrowsException() {
		Mockito.when(this.entryRepository.findAll()).thenThrow(IllegalStateException.class);

		EntryException entryException;

		entryException = assertThrows(EntryException.class, () -> {
			this.entryService.listar();
		});

		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, entryException.getHttpStatus());
		assertEquals(ServiceConstantVariables.ERRO_GENERICO.getValor(), entryException.getMessage());

		Mockito.verify(this.entryRepository, times(1)).findAll();
	}
	
	@Test
	void testExcluirThrowException() {
		Mockito.when(this.entryRepository.findById(1L)).thenReturn(Optional.of(entryEntity));
		Mockito.doThrow(IllegalStateException.class).when(this.entryRepository).deleteById(1L);

		EntryException entryException;

		entryException = assertThrows(EntryException.class, () -> {
			this.entryService.excluir(1L);
		});

		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, entryException.getHttpStatus());
		assertEquals(ServiceConstantVariables.ERRO_GENERICO.getValor(), entryException.getMessage());

		Mockito.verify(this.entryRepository, times(1)).findById(1L);
		Mockito.verify(this.entryRepository, times(1)).deleteById(1L);
	}
}
