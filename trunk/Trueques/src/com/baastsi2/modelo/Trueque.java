package com.baastsi2.modelo;


public abstract class Trueque {

	private TipoValor objeto;
	private String descripcion;
	private String urlImagen;
	private String ubicacion;
	
	public Trueque() {
	}
		
	public Trueque(TipoValor objeto, String descripcion, String urlImagen,
			String ubicacion) {
		this.objeto = objeto;
		this.descripcion = descripcion;
		this.urlImagen = urlImagen;
		this.ubicacion = ubicacion;
	}

	public TipoValor getObjeto() {
		return objeto;
	}

	public void setObjeto(TipoValor objeto) {
		this.objeto = objeto;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getUrlImagen() {
		return urlImagen;
	}

	public void setUrlImagen(String urlImagen) {
		this.urlImagen = urlImagen;
	}

	public String getUbicacion() {
		return ubicacion;
	}

	public void setUbicacion(String ubicacion) {
		this.ubicacion = ubicacion;
	}
}
