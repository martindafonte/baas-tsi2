package rest;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import sdk.SdkJson;

import com.trueque.MainActivity;
import com.trueque.R;
import com.trueque.VerTruequeActivity;
import com.trueque.VerTruequesActivity;


import com.trueque.VerTruequesActivity.adaptarElemento;

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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class ListarComunicacion extends AsyncTask <String,Integer,Boolean> {

	private ProgressDialog dialog;
	private Context context;
	public JSONArray array;

	
	
	public ListarComunicacion(Context c) {
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
		  
		Intent i = new Intent(this.context.getApplicationContext(),VerTruequesActivity.class);
		this.context.startActivity(i);
		super.onPostExecute(result);
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		this.dialog.setMessage("Cargando Trueques");
		this.dialog.show();
		
		
	}

	@Override
	protected Boolean doInBackground(String... params) {
		// TODO Auto-generated method stub
		SdkJson j = new SdkJson();
		JSONObject json = new JSONObject();
		try {
			json.put("tipoObjeto", params[0]);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String respuesta;
		try {
		    
		    array = j.obtenerLista(json,0,0,10);
		    
		    // LLenar
		    MainActivity.trueques = new String[array.length()];

	        for(int i=0;i<MainActivity.trueques.length;i++) {
	        	
					MainActivity.trueques[i] = array.getString(i);
	        }
			
		} catch (ClientProtocolException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return null;
	}
	

	
	// ********* ADAPTAR ITEM ***************
	public class adaptarElemento extends ArrayAdapter<Object>{
		Activity context;
		
		public adaptarElemento(Activity c) {
			// TODO Auto-generated constructor stub
			super(c, R.layout.ver_trueques,MainActivity.trueques);
			this.context = c;
	
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
		
			LayoutInflater i = context.getLayoutInflater();
			View item = i.inflate(R.layout.item_trueque, null);
			
			
			JSONObject j;
			try {
				j = new JSONObject(MainActivity.trueques[position]);
				
				TextView Tipo = (TextView) item.findViewById(R.id.title);
				Tipo.setText(j.getString("Tipo"));
				
				//TextView Valor = (TextView) item.findViewById(R.id.secondLine);
				//Valor.setText(j.getString("Valor"));
				
				TextView Descripcion = (TextView) item.findViewById(R.id.artist);
				Descripcion.setText(j.getString("Descripcion"));
				
				ImageView imagen = (ImageView) item.findViewById(R.id.list_image);
				
				byte [] encodeByte=Base64.decode(j.getString("imagen"),Base64.DEFAULT);
  		        Bitmap bitmap=BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
				imagen.setImageBitmap(bitmap);
  		        
				return item;	
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			return null;
		}
		
	}

}

