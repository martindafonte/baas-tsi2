package rest;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import sdk.ObtenerListaJson;

import com.trueque.MainActivity;
import com.trueque.VerTruequesActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

public class ListarComunicacion extends AsyncTask <String,Integer,Boolean> {

	private ProgressDialog dialog;
	private Context context;
	public JSONArray array;
	
	
	public ListarComunicacion(Context c) {
		super();
		context = c;
		dialog = new ProgressDialog(context);
	}
	
	@Override
	protected void onPostExecute(Boolean result) {
		// TODO Auto-generated method stub
		if (dialog.isShowing()){
			dialog.dismiss();
		}
		
		Intent i = new Intent(this.context.getApplicationContext(),VerTruequesActivity.class);
		this.context.startActivity(i);
		super.onPostExecute(result);
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		this.dialog.setMessage("Cargando Trueques");
		this.dialog.show();
		super.onPreExecute();
	}

	@Override
	protected Boolean doInBackground(String... params) {
		// TODO Auto-generated method stub
		ObtenerListaJson j = new ObtenerListaJson();
		JSONObject json = new JSONObject();
		try {
			json.put("tipoObjeto", params[0]);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String respuesta;
		try {
			respuesta = j.obtenerLista(json,0);
			JSONObject respJSON = new JSONObject(respuesta);
		    
		    array = new JSONArray(respJSON.get("json").toString());
		    
		    // LLenar
		    MainActivity.trueques = new String[array.length()];

	        for(int i=0;i<MainActivity.trueques.length;i++) {
	        	
					MainActivity.trueques[i] = array.getString(i);
	        }
			
		} catch (ClientProtocolException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return null;
	}

}
