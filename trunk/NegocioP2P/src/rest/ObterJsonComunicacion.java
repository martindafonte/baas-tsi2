package rest;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import baas.sdk.Factory;
import baas.sdk.messages.MessageJson;
import baas.sdk.utils.exceptions.NotInitilizedException;

//import com.trueque.VerTruequeActivity;

public class ObterJsonComunicacion extends AsyncTask  <String,Integer,Boolean> {
	
	private baas.sdk.ISDKJson sdkJson;
	static public String trueque;
	public Context c;
	public ObterJsonComunicacion(Context context) {
		super();
		this.c = context;
		
		
		
	}

	@Override
	protected Boolean doInBackground(String... params) {

		Factory.initialize(0, c);
		try {
			sdkJson = Factory.getJsonSDK();
		} catch (NotInitilizedException e) {
		}
		int id = Integer.parseInt(params[0]);
		MessageJson mj = sdkJson.getJson(id);
		trueque = mj.json.toString(); 			
		return null ;
	}

	@Override
	protected void onPostExecute(Boolean result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		
//		Intent i = new Intent(this.c.getApplicationContext(),VerTruequeActivity.class);
//		this.c.startActivity(i);
		super.onPostExecute(result);
		
		
		
	}


}
