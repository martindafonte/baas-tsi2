package com.trueque;

import org.json.JSONException;
import org.json.JSONObject;

import rest.ImagenGrande;
import rest.ListarComunicacion;
import android.annotation.SuppressLint;
import android.app.Activity;

import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import baas.sdk.utils.Constants;

public class VerTruequesActivity extends Fragment {

	// private TextView tv1;
	private ListView lv1;
	private adaptarElemento adapter;
	private String idimagen;
	private String idtrueque;
	ListarComunicacion bw_listarComunicacion;
	private ProgressDialog dialog;
	AsyncTask<Void, Void, Void> tarea;
	String n;



	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.ver_trueques, container, false);
	}

	@Override
	public void onStart() {
		n = getArguments().getString("idUsuario");
		bw_listarComunicacion = new ListarComunicacion(getActivity(),n);
		// Nick es null si es listar trueques si no es ver mis trueques
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
				dialog.setMessage("Cargando Trueques");
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
					bw_listarComunicacion.doInBackground("trueque");
				} catch (Exception e) {
					dialog.dismiss();
					lv1 = (ListView) getActivity().findViewById(R.id.list);
					lv1.setEmptyView(getActivity().findViewById(R.id.empty));
				}
				return null;
			}
		}.execute(null, null, null);

	}

	public void ponerAdapter() {
		lv1 = (ListView) getActivity().findViewById(R.id.list);
		

		lv1.setEmptyView(getActivity().findViewById(R.id.empty));
		if(MainActivity.trueques==null || MainActivity.trueques.length == 0){
			return;
		}
		adapter = new adaptarElemento(getActivity());
		lv1.setAdapter(adapter);

		lv1.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> myAdapter, View myView,
					int myItemInt, long mylng) {
				String selectedFromList = (String) (lv1
						.getItemAtPosition(myItemInt));
				try {
					JSONObject json = new JSONObject(selectedFromList);
					idimagen = json.getString(Constants.json_id_imagen_grande);
					idtrueque = json.getString(Constants.jsonidMongo);
					// Nuevoo***************
					Fragment f = new VerTruequeActivity();
					Bundle args = new Bundle();
					args.putString("idimagen", idimagen);
					args.putString("idtrueque", idtrueque);
					f.setArguments(args);
					android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
					fragmentManager.beginTransaction().replace(R.id.content_frame, f)
							.commit();
					MainActivity m = (MainActivity) getActivity();
					
					m.setTitle("Ver Trueque");
					
					//Buscarfoto();
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

	// @Override
	// public boolean onOptionsItemSelected(MenuItem item) {
	// // Handle presses on the action bar items
	// switch (item.getItemId()) {
	// case android.R.id.home:
	// Intent i = new Intent(getActivity(), MainActivity.class);
	// startActivity(i);
	// return true;
	// default:
	// return super.onOptionsItemSelected(item);
	// }
	// }

	// ********* ADAPTAR ITEM ***************
	public class adaptarElemento extends ArrayAdapter<Object> {
		Activity context;
		String[] categorias;

		public adaptarElemento(Activity c) {
			super(c, R.layout.ver_trueques, MainActivity.trueques);
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
				j = new JSONObject(MainActivity.trueques[position]);
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
