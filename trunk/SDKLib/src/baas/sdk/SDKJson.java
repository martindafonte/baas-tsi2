package baas.sdk;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import baas.sdk.messages.Message;
import baas.sdk.messages.MessageJson;
import baas.sdk.messages.MessageJsonList;
import baas.sdk.utils.Constants;
import baas.sdk.utils.Helper_Http;

public class SDKJson implements ISDKJson {

	private DefaultHttpClient l_httpClient;
	private String l_appid;
	static String l_baseURL;

	public SDKJson(long p_appid) {
		l_appid = String.valueOf(p_appid);
		l_baseURL = Constants.baseURL + "/MongoServicios";
		l_httpClient = new DefaultHttpClient();
	}

	@Override
	public MessageJson getJson(int jsonId) {
		MessageJson mj = new MessageJson();
		try {
			String id = Integer.toString(jsonId);
			String app = l_appid;
			HttpGet del = new HttpGet(l_baseURL + "/" + app + "/" + id);
			del.setHeader("content-type", "application/json");
			HttpResponse resp = l_httpClient.execute(del);
			JSONObject obj = Helper_Http.obtenerJSONRespuesta(resp);
			mj.codigo = Helper_Http.obtenerCodigo(obj);
			mj.descripcion = Helper_Http.obtenerDescripcion(obj);
			mj.json = new JSONObject(obj.getString(Constants.json));
			return mj;
		} catch (Exception e) {
			mj.codigo = Constants.JSON_Exception;
			mj.descripcion = e.getMessage();
			return mj;
		}

	}

	@Override
	public Message deleteJson(int jsonId) {
		Message mj = new Message();
		try {
			HttpDelete delete = new HttpDelete(l_baseURL + "/" + l_appid + "/"
					+ Integer.toString(jsonId));
			delete.setHeader("content-type", "application/json");
			HttpResponse resp = l_httpClient.execute(delete);
			JSONObject obj = Helper_Http.obtenerJSONRespuesta(resp);
			mj.codigo = Helper_Http.obtenerCodigo(obj);
			mj.descripcion = Helper_Http.obtenerDescripcion(obj);
		} catch (Exception e) {
			mj.codigo = Constants.JSON_Exception;
			mj.descripcion = e.getMessage();
		}
		return mj;
	}

	@Override
	public Message addJson(JSONObject data) {
		Message mj = new Message();
		try {
			HttpPost post = new HttpPost(l_baseURL + "/" + l_appid);
			post.setHeader("content-type", "application/json");
			StringEntity entity = new StringEntity(data.toString());
			post.setEntity(entity);
			HttpResponse resp = l_httpClient.execute(post);
			JSONObject obj = Helper_Http.obtenerJSONRespuesta(resp);
			mj.codigo = Helper_Http.obtenerCodigo(obj);
			mj.descripcion = Helper_Http.obtenerDescripcion(obj);
		} catch (Exception e) {
			mj.codigo = Constants.JSON_Exception;
			mj.descripcion = e.getMessage();
		}
		return mj;
	}

	@Override
	public Message updateJson(int jsonId, JSONObject json) {
		Message mj = new Message();
		try {
			HttpPut put = new HttpPut(l_baseURL + "/"
					+ String.valueOf(l_appid + "/" + Integer.toString(jsonId)));
			put.setHeader("content-type", "application/json");
			StringEntity entity = new StringEntity(json.toString());
			put.setEntity(entity);
			HttpResponse resp = l_httpClient.execute(put);
			JSONObject obj = Helper_Http.obtenerJSONRespuesta(resp);
			mj.codigo = Helper_Http.obtenerCodigo(obj);
			mj.descripcion = Helper_Http.obtenerDescripcion(obj);
		} catch (Exception e) {
			mj.codigo = Constants.JSON_Exception;
			mj.descripcion = e.getMessage();
		}
		return mj;
	}

	@Override
	public MessageJsonList getJsonList(JSONObject querry, int from, int cant) {
		MessageJsonList mj = new MessageJsonList();
		try {
			String appidString = l_appid;
			String desdeString = Integer.toString(from);
			String cantString = Integer.toString(cant);
			HttpPost post = new HttpPost(l_baseURL +"/" + appidString+ "/listaJson/"
					  + desdeString + "/" + cantString);
			post.setHeader("content-type", "application/json");
			StringEntity entity = new StringEntity(querry.toString());
			post.setEntity(entity);
			HttpResponse resp = l_httpClient.execute(post);
			JSONObject obj = Helper_Http.obtenerJSONRespuesta(resp);
			mj.codigo = Helper_Http.obtenerCodigo(obj);
			mj.descripcion = Helper_Http.obtenerDescripcion(obj);
			mj.resultList = new JSONArray(obj.get(Constants.json).toString());
		} catch (Exception e) {
			mj.codigo = Constants.JSON_Exception;
			mj.descripcion = e.getMessage();
		}
		return mj;
	}

}
