package com.controledegastosapi.controledegastos.repositoy;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.controledegastosapi.controledegastos.model.Pessoa;

@Repository
public interface IPessoaRepository extends JpaRepository<Pessoa, Long> {
	
	@Query("select a from Pessoa a "
			+ "where 	(a.codigo	 	= 		:codigo 	or 		:codigo  = null)"
			+ "and 		(a.nome 		like 	:nome 		or 		:nome	 =	'' )"
			+ "and		(a.nome			like	:texto		or		:texto	 =	'' )"
	)
	Page<Pessoa> findPaginado(
			@Param("texto") String texto,
			@Param ("codigo") Long codigo,
			@Param ("nome") String nome, 
			Pageable pageable);
	
	List<Pessoa> findByNomeContaining(String nome);

	@Query("SELECT a FROM Pessoa a "
			+ "WHERE (a.nome	LIKE	:nome	OR	:nome	=  '')")
	List<Pessoa> buscarPorNome(String nome);
}
