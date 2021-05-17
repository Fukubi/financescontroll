package com.learning.financescontroll.controller.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
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
import com.learning.financescontroll.entity.EntryEntity;
import com.learning.financescontroll.enumerators.TipoEnum;
import com.learning.financescontroll.repository.ICategoryRepository;
import com.learning.financescontroll.repository.IEntryRepository;
import com.learning.financescontroll.v1.dto.EntryDto;
import com.learning.financescontroll.v1.model.EntryModel;
import com.learning.financescontroll.v1.model.ResponseModel;
import com.learning.financescontroll.v1.utils.ConverterUtils;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@RunWith(JUnitPlatform.class)
class EntryControllerIntegratedTest {

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	private IEntryRepository entryRepository;

	@Autowired
	private ICategoryRepository categoryRepository;

	ModelMapper mapper = new ModelMapper();

	@BeforeEach
	public void init() {
		this.montaBaseDeDados();
	}

	@AfterEach
	public void finish() {
		this.entryRepository.deleteAll();
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

		this.categoryRepository.saveAll(List.of(categoryEntity1, categoryEntity2, categoryEntity3));
		List<CategoryEntity> categoriesList = this.categoryRepository.findAll();

		EntryEntity entryEntity1 = new EntryEntity();
		entryEntity1.setData(new Date());
		entryEntity1.setValor(100);
		entryEntity1.setTipo(TipoEnum.RECEITA);
		entryEntity1.setCategoria(categoriesList.get(0));

		EntryEntity entryEntity2 = new EntryEntity();
		entryEntity2.setData(new Date());
		entryEntity2.setValor(150);
		entryEntity2.setTipo(TipoEnum.DESPESA);
		entryEntity1.setCategoria(categoriesList.get(1));

		EntryEntity entryEntity3 = new EntryEntity();
		entryEntity3.setData(new Date());
		entryEntity3.setValor(130);
		entryEntity3.setTipo(TipoEnum.DESPESA);
		entryEntity1.setCategoria(categoriesList.get(2));
		this.entryRepository.saveAll(List.of(entryEntity1, entryEntity2, entryEntity3));
	}

	@Test
	void testListarEntries() {
		ResponseEntity<ResponseModel<List<EntryDto>>> entries = restTemplate.withBasicAuth("rasmoo", "msgradecurricular").exchange(
				"http://localhost:" + this.port + "/v1/entry/", HttpMethod.GET, null,
				new ParameterizedTypeReference<ResponseModel<List<EntryDto>>>() {
				});

		assertNotNull(entries.getBody().getData());
		assertEquals(3, entries.getBody().getData().size());
		assertEquals(200, entries.getBody().getStatusCode());
	}

	@Test
	void testConsultarEntryPorId() {
		List<EntryEntity> entryList = this.entryRepository.findAll();
		Long id = entryList.get(0).getId();

		ResponseEntity<ResponseModel<EntryDto>> entries = restTemplate.withBasicAuth("rasmoo", "msgradecurricular").exchange(
				"http://localhost:" + this.port + "/v1/entry/" + id, HttpMethod.GET, null,
				new ParameterizedTypeReference<ResponseModel<EntryDto>>() {
				});

		assertNotNull(entries.getBody().getData());
		assertEquals(id, entries.getBody().getData().getId());
		assertEquals(100, entries.getBody().getData().getValor());
		assertEquals(TipoEnum.RECEITA, entries.getBody().getData().getTipo());
		assertEquals(200, entries.getBody().getStatusCode());
	}

	@Test
	void testAtualizarEntry() {
		List<EntryEntity> entryList = this.entryRepository.findAll();
		EntryEntity entry = entryList.get(0);

		entry.setValor(139);

		HttpEntity<EntryModel> request = new HttpEntity<>(ConverterUtils.converterEntryEntityParaModel(entry));

		ResponseEntity<ResponseModel<Boolean>> entries = restTemplate.withBasicAuth("rasmoo", "msgradecurricular").exchange(
				"http://localhost:" + this.port + "/v1/entry/", HttpMethod.PUT, request,
				new ParameterizedTypeReference<ResponseModel<Boolean>>() {
				});

		EntryEntity entryAtualizada = this.entryRepository.findById(entry.getId()).get();

		assertTrue(entries.getBody().getData());
		assertEquals(139, entryAtualizada.getValor());
		assertEquals(200, entries.getBody().getStatusCode());
	}

	@Test
	void testCadastrarEntry() {
		EntryModel entryModel = new EntryModel();
		entryModel.setData(new Date());
		entryModel.setValor(100);
		entryModel.setTipo(TipoEnum.RECEITA);
		entryModel.setCategoriaId(this.categoryRepository.findAll().get(0).getId());

		HttpEntity<EntryModel> request = new HttpEntity<>(entryModel);

		ResponseEntity<ResponseModel<Boolean>> entries = restTemplate.withBasicAuth("rasmoo", "msgradecurricular").exchange(
				"http://localhost:" + this.port + "/v1/entry/", HttpMethod.POST, request,
				new ParameterizedTypeReference<ResponseModel<Boolean>>() {
				});

		List<EntryEntity> entryListAtualizada = this.entryRepository.findAll();

		assertTrue(entries.getBody().getData());
		assertEquals(4, entryListAtualizada.size());
		assertEquals(201, entries.getBody().getStatusCode());
	}

	@Test
	void testExcluirEntryPorId() {
		List<EntryEntity> entryList = this.entryRepository.findAll();
		Long id = entryList.get(0).getId();

		ResponseEntity<ResponseModel<Boolean>> entries = restTemplate.withBasicAuth("rasmoo", "msgradecurricular").exchange(
				"http://localhost:" + this.port + "/v1/entry/" + id, HttpMethod.DELETE, null,
				new ParameterizedTypeReference<ResponseModel<Boolean>>() {
				});

		List<EntryEntity> entryListAtualizada = this.entryRepository.findAll();

		assertTrue(entries.getBody().getData());
		assertEquals(2, entryListAtualizada.size());
		assertEquals(200, entries.getBody().getStatusCode());
	}
}
