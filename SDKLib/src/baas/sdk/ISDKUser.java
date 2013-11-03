package baas.sdk;

import baas.sdk.messages.Message;
import baas.sdk.utils.User;

public interface ISDKUser {
	public abstract Message login(String nick, String pass);
	public abstract Message register(User user);
	public abstract Message logout();
}
