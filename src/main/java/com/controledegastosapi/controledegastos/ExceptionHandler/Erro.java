package com.controledegastosapi.controledegastos.ExceptionHandler;

public class Erro {

	public String mensagemUsuario;
	public String mensagemDesenvolvedor;

	public Erro(String mensagemUsuario, String mensagemDesenvolvedor) {
		this.mensagemUsuario = mensagemUsuario;
		this.mensagemDesenvolvedor = mensagemDesenvolvedor;
	}

	public String getMensagemUsuario() {
		return mensagemUsuario;
	}

	public String getmensagemDesenvolvedor() {
		return mensagemDesenvolvedor;
	}
}
