package com.learning.financescontroll.v1.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.learning.financescontroll.config.SwaggerConfig;
import com.learning.financescontroll.v1.constants.ControllerConstantVariables;
import com.learning.financescontroll.v1.dto.UserDto;
import com.learning.financescontroll.v1.model.ResponseModel;
import com.learning.financescontroll.v1.service.IUserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(tags = SwaggerConfig.USER)
@RestController
@RequestMapping("/vi/usuario")
public class UserController {
	
	@Autowired
	IUserService userService;
	
	@ApiOperation("Listar todas os usuários cadastrados")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Usuários listadas com sucesso"),
			@ApiResponse(code = 500, message = "Erro interno no servidor") })
	@GetMapping
	public ResponseEntity<ResponseModel<List<UserDto>>> listarUsuarios() {
		ResponseModel<List<UserDto>> response = new ResponseModel<>();
		response.setData(userService.listar());
		response.setStatusCode(HttpStatus.OK.value());
		response.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class).listarUsuarios())
				.withSelfRel());
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	
	@ApiOperation("Consultar um usuário cadastrado")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Usuário consultado com sucesso"),
			@ApiResponse(code = 404, message = "Usuário não encontrado"),
			@ApiResponse(code = 500, message = "Erro interno no servidor") })
	@GetMapping("/{id}")
	public ResponseEntity<ResponseModel<UserDto>> consultarUsuario(@PathVariable Long id) {
		ResponseModel<UserDto> response = new ResponseModel<>();
		response.setData(userService.consultar(id));
		response.setStatusCode(HttpStatus.OK.value());
		response.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class).consultarUsuario(id))
				.withSelfRel());
		response.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class).deletarUsuario(id))
				.withRel(ControllerConstantVariables.EXCLUIR.getValor()));
		response.add(WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder.methodOn(UserController.class).atualizarUsuario(response.getData()))
				.withRel(ControllerConstantVariables.ATUALIZAR.getValor()));
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	
	@ApiOperation("Cadastrar um usuário")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Usuário cadastrado com sucesso"),
			@ApiResponse(code = 400, message = "Erro na requisição do usuário"),
			@ApiResponse(code = 500, message = "Erro interno no servidor") })
	@PostMapping
	public ResponseEntity<ResponseModel<Boolean>> cadastrarUsuario(@Valid @RequestBody UserDto user) {
		ResponseModel<Boolean> response = new ResponseModel<>();
		response.setData(userService.cadastrar(user));
		response.setStatusCode(HttpStatus.CREATED.value());
		response.add(WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder.methodOn(UserController.class).cadastrarUsuario(user)).withSelfRel());
		response.add(
				WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class).atualizarUsuario(user))
						.withRel(ControllerConstantVariables.ATUALIZAR.getValor()));
		response.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class).listarUsuarios())
				.withRel(ControllerConstantVariables.LISTAR.getValor()));
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}
	
	@ApiOperation("Atualizar um usuário cadastrado")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Usuário atualizado com sucesso"),
			@ApiResponse(code = 400, message = "Erro na requisição do cliente"),
			@ApiResponse(code = 404, message = "Usuário não encontrado"),
			@ApiResponse(code = 500, message = "Erro interno no servidor") })
	@PutMapping
	public ResponseEntity<ResponseModel<Boolean>> atualizarUsuario(@Valid @RequestBody UserDto user) {
		ResponseModel<Boolean> response = new ResponseModel<>();
		response.setData(userService.atualizar(user));
		response.setStatusCode(HttpStatus.OK.value());
		response.add(WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder.methodOn(UserController.class).atualizarUsuario(user)).withSelfRel());
		response.add(WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder.methodOn(UserController.class).deletarUsuario(user.getId()))
				.withRel(ControllerConstantVariables.EXCLUIR.getValor()));
		response.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class).listarUsuarios())
				.withRel(ControllerConstantVariables.LISTAR.getValor()));
		response.add(WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder.methodOn(UserController.class).consultarUsuario(user.getId()))
				.withRel(ControllerConstantVariables.CONSULTAR.getValor()));
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	
	@ApiOperation("Deletar um usuário cadastrado")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Usuário deletado com sucesso"),
			@ApiResponse(code = 400, message = "Erro na requisição do cliente"),
			@ApiResponse(code = 404, message = "Usuário não encontrado"),
			@ApiResponse(code = 500, message = "Erro interno no servidor") })
	@DeleteMapping("/{id}")
	public ResponseEntity<ResponseModel<Boolean>> deletarUsuario(@PathVariable Long id) {
		ResponseModel<Boolean> response = new ResponseModel<>();
		response.setData(userService.excluir(id));
		response.setStatusCode(HttpStatus.OK.value());
		response.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class).deletarUsuario(id))
				.withSelfRel());
		response.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class).listarUsuarios())
				.withRel(ControllerConstantVariables.LISTAR.getValor()));
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
}
