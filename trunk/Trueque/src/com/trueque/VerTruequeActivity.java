package com.trueque;

import org.json.JSONException;
import org.json.JSONObject;

import rest.EliminarComunicacion;
import rest.IngresarComunicacion;
import rest.ListarComunicacion;
import rest.ObterJsonComunicacion;
import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class VerTruequeActivity extends Activity {
	
	public static String trueque;
	public int indice;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ver_trueque);
		
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		
		
		try{
		
		Bundle bundle = getIntent().getExtras();
		String id = bundle.getString("id");
		
		indice = -1;
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
		
		j = new JSONObject(MainActivity.trueques[indice]);
		TextView Tipo = (TextView)findViewById(R.id.titulo);
		Tipo.setText(j.getString("Tipo"));
		
//		TextView Valor = (TextView)findViewById(R.id.imagen);
//		Valor.setText(j.getString("Valor"));
		
		TextView Descripcion = (TextView) findViewById(R.id.descripcion1);
		Descripcion.setText(j.getString("Descripcion"));
		
		ImageView imagen = (ImageView)findViewById(R.id.imagen);
		
		byte [] encodeByte=Base64.decode(j.getString("imagen"),Base64.DEFAULT);
	        Bitmap bitmap=BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
		imagen.setImageBitmap(bitmap);
		
		}catch(JSONException e){
			e.printStackTrace();
		}
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu); 
		MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.menu_trueque, menu);
	    return true;
		
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items

		Intent i;
	    switch (item.getItemId()) {
	    	case android.R.id.home:
	    		// Volver!
	    		 i = new Intent(this, VerTruequesActivity.class );
	            startActivity(i);
	    	case R.id.editar:
	    		 i = new Intent(this, EditarTrueque.class );
	    		 i.putExtra("trueque", MainActivity.trueques[indice]);
	    		 startActivity(i);
	            //openSearch();
	            return true;
	    	case R.id.borrar:
	    		 EliminarComunicacion eliminar = new EliminarComunicacion(this);
	    		 eliminar.execute(Integer.toString(indice));
            
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}

}
