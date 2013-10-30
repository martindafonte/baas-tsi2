package baas.sdk;

import baas.sdk.messages.Message;
import baas.sdk.utils.User;

public interface ISDKUser {
	public Message login(String nick, String pass);
	public Message register(User user);
	public Message logout();
}
