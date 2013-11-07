package com.trueque;

import org.json.JSONException;
import org.json.JSONObject;

import baas.sdk.utils.Constants;
import rest.ImagenGrande;
import rest.ObterJsonComunicacion;
import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class VerTruequesActivity extends Activity {
	
	//private TextView tv1;
	private ListView lv1;
	private adaptarElemento adapter;
	private String idimagen;
	private String idtrueque;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ver_trueques);
		
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);

        lv1 =(ListView)findViewById(R.id.list);  
        adapter = new adaptarElemento(this);
        
        lv1.setEmptyView(findViewById(R.id.empty));
        lv1.setAdapter(adapter);
        
        lv1.setOnItemClickListener(new OnItemClickListener() {
        	String id;
            public void onItemClick(AdapterView<?> myAdapter, View myView, int myItemInt, long mylng) {
              String selectedFromList =(String) (lv1.getItemAtPosition(myItemInt));
              try {
				JSONObject json = new JSONObject(selectedFromList);
				idimagen = json.getString(Constants.json_id_imagen_grande);
				idtrueque = json.getString(Constants.jsonidMongo);
				Buscarfoto();
			} catch (JSONException e) {
				e.printStackTrace();
			}
              
            }

			private void Buscarfoto() {
				ImagenGrande imagenGrande = new ImagenGrande(VerTruequesActivity.this);
				imagenGrande.execute(idimagen,idtrueque);
			}                 
      });
    
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.ver_trueques, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items
	    switch (item.getItemId()) {
	    	case android.R.id.home:
	    		Intent i = new Intent(this, MainActivity.class );
	            startActivity(i);
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	
	// ********* ADAPTAR ITEM ***************
	public class adaptarElemento extends ArrayAdapter<Object>{
		Activity context;
		String[] categorias; 
		
		
		public adaptarElemento(Activity c) {
			super(c, R.layout.ver_trueques,MainActivity.trueques);
			this.context = c;
			Resources res = getResources();
			categorias = res.getStringArray(R.array.array_categorias);	
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
		
			LayoutInflater i = context.getLayoutInflater();
			View item = i.inflate(R.layout.item_trueque, null);
			JSONObject j;
			try {
				j = new JSONObject(MainActivity.trueques[position]);			
				TextView Tipo = (TextView) item.findViewById(R.id.title);				
				Tipo.setText(categorias[Integer.parseInt(j.getString("tipo"))]);
				
//				TextView Valor = (TextView) item.findViewById(R.id.secondLine);
//				Valor.setText(j.getString("Valor"));
				
				TextView Descripcion = (TextView) item.findViewById(R.id.artist);
				Descripcion.setText(j.getString("descripcion"));
				
				ImageView imagen = (ImageView) item.findViewById(R.id.list_image);
				if (!j.getString("Imagen").equals("")){
					byte [] encodeByte=Base64.decode(j.getString("Imagen"),Base64.DEFAULT);
	  		        Bitmap bitmap=BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
	  		        imagen.setImageBitmap(bitmap);
				}
					
				return item;	
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			return null;
		}
		
	}

}
