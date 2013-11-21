package com.trueque;

import org.json.JSONException;
import org.json.JSONObject;

import baas.sdk.utils.Constants;

import com.trueque.VerTruequesActivity.adaptarElemento;

import rest.ImagenGrande;
import rest.ListarComunicacion;
import rest.ListarOfertas;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.app.Activity;

import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class VerOfertas extends BaseFragment{
	ListarOfertas bw_listarOfertas;
	private ProgressDialog dialog;
	AsyncTask<Void, Void, Void> tarea;
	private ListView lv1;
	private adaptarElemento adapter;
	private String idimagen;
	private String idoferta;
	private String idtrueque;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.ver_ofertas, container, false);
	}

	@Override
	public void onStart() {
		// ver nick!
		bw_listarOfertas = new ListarOfertas(getActivity(),getArguments().getString("idTrueque"));
		super.onStart();

	}
	
	@Override
	public void onResume() {
		super.onResume();
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		tarea = new AsyncTask<Void, Void, Void>() {
			@Override
			protected void onPostExecute(Void result) {
				super.onPostExecute(result);
				if (dialog.isShowing()) {
					dialog.dismiss();
				}
				ponerAdapter();
			}

			@Override
			protected void onPreExecute() {
				dialog = new ProgressDialog(getActivity());
				dialog.setMessage("Cargando Ofertas");
				dialog.show();
				super.onPreExecute();
			}

			@Override
			protected Void doInBackground(Void... params) {
				Looper l = Looper.myLooper();
				if (l == null) {
					Looper.prepare();
				}
				try {
					bw_listarOfertas.doInBackground("oferta");
				} catch (Exception e) {
					dialog.setMessage("Ocurrio una excepcion");
				}
				return null;
			}
		}.execute(null, null, null);

	}
	

//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.ver_ofertas, menu);
//		return true;
//	}
	
	
	public void ponerAdapter() {
		
		lv1 = (ListView) getActivity().findViewById(R.id.list);
		lv1.setEmptyView(getActivity().findViewById(R.id.empty));
		if(MainActivity.ofertas==null || MainActivity.ofertas.length == 0){
			return;
		}
		adapter = new adaptarElemento(getActivity());
		
		
		lv1.setAdapter(adapter);

		lv1.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> myAdapter, View myView,
					int myItemInt, long mylng) {
				String selectedFromList = (String) (lv1
						.getItemAtPosition(myItemInt));
//				 Intent intent = new Intent(getActivity(), VerOfertaColeccionActivity.class);
//				 intent.putExtra("indice", 1);
//				 intent.putExtra("truequeId", getArguments().getString("idTrueque"));
//                 startActivity(intent);
				try {
					JSONObject json = new JSONObject(selectedFromList);
					idimagen = json.getString(Constants.json_id_imagen_grande);
					idoferta = json.getString(Constants.jsonidMongo);
//					idtrueque = json.
					// Nuevoo***************
					Fragment f = new VerOferta();
					Bundle args = new Bundle();
					args.putString("idimagen", idimagen);
					args.putString("idoferta", idoferta);
					args.putString("jsonoferta", selectedFromList);
					f.setArguments(args);
					android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
					fragmentManager.beginTransaction().replace(R.id.content_frame, f)
							.commit();
					getActivity().setTitle("Ver Oferta");
					changeScreen(MainActivity.op_veroferta,null);
//					Buscarfoto();
				} catch (JSONException e) {
					e.printStackTrace();
				}

			}

//			private void Buscarfoto() {
//				ImagenGrande imagenGrande = new ImagenGrande(getActivity());
//				imagenGrande.execute(idimagen, idtrueque);
//			}
		});
	}
	
	// ********* ADAPTAR ITEM ***************
		public class adaptarElemento extends ArrayAdapter<Object> {
			Activity context;
			String[] categorias;

			public adaptarElemento(Activity c) {
				
				super(c, R.layout.ver_ofertas, MainActivity.ofertas);
				this.context = c;
				Resources res = getResources();
				categorias = res.getStringArray(R.array.array_categorias);
			}

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {

				LayoutInflater i = context.getLayoutInflater();
				View item = i.inflate(R.layout.item_trueque, null);
				JSONObject j;
				try {
					j = new JSONObject(MainActivity.ofertas[position]);
					TextView Tipo = (TextView) item.findViewById(R.id.title);
					Tipo.setText(categorias[Integer.parseInt(j.getString("tipo"))]);

					// TextView Valor = (TextView)
					// item.findViewById(R.id.secondLine);
					// Valor.setText(j.getString("Valor"));

					TextView Descripcion = (TextView) item
							.findViewById(R.id.artist);
					Descripcion.setText(j.getString("descripcion"));

					ImageView imagen = (ImageView) item
							.findViewById(R.id.list_image);
					if (!j.getString("Imagen").equals("")) {
						byte[] encodeByte = Base64.decode(j.getString("Imagen"),
								Base64.DEFAULT);
						Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte,
								0, encodeByte.length);
						imagen.setImageBitmap(bitmap);
					}

					return item;
				} catch (JSONException e) {

					e.printStackTrace();
				}

				return null;
			}

		}


}
