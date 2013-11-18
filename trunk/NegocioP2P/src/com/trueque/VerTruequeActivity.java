package com.trueque;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.json.JSONObject;

import com.trueque.R.menu;

import baas.sdk.Factory;
import baas.sdk.messages.MessageJsonList;
import baas.sdk.utils.Constants;
import baas.sdk.utils.exceptions.NotInitilizedException;
import rest.EliminarComunicacion;
import rest.ImagenGrande;
import rest.ListarComunicacion;
import rest.ListarOfertas;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class VerTruequeActivity extends Fragment {
	
	public static String trueque;
	public int indice;
	private JSONObject j;
	String imagenGrande;
	String nick;
	ImagenGrande bw_ImagenGrande;
	AsyncTask<Void, Void, Void> tarea;
	private ProgressDialog dialog;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.activity_ver_trueque, container, false);
	}
	
	@Override
	public void onStart() {
		bw_ImagenGrande = new ImagenGrande(getActivity());
		super.onStart();
	}
	
	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	
		tarea = new AsyncTask<Void, Void, Void>(){
			
			@Override
			protected void onPreExecute() {
				dialog = new ProgressDialog(getActivity());
				dialog.setMessage("Cargando");
				dialog.show();
				super.onPreExecute();
			}
			@Override
			protected void onPostExecute(Void result) {
				super.onPostExecute(result);
				if (dialog.isShowing()) {
					dialog.dismiss();
				}
				setVerTrueque();
			}
		
			@Override
		protected Void doInBackground(Void... params) {
				Factory.initialize(1, getActivity());
				try {
					baas.sdk.ISDKJson sdkJson = Factory.getJsonSDK();
					JSONObject query = new JSONObject();
					query.put("imagenId", getArguments().getString("idimagen"));
					MessageJsonList mjimagen = sdkJson.getJsonList(query, 0, 1);
					imagenGrande =mjimagen.resultList.getJSONObject(0).getString("Imagen");
					
				} catch (NotInitilizedException e) {
				}
				catch (JSONException e) {
					e.printStackTrace();
				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				return null;
			}
		}.execute(null, null, null);
				
	}
		
	

	public void setVerTrueque() {

		ActionBar actionBar = getActivity().getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		
		try{
		String id = getArguments().getString("idtrueque");	
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
		SharedPreferences sharedPref = getActivity().getSharedPreferences("claves", Context.MODE_PRIVATE);
		nick = sharedPref.getString(Constants.nickapp, null);
		
		Button b = (Button)getActivity().findViewById(R.id.buttonOfertar);
	
		try {
			if ((nick == null) || (nick.equals(j.getString("nick")))){
				b.setText("Ver Ofertas");
			}else{
				b.setText("Ofertar");
			}
				
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            	try {
					if ((nick == null) ||(nick.equals(j.getString("nick")))){
						verofertas();
					}else{
						
						ofertar();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
            }
        });;
		
		j = new JSONObject(MainActivity.trueques[indice]);
		TextView Tipo = (TextView)getActivity().findViewById(R.id.titulo);
		Tipo.setText(res.getStringArray(R.array.array_categorias)[Integer.parseInt(j.getString("tipo"))]);
		
		TextView Valor = (TextView)getActivity().findViewById(R.id.valor);
		String moneda = res.getStringArray(R.array.array_monedas)[j.getInt("moneda")];
		
		Valor.setText(moneda + " " +j.getString("valor"));
		
		TextView Descripcion = (TextView) getActivity().findViewById(R.id.descripcion1);
		Descripcion.setText(j.getString("descripcion"));
		
		ImageView imagen = (ImageView)getActivity().findViewById(R.id.imagen);
		byte [] encodeByte=Base64.decode(imagenGrande,Base64.DEFAULT);
        Bitmap bitmap=BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
        imagen.setImageBitmap(bitmap);
		
		}catch(JSONException e){
			e.printStackTrace();
		}
		
	}
	
	protected void verofertas() {
		Bundle args = new Bundle();
		try {
			args.putString("idTrueque",j.get("_id").toString());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Fragment f = new VerOfertas();
		f.setArguments(args);
		FragmentManager fragmentManager = getFragmentManager();
		fragmentManager.beginTransaction().replace(R.id.content_frame, f)
				.commit();
		getActivity().setTitle("Ver Trueque");
	
	}
	public void ofertar(){
		
		Bundle args = new Bundle();
		try {
			args.putString("idTrueque",j.get("_id").toString());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		Fragment f = new CrearOferta();
		f.setArguments(args);
		FragmentManager fragmentManager = getFragmentManager();
		fragmentManager.beginTransaction().replace(R.id.content_frame, f)
				.commit();
		getActivity().setTitle("Ofertar");
		
		
//		Intent i = new Intent(VerTruequeActivity.this, CrearOferta.class);
//		try {
//			i.putExtra("nick_trueque", j.getString("_id"));
//		} catch (JSONException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//        startActivity(i);
	}
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		super.onCreateOptionsMenu(menu); 
//		MenuInflater inflater = getMenuInflater();
//	    inflater.inflate(R.menu.menu_trueque, menu);
//	    
//	    try {
//			if (!nick.equals(j.getString("nick"))){
//				menu.removeItem(R.id.editar);
//		    	menu.removeItem(R.id.borrar);
//			}
//		} catch (JSONException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	    return true;
//		
//	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items

//		Intent i;
//	    switch (item.getItemId()) {
//	    	case android.R.id.home:
//	    		// Volver!
//	    		 i = new Intent(this, VerTruequesActivity.class );
//	            startActivity(i);
//	            return true;
//	    	case R.id.editar:
//	    		 i = new Intent(this, IngresarTrueque.class );
//	    		 i.putExtra("modo", "editar");
//	    		 i.putExtra("trueque", MainActivity.trueques[indice]);
//	    		 i.putExtra("imagen", imagenGrande);
//	    		 startActivity(i);
//	            //openSearch();
//	            return true;
//	    	case R.id.borrar:
//	    		 EliminarComunicacion eliminar = new EliminarComunicacion(this);
//	    		 eliminar.execute(j);
//	    		 return true;
//	        default:
//	            return super.onOptionsItemSelected(item);
//	    }
		return true;
		
	}
	
}
