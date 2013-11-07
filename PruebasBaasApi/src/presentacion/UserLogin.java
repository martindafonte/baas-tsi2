/**
 * 
 */
package presentacion;

import java.util.List;

import javax.ejb.EJB;
import javax.faces.context.FacesContext;

import org.json.JSONException;

import negocio.ServiciosLocal;

/**
 * @author bruno
 *
 */
public class UserLogin {
	
	private java.lang.String nick;
	private java.lang.String pass;
	private String rol;

	
	private Boolean login = false;
	
	public Boolean getLogin() {
		return login;
	}

	public void setLogin(Boolean login) {
		this.login = login;
	}
	

	@EJB
	private ServiciosLocal serv; 


	public UserLogin() {
	}
	
	
	public String getRol() {
		return rol;
	}
	
	public void setRol(String rol) {
		this.rol = rol;
	}
	
	public java.lang.String getNick() {
		return nick;
	}

	public void setNick(java.lang.String nick) {
		this.nick = nick;
	}

	public java.lang.String getPass() {
		return pass;
	}

	public void setPass(java.lang.String pass) {
		this.pass = pass;
	}
	
	public String login(){
		
		serv.login(nick, pass);
		login = true;
		return "/Usuarios.xhtml";
	}
	
	public String logout(){
		
		FacesContext context = FacesContext.getCurrentInstance();
		context.getExternalContext().getSessionMap().remove("userLogin");
		login = false;
		return "/index.xhtml";
		
	}
	
	public void agregarRol(){
		
		serv.setearRolUsuario(nick, rol);
	}
	
	public void quitarRol(){
		
		serv.quitarRolUsuario(nick, rol);
	}
	
	public String permitirA1(){
		try {
			List<String> lp = serv.obtenerPermisosUsuario(nick);
			if (lp.contains("Administrador"))
				return "/Administradores.xhtml";
		} catch (JSONException e) {
		}
		return "/ErrorPermiso.xhtml";
	}
	
	public String permitirD1(){
		try {
			List<String> lp = serv.obtenerPermisosUsuario(nick);
			if (lp.contains("Desarrollador"))
				return "/Desarrolladores.xhtml";
		} catch (JSONException e) {
		}
		return "/ErrorPermiso.xhtml";
	}
	
	public String go(){
		
		if (login == null){
			login = new Boolean(false);			
		}
		
		if (login){
			
			return "/MasterPage/logout.xhtml";
		}

		return "/MasterPage/login.xhtml";
	}
}
