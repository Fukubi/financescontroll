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

import com.learning.financescontroll.v1.dto.UserDto;
import com.learning.financescontroll.v1.model.ResponseModel;
import com.learning.financescontroll.v1.service.IUserService;

@RestController
@RequestMapping("/vi/usuario")
public class UserController {
	
	@Autowired
	IUserService userService;
	
	@GetMapping
	public ResponseEntity<ResponseModel<List<UserDto>>> listarUsuarios() {
		ResponseModel<List<UserDto>> response = new ResponseModel<>();
		response.setData(userService.listar());
		response.setStatusCode(HttpStatus.OK.value());
		response.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class).listarUsuarios())
				.withSelfRel());
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<ResponseModel<UserDto>> consultarUsuario(@PathVariable Long id) {
		ResponseModel<UserDto> response = new ResponseModel<>();
		response.setData(userService.consultar(id));
		response.setStatusCode(HttpStatus.OK.value());
		response.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class).consultarUsuario(id))
				.withSelfRel());
		response.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class).deletarUsuario(id))
				.withRel("DELETE"));
		response.add(WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder.methodOn(UserController.class).atualizarUsuario(response.getData()))
				.withRel("UPDATE"));
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	
	@PostMapping
	public ResponseEntity<ResponseModel<Boolean>> cadastrarUsuario(@Valid @RequestBody UserDto user) {
		ResponseModel<Boolean> response = new ResponseModel<>();
		response.setData(userService.cadastrar(user));
		response.setStatusCode(HttpStatus.CREATED.value());
		response.add(WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder.methodOn(UserController.class).cadastrarUsuario(user)).withSelfRel());
		response.add(
				WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class).atualizarUsuario(user))
						.withRel("UPDATE"));
		response.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class).listarUsuarios())
				.withRel("GET_ALL"));
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}
	
	@PutMapping
	public ResponseEntity<ResponseModel<Boolean>> atualizarUsuario(@Valid @RequestBody UserDto user) {
		ResponseModel<Boolean> response = new ResponseModel<>();
		response.setData(userService.atualizar(user));
		response.setStatusCode(HttpStatus.OK.value());
		response.add(WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder.methodOn(UserController.class).atualizarUsuario(user)).withSelfRel());
		response.add(WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder.methodOn(UserController.class).deletarUsuario(user.getId()))
				.withRel("DELETE"));
		response.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class).listarUsuarios())
				.withRel("GET_ALL"));
		response.add(WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder.methodOn(UserController.class).consultarUsuario(user.getId()))
				.withRel("GET"));
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<ResponseModel<Boolean>> deletarUsuario(@PathVariable Long id) {
		ResponseModel<Boolean> response = new ResponseModel<>();
		response.setData(userService.excluir(id));
		response.setStatusCode(HttpStatus.OK.value());
		response.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class).deletarUsuario(id))
				.withSelfRel());
		response.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class).listarUsuarios())
				.withRel("GET_ALL"));
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
}
