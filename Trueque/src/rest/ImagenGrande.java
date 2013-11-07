package rest;

import org.json.JSONException;
import org.json.JSONObject;

import com.trueque.VerTruequeActivity;
import com.trueque.VerTruequesActivity;

import baas.sdk.Factory;
import baas.sdk.messages.MessageJson;
import baas.sdk.messages.MessageJsonList;
import baas.sdk.utils.Constants;
import baas.sdk.utils.exceptions.NotInitilizedException;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

public class ImagenGrande extends AsyncTask< String, Integer, Boolean> {
	
	private ProgressDialog dialog;
	private Context context;
	private baas.sdk.ISDKJson sdkJson;
	MessageJsonList mjimagen;
	
	private String id;
	
	public ImagenGrande(Context c) {
		super();
		context = c;
		dialog = new ProgressDialog(context);
		
	}

	@Override
	protected Boolean doInBackground(String... params) {
		try{
			
			
			Factory.initialize(1, context);
			try {
				sdkJson = Factory.getJsonSDK();
			} catch (NotInitilizedException e) {
			}
			id = params[1];
			JSONObject query = new JSONObject();
			query.put("imagenId", params[0]);
//			//imagenGrande.put("Imagen",Constants.json_imagen_grande);
			mjimagen = sdkJson.getJsonList(query, 0, 1);
			
			
			
		}catch(Exception e){	
			e.printStackTrace();
		}
		return null;
	}

	@Override
	protected void onPostExecute(Boolean result) {
		// TODO Auto-generated method stub
		
		Intent i = new Intent(this.context.getApplicationContext() , VerTruequeActivity.class);
        i.putExtra("id", id);
        try {
			i.putExtra("imagen", mjimagen.resultList.getJSONObject(0).getString("Imagen"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
       this.context.startActivity(i);	
		
		super.onPostExecute(result);
	}

}
