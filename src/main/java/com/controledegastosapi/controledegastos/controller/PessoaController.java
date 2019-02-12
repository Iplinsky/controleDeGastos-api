package com.controledegastosapi.controledegastos.controller;

import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.controledegastosapi.controledegastos.event.RecursoCriadoEvent;
import com.controledegastosapi.controledegastos.model.Pessoa;
import com.controledegastosapi.controledegastos.service.PessoaService;

@RestController
@RequestMapping("/pessoas")
public class PessoaController {

	@Autowired 
	public PessoaService pessoaService;

	@Autowired
	private ApplicationEventPublisher publisher;

	@PostMapping 
	public ResponseEntity<Pessoa> salvar(@Valid @RequestBody Pessoa pessoa, HttpServletResponse response) {
		Pessoa pessoaSalva = pessoaService.salvar(pessoa);
		publisher.publishEvent(new RecursoCriadoEvent(this, response, pessoaSalva.getCodigo()));
		return ResponseEntity.status(HttpStatus.CREATED).body(pessoaSalva);
	}

	@GetMapping
	public List<Pessoa> listarTodos() { 
		return pessoaService.listarTodos();
	}

	@GetMapping("/termo/{termoBase64}")
	public List<Pessoa> listarPorTermo(@PathVariable("termoBase64") String texto) {
		return pessoaService.listarPorTermo(texto);
	}

	@GetMapping("/listarPaginado")
	public Page<Pessoa> listarPaginado(@RequestParam("elementosPorPagina") Integer elementosPorPagina,
			@RequestParam("pagina") Integer pagina,
			@RequestParam("termoBase64") String texto,
			@RequestParam(value = ("codigo"), required = false) Long codigo,
			@RequestParam(value = ("nome"), required = false) String nome,
			@RequestParam(value = ("status"), required = false) String status) {
		return pessoaService.listarPaginado(elementosPorPagina, pagina, codigo, nome, status);
	}

	@PutMapping("/{codigo}")
	public ResponseEntity<Pessoa> atualizar(@PathVariable Long codigo, @Valid @RequestBody Pessoa pessoa) {
		return ResponseEntity.ok(pessoaService.atualizar(codigo, pessoa));
	}

	@DeleteMapping("/{codigo}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void remover(@PathVariable Long codigo) {
		pessoaService.remover(codigo);
	}

	@GetMapping("/{codigo}")
	public ResponseEntity<Pessoa> buscarPessoaPeloCodigo(@PathVariable Long codigo) {
		Pessoa pessoa = pessoaService.buscarPessoaPeloCodigo(codigo);
		return pessoa != null ? ResponseEntity.ok(pessoa) : ResponseEntity.notFound().build();
	}

	@PutMapping("/{codigo}/ativo")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void atualizarPropriedadeAtivo(@PathVariable Long codigo, @RequestBody Boolean ativo) {
		pessoaService.atualizarPropriedadeAtivo(codigo, ativo);
	}
}