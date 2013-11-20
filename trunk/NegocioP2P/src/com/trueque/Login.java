package com.trueque;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import baas.sdk.Factory;
import baas.sdk.messages.Message;
import baas.sdk.utils.Constants;
import baas.sdk.utils.User;
import baas.sdk.utils.exceptions.NotInitilizedException;

public class Login extends BaseFragment {
	private static EditText nick;
	private static EditText contrasenia;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.login, container, false); 
	}

	@Override
	public void onStart() {
		super.onStart();
		nick = (EditText)getView().findViewById(R.id.loginUsuario);
		contrasenia = (EditText) getView().findViewById(R.id.loginPassword);
		getView().findViewById(R.id.buttonRegistrar).setOnClickListener(new View.OnClickListener() {
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
//            		IniciarSesion i = new IniciarSesion(getActivity());
//            		i.execute(u);
            		new AsyncTask<User, String, String>(){
            			User u;
						@Override
						protected String doInBackground(User... usuarios) {
							try {
								Factory.initialize(1, getActivity());
								u = usuarios[0];
								Message m = Factory.getUserSDK().login(u.Nick, u.Password);
							//	if (m.codigo == Constants.Exito){
									SharedPreferences sharedPref = getActivity().getSharedPreferences("claves", Context.MODE_PRIVATE);
									SharedPreferences.Editor editor = sharedPref.edit();
									editor.putString(Constants.nickapp, u.Nick);								
									editor.commit();
								//}
							} catch (NotInitilizedException e) {
							}
							return null;
						}
						@Override
						protected void onPostExecute(String result) {
							changeUser(u.Nick);
						}
            		}.execute(u);
            	}
            }
        });
	}
}