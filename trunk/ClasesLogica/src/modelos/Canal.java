package modelos;

import java.io.Serializable;
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
	@ManyToOne
	@JoinColumn(name = "aplicacion", referencedColumnName = "aplicacionId")
	private Aplicacion aplicacion;
	
	@OneToMany (mappedBy = "canal")
	private List<Mensaje> mensajes;
	private static final long serialVersionUID = 1L;
	

	public Canal() {
		super();
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
