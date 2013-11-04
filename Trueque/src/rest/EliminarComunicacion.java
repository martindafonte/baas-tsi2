package rest;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import baas.sdk.Factory;
import baas.sdk.utils.exceptions.NotInitilizedException;

import com.trueque.MainActivity;

public class EliminarComunicacion  extends AsyncTask<String,Integer,Boolean> {

	private ProgressDialog dialog;
	private Context context;
	private baas.sdk.ISDKJson sdkJson;
	
	public EliminarComunicacion(Context c) {
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
		this.dialog.setMessage("Eliminando");
		this.dialog.show();
		super.onPreExecute();
		super.onPreExecute();
	}

	@Override
	protected Boolean doInBackground(String... params) {
		// TODO Auto-generated method stub

						
			sdkJson.deleteJson(Integer.parseInt(params[0]));
			
			return true;
	}

}