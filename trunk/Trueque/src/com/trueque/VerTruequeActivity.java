package com.trueque;

import org.json.JSONException;
import org.json.JSONObject;

import rest.IngresarComunicacion;
import rest.ListarComunicacion;
import rest.ObterJsonComunicacion;
import android.os.Bundle;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class VerTruequeActivity extends Activity {
	
	public static String trueque;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ver_trueque);
		
		try{
		
		// Buscar item
		
		Bundle bundle = getIntent().getExtras();
		String id = bundle.getString("id");
		
		int indice = -1;
		JSONObject j;
		for(int i = 0; i < MainActivity.trueques.length;i++){
			try {
				j = new JSONObject(MainActivity.trueques[i]);
				if (j.get("_id").toString().equals(id)){
					indice = i;
					break;
				}	
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				j = null;
				e.printStackTrace();
			}
			
		} 
		
		
		
		
		
	//	JSONObject mensaje = new JSONObject(ObterJsonComunicacion.trueque);
		//JSONObject j = new JSONObject(mensaje.getString("json"));
			j = new JSONObject(MainActivity.trueques[indice]);
		TextView Tipo = (TextView)findViewById(R.id.textView2);
		Tipo.setText(j.getString("Tipo"));
		
		TextView Valor = (TextView)findViewById(R.id.valor_item);
		Valor.setText(j.getString("Valor"));
		
		TextView Descripcion = (TextView) findViewById(R.id.descripcion);
		Descripcion.setText(j.getString("Descripcion"));
		
		ImageView imagen = (ImageView)findViewById(R.id.Preview);
		
		byte [] encodeByte=Base64.decode(j.getString("imagen"),Base64.DEFAULT);
	        Bitmap bitmap=BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
		imagen.setImageBitmap(bitmap);
		
		}catch(JSONException e){
			e.printStackTrace();
		}
		
	}



}
