 package com.controledegastosapi.controledegastos.repositoy;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.controledegastosapi.controledegastos.model.Lancamento;
import com.controledegastosapi.controledegastos.model.TipoLancamento;

@Repository
public interface ILancamentoRepository extends JpaRepository<Lancamento, Long> {

	@Query("   Select a" + "  From Lancamento a"
			+ " where (a.codigo           = :codigo          or :codigo          = null)"
			+ "   and (a.tipo             = :tipo            or :tipo            = null)"
			+ "   and (a.categoria.codigo = :categoriaCodigo or :categoriaCodigo = null)"
			+ "   and (a.pessoa.codigo    = :pessoaCodigo    or :pessoaCodigo    = null)"
			+ "   and (a.descricao     like :descricao       or :descricao       = null)"
			+ "   and (a.descricao     like :texto           or a.categoria.nome like :texto"
			+ "     or a.pessoa.nome   like :texto           or :texto           = '')")
	
	Page<Lancamento> findPaginado(@Param("texto") String texto, @Param("codigo") Long codigo,
			@Param("descricao") String descricao, 
			@Param("tipo") TipoLancamento tipo,
			@Param("categoriaCodigo") Long categoriaCodigo,
			@Param("pessoaCodigo") Long pessoaCodigo,
			Pageable pageable);

//	List<Lancamento> findByNomeContaining(String texto);
}
