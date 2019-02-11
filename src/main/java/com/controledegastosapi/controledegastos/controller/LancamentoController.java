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
import com.controledegastosapi.controledegastos.model.Lancamento;
import com.controledegastosapi.controledegastos.service.LancamentoService;

@RestController
@RequestMapping("/lancamentos")
public class LancamentoController {

	@Autowired 
	private LancamentoService lancamentoService;

	@Autowired
	private ApplicationEventPublisher publisher;

	@PostMapping
	public ResponseEntity<Lancamento> salvar(@Valid @RequestBody Lancamento lancamento, HttpServletResponse response) {
		Lancamento lancamentoSalvo = lancamentoService.salvar(lancamento);
		publisher.publishEvent(new RecursoCriadoEvent(this, response, lancamento.getCodigo()));
		return ResponseEntity.status(HttpStatus.CREATED).body(lancamentoSalvo);
	}
	
	@PutMapping("/{codigo}")
	public ResponseEntity<Lancamento> atualizar(@PathVariable Long codigo, @Valid @RequestBody Lancamento lancamento) {
		return ResponseEntity.ok(lancamentoService.atualizar(codigo, lancamento));
	}
	
	@GetMapping
	public List<Lancamento> listarTodos() {
		return lancamentoService.listarTodos();
	}

	@GetMapping("/{codigo}")
	public ResponseEntity<Lancamento> buscarPeloCodigo(@PathVariable Long codigo) {
		Lancamento lancamento = lancamentoService.buscarPeloCodigo(codigo);
		return lancamento != null ? ResponseEntity.ok(lancamento) : ResponseEntity.notFound().build();
	}

	@GetMapping("/listarPaginado")
	public Page<Lancamento> listarPaginado(@RequestParam("elementosPorPagina") Integer elementosPorPagina,
			@RequestParam("pagina") int pagina, 
			@RequestParam("termoBase64") String texto,
			@RequestParam(value = ("codigo"), required = false) Long codigo,
			@RequestParam(value = ("descricao"), required = false) String descricao,
			@RequestParam(value = ("tipo"), required = false) String tipo,
			@RequestParam(value = ("categoriaCodigo"), required = false) Long categoriaCodigo,
			@RequestParam(value = ("pessoaCodigo"), required = false) Long pessoaCodigo) {

		return lancamentoService.listarPaginado(elementosPorPagina, pagina, texto, codigo, descricao, tipo,
				categoriaCodigo, pessoaCodigo);
	}

	@DeleteMapping("/{codigo}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void remover(@PathVariable Long codigo) {
		lancamentoService.remover(codigo);
	}
}