package negocio;

import java.util.List;
import javax.ejb.Local;
import org.json.JSONException;

@Local
public interface ServiciosLocal {

	public void login(String nick, String pass);
	public void logout(String nick, String pass);
	public boolean altaUsuario(String nick, String pass, String nombre, String apellido);
	public void setearRolUsuario(String nick, String rol);
	public void quitarRolUsuario(String nick, String rol);
	public List<String> obtenerPermisosUsuario(String nick) throws JSONException;
}
