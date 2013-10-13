package modelo;

import java.io.Serializable;
import java.lang.Long;
import java.lang.String;
import java.util.List;

import javax.persistence.*;

/**
 * Entity implementation class for Entity: Aplicacion
 *
 */
@Entity
public class Aplicacion implements Serializable {

	   
	@Id
	private Long aplicacionId;
	private String Nombre;
	@ManyToOne
	@JoinColumn(name= "owner", referencedColumnName="nick")
	private Usuario owner;
	
	@OneToMany(mappedBy="aplicacion")
	private List<Canal> canales;
	private static final long serialVersionUID = 1L;

	public Aplicacion() {
		super();
	}   
	
	public Long getAplicacionId() {
		return aplicacionId;
	}

	public void setAplicacionId(Long aplicacionId) {
		this.aplicacionId = aplicacionId;
	}

	public Usuario getOwner() {
		return owner;
	}

	public void setOwner(Usuario owner) {
		this.owner = owner;
	}

	public List<Canal> getCanales() {
		return canales;
	}

	public void setCanales(List<Canal> canales) {
		this.canales = canales;
	}

	public String getNombre() {
		return this.Nombre;
	}

	public void setNombre(String Nombre) {
		this.Nombre = Nombre;
	}
   
}
