package rest;


import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import baas.sdk.Factory;
import baas.sdk.messages.MessageJsonList;
import baas.sdk.utils.Constants;
import baas.sdk.utils.exceptions.NotInitilizedException;

import com.google.gson.JsonObject;
import com.trueque.MainActivity;

public class EliminarComunicacion  extends AsyncTask<JSONObject,Integer,Boolean> {

	private ProgressDialog dialog;
	private Context context;
	private baas.sdk.ISDKJson sdkJson;
	
	public EliminarComunicacion(Context c) {
		super();
		context = c;
		dialog = new ProgressDialog(context);
		
	}

	@Override
	protected void onPostExecute(Boolean result) {
		
		// Llamar a ver perfil.
		
		Intent i = new Intent(this.context.getApplicationContext(),MainActivity.class);
		this.context.startActivity(i);
		super.onPostExecute(result);
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		this.dialog.setMessage("Eliminando");
		this.dialog.show();
		super.onPreExecute();
		super.onPreExecute();
	}

	@Override
	protected Boolean doInBackground(JSONObject... params) {
		try{
			Factory.initialize(1, context);
			try {
				sdkJson = Factory.getJsonSDK();
			} catch (NotInitilizedException e) {
			}
			if (dialog.isShowing()){
				dialog.dismiss();
			}
			JSONObject j = params[0];
			String idImagenChica = j.getString(Constants.json_id_imagen_chica);
			String idImagenGrande = j.getString(Constants.json_id_imagen_grande);
			// Pedir ids y imagen id
			JSONObject j2 = new JSONObject();
			j2.put("imagenId", idImagenChica);
			j2.put("TipoObjeto", "ImagenChica");
			String[] s = new String[1];
			s[0] = "_id";
			MessageJsonList m = sdkJson.getJsonListSelection(j2, s, 0, 1);
			int imchicha = Integer.parseInt((m.resultList.getJSONObject(0)).getString("_id"));
			sdkJson.deleteJson(imchicha);
			j2 = new JSONObject();
			j2.put("imagenId", idImagenGrande);
			j2.put("TipoObjeto", "ImagenGrande");
			s = new String[1];
			s[0] = "_id";
			m = sdkJson.getJsonListSelection(j2, s, 0, 1);
			int imgrande = Integer.parseInt((m.resultList.getJSONObject(0)).getString("_id"));
			
			sdkJson.deleteJson(imgrande);
			sdkJson.deleteJson(j.getInt(Constants.jsonidMongo));
			
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}

}