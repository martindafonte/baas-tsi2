package rest;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import baas.sdk.Factory;
import baas.sdk.messages.MessageJson;
import baas.sdk.messages.MessageJsonList;
import baas.sdk.utils.Constants;
import baas.sdk.utils.exceptions.NotInitilizedException;

import com.trueque.MainActivity;
//import com.trueque.VerTruequesActivity;

public class ListarComunicacion{

	
	private Context context;
	public JSONArray array;
	private baas.sdk.ISDKJson sdkJson;
	private String nick;

	
	
	public ListarComunicacion(Context c,String nick) {
		super();
		context = c;
		this.nick = nick;
	}
	
//	@Override
//	protected void onPostExecute(Boolean result) {
////		Intent i = new Intent(this.context.getApplicationContext(),VerTruequesActivity.class);
////		this.context.startActivity(i);
//		super.onPostExecute(result);
//	}
//
//	@Override
//	protected void onPreExecute() {
//		super.onPreExecute();		
//	}

//	@Override
	public Boolean doInBackground(String... params) {
		Factory.initialize(1, context);
		try {
			sdkJson = Factory.getJsonSDK();
		} catch (NotInitilizedException e) {
		}
		
		
		JSONObject json = new JSONObject();
		try {
			json.put(Constants.jsonTipoMongo, params[0]);
			
			if (nick != null){
				json.put(Constants.nickapp, nick);
			}
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		try {	
			MessageJsonList mj = sdkJson.getJsonList(json,0,10);
		    array =mj.resultList;
		    
		    // LLenar
		    if(array == null){
		       	return null;
		    }
		    MainActivity.trueques = new String[array.length()];

	        for(int i=0;i<MainActivity.trueques.length;i++) {
//	        		JSONObject query = new JSONObject();
//	        		query.put("imagenId",array.getJSONObject(i).getString(Constants.json_id_imagen_chica));
//					MessageJsonList mjimagen = sdkJson.getJsonList(query, 0, 1);
//	        		MessageJson mjimagen = sdkJson.getJsonFromCache(23);
//					JSONObject j = (JSONObject) mjimagen.resultList.get(0);
	        		MessageJson mjimagen = sdkJson.getJsonFromCacheWithId("imagenId",array.getJSONObject(i).getString(Constants.json_id_imagen_chica));
	        		JSONObject j = mjimagen.json;
					array.getJSONObject(i).put("Imagen", j.getString("Imagen"));
					MainActivity.trueques[i] = array.getString(i);
	        }
			
		} catch (ClientProtocolException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		return null;
	}	

}

