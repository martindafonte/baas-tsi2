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

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import baas.sdk.datos.BaasDbHelper;
import baas.sdk.datos.BaasContract.ColaSinc;
import baas.sdk.messages.Message;
import baas.sdk.messages.MessageJson;
import baas.sdk.messages.MessageJsonList;
import baas.sdk.utils.Constants;
import baas.sdk.utils.Helper_Http;

public class SDKJson implements ISDKJson {

	private DefaultHttpClient l_httpClient;
	private String l_appid;
	static String l_baseURL;
	private Context l_ctx;
	
	public SDKJson(long p_appid, Context ctx) {
		l_appid = String.valueOf(p_appid);
		l_baseURL = Constants.baseURL + "/MongoServicios";
		l_httpClient = new DefaultHttpClient();
		l_ctx = ctx;
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
	public Message addJson(JSONObject data, boolean save_offline) {
		Message mj = new Message();
		if (!save_offline ||isNetworkAvailable()) {
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
		} else{
			if(saveInBD(Constants.create, data.toString(), "0")){
				mj.codigo=Constants.Exito;
				mj.descripcion="Guardado en la BD para enviar luego";
			}else{
				mj.codigo = Constants.JSON_Error_Saving_BD;
				mj.descripcion ="Ocurrio un error al guardar en la bd";
			}
		}
		return mj;
	}

	@Override
	public Message updateJson(int jsonId, JSONObject json, boolean save_offline) {
		Message mj = new Message();
		if (!save_offline ||isNetworkAvailable()) {
			try {
				HttpPut put = new HttpPut(l_baseURL
						+ "/"
						+ String.valueOf(l_appid + "/"
								+ Integer.toString(jsonId)));
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
		} else {
			if(saveInBD(Constants.update, json.toString(), String.valueOf(jsonId))){
				mj.codigo=Constants.Exito;
				mj.descripcion="Guardado en la BD para enviar luego";
			}else{
				mj.codigo = Constants.JSON_Error_Saving_BD;
				mj.descripcion ="Ocurrio un error al guardar en la bd";
			}
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
			HttpPost post = new HttpPost(l_baseURL + "/" + appidString
					+ "/listaJson/" + desdeString + "/" + cantString);
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

	private boolean isNetworkAvailable() {
		ConnectivityManager connectivityManager = (ConnectivityManager) Factory
				.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager
				.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}

	private boolean saveInBD(String accion, String json, String previousId) {
		// Gets the data repository in write mode
		BaasDbHelper dbHelper = new BaasDbHelper(l_ctx);
		SQLiteDatabase db = dbHelper.getWritableDatabase();

		// Create a new map of values, where column names are the keys
		ContentValues values = new ContentValues();
		values.put(ColaSinc.COLUMN_NAME_ACCION, accion);
		values.put(ColaSinc.COLUMN_NAME_JSON, json);
		values.put(ColaSinc.COLUMN_NAME_ITEM_ID, previousId);
		// Insert the new row, returning the primary key value of the new row
		long newRowId;
		newRowId = db.insert(ColaSinc.TABLE_NAME, null, values);
		return (newRowId > -1);
	}

	
}
