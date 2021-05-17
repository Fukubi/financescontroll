package com.learning.financescontroll.controller.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
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

import com.learning.financescontroll.entity.CredentialEntity;
import com.learning.financescontroll.entity.EntryEntity;
import com.learning.financescontroll.entity.UserEntity;
import com.learning.financescontroll.repository.IUserRepository;
import com.learning.financescontroll.v1.dto.UserDto;
import com.learning.financescontroll.v1.model.ResponseModel;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@RunWith(JUnitPlatform.class)
class UserControllerIntegratedTest {

	@LocalServerPort
	private int port;
	
	@Autowired
	private TestRestTemplate restTemplate;
	
	@Autowired
	private IUserRepository userRepository;
	
	@BeforeEach
	private void init() {
		this.montaBaseDeDados();
	}
	
	@AfterEach
	private void finish() {
		this.userRepository.deleteAll();
	}
	
	private void montaBaseDeDados() {
		UserEntity userEntity1 = new UserEntity();
		userEntity1.setId(1L);
		userEntity1.setNome("Teste");
		userEntity1.setEntries(new ArrayList<EntryEntity>());
		
		CredentialEntity credentialEntity1 = new CredentialEntity();
		credentialEntity1.setUsername("teste1");
		credentialEntity1.setPassword("teste1");
		
		userEntity1.setCredenciais(credentialEntity1);
		
		UserEntity userEntity2 = new UserEntity();
		userEntity2.setId(2L);
		userEntity2.setNome("Teste2");
		userEntity2.setEntries(new ArrayList<EntryEntity>());
		
		CredentialEntity credentialEntity2 = new CredentialEntity();
		credentialEntity2.setUsername("teste2");
		credentialEntity2.setPassword("teste2");
		
		userEntity2.setCredenciais(credentialEntity2);
		
		UserEntity userEntity3 = new UserEntity();
		userEntity3.setId(3L);
		userEntity3.setNome("Teste3");
		userEntity3.setEntries(new ArrayList<EntryEntity>());
		
		CredentialEntity credentialEntity3 = new CredentialEntity();
		credentialEntity3.setUsername("teste3");
		credentialEntity3.setPassword("teste3");
		
		userEntity3.setCredenciais(credentialEntity3);
		
		this.userRepository.saveAll(Arrays.asList(userEntity1, userEntity2, userEntity3));
	}
	
	@Test
	void testListarUsuarios() {
		ResponseEntity<ResponseModel<List<UserDto>>> usuarios = restTemplate.exchange(
				"http://localhost:" + this.port + "/v1/usuario/", HttpMethod.GET, null,
				new ParameterizedTypeReference<ResponseModel<List<UserDto>>>() {
				});

		assertNotNull(usuarios.getBody().getData());
		assertEquals(3, usuarios.getBody().getData().size());
		assertEquals(200, usuarios.getBody().getStatusCode());
	}
	
	@Test
	void testConsultarUsuarioPorId() {
		List<UserEntity> userList = this.userRepository.findAll();
		Long id = userList.get(0).getId();
		
		ResponseEntity<ResponseModel<UserDto>> usuarios = restTemplate.exchange(
				"http://localhost:" + this.port + "/v1/usuario/" + id, HttpMethod.GET, null,
				new ParameterizedTypeReference<ResponseModel<UserDto>>() {
				});

		assertNotNull(usuarios.getBody().getData());
		assertEquals(id, usuarios.getBody().getData().getId());
		assertEquals("Teste", usuarios.getBody().getData().getNome());
		assertEquals("teste1", usuarios.getBody().getData().getCredenciais().getUsername());
		assertEquals("teste1", usuarios.getBody().getData().getCredenciais().getPassword());
		assertEquals(200, usuarios.getBody().getStatusCode());
	}
	
	@Test
	void testAtualizarUsuario() {
		List<UserEntity> usuarioList = this.userRepository.findAll();
		UserEntity usuario = usuarioList.get(0);
		
		usuario.setNome("TESTANDO ATUALIZAR");
		
		HttpEntity<UserEntity> request = new HttpEntity<>(usuario);
		
		ResponseEntity<ResponseModel<Boolean>> usuarios = restTemplate.exchange(
				"http://localhost:" + this.port + "/v1/usuario/", HttpMethod.PUT, request,
				new ParameterizedTypeReference<ResponseModel<Boolean>>() {
				});
		
		UserEntity usuarioAtualizado = this.userRepository.findById(usuario.getId()).get();

		assertTrue(usuarios.getBody().getData());
		assertEquals("TESTANDO ATUALIZAR", usuarioAtualizado.getNome());
		assertEquals(200, usuarios.getBody().getStatusCode());
	}
	
	@Test
	void testCadastrarUsuario() {
		UserEntity userEntity4 = new UserEntity();
		userEntity4.setNome("Teste4");
		userEntity4.setEntries(new ArrayList<EntryEntity>());
		
		CredentialEntity credentialEntity4 = new CredentialEntity();
		credentialEntity4.setUsername("teste4");
		credentialEntity4.setPassword("teste4");
		
		userEntity4.setCredenciais(credentialEntity4);
		
		HttpEntity<UserEntity> request = new HttpEntity<>(userEntity4);
		
		ResponseEntity<ResponseModel<Boolean>> usuarios = restTemplate.exchange(
				"http://localhost:" + this.port + "/v1/usuario/", HttpMethod.POST, request,
				new ParameterizedTypeReference<ResponseModel<Boolean>>() {
				});
		
		List<UserEntity> userListAtualizada = this.userRepository.findAll();
		
		assertTrue(usuarios.getBody().getData());
		assertEquals(4, userListAtualizada.size());
		assertEquals(201, usuarios.getBody().getStatusCode());
	}
	
	@Test
	void testExcluirUsuarioPorId() {
		List<UserEntity> userList = this.userRepository.findAll();
		Long id = userList.get(0).getId();
		
		ResponseEntity<ResponseModel<Boolean>> usuarios = restTemplate.exchange(
				"http://localhost:" + this.port + "/v1/usuario/" + id, HttpMethod.DELETE, null,
				new ParameterizedTypeReference<ResponseModel<Boolean>>() {
				});
		
		List<UserEntity> userListAtualizada = this.userRepository.findAll();

		assertTrue(usuarios.getBody().getData());
		assertEquals(2, userListAtualizada.size());
		assertEquals(200, usuarios.getBody().getStatusCode());
	}
}
