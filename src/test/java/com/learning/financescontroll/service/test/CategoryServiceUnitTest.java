package com.learning.financescontroll.service.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;

import java.util.ArrayList;
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
import com.learning.financescontroll.repository.ICategoryRepository;
import com.learning.financescontroll.v1.constants.ServiceConstantVariables;
import com.learning.financescontroll.v1.dto.CategoryDto;
import com.learning.financescontroll.v1.exception.CategoryException;
import com.learning.financescontroll.v1.service.CategoryService;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
class CategoryServiceUnitTest {

	@Mock
	private ICategoryRepository categoryRepository;
	
	@InjectMocks
	private CategoryService categoryService;
	
	private static CategoryEntity categoryEntity;
	
	@BeforeAll
	public static void init() {
		categoryEntity = new CategoryEntity();
		categoryEntity.setId(1L);
		categoryEntity.setNome("Trabalho");
		categoryEntity.setDescricao("Emprego na rasmoo e freelas");
	}
	
	@Test
	void testListarSucesso() {
		List<CategoryEntity> listCategory = new ArrayList<>();
		listCategory.add(categoryEntity);
		
		Mockito.when(this.categoryRepository.findAll()).thenReturn(listCategory);
		
		List<CategoryDto> listCategoryDto = this.categoryService.listar();
		
		assertNotNull(listCategoryDto);
		assertEquals(1L, listCategoryDto.get(0).getId());
		assertEquals("Trabalho", listCategoryDto.get(0).getNome());
		assertEquals("Emprego na rasmoo e freelas", listCategoryDto.get(0).getDescricao());
		assertEquals("/v1/categoria/1", listCategoryDto.get(0).getLinks().getRequiredLink("self").getHref());
		assertEquals(1, listCategoryDto.size());
		
		Mockito.verify(this.categoryRepository, times(1)).findAll();
	}
	
	@Test
	void testConsultarSucesso() {
		Mockito.when(this.categoryRepository.findById(1L)).thenReturn(Optional.of(categoryEntity));
		CategoryDto categoryDto = this.categoryService.consultar(1L);
		
		assertNotNull(categoryDto);
		assertEquals(1L, categoryDto.getId());
		assertEquals("Trabalho", categoryDto.getNome());
		assertEquals("Emprego na rasmoo e freelas", categoryDto.getDescricao());
		
		Mockito.verify(this.categoryRepository, times(1)).findById(1L);
	}
	
	@Test
	void testCadastrarSucesso() {
		CategoryDto categoryDto = new CategoryDto();
		categoryDto.setNome("Trabalho");
		categoryDto.setDescricao("Emprego na rasmoo e freelas");
		
		categoryEntity.setId(null);
		
		Mockito.when(this.categoryRepository.save(categoryEntity)).thenReturn(categoryEntity);
		
		Boolean sucesso = this.categoryService.cadastrar(categoryDto);
		
		assertTrue(sucesso);
		
		Mockito.verify(this.categoryRepository, times(1)).save(categoryEntity);
		
		categoryEntity.setId(1L);
	}
	
	@Test
	void testAtualizarSucesso() {
		CategoryDto categoryDto = new CategoryDto();
		categoryDto.setId(1L);
		categoryDto.setNome("Trabalho");
		categoryDto.setDescricao("Emprego na rasmoo e freelas");
		
		Mockito.when(this.categoryRepository.findById(1L)).thenReturn(Optional.of(categoryEntity));
		Mockito.when(this.categoryRepository.save(categoryEntity)).thenReturn(categoryEntity);
		
		Boolean sucesso = this.categoryService.atualizar(categoryDto);
		
		assertTrue(sucesso);
		
		Mockito.verify(this.categoryRepository, times(1)).findById(1L);
		Mockito.verify(this.categoryRepository, times(1)).save(categoryEntity);
	}
	
	@Test
	void testExcluirSucesso() {
		Mockito.when(this.categoryRepository.findById(1L)).thenReturn(Optional.of(categoryEntity));
		
		Boolean sucesso = this.categoryService.excluir(1L);
		
		assertTrue(sucesso);
		
		Mockito.verify(this.categoryRepository, times(1)).findById(1L);
		Mockito.verify(this.categoryRepository, times(1)).deleteById(1L);
	}
	
	@Test
	void testAtualizarThrowCategoryException() {
		CategoryDto categoryDto = new CategoryDto();
		categoryDto.setId(1L);
		categoryDto.setNome("Trabalho");
		categoryDto.setDescricao("Emprego na rasmoo e freelas");
		
		Mockito.when(this.categoryRepository.findById(1L)).thenReturn(Optional.empty());
		
		CategoryException categoryException;
		
		categoryException = assertThrows(CategoryException.class, () -> {
			this.categoryService.atualizar(categoryDto);
		});
		
		assertEquals(HttpStatus.NOT_FOUND, categoryException.getHttpStatus());
		assertEquals(ServiceConstantVariables.NOT_FOUND.getValor(), categoryException.getMessage());
		
		Mockito.verify(this.categoryRepository, times(1)).findById(1L);
		Mockito.verify(this.categoryRepository, times(0)).save(categoryEntity);
	}
	
