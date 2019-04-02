package com.controledegastosapi.controledegastos.controller;

import java.time.LocalDateTime;
import java.util.Base64;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.controledegastosapi.controledegastos.model.Lancamento;
import com.controledegastosapi.controledegastos.service.LancamentoService;

@RestController
@RequestMapping("/lancamentos")
public class LancamentoController {

	@Autowired 
	private LancamentoService lancamentoService;

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public void salvar(@RequestBody Lancamento lancamento) {
		lancamento.setDatCriacao(LocalDateTime.now());
		lancamentoService.salvar(lancamento);
	}
	
	@PutMapping("/{codigo}")
	public ResponseEntity<Lancamento> atualizar(@PathVariable Long codigo, @Valid @RequestBody Lancamento lancamento) {
		lancamento.setDatAlteracao(LocalDateTime.now());
		return ResponseEntity.ok(lancamentoService.atualizar(codigo, lancamento));
	}
	
	@DeleteMapping("/{codigo}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void remover(@PathVariable Long codigo) {
		lancamentoService.remover(codigo);
	}

	@GetMapping("/{codigo}")
	public ResponseEntity<Lancamento> buscarPeloCodigo(@PathVariable Long codigo) {
		Lancamento lancamento = lancamentoService.buscarPeloCodigo(codigo);
		return lancamento != null ? ResponseEntity.ok(lancamento) : ResponseEntity.notFound().build();
	}

	@GetMapping
	public Page<Lancamento> listarPaginado(
			@RequestParam("pagina") Integer pagina, 
			@RequestParam("elementosPorPagina") Integer elementosPorPagina,			
			@RequestParam("termoBase64") String texto,
			@RequestParam(value = ("codigo"), required = false) Long codigo,
			@RequestParam(value = ("descricao"), required = false) String descricao,
			@RequestParam(value = ("tipo"), required = false) String tipo,
			@RequestParam(value = ("categoriaCodigo"), required = false) Long categoriaCodigo,
			@RequestParam(value = ("pessoaCodigo"), required = false) Long pessoaCodigo) {

		texto = new String(Base64.getDecoder().decode(texto));
		return lancamentoService.listarPaginado(
				elementosPorPagina,
				pagina,
				texto, 
				codigo, 
				descricao,
				tipo,
				categoriaCodigo, 
				pessoaCodigo);
	}
}