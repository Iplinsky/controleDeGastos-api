package com.controledegastosapi.controledegastos.controller;

import java.util.List;

import javax.servlet.http.HttpServletResponse;
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

import com.controledegastosapi.controledegastos.model.Categoria;
import com.controledegastosapi.controledegastos.service.CategoriaService;

@RestController
@RequestMapping("/categorias")
public class CategoriaController {

	@Autowired
	private CategoriaService categoriaService;
	
	@PostMapping
	public ResponseEntity<Categoria> salvar(@Valid @RequestBody Categoria categoria, HttpServletResponse response) {
		return ResponseEntity.status(HttpStatus.CREATED).body(categoriaService.save(categoria));
	}

	@PutMapping("/{codigo}")
	public ResponseEntity<Categoria> atualizar(@PathVariable Long codigo, @Valid @RequestBody Categoria categoria) {
		return ResponseEntity.ok(categoriaService.atualizar(codigo, categoria));
	}
	
	@DeleteMapping("/{codigo}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void remover(@PathVariable Long codigo) {
		categoriaService.delete(codigo);
	}
	
	@GetMapping("/{codigo}")
	public ResponseEntity<Categoria> buscarPeloCodigo(@PathVariable Long codigo) {
		Categoria categoria = categoriaService.buscarPeloCodigo(codigo);
		return categoria != null ? ResponseEntity.ok(categoria) : ResponseEntity.notFound().build();
	}

	@GetMapping
	public Page<Categoria> listarPaginado(
			@RequestParam("pagina") Integer pagina, 
			@RequestParam("elementosPorPagina") Integer elementosPorPagina,			
			@RequestParam(value = ("codigo"), required = false) Long codigo,
			@RequestParam(value = ("nome"), required = false) String nome) {
		
		return categoriaService.listarPaginado(pagina, elementosPorPagina, codigo, nome);
	}

	@GetMapping("/termo/{termoBase64}")
	public List<Categoria> listarPorTermo(@PathVariable("termoBase64") String nome) {
		return categoriaService.listarPorTermo(nome);
	}
}