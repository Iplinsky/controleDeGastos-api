package com.controledegastosapi.controledegastos.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.controledegastosapi.controledegastos.ExceptionHandler.RegraNegocioException;
import com.controledegastosapi.controledegastos.model.Categoria;
import com.controledegastosapi.controledegastos.repositoy.ICategoriaRepository;

@Service
public class CategoriaService {

	@Autowired
	private ICategoriaRepository categoriaRepository;

	public Categoria save(Categoria categoria) {
		verificarNomeCategoria(categoria);
		return categoriaRepository.save(categoria);
	}

	public void delete(Long codigo) {
		try {
			categoriaRepository.deleteById(codigo);
		} catch (DataIntegrityViolationException e) {
			e.getMessage();
		}		
	}

	public Categoria atualizar(Long codigo, Categoria categoria) {
		verificarNomeCategoria(categoria);
		return categoriaRepository.save(categoria);
	}
	
	public Categoria buscarPeloCodigo(Long codigo) {
		return categoriaRepository.findById(codigo).orElse(null); 
	}
	
	public List<Categoria> listarPorTermo(String nome) {
		return categoriaRepository.findByNomeContaining(nome);
	}

	public Page<Categoria> listarPaginado(
			Integer pagina,
			Integer elementosPorPagina,			
			Long codigo,
			String nome) {
		
		Pageable pageable = PageRequest.of(pagina - 1, elementosPorPagina);
		nome = nome == null ? "%%" : "%" + nome.concat("%");
		return categoriaRepository.findPaginado(codigo, nome, pageable);
	}

	private void verificarNomeCategoria(Categoria categoria) {
		if (!categoriaRepository.buscarPorNomeDaCategoria(categoria.getNome()).isEmpty()) {
			throw new RegraNegocioException(
					String.format("A categoria %s já está cadastrada!", categoria.getNome()));
		}
	}
}
