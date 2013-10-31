package rest;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.json.JSONObject;

import sdk.IngresarJson;

import com.trueque.MainActivity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

public class IngresarComunicacion extends AsyncTask<String,Integer,Boolean> {
	

	private Context context;
	
	public IngresarComunicacion(Context c) {
		super();
		context = c;
	}

	@Override
	protected void onPostExecute(Boolean result) {
		// TODO Auto-generated method stub
		
		Intent i = new Intent(this.context.getApplicationContext(),MainActivity.class);
		this.context.startActivity(i);
		super.onPostExecute(result);
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
	}

	@Override
	protected Boolean doInBackground(String... params) {
		// TODO Auto-generated method stub

			IngresarJson j = new IngresarJson();			
			try {
				
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
				j.Ingresar(dato,0);
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return true;
	}
	
}