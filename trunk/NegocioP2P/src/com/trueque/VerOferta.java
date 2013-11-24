package com.trueque;

import org.json.JSONException;
import org.json.JSONObject;

import rest.ImagenGrande;
import android.R.array;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import baas.sdk.Factory;
import baas.sdk.ISDKJson;
import baas.sdk.ISDKPush;
import baas.sdk.messages.MessageJson;
import baas.sdk.utils.exceptions.NotInitilizedException;

public class VerOferta extends Fragment {

	ImagenGrande bw_ImagenGrande;
	AsyncTask<Void, Void, Void> tarea;
	private ProgressDialog dialog;
	String imagenGrande;
	int truqueid;
	String nickDestinatario;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.ver_oferta_object, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {

		super.onActivityCreated(savedInstanceState);
		tarea = new AsyncTask<Void, Void, Void>() {

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
				setVerOferta();
				if (dialog.isShowing()) {
					dialog.dismiss();
				}
			}

			@Override
			protected Void doInBackground(Void... params) {
				Factory.initialize(1, getActivity());
				try {
					baas.sdk.ISDKJson sdkJson = Factory.getJsonSDK();
					JSONObject query = new JSONObject();
					query.put("imagenId", getArguments().getString("idimagen"));
					// MessageJsonList mjimagen = sdkJson.getJsonList(query, 0,
					// 1);
					MessageJson mjimagen = sdkJson.getJsonFromCacheWithId(
							"imagenId", getArguments().getString("idimagen"));
					// imagenGrande
					// =mjimagen.resultList.getJSONObject(0).getString("Imagen");
					if(!((mjimagen.json == null) ||(mjimagen.json.isNull("Imagen")))){
						imagenGrande = mjimagen.json.getString("Imagen");
					}
					

				} catch (NotInitilizedException e) {
				} catch (JSONException e) {
					e.printStackTrace();
				}
				return null;
			}
		}.execute(null, null, null);
	}

	@Override
	public void onStart() {
//		bw_ImagenGrande = new ImagenGrande(getActivity());
		super.onStart();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
	
	
	public void setVerOferta() {
		try{	
		String jsonFromArgs = getArguments().getString("jsonoferta");
		JSONObject j = new JSONObject(jsonFromArgs); 
		truqueid= j.getInt("idTrueque");
		nickDestinatario= j.getString("nick");
		Resources res = getResources();
		getView().findViewById(R.id.buttonAceptarOferta)	
        .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            	new AsyncTask<Void, Void, Void>(){

					@Override
					protected Void doInBackground(Void... params) {
						try {
							ISDKJson sdkJson = Factory.getJsonSDK();
							ISDKPush sdkPush = Factory.getPushSDK();
							MessageJson mj =sdkJson.getJson(truqueid);
							mj.json.put("activo", false);
							sdkJson.updateJson(mj.jsonid, mj.json, true);
							sdkPush.sendToUser(nickDestinatario, "Su oferta para el trueque de "+mj.json.getString("TipoObjeto")+" del usuario "+ mj.json.getString("nick")+" fue aceptada");
						} catch (NotInitilizedException e) {
						} catch (JSONException e) {
						}
						
						return null;
					}
            		
            	}.execute();
            	Intent upIntent = new Intent(getActivity(), MainActivity.class);
                NavUtils.navigateUpTo(getActivity(), upIntent);
            }
        });;
		TextView Tipo = (TextView) getActivity().findViewById(R.id.tituloOferta);
		
	//	String[] a = );
	//	String num = j.getString("tipo");
	//	String texto = a[num];
		Tipo.setText(res.getStringArray(R.array.array_categorias)[Integer.parseInt(j.getString("tipo"))]);
		
		TextView Valor = (TextView)getActivity().findViewById(R.id.valorOferta);
		String moneda = res.getStringArray(R.array.array_monedas)[j.getInt("moneda")];
		
		Valor.setText(moneda + " " +j.getString("valor"));
		
		TextView Descripcion = (TextView) getActivity().findViewById(R.id.descripcion1Oferta);
		Descripcion.setText(j.getString("descripcion"));
		
		ImageView imagen = (ImageView)getActivity().findViewById(R.id.imagenOferta);
		byte [] encodeByte=Base64.decode(imagenGrande,Base64.DEFAULT);
        Bitmap bitmap=BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
        imagen.setImageBitmap(bitmap);
		
		}catch(JSONException e){
		}
	}
}
