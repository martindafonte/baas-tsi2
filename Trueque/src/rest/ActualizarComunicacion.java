package rest;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.json.JSONObject;



import baas.sdk.Factory;
import baas.sdk.utils.exceptions.NotInitilizedException;

import com.trueque.MainActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

public class ActualizarComunicacion extends AsyncTask<String,Integer,Boolean> {

	private ProgressDialog dialog;
	private Context context;
	private baas.sdk.ISDKJson sdkJson;
	
	public ActualizarComunicacion(Context c) {
		super();
		context = c;
		dialog = new ProgressDialog(context);
		Factory.initialize(0, c);
		try {
			sdkJson = Factory.getJsonSDK();
		} catch (NotInitilizedException e) {
		}
	}

	@Override
	protected void onPostExecute(Boolean result) {
		// TODO Auto-generated method stub
		if (dialog.isShowing()){
			dialog.dismiss();
		}
		// Llamar a ver perfil.
		
		Intent i = new Intent(this.context.getApplicationContext(),MainActivity.class);
		this.context.startActivity(i);
		super.onPostExecute(result);
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		this.dialog.setMessage("Guardando");
		this.dialog.show();
		super.onPreExecute();
		super.onPreExecute();
	}

	@Override
	protected Boolean doInBackground(String... params) {
		// TODO Auto-generated method stub
	
			JSONObject dato = new JSONObject();
		    try {
		    	dato.put("tipoObjeto", params[0]);
				dato.put("Tipo",params[1] );
				dato.put("Valor", params[2]);
				dato.put("Descripcion", params[3]);
				dato.put("imagen", params[4]);		
		    }catch(JSONException e){
		    	e.printStackTrace();
		    }
			sdkJson.updateJson(Integer.parseInt(params[5]), dato);
			
			return true;
	}

}
