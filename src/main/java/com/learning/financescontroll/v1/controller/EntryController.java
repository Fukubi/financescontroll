package com.learning.financescontroll.v1.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
import com.learning.financescontroll.v1.dto.EntryDto;
import com.learning.financescontroll.v1.model.EntryModel;
import com.learning.financescontroll.v1.model.ResponseModel;
import com.learning.financescontroll.v1.service.IEntryService;
import com.learning.financescontroll.v1.utils.ConverterUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(tags = SwaggerConfig.ENTRY)
@RestController
@RequestMapping("/v1/entry")
@PreAuthorize(value = "#oath2.hasScope('cw_logado') and hasRole('ROLE_CUSTOMER')")
public class EntryController {

	@Autowired
	private IEntryService entryService;

	@ApiOperation("Listar todas os lançamentos cadastrados")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Lançamentos listadas com sucesso"),
			@ApiResponse(code = 500, message = "Erro interno no servidor") })
	@GetMapping
	@PreAuthorize(value = "#oauth2.hasAnyScope('cw_logado', 'cc_logado') and hasRole('ROLE_CUSTOMER')")
	public ResponseEntity<ResponseModel<List<EntryDto>>> listarLancamentos() {
		ResponseModel<List<EntryDto>> response = new ResponseModel<>();
		response.setData(entryService.listar());
		response.setStatusCode(HttpStatus.OK.value());
		response.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EntryController.class).listarLancamentos())
				.withSelfRel());
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@ApiOperation("Consultar um lançamento cadastrado")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Lançamento consultado com sucesso"),
			@ApiResponse(code = 404, message = "Lançamento não encontrado"),
			@ApiResponse(code = 500, message = "Erro interno no servidor") })
	@GetMapping("/{id}")
	@PreAuthorize(value = "#oauth2.hasAnyScope('cw_logado', 'cc_logado') and hasRole('ROLE_CUSTOMER')")
	public ResponseEntity<ResponseModel<EntryDto>> consultarLancamento(@PathVariable Long id) {
		ResponseModel<EntryDto> response = new ResponseModel<>();
		response.setData(entryService.consultar(id));
		response.setStatusCode(HttpStatus.OK.value());
		response.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EntryController.class).consultarLancamento(id))
				.withSelfRel());
		response.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EntryController.class).deletarLancamento(id))
				.withRel(ControllerConstantVariables.EXCLUIR.getValor()));
		response.add(WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder.methodOn(EntryController.class).atualizarLancamento(ConverterUtils.converterEntryDtoParaModel(response.getData())))
				.withRel(ControllerConstantVariables.ATUALIZAR.getValor()));
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@ApiOperation("Cadastrar um lançamento")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Lançamento cadastrado com sucesso"),
			@ApiResponse(code = 400, message = "Erro na requisição do cliente"),
			@ApiResponse(code = 500, message = "Erro interno no servidor") })
	@PostMapping
	public ResponseEntity<ResponseModel<Boolean>> cadastrarLancamento(@Valid @RequestBody EntryModel entry) {
		ResponseModel<Boolean> response = new ResponseModel<>();
		response.setData(entryService.cadastrar(entry));
		response.setStatusCode(HttpStatus.CREATED.value());
		response.add(WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder.methodOn(EntryController.class).cadastrarLancamento(entry)).withSelfRel());
		response.add(
				WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EntryController.class).atualizarLancamento(entry))
						.withRel(ControllerConstantVariables.ATUALIZAR.getValor()));
		response.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EntryController.class).listarLancamentos())
				.withRel(ControllerConstantVariables.LISTAR.getValor()));
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@ApiOperation("Atualizar um lançamento cadastrado")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Lançamento atualizado com sucesso"),
			@ApiResponse(code = 400, message = "Erro na requisição do cliente"),
			@ApiResponse(code = 404, message = "Lançamento não encontrado"),
			@ApiResponse(code = 500, message = "Erro interno no servidor") })
	@PutMapping
	public ResponseEntity<ResponseModel<Boolean>> atualizarLancamento(@Valid @RequestBody EntryModel entry) {
		ResponseModel<Boolean> response = new ResponseModel<>();
		response.setData(entryService.atualizar(entry));
		response.setStatusCode(HttpStatus.OK.value());
		response.add(WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder.methodOn(EntryController.class).atualizarLancamento(entry)).withSelfRel());
		response.add(WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder.methodOn(EntryController.class).deletarLancamento(entry.getId()))
				.withRel(ControllerConstantVariables.EXCLUIR.getValor()));
		response.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EntryController.class).listarLancamentos())
				.withRel(ControllerConstantVariables.LISTAR.getValor()));
		response.add(WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder.methodOn(EntryController.class).consultarLancamento(entry.getId()))
				.withRel(ControllerConstantVariables.CONSULTAR.getValor()));
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@ApiOperation("Deletar um lançamento cadastrado")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Lançamento deletado com sucesso"),
			@ApiResponse(code = 400, message = "Erro na requisição do cliente"),
			@ApiResponse(code = 404, message = "Lançamento não encontrado"),
			@ApiResponse(code = 500, message = "Erro interno no servidor") })
	@DeleteMapping("/{id}")
	public ResponseEntity<ResponseModel<Boolean>> deletarLancamento(@PathVariable Long id) {
		ResponseModel<Boolean> response = new ResponseModel<>();
		response.setData(entryService.excluir(id));
		response.setStatusCode(HttpStatus.OK.value());
		response.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EntryController.class).deletarLancamento(id))
				.withSelfRel());
		response.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EntryController.class).listarLancamentos())
				.withRel(ControllerConstantVariables.LISTAR.getValor()));
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

}
