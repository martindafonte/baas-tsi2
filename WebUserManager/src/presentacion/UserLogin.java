/**
 * 
 */
package presentacion;

import javax.ejb.EJB;
import javax.faces.context.FacesContext;

import negocio.ServiciosLocal;

/**
 * @author bruno
 *
 */

public class UserLogin {
	private java.lang.String nick;
	private java.lang.String pass;

	private Boolean login = false;
	
	@EJB
	private ServiciosLocal serv; 
	

	public UserLogin() {
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
		
		Boolean error = serv.existeUsuario(nick, pass);
		if(!error){
			return "errorPage.xhtml";
		}
		login = true;
		return "/index.xhtml";
		
	}
	
	public String logout(){
		
		FacesContext context = FacesContext.getCurrentInstance();
		context.getExternalContext().getSessionMap().remove("userLogin");
		login = false;
		return "/index.xhtml";
		
	}
	
	
	public String go(){
		
		if (login){
			
			return "/MasterPage/logout.xhtml";
		}
		
		return "/MasterPage/login.xhtml";
	}
	
}
