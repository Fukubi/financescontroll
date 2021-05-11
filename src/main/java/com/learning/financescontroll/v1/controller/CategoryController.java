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

import com.learning.financescontroll.v1.dto.CategoryDto;
import com.learning.financescontroll.v1.model.ResponseModel;
import com.learning.financescontroll.v1.service.ICategoryService;

@RestController
@RequestMapping("/v1/categoria")
public class CategoryController {

	@Autowired
	private ICategoryService categoryService;

	@GetMapping
	public ResponseEntity<ResponseModel<List<CategoryDto>>> listarCategorias() {
		ResponseModel<List<CategoryDto>> response = new ResponseModel<>();
		response.setData(categoryService.listar());
		response.setStatusCode(HttpStatus.OK.value());
		response.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CategoryController.class).listarCategorias())
				.withSelfRel());
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@GetMapping("/{id}")
	public ResponseEntity<ResponseModel<CategoryDto>> consultarCategorias(@PathVariable Long id) {
		ResponseModel<CategoryDto> response = new ResponseModel<>();
		response.setData(categoryService.consultar(id));
		response.setStatusCode(HttpStatus.OK.value());
		response.add(WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder.methodOn(CategoryController.class).consultarCategorias(id)).withSelfRel());
		response.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CategoryController.class).deletarCategoria(id))
				.withRel("DELETE"));
		response.add(WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder.methodOn(CategoryController.class).atualizarCategoria(response.getData()))
				.withRel("UPDATE"));
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@PostMapping
	public ResponseEntity<ResponseModel<Boolean>> cadastrarCategoria(@Valid @RequestBody CategoryDto category) {
		ResponseModel<Boolean> response = new ResponseModel<>();
		response.setData(categoryService.cadastrar(category));
		response.setStatusCode(HttpStatus.CREATED.value());
		response.add(WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder.methodOn(CategoryController.class).cadastrarCategoria(category))
				.withSelfRel());
		response.add(WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder.methodOn(CategoryController.class).atualizarCategoria(category))
				.withRel("UPDATE"));
		response.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CategoryController.class).listarCategorias())
				.withRel("GET_ALL"));
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@PutMapping
	public ResponseEntity<ResponseModel<Boolean>> atualizarCategoria(@Valid @RequestBody CategoryDto category) {
		ResponseModel<Boolean> response = new ResponseModel<>();
		response.setData(categoryService.atualizar(category));
		response.setStatusCode(HttpStatus.OK.value());
		response.add(WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder.methodOn(CategoryController.class).atualizarCategoria(category))
				.withSelfRel());
		response.add(WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder.methodOn(CategoryController.class).deletarCategoria(category.getId()))
				.withRel("DELETE"));
		response.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CategoryController.class).listarCategorias())
				.withRel("GET_ALL"));
		response.add(WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder.methodOn(CategoryController.class).consultarCategorias(category.getId()))
				.withRel("GET"));
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<ResponseModel<Boolean>> deletarCategoria(@PathVariable Long id) {
		ResponseModel<Boolean> response = new ResponseModel<>();
		response.setData(categoryService.excluir(id));
		response.setStatusCode(HttpStatus.OK.value());
		response.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CategoryController.class).deletarCategoria(id))
				.withSelfRel());
		response.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CategoryController.class).listarCategorias())
				.withRel("GET_ALL"));
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

}
