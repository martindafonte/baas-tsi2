package rest;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import baas.sdk.Factory;
import baas.sdk.messages.MessageJson;
import baas.sdk.messages.MessageJsonList;
import baas.sdk.utils.Constants;
import baas.sdk.utils.exceptions.NotInitilizedException;

import com.trueque.MainActivity;
//import com.trueque.VerTruequesActivity;

public class ListarOfertas extends AsyncTask <String,Integer,Boolean> {

	private ProgressDialog dialog;
	private Context context;
	public JSONArray array;
	private baas.sdk.ISDKJson sdkJson;
	private String idTrueque;

	
	
	public ListarOfertas(Context c,String idTrueque) {
		super();
		context = c;
		this.idTrueque = idTrueque;
		dialog = new ProgressDialog(context);
		System.out.println("****** listar ofertas**********");
		
	}
	
//	@Override
//	protected void onPostExecute(Boolean result) {
//		if (dialog.isShowing()){
//			dialog.dismiss();
//		}
////		Intent i = new Intent(this.context.getApplicationContext(),VerTruequesActivity.class);
////		this.context.startActivity(i);
//		super.onPostExecute(result);
//	}

//	@Override
//	protected void onPreExecute() {
//		// TODO Auto-generated method stub
//		super.onPreExecute();
//		this.dialog.setMessage("Cargando Trueques");
//		this.dialog.show();
//		
//		
//	}

	@Override
	public Boolean doInBackground(String... params) {
		Factory.initialize(1, context);
		try {
			sdkJson = Factory.getJsonSDK();
		} catch (NotInitilizedException e) {
		}
		
		JSONObject json = new JSONObject();
		try {
			json.put(Constants.jsonTipoMongo, params[0]);
			json.put("idTrueque", idTrueque);
			
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		try {	
			MessageJsonList mj = sdkJson.getJsonList(json,0,10);
		    array =mj.resultList;
		    // LLenar
		    
		    MainActivity.ofertas = new String[array.length()];

	        for(int i=0;i<MainActivity.ofertas.length;i++) {
	        	try{
	        		JSONObject query = new JSONObject();
	        		query.put("imagenId",array.getJSONObject(i).getString(Constants.json_id_imagen_chica));
	        		query.put("TipoObjeto", "ImagenChica");
					MessageJsonList mjimagen = sdkJson.getJsonList(query, 0, 1);
					JSONObject j = (JSONObject) mjimagen.resultList.get(0);
					array.getJSONObject(i).put("Imagen", j.getString("Imagen"));
				//	array.getJSONObject(i).put("Imagen", "");
					MainActivity.ofertas[i] = array.getString(i);
	        	}catch(Exception e){
	        		
	        	}
	        }
			
		} catch (ClientProtocolException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		} //catch (JSONException e1) {
//			e1.printStackTrace();
//		}
		return null;
	}	

}

