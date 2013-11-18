package rest;

import org.json.JSONException;
import org.json.JSONObject;

//import com.trueque.VerTruequeActivity;
//import com.trueque.VerTruequesActivity;



import com.trueque.IngresarTrueque;
import com.trueque.R;
import com.trueque.VerTruequeActivity;

import baas.sdk.Factory;
import baas.sdk.messages.MessageJson;
import baas.sdk.messages.MessageJsonList;
import baas.sdk.utils.Constants;
import baas.sdk.utils.exceptions.NotInitilizedException;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

public class ImagenGrande extends AsyncTask< String, Integer, String> {
	
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
	public String doInBackground(String... params) {
		try{
			
			
			Factory.initialize(1, context);
			try {
				sdkJson = Factory.getJsonSDK();
			} catch (NotInitilizedException e) {
			}
			id = params[1];
			JSONObject query = new JSONObject();
			query.put("imagenId", params[0]);
			mjimagen = sdkJson.getJsonList(query, 0, 1);
			return mjimagen.resultList.getJSONObject(0).getString("Imagen");
			
			
		}catch(Exception e){	
			e.printStackTrace();
			return null;
		}

	}

//	@Override
//	protected void onPostExecute(Boolean result) {
//		
		
//		
//		Intent i = new Intent(this.context.getApplicationContext() , VerTruequeActivity.class);
//        i.putExtra("id", id);
//        try {
//			i.putExtra("imagen", mjimagen.resultList.getJSONObject(0).getString("Imagen"));
//		} catch (JSONException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//       this.context.startActivity(i);	
		
//		super.onPostExecute(result);
//	}

}
