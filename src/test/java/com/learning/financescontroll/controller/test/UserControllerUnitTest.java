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

import com.learning.financescontroll.entity.CredentialEntity;
import com.learning.financescontroll.entity.EntryEntity;
import com.learning.financescontroll.v1.dto.UserDto;
import com.learning.financescontroll.v1.model.ResponseModel;
import com.learning.financescontroll.v1.service.IUserService;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@RunWith(JUnitPlatform.class)
class UserControllerUnitTest {

	@LocalServerPort
	private int port;
	
	@MockBean
	private IUserService userService;
	
	@Autowired
	private TestRestTemplate restTemplate;
	
	private static UserDto userDto;
	
	@BeforeAll
	public static void init() {
		userDto = new UserDto();
		userDto.setId(1L);
		userDto.setNome("Teste");
		userDto.setEntries(new ArrayList<EntryEntity>());
		
		CredentialEntity credentialEntity = new CredentialEntity();
		credentialEntity.setUsername("teste");
		credentialEntity.setPassword("teste");
		
		userDto.setCredenciais(credentialEntity);
	}
	
	@Test
	void testListarUsuarios() {
		Mockito.when(this.userService.listar()).thenReturn(new ArrayList<UserDto>());

		ResponseEntity<ResponseModel<List<UserDto>>> usuarios = restTemplate.withBasicAuth("rasmoo", "msgradecurricular").exchange(
				"http://localhost:" + this.port + "/v1/usuario/", HttpMethod.GET, null,
				new ParameterizedTypeReference<ResponseModel<List<UserDto>>>() {
				});

		assertNotNull(usuarios.getBody().getData());
		assertEquals(200, usuarios.getBody().getStatusCode());
	}
	
	@Test
	void testConsultarUsuario() {
		Mockito.when(this.userService.consultar(1L)).thenReturn(userDto);

		ResponseEntity<ResponseModel<UserDto>> usuarios = restTemplate.withBasicAuth("rasmoo", "msgradecurricular").exchange(
				"http://localhost:" + this.port + "/v1/usuario/1", HttpMethod.GET, null,
				new ParameterizedTypeReference<ResponseModel<UserDto>>() {
				});

		assertNotNull(usuarios.getBody().getData());
		assertEquals(200, usuarios.getBody().getStatusCode());
	}
	
	@Test
	void testCadastrarUsuario() {
		Mockito.when(this.userService.cadastrar(userDto)).thenReturn(Boolean.TRUE);

		HttpEntity<UserDto> request = new HttpEntity<>(userDto);

		ResponseEntity<ResponseModel<Boolean>> usuarios = restTemplate.withBasicAuth("rasmoo", "msgradecurricular").exchange(
				"http://localhost:" + this.port + "/v1/usuario/", HttpMethod.POST, request,
				new ParameterizedTypeReference<ResponseModel<Boolean>>() {
				});

		assertNotNull(usuarios.getBody().getData());
		assertTrue(usuarios.getBody().getData());
		assertEquals(201, usuarios.getBody().getStatusCode());
	}
	
	@Test
	void testAtualizarCategoria() {
		Mockito.when(this.userService.atualizar(userDto)).thenReturn(Boolean.TRUE);

		HttpEntity<UserDto> request = new HttpEntity<>(userDto);

		ResponseEntity<ResponseModel<Boolean>> usuarios = restTemplate.withBasicAuth("rasmoo", "msgradecurricular").exchange(
				"http://localhost:" + this.port + "/v1/usuario/", HttpMethod.PUT, request,
				new ParameterizedTypeReference<ResponseModel<Boolean>>() {
				});

		assertNotNull(usuarios.getBody().getData());
		assertTrue(usuarios.getBody().getData());
		assertEquals(200, usuarios.getBody().getStatusCode());
	}
	
	@Test
	void testExcluirCategoria() {
		Mockito.when(this.userService.excluir(1L)).thenReturn(Boolean.TRUE);

		ResponseEntity<ResponseModel<Boolean>> usuarios = restTemplate.withBasicAuth("rasmoo", "msgradecurricular").exchange(
				"http://localhost:" + this.port + "/v1/usuario/1", HttpMethod.DELETE, null,
				new ParameterizedTypeReference<ResponseModel<Boolean>>() {
				});

		assertNotNull(usuarios.getBody().getData());
		assertTrue(usuarios.getBody().getData());
		assertEquals(200, usuarios.getBody().getStatusCode());
	}
}
