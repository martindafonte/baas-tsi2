package com.trueque;

import rest.ListarComunicacion;
import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.NavUtils;
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
	
	public static  String trueques[];
	private DrawerLayout mDrawer;
	private ListView mDrawerOptions;
	private static final String[] values = {"My perfil", "Mis trueques", "Cerrar sesion"};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		
		
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
	    return true;
		
	}
	
	@Override
	public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
	    Toast.makeText(this, "Pulsado " + values[i], Toast.LENGTH_SHORT).show();
	    mDrawer.closeDrawers();
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


