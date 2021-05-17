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

import com.learning.financescontroll.entity.CredentialEntity;
import com.learning.financescontroll.entity.EntryEntity;
import com.learning.financescontroll.entity.UserEntity;
import com.learning.financescontroll.repository.IUserRepository;
import com.learning.financescontroll.v1.constants.ServiceConstantVariables;
import com.learning.financescontroll.v1.dto.UserDto;
import com.learning.financescontroll.v1.exception.UserException;
import com.learning.financescontroll.v1.service.UserService;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
class UserServiceUnitTest {

	@Mock
	private IUserRepository userRepository;

	@InjectMocks
	private UserService userService;

	private static UserEntity userEntity;

	@BeforeAll
	public static void init() {
		userEntity = new UserEntity();
		userEntity.setId(1L);
		userEntity.setNome("Teste");
		userEntity.setEntries(new ArrayList<EntryEntity>());

		CredentialEntity credentialEntity = new CredentialEntity();
		credentialEntity.setUsername("teste");
		credentialEntity.setPassword("teste");

		userEntity.setCredenciais(credentialEntity);
	}

	@Test
	void testListarSucesso() {
		List<UserEntity> listUser = new ArrayList<>();
		listUser.add(userEntity);

		Mockito.when(this.userRepository.findAll()).thenReturn(listUser);

		List<UserDto> listUserDto = this.userService.listar();

		assertNotNull(listUserDto);
		assertEquals(1L, listUserDto.get(0).getId());
		assertEquals("Teste", listUserDto.get(0).getNome());
		assertEquals("teste", listUserDto.get(0).getCredenciais().getUsername());
		assertEquals("teste", listUserDto.get(0).getCredenciais().getPassword());
		assertEquals("/v1/usuario/1", listUserDto.get(0).getLinks().getRequiredLink("self").getHref());
		assertEquals(1, listUserDto.size());

		Mockito.verify(this.userRepository, times(1)).findAll();
	}

	@Test
	void testConsultarSucesso() {
		Mockito.when(this.userRepository.findById(1L)).thenReturn(Optional.of(userEntity));
		UserDto userDto = this.userService.consultar(1L);

		assertNotNull(userDto);
		assertEquals(1L, userDto.getId());
		assertEquals("Teste", userDto.getNome());
		assertEquals("teste", userDto.getCredenciais().getUsername());
		assertEquals("teste", userDto.getCredenciais().getPassword());

		Mockito.verify(this.userRepository, times(1)).findById(1L);
	}

	@Test
	void testCadastrarSucesso() {
		UserDto userDto = new UserDto();
		userDto.setNome("Teste");
		userDto.setEntries(new ArrayList<EntryEntity>());

		CredentialEntity credentialEntity1 = new CredentialEntity();
		credentialEntity1.setUsername("teste");
		credentialEntity1.setPassword("teste");

		userDto.setCredenciais(credentialEntity1);

		userEntity.setId(null);

		Mockito.when(this.userRepository.save(userEntity)).thenReturn(userEntity);

		Boolean sucesso = this.userService.cadastrar(userDto);

		assertTrue(sucesso);

		Mockito.verify(this.userRepository, times(1)).save(userEntity);

		userEntity.setId(1L);
	}

	@Test
	void testAtualizarSucesso() {
		UserDto userDto = new UserDto();
		userDto.setId(1L);
		userDto.setNome("Teste");
		userDto.setEntries(new ArrayList<EntryEntity>());

		CredentialEntity credentialEntity1 = new CredentialEntity();
		credentialEntity1.setUsername("teste");
		credentialEntity1.setPassword("teste");

		userDto.setCredenciais(credentialEntity1);

		Mockito.when(this.userRepository.findById(1L)).thenReturn(Optional.of(userEntity));
		Mockito.when(this.userRepository.save(userEntity)).thenReturn(userEntity);

		Boolean sucesso = this.userService.atualizar(userDto);

		assertTrue(sucesso);

		Mockito.verify(this.userRepository, times(1)).findById(1L);
		Mockito.verify(this.userRepository, times(1)).save(userEntity);
	}

	@Test
	void testExcluirSucesso() {
		Mockito.when(this.userRepository.findById(1L)).thenReturn(Optional.of(userEntity));

		Boolean sucesso = this.userService.excluir(1L);

		assertTrue(sucesso);

		Mockito.verify(this.userRepository, times(1)).findById(1L);
		Mockito.verify(this.userRepository, times(1)).deleteById(1L);
	}

	@Test
	void testAtualizarThrowUserException() {
		UserDto userDto = new UserDto();
		userDto.setId(1L);
		userDto.setNome("Teste");
		userDto.setEntries(new ArrayList<EntryEntity>());

		CredentialEntity credentialEntity1 = new CredentialEntity();
		credentialEntity1.setUsername("teste");
		credentialEntity1.setPassword("teste");

		userDto.setCredenciais(credentialEntity1);

		Mockito.when(this.userRepository.findById(1L)).thenReturn(Optional.empty());

		UserException userException;

		userException = assertThrows(UserException.class, () -> {
			this.userService.atualizar(userDto);
		});

		assertEquals(HttpStatus.NOT_FOUND, userException.getHttpStatus());
		assertEquals(ServiceConstantVariables.NOT_FOUND.getValor(), userException.getMessage());

		Mockito.verify(this.userRepository, times(1)).findById(1L);
		Mockito.verify(this.userRepository, times(0)).save(userEntity);
	}

