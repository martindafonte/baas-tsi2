package com.baastsi2.modelo;

import java.util.List;

public class PedidoTrueque extends Trueque {
	
	private List<TipoValor> esperados;
	private List<OfertaTrueque> ofertas;
	private boolean finalizado;

	public PedidoTrueque() {
		super();
	}

	public PedidoTrueque(List<TipoValor> esperados,
			List<OfertaTrueque> ofertas, boolean finalizado) {
		super();
		this.esperados = esperados;
		this.ofertas = ofertas;
		this.finalizado = finalizado;
	}

	public List<TipoValor> getEsperados() {
		return esperados;
	}

	public void setEsperados(List<TipoValor> esperados) {
		this.esperados = esperados;
	}

	public List<OfertaTrueque> getOfertas() {
		return ofertas;
	}

	public void setOfertas(List<OfertaTrueque> ofertas) {
		this.ofertas = ofertas;
	}

	public boolean isFinalizado() {
		return finalizado;
	}

	public void setFinalizado(boolean finalizado) {
		this.finalizado = finalizado;
	}
	
}
