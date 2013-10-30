package com.baastsi2.modelo;

public class TipoValor {

	private String tipo;
	private int valorEstimado;
	
	public TipoValor() {
	}
		
	public TipoValor(String tipo, int valorEstimado) {
		super();
		this.tipo = tipo;
		this.valorEstimado = valorEstimado;
	}

	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public int getValorEstimado() {
		return valorEstimado;
	}
	public void setValorEstimado(int valorEstimado) {
		this.valorEstimado = valorEstimado;
	}	
}
