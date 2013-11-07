package baas.sdk;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONObject;

import baas.sdk.messages.Message;
import baas.sdk.messages.MessageJson;
import baas.sdk.messages.MessageJsonList;

public interface ISDKJson {
	public abstract MessageJson getJson(int jsonId);
	
	public abstract MessageJsonList getJsonList(JSONObject querry, int from, int cant) throws ClientProtocolException, IOException;
	
	public abstract MessageJson addJson(JSONObject data,boolean save_offline) throws ClientProtocolException, IOException;
	
	public abstract Message deleteJson(int jsonId);
	
	public abstract Message updateJson(int jsonId, JSONObject json,boolean save_offline);
	
	
	public abstract Query getQuery(JSONObject consulta, int cantPorPagina);
}
