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
import com.learning.financescontroll.v1.dto.CategoryDto;
import com.learning.financescontroll.v1.model.ResponseModel;
import com.learning.financescontroll.v1.service.ICategoryService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(tags = SwaggerConfig.CATEGORY)
@RestController
@RequestMapping({"/v1/categoria", "/v2/categoria"})
public class CategoryController {

	@Autowired
	private ICategoryService categoryService;

	@ApiOperation("Listar todas as categorias cadastradas")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Categorias listadas com sucesso"),
			@ApiResponse(code = 500, message = "Erro interno no servidor") })
	@GetMapping
	public ResponseEntity<ResponseModel<List<CategoryDto>>> listarCategorias() {
		ResponseModel<List<CategoryDto>> response = new ResponseModel<>();
		response.setData(categoryService.listar());
		response.setStatusCode(HttpStatus.OK.value());
		response.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CategoryController.class).listarCategorias())
				.withSelfRel());
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@ApiOperation("Consultar uma categoria cadastrada")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Categorias consultada com sucesso"),
			@ApiResponse(code = 404, message = "Categoria não encontrada"),
			@ApiResponse(code = 500, message = "Erro interno no servidor") })
	@GetMapping("/{id}")
	public ResponseEntity<ResponseModel<CategoryDto>> consultarCategorias(@PathVariable Long id) {
		ResponseModel<CategoryDto> response = new ResponseModel<>();
		response.setData(categoryService.consultar(id));
		response.setStatusCode(HttpStatus.OK.value());
		response.add(WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder.methodOn(CategoryController.class).consultarCategorias(id)).withSelfRel());
		response.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CategoryController.class).deletarCategoria(id))
				.withRel(ControllerConstantVariables.EXCLUIR.getValor()));
		response.add(WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder.methodOn(CategoryController.class).atualizarCategoria(response.getData()))
				.withRel(ControllerConstantVariables.ATUALIZAR.getValor()));
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@ApiOperation("Cadastrar uma categoria")
	@ApiResponses(value = { @ApiResponse(code = 201, message = "Categoria cadastrada com sucesso"),
			@ApiResponse(code = 400, message = "Erro na requisição do cliente"),
			@ApiResponse(code = 500, message = "Erro interno no servidor") })
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
				.withRel(ControllerConstantVariables.ATUALIZAR.getValor()));
		response.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CategoryController.class).listarCategorias())
				.withRel(ControllerConstantVariables.LISTAR.getValor()));
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@ApiOperation("Atualizar uma categoria cadastrada")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Categorias atualizada com sucesso"),
			@ApiResponse(code = 400, message = "Erro na requisição do cliente"),
			@ApiResponse(code = 404, message = "Categoria não encontrada"),
			@ApiResponse(code = 500, message = "Erro interno no servidor") })
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
				.withRel(ControllerConstantVariables.EXCLUIR.getValor()));
		response.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CategoryController.class).listarCategorias())
				.withRel(ControllerConstantVariables.LISTAR.getValor()));
		response.add(WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder.methodOn(CategoryController.class).consultarCategorias(category.getId()))
				.withRel(ControllerConstantVariables.CONSULTAR.getValor()));
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@ApiOperation("Deletar uma categoria cadastrada")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Categorias deletada com sucesso"),
			@ApiResponse(code = 400, message = "Erro na requisição do cliente"),
			@ApiResponse(code = 404, message = "Categoria não encontrada"),
			@ApiResponse(code = 500, message = "Erro interno no servidor") })
	@DeleteMapping("/{id}")
	public ResponseEntity<ResponseModel<Boolean>> deletarCategoria(@PathVariable Long id) {
		ResponseModel<Boolean> response = new ResponseModel<>();
		response.setData(categoryService.excluir(id));
		response.setStatusCode(HttpStatus.OK.value());
		response.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CategoryController.class).deletarCategoria(id))
				.withSelfRel());
		response.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CategoryController.class).listarCategorias())
				.withRel(ControllerConstantVariables.LISTAR.getValor()));
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

}
