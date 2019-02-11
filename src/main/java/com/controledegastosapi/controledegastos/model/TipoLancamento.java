package com.controledegastosapi.controledegastos.model;

public enum TipoLancamento {

	RECEITA, DESPESA;

	private String descricao;

	public String getDescricao() {
		return descricao;
	}
}
