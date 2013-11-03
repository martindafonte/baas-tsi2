package com.trueque;

import rest.ListarComunicacion;
import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;


public class MainActivity extends Activity  {
	
	public static  String trueques[];

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
	    	
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu); 
		MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.main_activity_actions, menu);
	    return true;
		
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items

		
	    switch (item.getItemId()) {
	    	case android.R.id.home:
	    		
	    
	    	case R.id.listar:
	            //openSearch();
	    		
				verTrueques();
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
		Intent i = new Intent(this, IngresarTrueque.class );
        startActivity(i);
	}

	public void verTrueques() {
		ListarComunicacion j = new ListarComunicacion(this);
	    j.execute("trueque");
	}

}


