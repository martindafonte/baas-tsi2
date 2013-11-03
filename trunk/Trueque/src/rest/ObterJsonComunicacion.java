package rest;

import com.trueque.VerTruequeActivity;
import com.trueque.VerTruequesActivity;


import sdk.SdkJson;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

public class ObterJsonComunicacion extends AsyncTask  <String,Integer,Boolean> {
	
	static public String trueque;
	public Context context;
	public ObterJsonComunicacion(Context context) {
		super();
		this.context = context;
	}

	@Override
	protected Boolean doInBackground(String... params) {
		// TODO Auto-generated method stub
		
		int id = Integer.parseInt(params[0]);
		SdkJson j = new SdkJson();
		trueque = j.obtenerJson(0,id);
		return null ;
	}

	@Override
	protected void onPostExecute(Boolean result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		
		Intent i = new Intent(this.context.getApplicationContext(),VerTruequeActivity.class);
		this.context.startActivity(i);
		super.onPostExecute(result);
		
		
		
	}


}
