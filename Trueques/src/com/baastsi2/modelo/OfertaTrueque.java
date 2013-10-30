package com.baastsi2.modelo;

public class OfertaTrueque extends Trueque {

	private boolean aceptado;

	public OfertaTrueque() {
		super();
	}

	public OfertaTrueque(boolean aceptado) {
		super();
		this.aceptado = aceptado;
	}

	public boolean isAceptado() {
		return aceptado;
	}

	public void setAceptado(boolean aceptado) {
		this.aceptado = aceptado;
	}
	
}
