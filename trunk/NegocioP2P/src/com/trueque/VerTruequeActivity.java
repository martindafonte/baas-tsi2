package com.trueque;

import org.json.JSONException;
import org.json.JSONObject;

import rest.EliminarComunicacion;
import rest.ImagenGrande;
import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import baas.sdk.Factory;
import baas.sdk.messages.MessageJson;
import baas.sdk.utils.Constants;
import baas.sdk.utils.exceptions.NotInitilizedException;

public class VerTruequeActivity extends BaseFragment {
	
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
//					MessageJsonList mjimagen = sdkJson.getJsonList(query, 0, 1);
					MessageJson mjimagen = sdkJson.getJsonFromCacheWithId("imagenId", getArguments().getString("idimagen"));
//					imagenGrande =mjimagen.resultList.getJSONObject(0).getString("Imagen");
					if(!((mjimagen.json == null) ||(mjimagen.json.isNull("Imagen"))))
						imagenGrande = mjimagen.json.getString("Imagen");
					
				} catch (NotInitilizedException e) {
				}
				catch (JSONException e) {
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
		MainActivity m = (MainActivity) getActivity();
		m.vistaActual = m.op_verTrueque;
		
		try {
			if ((nick == null) || (nick.equals(j.getString("nick")))){
				b.setText("Ver Ofertas");
				m.op_miTrueque = true;
				
			}else{
				b.setText("Ofertar");
				m.op_miTrueque = false;
			}
				
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		m.indice = indice;
		m.invalidateOptionsMenu();
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
		
		MainActivity m = (MainActivity)getActivity();
		
		Fragment f = new VerOfertas();
		f.setArguments(args);
		FragmentManager fragmentManager = getFragmentManager();
		fragmentManager.beginTransaction().replace(R.id.content_frame, f)
				.commit();
		getActivity().setTitle("Ofertas");
		m.vistaActual = m.op_verofertas;
		m.imagenGrande = imagenGrande;
		getActivity().invalidateOptionsMenu();
		this.changeScreen(m.op_verofertas, f);
		
	
	}
	public void ofertar(){
		
		Bundle args = new Bundle();
		try {
			args.putString("idTrueque",j.get("_id").toString());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		MainActivity m = (MainActivity) getActivity();
		Fragment f = new CrearOferta();
		f.setArguments(args);
		FragmentManager fragmentManager = getFragmentManager();
		fragmentManager.beginTransaction().replace(R.id.content_frame, f)
				.commit();
		m.setTitle("Ofertar");
		getActivity().setTitle("Ver Trueque");
		m.vistaActual = m.op_altaoferta;
		getActivity().invalidateOptionsMenu();
		this.changeScreen(m.op_altaoferta, f);
		
		
//		Intent i = new Intent(VerTruequeActivity.this, CrearOferta.class);
//		try {
//			i.putExtra("nick_trueque", j.getString("_id"));
//		} catch (JSONException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//        startActivity(i);
//	}
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
	
//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//	    // Handle presses on the action bar items
//		MainActivity m = (MainActivity) getActivity();
//		switch (item.getItemId()) {
//		case R.id.itemeditar:
//			
//			break;
//		case R.id.itemeborrar:
//			EliminarComunicacion eliminar = new EliminarComunicacion(m);
//			eliminar.execute(j);
//			break;
//		}
		
			
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
	//	return true;
		
	}
	
}
