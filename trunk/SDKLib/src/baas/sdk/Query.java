package baas.sdk;

import org.json.JSONArray;

public class Query {
	private String consulta;
	private int primero;
	private int ultimo;
	private int cantPorPagina;
	
	
	
	
	public Query(String consulta, int primero, int ultimo, int cantPorPagina) {
		super();
		this.consulta = consulta;
		this.primero = primero;
		this.ultimo = ultimo;
		this.cantPorPagina = cantPorPagina;
	}
	
	public boolean haySiguiente(){
		return true;
	}
	
	public JSONArray  primeraPagina(){
		
		return null;
	};
	
	public JSONArray proximaPagina(){
		return null;
	} 
	
	public JSONArray anteriorPagina(){
		return null;
	} 
	
	public String getConsulta() {
		return consulta;
	}
	public void setConsulta(String consulta) {
		this.consulta = consulta;
	}
	public int getPrimero() {
		return primero;
	}
	public void setPrimero(int primero) {
		this.primero = primero;
	}
	public int getUltimo() {
		return ultimo;
	}
	public void setUltimo(int ultimo) {
		this.ultimo = ultimo;
	}
	public int getCantPorPagina() {
		return cantPorPagina;
	}
	public void setCantPorPagina(int cantPorPagina) {
		this.cantPorPagina = cantPorPagina;
	}
	
	
}
