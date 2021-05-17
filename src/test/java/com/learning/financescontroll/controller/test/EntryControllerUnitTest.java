package com.learning.financescontroll.controller.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Date;
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

import com.learning.financescontroll.entity.CategoryEntity;
import com.learning.financescontroll.enumerators.TipoEnum;
import com.learning.financescontroll.v1.dto.EntryDto;
import com.learning.financescontroll.v1.model.EntryModel;
import com.learning.financescontroll.v1.model.ResponseModel;
import com.learning.financescontroll.v1.service.IEntryService;
import com.learning.financescontroll.v1.utils.ConverterUtils;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@RunWith(JUnitPlatform.class)
class EntryControllerUnitTest {

	@LocalServerPort
	private int port;
	
	@MockBean
	private IEntryService entryService;
	
	@Autowired
	private TestRestTemplate restTemplate;
	
	private static EntryDto entryDto;
	
	@BeforeAll
	public static void init() {
		entryDto = new EntryDto();
		entryDto.setId(1L);
		entryDto.setData(new Date());
		entryDto.setValor(100);
		entryDto.setTipo(TipoEnum.RECEITA);
		
		CategoryEntity category = new CategoryEntity();
		category.setId(1L);
		category.setNome("Trabalho");
		category.setDescricao("Emprego na rasmoo e freelas");
		entryDto.setCategoria(category);
	}
	
	@Test
	void testListarEntry() {
		Mockito.when(this.entryService.listar()).thenReturn(new ArrayList<EntryDto>());

		ResponseEntity<ResponseModel<List<EntryDto>>> entries = restTemplate.exchange(
				"http://localhost:" + this.port + "/v1/entry/", HttpMethod.GET, null,
				new ParameterizedTypeReference<ResponseModel<List<EntryDto>>>() {
				});

		assertNotNull(entries.getBody().getData());
		assertEquals(200, entries.getBody().getStatusCode());
	}
	
	@Test
	void testConsultarEntry() {
		Mockito.when(this.entryService.consultar(1L)).thenReturn(entryDto);

		ResponseEntity<ResponseModel<EntryDto>> entries = restTemplate.exchange(
				"http://localhost:" + this.port + "/v1/entry/1", HttpMethod.GET, null,
				new ParameterizedTypeReference<ResponseModel<EntryDto>>() {
				});

		assertNotNull(entries.getBody().getData());
		assertEquals(200, entries.getBody().getStatusCode());
	}
	
	@Test
	void testCadastrarEntry() {
		EntryModel entryModel = ConverterUtils.converterEntryDtoParaModel(entryDto);
		Mockito.when(this.entryService.cadastrar(entryModel)).thenReturn(Boolean.TRUE);

		HttpEntity<EntryModel> request = new HttpEntity<>(entryModel);

		ResponseEntity<ResponseModel<Boolean>> entries = restTemplate.exchange(
				"http://localhost:" + this.port + "/v1/entry/", HttpMethod.POST, request,
				new ParameterizedTypeReference<ResponseModel<Boolean>>() {
				});

		System.out.println(entries.getBody().getData());
		assertNotNull(entries.getBody().getData());
		assertTrue(entries.getBody().getData());
		assertEquals(201, entries.getBody().getStatusCode());
	}
	
	@Test
	void testAtualizarCategoria() {
		EntryModel entryModel = ConverterUtils.converterEntryDtoParaModel(entryDto);
		Mockito.when(this.entryService.atualizar(entryModel)).thenReturn(Boolean.TRUE);

		HttpEntity<EntryModel> request = new HttpEntity<>(entryModel);

		ResponseEntity<ResponseModel<Boolean>> entries = restTemplate.exchange(
				"http://localhost:" + this.port + "/v1/entry/", HttpMethod.PUT, request,
				new ParameterizedTypeReference<ResponseModel<Boolean>>() {
				});

		assertNotNull(entries.getBody().getData());
		assertTrue(entries.getBody().getData());
		assertEquals(200, entries.getBody().getStatusCode());
	}
	
	@Test
	void testExcluirEntry() {
		Mockito.when(this.entryService.excluir(1L)).thenReturn(Boolean.TRUE);

		ResponseEntity<ResponseModel<Boolean>> entries = restTemplate.exchange(
				"http://localhost:" + this.port + "/v1/entry/1", HttpMethod.DELETE, null,
				new ParameterizedTypeReference<ResponseModel<Boolean>>() {
				});

		assertNotNull(entries.getBody().getData());
		assertTrue(entries.getBody().getData());
		assertEquals(200, entries.getBody().getStatusCode());
	}
}