	@Test
	void testExcluirThrowsCategoryException() {
		Mockito.when(this.categoryRepository.findById(1L)).thenReturn(Optional.empty());
		
		CategoryException categoryException;
		
		categoryException = assertThrows(CategoryException.class, () -> {
			this.categoryService.excluir(1L);
		});
		
		assertEquals(HttpStatus.NOT_FOUND, categoryException.getHttpStatus());
		assertEquals(ServiceConstantVariables.NOT_FOUND.getValor(), categoryException.getMessage());
		
		Mockito.verify(this.categoryRepository, times(1)).findById(1L);
		Mockito.verify(this.categoryRepository, times(0)).deleteById(1L);
	}
	
	@Test
	void testCadastrarComIdThrowsCategoryException() {
		CategoryDto categoryDto = new CategoryDto();
		categoryDto.setId(1L);
		categoryDto.setNome("Trabalho");
		categoryDto.setDescricao("Emprego na rasmoo e freelas");
		
		CategoryException categoryException;
		
		categoryException = assertThrows(CategoryException.class, () -> {
			this.categoryService.cadastrar(categoryDto);
		});
		
		assertEquals(HttpStatus.BAD_REQUEST, categoryException.getHttpStatus());
		assertEquals(ServiceConstantVariables.ID_NOT_PERMITTED.getValor(), categoryException.getMessage());
		
		Mockito.verify(this.categoryRepository, times(0)).save(categoryEntity);
	}
	
	@Test
	void testConsultarThrowsCategoryException() {
		Mockito.when(this.categoryRepository.findById(1L)).thenReturn(Optional.empty());
		
		CategoryException categoryException;
		
		categoryException = assertThrows(CategoryException.class, () -> {
			this.categoryService.consultar(1L);
		});
		
		assertEquals(HttpStatus.NOT_FOUND, categoryException.getHttpStatus());
		assertEquals(ServiceConstantVariables.NOT_FOUND.getValor(), categoryException.getMessage());
		
		Mockito.verify(this.categoryRepository, times(1)).findById(1L);
	}
	
	@Test
	void testAtualizarThrowException() {
		CategoryDto categoryDto = new CategoryDto();
		categoryDto.setId(1L);
		categoryDto.setNome("Trabalho");
		categoryDto.setDescricao("Emprego na rasmoo e freelas");
		
		Mockito.when(this.categoryRepository.findById(1L)).thenReturn(Optional.of(categoryEntity));
		Mockito.when(this.categoryRepository.save(categoryEntity)).thenThrow(IllegalStateException.class);
		
		CategoryException categoryException;
		
		categoryException = assertThrows(CategoryException.class, () -> {
			this.categoryService.atualizar(categoryDto);
		});
		
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, categoryException.getHttpStatus());
		assertEquals(ServiceConstantVariables.ERRO_GENERICO.getValor(), categoryException.getMessage());
		
		Mockito.verify(this.categoryRepository, times(1)).findById(1L);
		Mockito.verify(this.categoryRepository, times(1)).save(categoryEntity);
	}
	
	@Test
	void testCadastrarThrowsException() {
		CategoryDto categoryDto = new CategoryDto();
		categoryDto.setNome("Trabalho");
		categoryDto.setDescricao("Emprego na rasmoo e freelas");
		
		categoryEntity.setId(null);
		
		Mockito.when(this.categoryRepository.save(categoryEntity)).thenThrow(IllegalStateException.class);
		
		CategoryException categoryException;
		
		categoryException = assertThrows(CategoryException.class, () -> {
			this.categoryService.cadastrar(categoryDto);
		});
		
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, categoryException.getHttpStatus());
		assertEquals(ServiceConstantVariables.ERRO_GENERICO.getValor(), categoryException.getMessage());
		
		Mockito.verify(this.categoryRepository, times(1)).save(categoryEntity);
		
		categoryEntity.setId(1L);
	}
	
	@Test
	void testConsultarThrowsException() {
		Mockito.when(this.categoryRepository.findById(1L)).thenThrow(IllegalStateException.class);
		
		CategoryException categoryException;
		
		categoryException = assertThrows(CategoryException.class, () -> {
			this.categoryService.consultar(1L);
		});
		
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, categoryException.getHttpStatus());
		assertEquals(ServiceConstantVariables.ERRO_GENERICO.getValor(), categoryException.getMessage());
		
		Mockito.verify(this.categoryRepository, times(1)).findById(1L);
	}
	
	@Test
	void testListarThrowException() {
		Mockito.when(this.categoryRepository.findAll()).thenThrow(IllegalStateException.class);
		
		CategoryException categoryException;
		
		categoryException = assertThrows(CategoryException.class, () -> {
			this.categoryService.listar();
		});
		
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, categoryException.getHttpStatus());
		assertEquals(ServiceConstantVariables.ERRO_GENERICO.getValor(), categoryException.getMessage());
		
		Mockito.verify(this.categoryRepository, times(1)).findAll();
	}
	
	@Test
	void testExcluirThrowsException() {
		Mockito.when(this.categoryRepository.findById(1L)).thenReturn(Optional.of(categoryEntity));
		Mockito.doThrow(IllegalStateException.class).when(this.categoryRepository).deleteById(1L);
		
		CategoryException categoryException;
		
		categoryException = assertThrows(CategoryException.class, () -> {
			this.categoryService.excluir(1L);
		});
		
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, categoryException.getHttpStatus());
		assertEquals(ServiceConstantVariables.ERRO_GENERICO.getValor(), categoryException.getMessage());
		
		Mockito.verify(this.categoryRepository, times(1)).findById(1L);
		Mockito.verify(this.categoryRepository, times(1)).deleteById(1L);
	}
}
