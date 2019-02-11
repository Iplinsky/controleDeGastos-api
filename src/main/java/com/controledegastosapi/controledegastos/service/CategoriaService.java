package com.controledegastosapi.controledegastos.service;

import java.util.Base64;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
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

	public List<Categoria> listarTodos() {
		return categoriaRepository.findAll();
	}

	public Categoria buscarPeloCodigo(Long codigo) {
		return categoriaRepository.findById(codigo).orElse(null); 
	}

	public Categoria save(Categoria categoria) {
		return categoriaRepository.save(categoria);
	}

	public void delete(Long codigo) {
		categoriaRepository.deleteById(codigo);
	}

	public Categoria atualizar(Long codigo, Categoria categoria) {
		Categoria categoriaSalva = verificarCategoriaExistente(codigo);
		verificarNomeCategoria(categoria);
		BeanUtils.copyProperties(categoria, categoriaSalva, "codigo");
		return categoriaRepository.save(categoriaSalva);
	}

	private void verificarNomeCategoria(Categoria categoria) {
		if (!categoriaRepository.findByNome(categoria.getNome()).isEmpty()) {
			throw new RegraNegocioException(String.format("A categoria %s já está cadastrada!", categoria.getNome()));
		}
	}

	private Categoria verificarCategoriaExistente(Long codigo) {
		Categoria categoriaSalva = buscarPeloCodigo(codigo);
		if (categoriaSalva == null) {
			throw new EmptyResultDataAccessException(1);
		}
		return categoriaSalva;
	}

	public Page<Categoria> listarPaginado(Integer elementosPorPagina, Integer pagina, Long codigo, String nome) {
		Pageable pageable = PageRequest.of(pagina - 1, elementosPorPagina);
		nome = nome == null ? null : "%" + nome.concat("%");
		return categoriaRepository.findPaginado(codigo, nome, pageable);
	}

	public List<Categoria> listarPorTermo(String nome) {
		nome = new String(Base64.getDecoder().decode(nome));
		return categoriaRepository.findByNomeContaining(nome);
	}
}
