package baas.sdk.messages;

import baas.sdk.utils.Constants;
import baas.sdk.utils.User;

public class MessageUser extends Message {
	public User user;
	/**Crea un mensaje con un usuario y codigo de exito
	 * */
	public MessageUser(User p_u){
		super(Constants.Exito);
		user = p_u;
	}
	
	public MessageUser(){
	}
}
