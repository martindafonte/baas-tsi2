package rest;

import org.json.JSONException;
import org.json.JSONObject;

import sdk.SdkJson;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import com.trueque.MainActivity;

public class EliminarComunicacion  extends AsyncTask<String,Integer,Boolean> {

	private ProgressDialog dialog;
	private Context context;
	
	public EliminarComunicacion(Context c) {
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

			SdkJson j = new SdkJson();			
			j.EliminarJson(Integer.parseInt(params[0]), 0);
			
			return true;
	}

}