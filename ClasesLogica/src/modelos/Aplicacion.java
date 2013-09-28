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
	private Long AplicacionId;
	private String Nombre;
	private static final long serialVersionUID = 1L;

	public Aplicacion() {
		super();
	}   
	public Long getAplicacionId() {
		return this.AplicacionId;
	}

	public void setAplicacionId(Long AplicacionId) {
		this.AplicacionId = AplicacionId;
	}   
	public String getNombre() {
		return this.Nombre;
	}

	public void setNombre(String Nombre) {
		this.Nombre = Nombre;
	}
   
}
