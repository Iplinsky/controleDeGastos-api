package com.controledegastosapi.controledegastos.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.controledegastosapi.controledegastos.ExceptionHandler.PessoaInexistenteOuInativaException;
import com.controledegastosapi.controledegastos.ExceptionHandler.RegraNegocioException;
import com.controledegastosapi.controledegastos.model.Lancamento;
import com.controledegastosapi.controledegastos.model.Pessoa;
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
		if (pessoa == null || pessoa.isInativo()) {
			throw new PessoaInexistenteOuInativaException();
		}
		return lancamentoRepository.save(lancamento);
	}

	public Lancamento atualizar(Long codigo, Lancamento lancamento) {
		verificarLancamento(lancamento.getDescricao());
		return lancamentoRepository.save(lancamento);
	}

	public Lancamento buscarPeloCodigo(Long codigo) {
		return lancamentoRepository.findById(codigo).orElse(null);
	}

	public void remover(Long codigo) {
		try {
			lancamentoRepository.deleteById(codigo);	
		} catch (DataIntegrityViolationException e) {
			e.getMessage();
		}		
	}
	
	public Page<Lancamento> listarPaginado(
			Integer pagina, 
			Integer elementosPorPagina,			
			String texto,
			Long codigo,
			String descricao,
			String tipo,
			Long categoriaCodigo,
			Long pessoaCodigo) {
		
		Pageable pageable = PageRequest.of(pagina - 1, elementosPorPagina);
		texto = texto == "" ? texto : "%" + texto + "%";
		descricao = descricao == null ? "%%" : descricao.concat("%");		
		TipoLancamento tipoLancamento = null;
		
		if (tipo != null) {
			tipoLancamento = tipo.equals(TipoLancamento.DESPESA.toString()) ? TipoLancamento.RECEITA : TipoLancamento.RECEITA;
		}
		return lancamentoRepository.findPaginado(
				texto, codigo, descricao, tipoLancamento, categoriaCodigo, pessoaCodigo, pageable);
	}

	private void verificarLancamento(String descricao) {
		if (lancamentoRepository.verificaLancamento(descricao).isEmpty()) {
			throw new RegraNegocioException(
					String.format("O sistema já possui um registro com {%s} informado!", descricao));
		}
	}	
}