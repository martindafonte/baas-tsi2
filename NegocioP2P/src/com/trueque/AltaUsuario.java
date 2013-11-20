package com.trueque;

import java.util.concurrent.ExecutionException;

import rest.CrearUsuario;
import android.app.ActionBar;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import baas.sdk.utils.User;

public class AltaUsuario extends Fragment {
	
	private static EditText  nombre;
	private static EditText apellido;
	private static EditText nick;
	private static EditText contrasenia;
	private static EditText repetircontrasenia;
	@SuppressWarnings("unused")
	private static Button registrarse;
	
	
	public AltaUsuario(){
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		ActionBar actionBar = getActivity().getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		
		nombre = (EditText)getActivity().findViewById(R.id.editTextNombre);
		apellido = (EditText)getActivity().findViewById(R.id.editTextApellido);
		nick = (EditText)getActivity().findViewById(R.id.editTextNick);
		contrasenia = (EditText) getActivity().findViewById(R.id.editTextContrasenia);
		repetircontrasenia = (EditText) getActivity().findViewById(R.id.editTextRepetirContrasenia);
		getActivity().findViewById(R.id.buttonRegistrar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            	registarse();
            }
        });;
	}

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
        return inflater.inflate(R.layout.alta_usuario, container, false);
    }
//	
	
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getActivity().onCreateOptionsMenu(menu); 
//		MenuInflater inflater = getActivity().getMenuInflater();
//	    inflater.inflate(R.menu.alta_usuario,menu);
//		return true;
//	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items
	    switch (item.getItemId()) {
	    	case android.R.id.home:
//	    		Intent i = new Intent(getActivity(), MainActivity.class );
//	            startActivity(i);
	            return true;
	    	case R.id.itemAgregar:
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
			CrearUsuario cu = new CrearUsuario(getActivity());
			cu.execute(u);
			try {
				cu.get();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
			MainActivity ma = (MainActivity)getActivity();
			ma.logeado(u.Nick);
		}
	}

}
