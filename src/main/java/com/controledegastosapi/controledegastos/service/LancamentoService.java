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

import com.controledegastosapi.controledegastos.ExceptionHandler.PessoaInexistenteOuInativaException;
import com.controledegastosapi.controledegastos.model.Lancamento;
import com.controledegastosapi.controledegastos.model.Pessoa;
import com.controledegastosapi.controledegastos.model.Status;
import com.controledegastosapi.controledegastos.model.TipoLancamento;
import com.controledegastosapi.controledegastos.repositoy.ILancamentoRepository;
import com.controledegastosapi.controledegastos.repositoy.IPessoaRepository;

@Service
public class LancamentoService {

	@Autowired
	private ILancamentoRepository lancamentoRepository;

	@Autowired
	private IPessoaRepository pessoaRepository;

	public Lancamento salvar(Lancamento lancamento) {
		Pessoa pessoa = pessoaRepository.findById(lancamento.getPessoa().getCodigo()).orElse(null);
		Status status;
		if (pessoa == null) {
			throw new PessoaInexistenteOuInativaException();
		}
		if (pessoa.setStatus(Status.INATIVO)){
			throw new PessoaInexistenteOuInativaException();
		}
		return lancamentoRepository.save(lancamento);
	}

	public Lancamento atualizar(Long codigo, Lancamento lancamento) {
		Lancamento lancamentoSalvo = verificarLancamento(codigo);
		BeanUtils.copyProperties(lancamento, lancamentoSalvo, "codigo");
		return lancamentoRepository.save(lancamentoSalvo);
	}

	public List<Lancamento> listarTodos() {
		return lancamentoRepository.findAll();
	}

	public Lancamento buscarPeloCodigo(Long codigo) {
		return lancamentoRepository.findById(codigo).orElse(null);
	}

	public Page<Lancamento> listarPaginado(Integer elementosPorPagina, Integer pagina, String texto, Long codigo,
			String descricao, String tipo, Long categoriaCodigo, Long pessoaCodigo) {
		Pageable pageable = PageRequest.of(pagina - 1, elementosPorPagina);
		descricao = descricao == null ? descricao : descricao.concat("%");
		texto = new String(Base64.getDecoder().decode(texto));
		texto = texto == "" ? texto : "%" + texto + "%";
		TipoLancamento tipoLancamento = null;
		if (tipo != null) {
			tipoLancamento = tipo.equals(TipoLancamento.DESPESA.toString()) ? TipoLancamento.RECEITA : TipoLancamento.RECEITA;
		}
		return lancamentoRepository.findPaginado(texto, codigo, descricao, tipoLancamento, categoriaCodigo,
				pessoaCodigo, pageable);
	}

	public void remover(Long codigo) {
		lancamentoRepository.deleteById(codigo);
	}

//	public List<Lancamento> listarPorTermo(String texto) {
//		texto = new String(Base64.getDecoder().decode(texto));
//		return lancamentoRepository.findByNomeContaining(texto);
//	} 	

	private Lancamento verificarLancamento(Long codigo) {
		Lancamento lancamentoSalvo = buscarPeloCodigo(codigo);
		if (lancamentoSalvo == null) {
			throw new EmptyResultDataAccessException(1);
		}
		return lancamentoSalvo;
	}

}