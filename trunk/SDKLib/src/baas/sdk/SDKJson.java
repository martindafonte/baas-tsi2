package baas.sdk;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;
import org.apache.http.NameValuePair;

import com.example.android.network.sync.basicsyncadapter.util.SelectionBuilder;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import baas.sdk.datos.BaasContract.Cache;
import baas.sdk.datos.BaasContract;
import baas.sdk.datos.BaasDbCache;
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
		l_baseURL = Factory.baseURL + "/MongoServicios";
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
	public MessageJson addJson(JSONObject data,boolean save_offline) {
		MessageJson mj = new MessageJson();
		
		boolean a = isNetworkAvailable();
		if (!save_offline ||a) {
		try {
			HttpPost post = new HttpPost(l_baseURL + "/" + l_appid);
			post.setHeader("content-type", "application/json");
			StringEntity entity = new StringEntity(data.toString());
			post.setEntity(entity);
			HttpResponse resp = l_httpClient.execute(post);
			JSONObject obj = Helper_Http.obtenerJSONRespuesta(resp);
			
			mj.codigo = Helper_Http.obtenerCodigo(obj);
			mj.descripcion = Helper_Http.obtenerDescripcion(obj);
			mj.jsonid = Helper_Http.obtenerJsonId(obj);
			mj.json = null;
		} catch (Exception e) {
			mj.codigo = Constants.JSON_Exception;
			mj.descripcion = e.getMessage();
			mj.jsonid = -1;
			mj.json = null;
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
		if (!save_offline || isNetworkAvailable()) {
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
		} 
			if(saveInBD(Constants.update, json.toString(), String.valueOf(jsonId))){
				mj.codigo=Constants.Exito;
				mj.descripcion="Guardado en la BD";
			}else{
				mj.codigo = Constants.JSON_Error_Saving_BD;
				mj.descripcion ="Ocurrio un error al guardar en la bd";
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
			String a = obj.getString(Constants.json); 
			if ( obj.getString(Constants.json).equals("null"))
				mj.resultList = new JSONArray();
			else
				mj.resultList = new JSONArray(obj.get(Constants.json).toString());
			
		} catch (Exception e) {
			mj.codigo = Constants.JSON_Exception;
			mj.descripcion = e.getMessage();
		}
		return mj;
	}
	
	public Query getQuery(JSONObject consulta, int cantPorPagina){
		Query q = new Query(consulta, cantPorPagina,l_appid,l_baseURL);
		return q;
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

	
	public MessageJsonList getJsonListSelection(JSONObject querry, String[] campos, int from, int cant) {
		MessageJsonList mj = new MessageJsonList();
		try {
			String appidString = l_appid;
			String desdeString = Integer.toString(from);
			String cantString = Integer.toString(cant);
			HttpPost post = new HttpPost(l_baseURL + "/" + appidString
					+ "/listaJsonCampos/" + desdeString + "/" + cantString);
			//post.setHeader("content-type", "application/json");
			//StringEntity entity = new StringEntity(querry.toString());
			
		//	post.setEntity(entity);
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
			nameValuePairs.add(new BasicNameValuePair("filtro", querry.toString()));
		    
		    JSONArray j = new JSONArray();
		    for (int i = 0; i< campos.length; i++){
		    	j.put(campos[i]);
		    }
		    JSONObject json = new JSONObject();
		    json.put("select", j);
		    nameValuePairs.add(new BasicNameValuePair("campos", json.toString()));

		    post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			
			
			HttpResponse resp = l_httpClient.execute(post);
			JSONObject obj = Helper_Http.obtenerJSONRespuesta(resp);
			mj.codigo = Helper_Http.obtenerCodigo(obj);
			mj.descripcion = Helper_Http.obtenerDescripcion(obj);
			obj.getString(Constants.json); 
			if ( obj.getString(Constants.json).equals("null"))
				mj.resultList = new JSONArray();
			else
				mj.resultList = new JSONArray(obj.get(Constants.json).toString());
			
		} catch (Exception e) {
			mj.codigo = Constants.JSON_Exception;
			mj.descripcion = e.getMessage();
		}
		return mj;
	}
	
    private static final String[] PROJECTION = new String[] {
    	Cache._ID,
    	Cache.COLUMN_NAME_ITEM_ID,
    	Cache.COLUMN_NAME_JSON
    };
    public static final int COLUMN_ID = 0;
    public static final int COLUMN_ITEM_ID = 1;
    public static final int COLUMN_JSON = 2;

	@Override
	public MessageJson getJsonFromCache(int jsonId) {
		MessageJson mj = new MessageJson();
		String json;
		try {
			BaasDbCache dbHelper = new BaasDbCache(l_ctx);
			SQLiteDatabase db = dbHelper.getWritableDatabase();
//			SelectionBuilder sb = new SelectionBuilder();
			Cursor c= db.query(Cache.TABLE_NAME, PROJECTION, Cache.COLUMN_NAME_ITEM_ID+"="+ String.valueOf(jsonId), null, null, null, null);
//		    sb.table(Cache.TABLE_NAME);
//		    sb.where(Cache.COLUMN_NAME_ITEM_ID +"=?s", );
//		    =sb.query(db, PROJECTION, Cache._ID);
			if(c.moveToFirst()){
				json =c.getString(COLUMN_JSON);
				mj.json=new JSONObject(json);
				mj.codigo=0;
			}else{
			String id = Integer.toString(jsonId);
			String app = l_appid;
			HttpGet del = new HttpGet(l_baseURL + "/" + app + "/" + id);
			del.setHeader("content-type", "application/json");
			HttpResponse resp = l_httpClient.execute(del);
			JSONObject obj = Helper_Http.obtenerJSONRespuesta(resp);
			mj.codigo = Helper_Http.obtenerCodigo(obj);
			mj.descripcion = Helper_Http.obtenerDescripcion(obj);
			json=obj.getString(Constants.json);
			mj.json = new JSONObject(json);
			//Guardo la copia en cache
			
			ContentValues values = new ContentValues();
			values.put(Cache.COLUMN_NAME_JSON,json);
			values.put(Cache.COLUMN_NAME_ITEM_ID, String.valueOf(jsonId));
			long idkey=db.insert(Cache.TABLE_NAME, null, values);
			}
			db.close();
			return mj;
		} catch (Exception e) {
			mj.codigo = Constants.JSON_Exception;
			mj.descripcion = e.getMessage();
			return mj;
		}

	}
	
	@Override
	public MessageJson getJsonFromCacheWithId(String keyname, String keyvalue) {
		MessageJson mj = new MessageJson();
		String json;
		try {
			BaasDbCache dbHelper = new BaasDbCache(l_ctx);
			SQLiteDatabase db = dbHelper.getWritableDatabase();
			Cursor c= db.query(Cache.TABLE_NAME, PROJECTION, Cache.COLUMN_NAME_ITEM_ID+"= '"+keyvalue+"'",null, null, null, null);
			if(c.moveToFirst()){
				json =c.getString(COLUMN_JSON);
				mj.json=new JSONObject(json);
				mj.codigo=0;
			}else{
				JSONObject querry = new JSONObject();
				querry.put(keyname, keyvalue);
			MessageJsonList mjl = getJsonList(querry, 0, 1);
			//Guardo la copia en cache
			mj.json = mjl.resultList.getJSONObject(0);
			if(mj.json != null){
				mj.codigo = 0;
			}else{
				mj.codigo = -1;
			}
			ContentValues values = new ContentValues();
			values.put(Cache.COLUMN_NAME_JSON,mj.json.toString());
			values.put(Cache.COLUMN_NAME_ITEM_ID, keyvalue);
			long idkey=db.insert(Cache.TABLE_NAME, null, values);
			}
			db.close();
			return mj;
		} catch (Exception e) {
			mj.codigo = Constants.JSON_Exception;
			mj.descripcion = e.getMessage();
			return mj;
		}

	}
}
