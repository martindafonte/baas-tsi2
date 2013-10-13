package negocio;

import javax.ejb.Local;

import modelo.Usuario;

@Local
public interface ServiciosLocal {

	public void altaUsuario(String nick, String pass, String nombre, String apellido);
	public Usuario getUsuario(String nick, String pass);
	public Boolean existeUsuario(String nick, String pass);
	
}
