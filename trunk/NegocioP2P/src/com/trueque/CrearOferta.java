package com.trueque;

import java.io.File;
import java.io.IOException;

import modelo.Oferta;

import org.json.JSONException;

import rest.IngresarComunicacion;
import baas.sdk.utils.Constants;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.app.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

public class CrearOferta extends Fragment {
	

	private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;

	public static final int MEDIA_TYPE_IMAGE = 1;
	public static final int MEDIA_TYPE_VIDEO = 2;
	private ImageView imagen;
	private Uri fileUri;

	int TAKE_PHOTO_CODE = 0;
	public static int count = 0;

	
	
	private Spinner spinner;
	private Spinner spinnerMoneda; 
	String idTrueque;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		final String dir = Environment
				.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
				+ "/Trueque/";
		File newdir = new File(dir);
		newdir.mkdirs();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.crear_oferta, container, false); 
	}
	
	
	
	@Override
	public void onStart() {
		super.onStart();

		//Bundle bundle = getIntent().getExtras();
		// id del Trueque!!
		idTrueque = getArguments().getString("idTrueque");
		imagen = (ImageView) getView().findViewById(R.id.Preview);
		spinner = (Spinner) getActivity().findViewById(R.id.spinnerCategorias);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
        R.array.array_categorias, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
     
        spinnerMoneda = (Spinner) getActivity().findViewById(R.id.spinnerMoneda);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(getActivity(),
        R.array.array_monedas, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMoneda.setAdapter(adapter2);
	}

	public void agregarOferta() {
		Oferta o = new Oferta();
		Spinner spinner = (Spinner) getActivity().findViewById(R.id.spinnerCategorias);
		o.tipo = spinner.getSelectedItemPosition();
		Spinner spinnerMoneda = (Spinner) getActivity().findViewById(R.id.spinnerMoneda);
		o.moneda = spinnerMoneda.getSelectedItemPosition();
		final EditText valor = (EditText)getActivity().findViewById(R.id.editTextValor);
		o.valor = Integer.parseInt(valor.getText().toString());
		final EditText descripcion = (EditText)getActivity().findViewById(R.id.editTextDescripcion);
		o.descripcion = descripcion.getText().toString();
		BitmapFactory.Options options = new BitmapFactory.Options();
		
        options.inSampleSize = 8;
        String imagenchica;
        String imagenGrande;
        if (fileUri != null){
       	 	Bitmap bitmap = BitmapFactory.decodeFile(fileUri.getPath(),options);
       	 	imagenchica = Camara.redimensionarImagenMaximo(bitmap, 53, 53); 	 
       	 	imagenGrande = Camara.redimensionarImagenMaximo(bitmap,580,285);
        }else{
       	 imagenchica = "";
       	 imagenGrande = "";
        }
//        
        o.imagenChica = imagenchica;
        o.imagenGrande = imagenGrande;
//        SharedPreferences sharedPref = this.getSharedPreferences("claves", Context.MODE_PRIVATE);
//		t.nick = sharedPref.getString(Constants.nickapp, null);
//		
//		for (int i = 0; i < adapterCategorias.checkBoxState.length;i++){
//			if (adapterCategorias.checkBoxState[i]){
//				
//				t.tipoOfertas.add(Integer.valueOf(i));
//			}
//		}
//		
//		
		SharedPreferences sharedPref = getActivity().getSharedPreferences("claves", Context.MODE_PRIVATE);
		o.nick = sharedPref.getString(Constants.nickapp, null);
		
		o.idTrueque = idTrueque;
		IngresarComunicacion claseInsertar = new IngresarComunicacion(getActivity());
		claseInsertar.execute(o);
		
	}
	
	/**
	 * Checking device has camera hardware or not
	 * */
	private boolean isDeviceSupportCamera() {
		if (getActivity().getApplicationContext().getPackageManager()
				.hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
			return true;
		} else {
			return false;
		}
	}

	public void captureImage() {
		if (isDeviceSupportCamera()) {
			// imagen.setOnClickListener(new View.OnClickListener() {
			// public void onClick(View v) {
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
			startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
			// };
			// });
		} else {
			// no spoprta camara!
			Toast.makeText(getActivity(), "No soporta Camara",
					Toast.LENGTH_SHORT).show();
		}

	}

	public Uri getOutputMediaFileUri(int type) {
		return Uri.fromFile(getOutputMediaFile(type));
	}

	private static File getOutputMediaFile(int type) {
		final String dir = Environment
				.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
				+ "/Trueque/";
		File newdir = new File(dir);
		newdir.mkdirs();
		count++;
		String file = dir + count + ".jpg";
		File newfile = new File(file);
		try {
			newfile.createNewFile();
		} catch (IOException e) {
		}

		return newfile;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
//		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
			if (resultCode == android.app.Activity.RESULT_OK) {
				previewCapturedImage();
			} else if (resultCode == android.app.Activity.RESULT_CANCELED) {
				Toast.makeText(getActivity().getApplicationContext(),
						"User cancelled image capture", Toast.LENGTH_SHORT)
						.show();
			} else {
				Toast.makeText(getActivity().getApplicationContext(),
						"Sorry! Failed to capture image", Toast.LENGTH_SHORT)
						.show();
			}
		}
		//fromcamera = true;

	}

	private void previewCapturedImage() {
		try {
			imagen.setVisibility(View.VISIBLE);
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inSampleSize = 8;
			final Bitmap bitmap = BitmapFactory.decodeFile(fileUri.getPath(),
					options);
			imagen.setImageBitmap(bitmap);
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
	}
	
}
