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
import com.controledegastosapi.controledegastos.model.Categoria;
import com.controledegastosapi.controledegastos.service.CategoriaService;

@RestController
@RequestMapping("/categorias")
public class CategoriaController {

	@Autowired
	private CategoriaService categoriaService;
 
	@Autowired
	private ApplicationEventPublisher publisher;

	@GetMapping 
	public List<Categoria> listarTodos() {
		return categoriaService.listarTodos();
	}

	@GetMapping("/{codigo}")
	public ResponseEntity<Categoria> buscarPeloCodigo(@PathVariable Long codigo) {
		Categoria categoria = categoriaService.buscarPeloCodigo(codigo);
		return categoria != null ? ResponseEntity.ok(categoria) : ResponseEntity.notFound().build();
	}

	@GetMapping("/listarPaginado")
	public Page<Categoria> listarPaginado(
			@RequestParam("elementosPorPagina") Integer elementosPorPagina,
			@RequestParam("pagina") Integer pagina, 
			@RequestParam(value = ("codigo"), required = false) Long codigo,
			@RequestParam(value = ("nome"), required = false) String nome) {
		return categoriaService.listarPaginado(elementosPorPagina,pagina, codigo, nome);
	}

	@PostMapping
	public ResponseEntity<Categoria> salvar(@Valid @RequestBody Categoria categoria, HttpServletResponse response) {
		Categoria categoriaSalva = categoriaService.save(categoria);
		publisher.publishEvent(new RecursoCriadoEvent(this, response, categoriaSalva.getCodigo()));
		return ResponseEntity.status(HttpStatus.CREATED).body(categoriaSalva);
	}

	@PutMapping("/{codigo}")
	public ResponseEntity<Categoria> atualizar(@PathVariable Long codigo, @Valid @RequestBody Categoria categoria) {
		return ResponseEntity.ok(categoriaService.atualizar(codigo, categoria));
	}

	@GetMapping("/termo/{termoBase64}")
	public List<Categoria> listarPorTermo(@PathVariable("termoBase64") String nome) {
		return categoriaService.listarPorTermo(nome);
	}

	@DeleteMapping("/{codigo}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void remover(@PathVariable Long codigo) {
		categoriaService.delete(codigo);
	}
}