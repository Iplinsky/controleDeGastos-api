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

import com.controledegastosapi.controledegastos.model.Pessoa;
import com.controledegastosapi.controledegastos.model.Status;
import com.controledegastosapi.controledegastos.repositoy.IPessoaRepository;

@Service
public class PessoaService {

	@Autowired
	private IPessoaRepository pessoaRepository;

	public Pessoa salvar(Pessoa pessoa) {
		return pessoaRepository.save(pessoa);
	}

	public List<Pessoa> listarTodos() {
		return pessoaRepository.findAll();
	}

	public Pessoa atualizar(Long codigo, Pessoa pessoa) {
		Pessoa pessoaSalva = verificarPessoaExistente(codigo);
		BeanUtils.copyProperties(pessoa, pessoaSalva, "codigo");
		return pessoaRepository.save(pessoaSalva);
	}

	public void remover(Long codigo) {
		pessoaRepository.deleteById(codigo);
	}

	public Pessoa buscarPessoaPeloCodigo(Long codigo) {
		Pessoa pessoaSalva = pessoaRepository.findById(codigo).orElse(null);
		if (pessoaSalva == null) {
			throw new EmptyResultDataAccessException(1);
		}
		return pessoaSalva;
	}

//	Busca paginada ----------------------

	public Page<Pessoa> listarPaginado(Integer elementosPorPagina, Integer pagina, Long codigo, String nome,
			String status) {
		Pageable pageable = PageRequest.of(pagina - 1, elementosPorPagina);
		nome = nome == null ? null : "%" + nome.concat("%");
		Status tipoStatus;
		if (status != null) {
			tipoStatus = status.equals(Status.ATIVO) ? Status.ATIVO : Status.INATIVO;
		}

		return pessoaRepository.findPaginado(codigo, nome, status, pageable);
	}

//	 Codigo de verificação --------------

	public Pessoa verificarPessoaExistente(Long codigo) {
		Pessoa pessoaSalva = buscarPessoaPeloCodigo(codigo);
		if (pessoaSalva == null) {
			throw new EmptyResultDataAccessException(1);
		}
		return pessoaSalva;
	}

	public void atualizarPropriedadeAtivo(Long codigo, Boolean ativo) {
		Pessoa pessoaSalva = buscarPessoaPeloCodigo(codigo);
		pessoaSalva.setStatus(Status.ATIVO);
		pessoaRepository.save(pessoaSalva);
	}

	public List<Pessoa> listarPorTermo(String texto) {
		texto = new String(Base64.getDecoder().decode(texto));
		return pessoaRepository.findByNomeContaining(texto);
	}
}
