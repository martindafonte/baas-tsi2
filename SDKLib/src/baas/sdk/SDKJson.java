package baas.sdk;

import baas.sdk.messages.Message;
import baas.sdk.messages.MessageJson;
import baas.sdk.messages.MessageJsonList;

public class SDKJson implements ISDKJson {
	private long l_appid;
	public SDKJson(long p_appid){
		l_appid =p_appid;
	}
	@Override
	public MessageJson getJson(int jsonId) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public MessageJsonList getJsonList(String querry, int pagesize, int page) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public MessageJson setJson(String json) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Message deleteJson(int jsonId) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public MessageJson updateJson(int jsonId, String json) {
		// TODO Auto-generated method stub
		return null;
	}
}
