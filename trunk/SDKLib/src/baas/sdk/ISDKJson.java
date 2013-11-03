package baas.sdk;

import baas.sdk.messages.Message;
import baas.sdk.messages.MessageJson;
import baas.sdk.messages.MessageJsonList;

public interface ISDKJson {
	public abstract MessageJson getJson(int jsonId);
	
	public abstract MessageJsonList getJsonList(String querry, int pagesize, int page);
	
	public abstract MessageJson setJson(String json);
	
	public abstract Message deleteJson(int jsonId);
	
	public abstract MessageJson updateJson(int jsonId, String json);
}
