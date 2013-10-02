package modelos;

import java.io.Serializable;
import java.lang.Long;
import java.lang.String;
import javax.persistence.*;

/**
 * Entity implementation class for Entity: Aplicacion
 *
 */
@Entity
public class Aplicacion implements Serializable {

	   
	@Id
	private Long id;
	private String Nombre;
	@ManyToOne
	@JoinColumn(name= "owner", referencedColumnName="personaId")
	private Desarrollador owner;
	private static final long serialVersionUID = 1L;

	public Aplicacion() {
		super();
	}   
	public Long getid() {
		return this.id;
	}

	public void setid(Long AplicacionId) {
		this.id = AplicacionId;
	}   
	public String getNombre() {
		return this.Nombre;
	}

	public void setNombre(String Nombre) {
		this.Nombre = Nombre;
	}
   
}