	@Test
	void testExcluirThrowsUserException() {
		Mockito.when(this.userRepository.findById(1L)).thenReturn(Optional.empty());

		UserException userException;

		userException = assertThrows(UserException.class, () -> {
			this.userService.excluir(1L);
		});

		assertEquals(HttpStatus.NOT_FOUND, userException.getHttpStatus());
		assertEquals(ServiceConstantVariables.NOT_FOUND.getValor(), userException.getMessage());

		Mockito.verify(this.userRepository, times(1)).findById(1L);
		Mockito.verify(this.userRepository, times(0)).deleteById(1L);
	}

	@Test
	void testCadastrarThrowsUserException() {
		UserDto userDto = new UserDto();
		userDto.setId(1L);
		userDto.setNome("Teste");
		userDto.setEntries(new ArrayList<EntryEntity>());

		CredentialEntity credentialEntity1 = new CredentialEntity();
		credentialEntity1.setUsername("teste");
		credentialEntity1.setPassword("teste");

		userDto.setCredenciais(credentialEntity1);

		UserException userException;

		userException = assertThrows(UserException.class, () -> {
			this.userService.cadastrar(userDto);
		});

		assertEquals(HttpStatus.BAD_REQUEST, userException.getHttpStatus());
		assertEquals(ServiceConstantVariables.ID_NOT_PERMITTED.getValor(), userException.getMessage());

		Mockito.verify(this.userRepository, times(0)).save(userEntity);
	}

	@Test
	void testConsultarThrowsUserException() {
		Mockito.when(this.userRepository.findById(1L)).thenReturn(Optional.empty());

		UserException userException;

		userException = assertThrows(UserException.class, () -> {
			this.userService.consultar(1L);
		});

		assertEquals(HttpStatus.NOT_FOUND, userException.getHttpStatus());
		assertEquals(ServiceConstantVariables.NOT_FOUND.getValor(), userException.getMessage());

		Mockito.verify(this.userRepository, times(1)).findById(1L);
	}

	@Test
	void testAtualizarThrowException() {
		UserDto userDto = new UserDto();
		userDto.setId(1L);
		userDto.setNome("Teste");
		userDto.setEntries(new ArrayList<EntryEntity>());

		CredentialEntity credentialEntity1 = new CredentialEntity();
		credentialEntity1.setUsername("teste");
		credentialEntity1.setPassword("teste");

		userDto.setCredenciais(credentialEntity1);

		Mockito.when(this.userRepository.findById(1L)).thenReturn(Optional.of(userEntity));
		Mockito.when(this.userRepository.save(userEntity)).thenThrow(IllegalStateException.class);

		UserException userException;

		userException = assertThrows(UserException.class, () -> {
			this.userService.atualizar(userDto);
		});

		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, userException.getHttpStatus());
		assertEquals(ServiceConstantVariables.ERRO_GENERICO.getValor(), userException.getMessage());

		Mockito.verify(this.userRepository, times(1)).findById(1L);
		Mockito.verify(this.userRepository, times(1)).save(userEntity);
	}

	@Test
	void testCadastrarThrowsException() {
		UserDto userDto = new UserDto();
		userDto.setNome("Teste");
		userDto.setEntries(new ArrayList<EntryEntity>());

		CredentialEntity credentialEntity1 = new CredentialEntity();
		credentialEntity1.setUsername("teste");
		credentialEntity1.setPassword("teste");

		userDto.setCredenciais(credentialEntity1);

		userEntity.setId(null);
		Mockito.when(this.userRepository.save(userEntity)).thenThrow(IllegalStateException.class);

		UserException userException;

		userException = assertThrows(UserException.class, () -> {
			this.userService.cadastrar(userDto);
		});

		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, userException.getHttpStatus());
		assertEquals(ServiceConstantVariables.ERRO_GENERICO.getValor(), userException.getMessage());

		Mockito.verify(this.userRepository, times(1)).save(userEntity);
		userEntity.setId(1L);
	}

	@Test
	void testConsultarThrowsException() {
		Mockito.when(this.userRepository.findById(1L)).thenThrow(IllegalStateException.class);

		UserException userException;

		userException = assertThrows(UserException.class, () -> {
			this.userService.consultar(1L);
		});

		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, userException.getHttpStatus());
		assertEquals(ServiceConstantVariables.ERRO_GENERICO.getValor(), userException.getMessage());

		Mockito.verify(this.userRepository, times(1)).findById(1L);
	}

	@Test
	void testListarThrowsException() {
		Mockito.when(this.userRepository.findAll()).thenThrow(IllegalStateException.class);

		UserException userException;

		userException = assertThrows(UserException.class, () -> {
			this.userService.listar();
		});

		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, userException.getHttpStatus());
		assertEquals(ServiceConstantVariables.ERRO_GENERICO.getValor(), userException.getMessage());

		Mockito.verify(this.userRepository, times(1)).findAll();
	}
	
	@Test
	void testExcluirThrowsException() {
		Mockito.when(this.userRepository.findById(1L)).thenReturn(Optional.of(userEntity));
		Mockito.doThrow(IllegalStateException.class).when(this.userRepository).deleteById(1L);

		UserException userException;

		userException = assertThrows(UserException.class, () -> {
			this.userService.excluir(1L);
		});

		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, userException.getHttpStatus());
		assertEquals(ServiceConstantVariables.ERRO_GENERICO.getValor(), userException.getMessage());

		Mockito.verify(this.userRepository, times(1)).findById(1L);
		Mockito.verify(this.userRepository, times(1)).deleteById(1L);
	}
}
