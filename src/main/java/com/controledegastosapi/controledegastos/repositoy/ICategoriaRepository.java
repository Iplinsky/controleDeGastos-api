package com.controledegastosapi.controledegastos.repositoy;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.controledegastosapi.controledegastos.model.Categoria;

@Repository
public interface ICategoriaRepository extends JpaRepository<Categoria, Long> {
	
	@Query("select a from Categoria a "
			+ "where (a.codigo	 = 		 :codigo  or 	:codigo 	is 	null) "
			+ "and   (a.nome 	 like	 :nome	  or 	:nome		= 	 '' )"
	)
	Page<Categoria> findPaginado(
			@Param("codigo") Long codigo,
			@Param("nome") String nome,
			Pageable pageable);	
	
	@Query("SELECT n FROM Categoria n "
			+ "WHERE (n.nome	LIKE	:nome	or	:nome	=  '')")
	List<Categoria> buscarPorNomeDaCategoria(String nome);
	
	List<Categoria> findByNomeContaining(String nome);
}
