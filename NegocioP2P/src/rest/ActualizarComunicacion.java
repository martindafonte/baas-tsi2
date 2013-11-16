package rest;

import java.io.IOException;
import java.util.Calendar;

import modelo.Transaccion;
import modelo.Trueque;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.json.JSONObject;










import baas.sdk.Factory;
import baas.sdk.messages.MessageJson;
import baas.sdk.utils.Constants;
import baas.sdk.utils.exceptions.NotInitilizedException;

import com.google.gson.Gson;
import com.trueque.MainActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

public class ActualizarComunicacion extends AsyncTask<Trueque,Integer,Boolean> {

	private ProgressDialog dialog;
	private Context context;
	private baas.sdk.ISDKJson sdkJson;
	
	public ActualizarComunicacion(Context c) {
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
		this.dialog.setMessage("Guardando");
		this.dialog.show();
		super.onPreExecute();
		super.onPreExecute();
	}

	@Override
	protected Boolean doInBackground(Trueque... trueques) {
		Factory.initialize(1, context);
		try {
			sdkJson = Factory.getJsonSDK();
		} catch (NotInitilizedException e) {
		}
			
			Gson g = new Gson();
			Transaccion t = trueques[0];
			int tiempo = Calendar.getInstance().get(Calendar.SECOND);
			try {	
				// si no cambio la foto no actualizo
				if (!t.imagenChica.equals("")){
					JSONObject imagenChica = new JSONObject();
					imagenChica.put(Constants.jsonTipoMongo,Constants.json_imagen_chica);
					imagenChica.put("Imagen",t.imagenChica);
					imagenChica.put("imagenId","chica"+t.nick+tiempo);
					MessageJson mjImagenChica = sdkJson.addJson(imagenChica,true);
					//sdkJson.updateJson(t.id_imagenChica,imagenChica);
					
					JSONObject imagenGrande = new JSONObject();
					imagenGrande.put(Constants.jsonTipoMongo, Constants.json_imagen_grande);
					imagenGrande.put("Imagen",t.imagenGrande);
					imagenGrande.put("imagenId","grande"+t.nick+tiempo);
					MessageJson mjImagenGrande = sdkJson.addJson(imagenGrande,true);
					//sdkJson.updateJson(t.id_imagenGrande, imagenGrande);
					
					t.id_imagenChica = "chica"+t.nick+tiempo;
					t.id_imagenGrande = "grande"+t.nick+tiempo;
				}
				t.imagenChica = "";
				t.imagenGrande = "";
				JSONObject dato = new JSONObject(g.toJson(t));
				sdkJson.updateJson(t.id, dato,true);
				
		    }catch(JSONException e){
		    	e.printStackTrace();
		    } catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} 
			return true;
	}


}
