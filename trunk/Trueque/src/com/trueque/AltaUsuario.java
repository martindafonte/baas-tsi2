package com.trueque;

import org.json.JSONException;
import org.json.JSONObject;

import baas.sdk.utils.Constants;
import baas.sdk.utils.User;
import rest.CrearUsuario;
import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.AdapterView.OnItemClickListener;

public class AltaUsuario extends Activity {
	
	private static EditText  nombre;
	private static EditText apellido;
	private static EditText nick;
	private static EditText contrasenia;
	private static EditText repetircontrasenia;
	private static Button registrarse;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.alta_usuario);
		
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		
		nombre = (EditText)findViewById(R.id.editTextNombre);
		apellido = (EditText)findViewById(R.id.editTextApellido);
		nick = (EditText)findViewById(R.id.editTextNick);
		contrasenia = (EditText) findViewById(R.id.editTextContrasenia);
		repetircontrasenia = (EditText) findViewById(R.id.editTextRepetirContrasenia);
		findViewById(R.id.buttonRegistrar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            	registarse();
            }
        });;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		super.onCreateOptionsMenu(menu); 
		MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.alta_usuario,menu);
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
	    	case R.id.itemAgregarUsuario:
	    		registarse();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	
	public void registarse(){
		boolean error = false;
		if (nombre.getText().toString().equals("")){
			error = true;
		}
		if (apellido.getText().toString().equals("")){
			error = true;
		}
		if (nick.getText().toString().equals("")){
			error = true;
		}
		if (contrasenia.getText().toString().equals("")){
			error = true;
		}
		if (repetircontrasenia.getText().toString().equals("")){
			error = true;
		}
		if (!contrasenia.getText().toString().equals(repetircontrasenia.getText().toString())){
			error = true;
		}
		
		if (!error){
			User u = new User();
			u.Name = nombre.getText().toString();
			u.Surename = apellido.getText().toString();
			u.Nick = nick.getText().toString();
			u.Password = contrasenia.getText().toString();
			CrearUsuario cu = new CrearUsuario(this);
			cu.execute(u);
		}
	}

}
