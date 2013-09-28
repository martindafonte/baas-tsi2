package modelos;

import java.io.Serializable;
import java.lang.String;
import javax.persistence.*;

/**
 * Entity implementation class for Entity: Persona
 *
 */
@Entity
public class Persona implements Serializable {

	   
	@Id
	@GeneratedValue
	private long personaId;
	private String nick;
	private String pass;
	private String nombre;
	private String apellido;
	private static final long serialVersionUID = 1L;

	public Persona() {
		super();
	}   
	public long getPersonaId() {
		return this.personaId;
	}

	public void setPersonaId(long personaId) {
		this.personaId = personaId;
	}   
	public String getNick() {
		return this.nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}   
	public String getPass() {
		return this.pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}   
	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}   
	public String getApellido() {
		return this.apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}
   
}
