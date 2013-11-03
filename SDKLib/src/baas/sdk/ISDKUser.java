package baas.sdk;

import baas.sdk.messages.Message;
import baas.sdk.messages.MessageStringList;
import baas.sdk.messages.MessageUser;
import baas.sdk.utils.User;

public interface ISDKUser {
	public abstract Message login(String nick, String pass);
	public abstract Message register(User user);
	public abstract Message logout();
	public abstract MessageUser getUser(String nick);
	public abstract MessageStringList getUserPermissions(String nick);
	public abstract Message setUserRole(String nick, String role);
	public abstract Message removeUserRole(String nick, String role);
	public abstract Message isloggedIn(String nick);
}
