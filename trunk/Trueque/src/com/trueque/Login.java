package com.trueque;

import baas.sdk.Factory;
import baas.sdk.messages.Message;
import baas.sdk.utils.Constants;
import baas.sdk.utils.User;
import baas.sdk.utils.exceptions.NotInitilizedException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Login extends Activity {
	private static EditText nick;
	private static EditText contrasenia;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		
		nick = (EditText)findViewById(R.id.loginUsuario);
		contrasenia = (EditText) findViewById(R.id.loginPassword);
		findViewById(R.id.buttonRegistrar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            	
            	User u = new User();
            	u.Nick = nick.getText().toString();
            	u.Password = contrasenia.getText().toString();
            	boolean error = false;
            	if (u.Nick.isEmpty()){
            		error = true;
            	}
            	if (u.Password.isEmpty()){
            		error = true;
            	}
            	if (!error){
            		IniciarSesion i = new IniciarSesion(Login.this);
            		i.execute(u);
            	}
            }
        });
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}
	
	public void iniciarSesion(){
		int i = 8;
		if (i == 7){
			System.out.println("********");
		}
	}
}

class IniciarSesion extends AsyncTask<User, String, String>{
	
	private Context c;
	
	
	public IniciarSesion(Context context){
		c = context;
		
	}
	
	@Override
	protected String doInBackground(User... usuarios) {
		try {
			Factory.initialize(1, c);
			User u = usuarios[0];
			Message m = Factory.getUserSDK().login(u.Nick, u.Password);
			if (m.codigo == Constants.Exito){
				SharedPreferences sharedPref = c.getSharedPreferences("claves", Context.MODE_PRIVATE);
				SharedPreferences.Editor editor = sharedPref.edit();
				editor.putString(Constants.nickapp, u.Nick);
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
