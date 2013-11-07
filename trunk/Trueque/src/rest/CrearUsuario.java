package rest;


import com.trueque.MainActivity;

import baas.sdk.Factory;
import baas.sdk.SDKJson;
import baas.sdk.messages.Message;
import baas.sdk.utils.Constants;
import baas.sdk.utils.User;
import baas.sdk.utils.exceptions.NotInitilizedException;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;

public class CrearUsuario extends AsyncTask<User, String, Boolean> {

	private ProgressDialog dialog;
	private Context context;
	private baas.sdk.ISDKUser sdkUser;
	private Object MODE_PRIVATE;

	public CrearUsuario(Context c) {
		super();
		context = c;
		dialog = new ProgressDialog(context);
		
	}
	
	@Override
	protected Boolean doInBackground(User... usuarios) {
		Factory.initialize(1, context);
		try {
			sdkUser = Factory.getUserSDK();
		} catch (NotInitilizedException e) {
		}
		User u = usuarios[0];
		Message m = sdkUser.register(u);
		if (m.codigo == Constants.Exito){
			m = sdkUser.login(u.Nick, u.Password);
			if (m.codigo == Constants.Exito){
				// guardo el nick
				
				SharedPreferences sharedPref = context.getSharedPreferences("claves", Context.MODE_PRIVATE);
				SharedPreferences.Editor editor = sharedPref.edit();
				editor.putString(Constants.nickapp, u.Nick);
				editor.commit();		
			}
		}
		return null;
	}
		

	@Override
	protected void onPostExecute(Boolean result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		Intent i = new Intent(this.context.getApplicationContext(),MainActivity.class);
		this.context.startActivity(i);
	}

}
