package com.controledegastosapi.controledegastos.service;

import java.util.Base64;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.controledegastosapi.controledegastos.ExceptionHandler.RegraNegocioException;
import com.controledegastosapi.controledegastos.model.Pessoa;
import com.controledegastosapi.controledegastos.repositoy.IPessoaRepository;

@Service
public class PessoaService {

	@Autowired
	private IPessoaRepository pessoaRepository;

	public Pessoa salvar(Pessoa pessoa) { 
		verificarPessoaExistente(pessoa.getNome());
		return pessoaRepository.save(pessoa);
	}
	
	public Pessoa atualizar(Long codigo, Pessoa pessoa) {
		verificarPessoaExistente(pessoa.getNome());
		return pessoaRepository.save(pessoa);
	}

	public void remover(Long codigo) {
		try {
			pessoaRepository.deleteById(codigo);	
		} catch (DataIntegrityViolationException e) {
			e.getMessage();
		}
	}

	public Pessoa buscarPessoaPeloCodigo(Long codigo) {
		Pessoa pessoaSalva = pessoaRepository.findById(codigo).orElse(null);
		if (pessoaSalva == null) {
			throw new EmptyResultDataAccessException(1);
		}
		return pessoaSalva;
	}
	
//	Busca paginada ----------------------
	
	public Page<Pessoa> listarPaginado (
			Integer pagina,
			Integer elementosPorPagina,		
			String texto,
			Long codigo,
			String nome) {
		
		Pageable pageable = PageRequest.of(pagina - 1, elementosPorPagina);		
		texto = texto == "" ? texto : "%" + texto + "%";
		nome = nome == null ? "%%" : "%" + nome.concat("%");
		
		return pessoaRepository.findPaginado(texto, codigo, nome, pageable);
	}

//	 Codigo de verificação --------------
	
	public void verificarPessoaExistente(String nome) {
		if (!pessoaRepository.buscarPorNome(nome).isEmpty()) {
			throw new RegraNegocioException(
					String.format("O sistema já possui um registro com {%s} informado.", nome));
		}
	}

	public void atualizarPropriedadeAtivo(Long codigo, Boolean ativo) {
		Pessoa pessoaSalva = buscarPessoaPeloCodigo(codigo);
		pessoaSalva.setAtivo(ativo);
		pessoaRepository.save(pessoaSalva);
	}

	public List<Pessoa> listarPorTermo(String texto) {
		texto = new String(Base64.getDecoder().decode(texto));
		return pessoaRepository.findByNomeContaining(texto);
	}
}
