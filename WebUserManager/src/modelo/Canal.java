package modelo;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.*;

/**
 * Entity implementation class for Entity: Canal
 *
 */
@Entity

public class Canal implements Serializable {

	   
	@Id
	@GeneratedValue
	private long canalId;
	
	public String getNombreCanal() {
		return nombreCanal;
	}

	public void setNombreCanal(String nombreCanal) {
		this.nombreCanal = nombreCanal;
	}

	private String nombreCanal;
	@ManyToOne
	@JoinColumn(name = "aplicacion", referencedColumnName = "aplicacionId")
	private Aplicacion aplicacion;
	
	List<String> regIds;
	
	private static final long serialVersionUID = 1L;
	

	public Canal() {
		super();
		regIds = new LinkedList<String>();
	}

	public long getCanalId() {
		return canalId;
	}

	public void setCanalId(long canalId) {
		this.canalId = canalId;
	}

	public Aplicacion getAplicacion() {
		return aplicacion;
	}

	public void setAplicacion(Aplicacion aplicacion) {
		this.aplicacion = aplicacion;
	}   
	
   
}
