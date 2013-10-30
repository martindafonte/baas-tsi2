package com.google.android.gcm;

public class Notificacion {
	private String msj;
	private int canal;
	private Boolean error;
	
	public Notificacion(String m, int c, Boolean e) {
		msj = m;
		canal = c;
		error = e;
	}
	
	public String getMsj() {
		return msj;
	}
	
	public int getCanal() {
		return canal;
	}
	
	public Boolean getError() {
		return error;
	}
	
	public void setMsj(String msj) {
		this.msj = msj;
	}
	
	public void setCanal(int canal) {
		this.canal = canal;
	}
	
	public void setError(Boolean error) {
		this.error = error;
	}
}
