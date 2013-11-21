package rest;

import java.io.IOException;
import java.sql.Date;
import java.util.Calendar;

import modelo.Oferta;
import modelo.Transaccion;
import modelo.Trueque;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.provider.ContactsContract.Contacts.Data;
import android.text.format.DateUtils;
import android.text.method.DateTimeKeyListener;
import baas.sdk.Factory;
import baas.sdk.messages.MessageJson;
import baas.sdk.messages.MessageJsonList;
import baas.sdk.utils.Constants;
import baas.sdk.utils.exceptions.NotInitilizedException;

import com.google.gson.Gson;
import com.trueque.MainActivity;


public class IngresarComunicacion extends AsyncTask<Transaccion, Integer, Boolean> {

	private ProgressDialog dialog;
	private Context context;
	private baas.sdk.ISDKJson sdkJson;

	public IngresarComunicacion(Context c) {
		super();
		context = c;
		dialog = new ProgressDialog(context);
		
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

	//@Override
	protected Boolean doInBackground(Transaccion... objetos) {
		try {
			Factory.initialize(1, context);
			try {
				sdkJson = Factory.getJsonSDK();
			} catch (NotInitilizedException e) {
			}
			
			Transaccion t = objetos[0];
			int tiempo = Calendar.getInstance().get(Calendar.SECOND);
			// guardar imagen en 2 tamanios.
			JSONObject imagenChica = new JSONObject();
			imagenChica.put(Constants.jsonTipoMongo,Constants.json_imagen_chica);
			imagenChica.put("Imagen",t.imagenChica);
			imagenChica.put("imagenId","chica"+t.nick+tiempo);
			MessageJson mjImagenChica = sdkJson.addJson(imagenChica,true);
			
			JSONObject imagenGrande = new JSONObject();
			imagenGrande.put(Constants.jsonTipoMongo, Constants.json_imagen_grande);
			imagenGrande.put("Imagen",t.imagenGrande);
			imagenGrande.put("imagenId","grande"+t.nick+tiempo);
			MessageJson mjImagenGrande = sdkJson.addJson(imagenGrande,true);
			
			t.id_imagenChica = "chica"+t.nick+tiempo;
			t.id_imagenGrande = "grande"+t.nick+tiempo;
			Gson g = new Gson();
			t.imagenChica = "";
			t.imagenGrande = "";
			JSONObject dato = new JSONObject(g.toJson(t));
					
			
			if (t.TipoObjeto == Constants.tipoOferta){
				Oferta o = (Oferta) t;
				try {
					//Resources res = getResources();
					//String[] categorias = res.getStringArray(R.array.array_categorias);
//					JSONObject j = new JSONObject();
//					j.put("_id", Integer.parseInt(o.idTrueque));
//					String[] s = new String[1];
//					s[0] = "nick";
					MessageJson mj = sdkJson.getJson(Integer.parseInt(o.idTrueque));
					o.nickDestinatario = mj.json.getString("nick");
					dato.put("nickDestinatario", mj.json.getString("nick"));
					Factory.getPushSDK().sendToUser(o.nickDestinatario, "El usuario "+o.nick +" ha ofertado por en tu trueque.");
				} catch (NotInitilizedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			sdkJson.addJson(dato,true);
			
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}

}