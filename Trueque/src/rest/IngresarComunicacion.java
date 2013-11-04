package rest;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import baas.sdk.Factory;
import baas.sdk.utils.exceptions.NotInitilizedException;

import com.trueque.MainActivity;

public class IngresarComunicacion extends AsyncTask<String, Integer, Boolean> {

	private ProgressDialog dialog;
	private Context context;
	private baas.sdk.ISDKJson sdkJson;

	public IngresarComunicacion(Context c) {
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
		if (dialog.isShowing()) {
			dialog.dismiss();
		}
		Intent i = new Intent(this.context.getApplicationContext(),
				MainActivity.class);
		this.context.startActivity(i);
		super.onPostExecute(result);
	}

	@Override
	protected void onPreExecute() {
		this.dialog.setMessage("Guardando");
		this.dialog.show();
		super.onPreExecute();
		super.onPreExecute();
	}

	@Override
	protected Boolean doInBackground(String... params) {
		try {
			JSONObject dato = new JSONObject();
			try {
				dato.put("tipoObjeto", params[0]);
				dato.put("Tipo", params[1]);
				dato.put("Valor", params[2]);
				dato.put("Descripcion", params[3]);
				dato.put("imagen", params[4]);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			sdkJson.addJson(dato);
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