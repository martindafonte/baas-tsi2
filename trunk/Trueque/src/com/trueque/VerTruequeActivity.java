package com.trueque;

import org.json.JSONException;
import org.json.JSONObject;

import com.trueque.R.menu;

import baas.sdk.messages.MessageJsonList;
import baas.sdk.utils.Constants;
import rest.EliminarComunicacion;
import rest.ListarComunicacion;
import rest.ListarOfertas;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class VerTruequeActivity extends Activity {
	
	public static String trueque;
	public int indice;
	private JSONObject j;
	String imagenGrande;
	String nick;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ver_trueque);
		
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		
		try{
		
		Bundle bundle = getIntent().getExtras();
		String id = bundle.getString("id");
		imagenGrande = bundle.getString("imagen");
		
		indice = -1;
	
		for(int i = 0; i < MainActivity.trueques.length;i++){
			try {
				j = new JSONObject(MainActivity.trueques[i]);
				if (j.get("_id").toString().equals(id)){
					indice = i;
					break;
				}	
			} catch (JSONException e) {
				j = null;
				e.printStackTrace();
			}
			
		} 
		Resources res = getResources();
		SharedPreferences sharedPref = this.getSharedPreferences("claves", Context.MODE_PRIVATE);
		nick = sharedPref.getString(Constants.nickapp, null);
		Button b = (Button)findViewById(R.id.buttonOfertar);
	
		try {
			if ((nick == null) || (nick.equals(j.getString("nick")))){
				b.setText("Ver Ofertas");
			}else{
				b.setText("Ofertar");
//				bOfertar.setVisibility(View.GONE);
			}
				
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            	try {
					if (nick.equals(j.getString("nick"))){
						verofertas();
					}else{
						
						ofertar();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        });;
		
		j = new JSONObject(MainActivity.trueques[indice]);
		TextView Tipo = (TextView)findViewById(R.id.titulo);
		Tipo.setText(res.getStringArray(R.array.array_categorias)[Integer.parseInt(j.getString("tipo"))]);
		
		TextView Valor = (TextView)findViewById(R.id.valor);
		String moneda = res.getStringArray(R.array.array_monedas)[j.getInt("moneda")];
		
		Valor.setText(moneda + " " +j.getString("valor"));
		
		TextView Descripcion = (TextView) findViewById(R.id.descripcion1);
		Descripcion.setText(j.getString("descripcion"));
		
		ImageView imagen = (ImageView)findViewById(R.id.imagen);
		
		byte [] encodeByte=Base64.decode(imagenGrande,Base64.DEFAULT);
	        Bitmap bitmap=BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
		imagen.setImageBitmap(bitmap);
		
		}catch(JSONException e){
			e.printStackTrace();
		}
		
	}
	protected void verofertas() {
		ListarOfertas jc;
		try {
			jc = new ListarOfertas(this,j.get("_id").toString());
			jc.execute("oferta");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
		
	}
	public void ofertar(){
		Intent i = new Intent(VerTruequeActivity.this, CrearOferta.class);
		try {
			i.putExtra("nick_trueque", j.getString("_id"));
			i.putExtra("nicktrueque",j.getString("nick"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
        startActivity(i);
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu); 
		MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.menu_trueque, menu);
	    
	    try {
			if (!nick.equals(j.getString("nick"))){
				menu.removeItem(R.id.editar);
		    	menu.removeItem(R.id.borrar);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
	            return true;
	    	case R.id.editar:
	    		 i = new Intent(this, IngresarTrueque.class );
	    		 i.putExtra("modo", "editar");
	    		 i.putExtra("trueque", MainActivity.trueques[indice]);
	    		 i.putExtra("imagen", imagenGrande);
	    		 startActivity(i);
	            //openSearch();
	            return true;
	    	case R.id.borrar:
	    		 EliminarComunicacion eliminar = new EliminarComunicacion(this);
	    		 eliminar.execute(j);
	    		 return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
}
