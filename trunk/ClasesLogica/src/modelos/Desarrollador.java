package modelos;

import java.io.Serializable;




import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.*;

import modelos.Persona;

/**
 * Entity implementation class for Entity: Desarrollador
 *
 */
@Entity

public class Desarrollador extends Persona implements Serializable {

	@OneToMany(mappedBy="owner")
	private Collection<Aplicacion> aplicaciones;
	
	
	public Desarrollador() {
		super();
		aplicaciones = new LinkedList<Aplicacion>();
		
	}   
	
	public Collection<Aplicacion> getAplicaciones() {
		return aplicaciones;
	}

	public void addAplicacion(Aplicacion p_app){
		aplicaciones.add(p_app);
	}
	protected void setAplicaciones(List<Aplicacion> aplicaciones) {
		this.aplicaciones = aplicaciones;
	}

	private static final long serialVersionUID = 1L;

	
	
   
}
