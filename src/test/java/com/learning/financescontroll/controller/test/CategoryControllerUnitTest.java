package com.learning.financescontroll.controller.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import com.learning.financescontroll.v1.dto.CategoryDto;
import com.learning.financescontroll.v1.model.ResponseModel;
import com.learning.financescontroll.v1.service.ICategoryService;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@RunWith(JUnitPlatform.class)
class CategoryControllerUnitTest {

	@LocalServerPort
	private int port;

	@MockBean
	private ICategoryService categoryService;

	@Autowired
	private TestRestTemplate restTemplate;

	private static CategoryDto categoryDto;

	@BeforeAll
	public static void init() {
		categoryDto = new CategoryDto();
		categoryDto.setId(1L);
		categoryDto.setNome("Trabalho");
		categoryDto.setDescricao("Emprego na rasmoo e freelas");
	}

	@Test
	void testListarCategorias() {
		Mockito.when(this.categoryService.listar()).thenReturn(new ArrayList<CategoryDto>());

		ResponseEntity<ResponseModel<List<CategoryDto>>> categorias = restTemplate.exchange(
				"http://localhost:" + this.port + "/v1/categoria/", HttpMethod.GET, null,
				new ParameterizedTypeReference<ResponseModel<List<CategoryDto>>>() {
				});

		assertNotNull(categorias.getBody().getData());
		assertEquals(200, categorias.getBody().getStatusCode());
	}

	@Test
	void testConsultarCategoria() {
		Mockito.when(this.categoryService.consultar(1L)).thenReturn(categoryDto);

		ResponseEntity<ResponseModel<CategoryDto>> categorias = restTemplate.exchange(
				"http://localhost:" + this.port + "/v1/categoria/1", HttpMethod.GET, null,
				new ParameterizedTypeReference<ResponseModel<CategoryDto>>() {
				});

		assertNotNull(categorias.getBody().getData());
		assertEquals(200, categorias.getBody().getStatusCode());
	}

	@Test
	void testCadastrarCategoria() {
		Mockito.when(this.categoryService.cadastrar(categoryDto)).thenReturn(Boolean.TRUE);

		HttpEntity<CategoryDto> request = new HttpEntity<>(categoryDto);

		ResponseEntity<ResponseModel<Boolean>> categorias = restTemplate.exchange(
				"http://localhost:" + this.port + "/v1/categoria/", HttpMethod.POST, request,
				new ParameterizedTypeReference<ResponseModel<Boolean>>() {
				});

		assertNotNull(categorias.getBody().getData());
		assertTrue(categorias.getBody().getData());
		assertEquals(201, categorias.getBody().getStatusCode());
	}

	@Test
	void testAtualizarCategoria() {
		Mockito.when(this.categoryService.atualizar(categoryDto)).thenReturn(Boolean.TRUE);

		HttpEntity<CategoryDto> request = new HttpEntity<>(categoryDto);

		ResponseEntity<ResponseModel<Boolean>> categorias = restTemplate.exchange(
				"http://localhost:" + this.port + "/v1/categoria/", HttpMethod.PUT, request,
				new ParameterizedTypeReference<ResponseModel<Boolean>>() {
				});

		assertNotNull(categorias.getBody().getData());
		assertTrue(categorias.getBody().getData());
		assertEquals(200, categorias.getBody().getStatusCode());
	}

	@Test
	void testExcluirCategoria() {
		Mockito.when(this.categoryService.excluir(1L)).thenReturn(Boolean.TRUE);

		ResponseEntity<ResponseModel<Boolean>> categorias = restTemplate.exchange(
				"http://localhost:" + this.port + "/v1/categoria/1", HttpMethod.DELETE, null,
				new ParameterizedTypeReference<ResponseModel<Boolean>>() {
				});

		assertNotNull(categorias.getBody().getData());
		assertTrue(categorias.getBody().getData());
		assertEquals(200, categorias.getBody().getStatusCode());
	}

}
