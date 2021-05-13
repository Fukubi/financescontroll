package com.learning.financescontroll.controller.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import com.learning.financescontroll.entity.CategoryEntity;
import com.learning.financescontroll.repository.ICategoryRepository;
import com.learning.financescontroll.v1.dto.CategoryDto;
import com.learning.financescontroll.v1.model.ResponseModel;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@RunWith(JUnitPlatform.class)
class CategoryControllerIntegratedTest {

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	private ICategoryRepository categoryRepository;

	@BeforeEach
	public void init() {
		this.montaBaseDeDados();
	}

	@AfterEach
	public void finish() {
		this.categoryRepository.deleteAll();
	}

	private void montaBaseDeDados() {
		CategoryEntity categoryEntity1 = new CategoryEntity();
		categoryEntity1.setNome("Trabalho");
		categoryEntity1.setDescricao("Emprego na rasmoo e freelas");

		CategoryEntity categoryEntity2 = new CategoryEntity();
		categoryEntity2.setNome("Deposito");
		categoryEntity2.setDescricao("Deposito bancário");

		CategoryEntity categoryEntity3 = new CategoryEntity();
		categoryEntity3.setNome("PayPal");
		categoryEntity3.setDescricao("Pagamento via paypal");

		this.categoryRepository.saveAll(Arrays.asList(categoryEntity1, categoryEntity2, categoryEntity3));
	}

	@Test
	void testListarCategoria() {
		ResponseEntity<ResponseModel<List<CategoryDto>>> categorias = restTemplate.exchange(
				"http://localhost:" + this.port + "/v1/categoria/", HttpMethod.GET, null,
				new ParameterizedTypeReference<ResponseModel<List<CategoryDto>>>() {
				});

		assertNotNull(categorias.getBody().getData());
		assertEquals(3, categorias.getBody().getData().size());
		assertEquals(200, categorias.getBody().getStatusCode());
	}
	
	@Test
	void testConsultarCategoriaPorId() {
		List<CategoryEntity> categoryList = this.categoryRepository.findAll();
		Long id = categoryList.get(0).getId();
		
		ResponseEntity<ResponseModel<CategoryDto>> categorias = restTemplate.exchange(
				"http://localhost:" + this.port + "/v1/categoria/" + id, HttpMethod.GET, null,
				new ParameterizedTypeReference<ResponseModel<CategoryDto>>() {
				});

		assertNotNull(categorias.getBody().getData());
		assertEquals(id, categorias.getBody().getData().getId());
		assertEquals("Trabalho", categorias.getBody().getData().getNome());
		assertEquals("Emprego na rasmoo e freelas", categorias.getBody().getData().getDescricao());
		assertEquals(200, categorias.getBody().getStatusCode());
	}
	
	@Test
	void testAtualizarCategoria() {
		List<CategoryEntity> categoryList = this.categoryRepository.findAll();
		CategoryEntity category = categoryList.get(0);
		
		category.setNome("TESTANDO ATUALIZAR");
		
		HttpEntity<CategoryEntity> request = new HttpEntity<>(category);
		
		ResponseEntity<ResponseModel<Boolean>> categorias = restTemplate.exchange(
				"http://localhost:" + this.port + "/v1/categoria/", HttpMethod.PUT, request,
				new ParameterizedTypeReference<ResponseModel<Boolean>>() {
				});
		
		CategoryEntity categoryAtualizada = this.categoryRepository.findById(category.getId()).get();

		assertTrue(categorias.getBody().getData());
		assertEquals("TESTANDO ATUALIZAR", categoryAtualizada.getNome());
		assertEquals(200, categorias.getBody().getStatusCode());
	}
	
	@Test
	void testCadastrarCategoria() {
		CategoryEntity categoryEntity4 = new CategoryEntity();
		categoryEntity4.setNome("Pix");
		categoryEntity4.setDescricao("Transferencia utilizando pix");
		
		HttpEntity<CategoryEntity> request = new HttpEntity<>(categoryEntity4);
		
		ResponseEntity<ResponseModel<Boolean>> categorias = restTemplate.exchange(
				"http://localhost:" + this.port + "/v1/categoria/", HttpMethod.POST, request,
				new ParameterizedTypeReference<ResponseModel<Boolean>>() {
				});
		
		List<CategoryEntity> categoryListAtualizada = this.categoryRepository.findAll();
		
		assertTrue(categorias.getBody().getData());
		assertEquals(4, categoryListAtualizada.size());
		assertEquals(201, categorias.getBody().getStatusCode());
	}
	
	@Test
	void testExcluirMateriaPorId() {
		List<CategoryEntity> categoryList = this.categoryRepository.findAll();
		Long id = categoryList.get(0).getId();
		
		ResponseEntity<ResponseModel<Boolean>> categorias = restTemplate.exchange(
				"http://localhost:" + this.port + "/v1/categoria/" + id, HttpMethod.DELETE, null,
				new ParameterizedTypeReference<ResponseModel<Boolean>>() {
				});
		
		List<CategoryEntity> categoryListAtualizada = this.categoryRepository.findAll();

		assertTrue(categorias.getBody().getData());
		assertEquals(2, categoryListAtualizada.size());
		assertEquals(200, categorias.getBody().getStatusCode());
	}

}
