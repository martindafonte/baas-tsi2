package modelo;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedList;

import javax.persistence.*;


/**
 * The persistent class for the usuario database table.
 * 
 */
@Entity
@NamedQuery(name="Usuario.findAll", query="SELECT u FROM Usuario u")
public class Usuario implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String nick;

	private String apellido;

	private String nombre;

	public Collection<Aplicacion> getAplicaciones() {
		return aplicaciones;
	}

	public void setAplicaciones(Collection<Aplicacion> aplicaciones) {
		this.aplicaciones = aplicaciones;
	}

	private String pass;

	@OneToMany(mappedBy="owner")
	private Collection<Aplicacion> aplicaciones;
	
	public Usuario() {
		aplicaciones = new LinkedList<>();
						}

	public String getNick() {
		return this.nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public String getApellido() {
		return this.apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getPass() {
		return this.pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

}