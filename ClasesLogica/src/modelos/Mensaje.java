package modelos;

import java.io.Serializable;
import java.lang.String;
import javax.persistence.*;

/**
 * Entity implementation class for Entity: Mensaje
 *
 */
@Entity

public class Mensaje implements Serializable {

	@Id
	@GeneratedValue
	private long mensajeId;
	private String texto;
	@ManyToOne
	@JoinColumn(name = "canal", referencedColumnName="canalId")
	private Canal canal;
	private static final long serialVersionUID = 1L;

	public Mensaje() {
		super();
	}   
	
	public long getMensajeId() {
		return mensajeId;
	}

	public void setMensajeId(long mensajeId) {
		this.mensajeId = mensajeId;
	}

	public String getTexto() {
		return this.texto;
	}

	public void setTexto(String texto) {
		this.texto = texto;
	}
   
}
