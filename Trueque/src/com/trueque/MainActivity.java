package com.trueque;

import baas.sdk.Factory;
import baas.sdk.messages.Message;
import baas.sdk.utils.Constants;
import baas.sdk.utils.exceptions.NotInitilizedException;
import rest.CrearUsuario;
import rest.ListarComunicacion;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;


public class MainActivity extends Activity implements OnItemClickListener  {
	
	public static Context c;
	public static  String trueques[];
	private DrawerLayout mDrawer;
	private ListView mDrawerOptions;
	private String[] values; 
	private String nick;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		c = this;
		
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
	
		// Buscar nick
		SharedPreferences sharedPref = this.getSharedPreferences("claves", Context.MODE_PRIVATE);
		nick = sharedPref.getString(Constants.nickapp, null);
		try {
			if ((nick != null) ) {
				// preguntar si esta logueado contra el servidor.
				values = new String[3];
				values[0] = nick;
				values[1] = "Mis trueques";
				values[2] = "Cerrar sesion";
			}else{
				values = new String[2];
				values[0] = "Iniciar Sesion";
				values[1] = "Alta Usuario";
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 mDrawerOptions = (ListView) findViewById(R.id.left_drawer);
	     mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
	     mDrawerOptions.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, values));
	     mDrawerOptions.setOnItemClickListener(this);
	    	
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu); 
		MenuInflater inflater = getMenuInflater();
	    
		
		inflater.inflate(R.menu.main_activity_actions, menu);
	    if (nick == null){
	    	menu.removeItem(R.id.itemAgregar);
	    }	
	    return true;
	    	
		
	}
	
	@Override
	public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
	    if (values[i].equals("Iniciar Sesion")){
	    	Intent in = new Intent(this, Login.class);
	        startActivity(in);
	    }else if(values[i].equals("Alta Usuario")){
	    	Intent in = new Intent(this, AltaUsuario.class);
	        startActivity(in);
	    }else if(values[i].equals("My perfil")){
	    	
	    }else if(values[i].equals("Mis trueques")){
	    	verTrueques(nick);
	    }else if(values[i].equals("Cerrar sesion")){
	    	cerrarSesion();
	    }
	}
	
	private void cerrarSesion() {
		CerrarSesion c = new CerrarSesion(this);
		c.execute();
		
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items
	    switch (item.getItemId()) {
	    	case android.R.id.home:
	    		if (mDrawer.isDrawerOpen(mDrawerOptions)){
	                mDrawer.closeDrawers();
	            }else{
	                mDrawer.openDrawer(mDrawerOptions);
	            }
	    	      return true;
	    
	    	case R.id.listar:
	            //openSearch();
				verTrueques(null);
	            return true;
	        case R.id.itemAgregar:
	            //openSettings();
	        	cambiar();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	public void cambiar(){
		Intent i = new Intent(this, IngresarTrueque.class);
		i.putExtra("modo", "insertar");
        startActivity(i);
	}

	public void verTrueques(String n) {
		ListarComunicacion j = new ListarComunicacion(this,n);
	    j.execute("trueque");
	}

}


class CerrarSesion extends AsyncTask<String, String, String>{
	
	private Context c;
	
	
	public CerrarSesion(Context context){
		c = context;
		Factory.initialize(0, c);
	}
	
	@Override
	protected String doInBackground(String... params) {
		try {
			
			Message m = Factory.getUserSDK().logout();
			if (m.codigo == Constants.Exito){
				SharedPreferences sharedPref = c.getSharedPreferences("claves", Context.MODE_PRIVATE);
				SharedPreferences.Editor editor = sharedPref.edit();
				editor.putString(Constants.nickapp, null);
				editor.commit();
			}else{
				SharedPreferences sharedPref = c.getSharedPreferences("claves", Context.MODE_PRIVATE);
				SharedPreferences.Editor editor = sharedPref.edit();
				editor.putString(Constants.nickapp, null);
				editor.commit();
			}
			
			
		} catch (NotInitilizedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	protected void onPostExecute(String result) {
		
		super.onPostExecute(result);
		Intent i = new Intent(this.c.getApplicationContext(),MainActivity.class);
		this.c.startActivity(i);
	}
	
	
}

