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

import com.learning.financescontroll.v1.dto.EntryDto;
import com.learning.financescontroll.v1.model.ResponseModel;
import com.learning.financescontroll.v1.service.IEntryService;

@RestController
@RequestMapping("/v1/entry")
public class EntryController {

	@Autowired
	private IEntryService entryService;

	@GetMapping
	public ResponseEntity<ResponseModel<List<EntryDto>>> listarLancamentos() {
		ResponseModel<List<EntryDto>> response = new ResponseModel<>();
		response.setData(entryService.listar());
		response.setStatusCode(HttpStatus.OK.value());
		response.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EntryController.class).listarLancamentos())
				.withSelfRel());
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@GetMapping("/{id}")
	public ResponseEntity<ResponseModel<EntryDto>> consultarLancamento(@PathVariable Long id) {
		ResponseModel<EntryDto> response = new ResponseModel<>();
		response.setData(entryService.consultar(id));
		response.setStatusCode(HttpStatus.OK.value());
		response.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EntryController.class).consultarLancamento(id))
				.withSelfRel());
		response.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EntryController.class).deletarLancamento(id))
				.withRel("DELETE"));
		response.add(WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder.methodOn(EntryController.class).atualizarLancamento(response.getData()))
				.withRel("UPDATE"));
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@PostMapping
	public ResponseEntity<ResponseModel<Boolean>> cadastrarLancamento(@Valid @RequestBody EntryDto entry) {
		ResponseModel<Boolean> response = new ResponseModel<>();
		response.setData(entryService.cadastrar(entry));
		response.setStatusCode(HttpStatus.CREATED.value());
		response.add(WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder.methodOn(EntryController.class).cadastrarLancamento(entry)).withSelfRel());
		response.add(
				WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EntryController.class).atualizarLancamento(entry))
						.withRel("UPDATE"));
		response.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EntryController.class).listarLancamentos())
				.withRel("GET_ALL"));
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@PutMapping
	public ResponseEntity<ResponseModel<Boolean>> atualizarLancamento(@Valid @RequestBody EntryDto entry) {
		ResponseModel<Boolean> response = new ResponseModel<>();
		response.setData(entryService.atualizar(entry));
		response.setStatusCode(HttpStatus.OK.value());
		response.add(WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder.methodOn(EntryController.class).atualizarLancamento(entry)).withSelfRel());
		response.add(WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder.methodOn(EntryController.class).deletarLancamento(entry.getId()))
				.withRel("DELETE"));
		response.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EntryController.class).listarLancamentos())
				.withRel("GET_ALL"));
		response.add(WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder.methodOn(EntryController.class).consultarLancamento(entry.getId()))
				.withRel("GET"));
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<ResponseModel<Boolean>> deletarLancamento(@PathVariable Long id) {
		ResponseModel<Boolean> response = new ResponseModel<>();
		response.setData(entryService.excluir(id));
		response.setStatusCode(HttpStatus.OK.value());
		response.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EntryController.class).deletarLancamento(id))
				.withSelfRel());
		response.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EntryController.class).listarLancamentos())
				.withRel("GET_ALL"));
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

}
