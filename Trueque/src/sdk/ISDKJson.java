package sdk;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONObject;

public interface ISDKJson {

	public abstract String Ingresar(JSONObject dato, int appid)
			throws ClientProtocolException, IOException;

	public abstract String obtenerJson(int appid, int jsonid);

	public abstract JSONArray obtenerLista(JSONObject filtro, int appid,
			int desde, int cant) throws ClientProtocolException, IOException;

	public abstract String ActualizarJson(JSONObject json, int id, int appid);

	public abstract String EliminarJson(int id, int appid);

}