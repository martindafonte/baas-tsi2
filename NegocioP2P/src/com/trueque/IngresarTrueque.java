package com.trueque;

import java.io.File;
import java.io.IOException;

import modelo.Trueque;

import org.json.JSONException;
import org.json.JSONObject;

import rest.ActualizarComunicacion;
import rest.IngresarComunicacion;
import android.app.ActionBar;
import android.app.Activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import baas.sdk.utils.Constants;

public class IngresarTrueque extends Fragment {

	private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;

	public static final int MEDIA_TYPE_IMAGE = 1;
	public static final int MEDIA_TYPE_VIDEO = 2;
	private ImageView imagen;
	private Uri fileUri;

	int TAKE_PHOTO_CODE = 0;
	public static int count = 0;
	Camera camera;
	Button botonImagen;
	JSONObject jsonActual;
	String modo;
	private Spinner spinner;
	private Spinner spinnerMoneda;
	private EditText valor;
	private EditText descripcion;
	private Bundle bundle;
	private ListView catAceptadas;
	private AdaptarCategoria adapterCategorias;
	public String[] categorias;
	public String nick;
	public boolean fromcamera;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActionBar actionBar = getActivity().getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);

		// **********************************************
		// Carpeta para guardar imagen
		final String dir = Environment
				.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
				+ "/Trueque/";
		File newdir = new File(dir);
		newdir.mkdirs();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View v =inflater.inflate(R.layout.creartrueque, container, false);
		
		return v;
	}
	
	public void Aceptar(){
		if (modo.equals("editar")){
			try {
				editarTrueque();
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}else{
			agregarTrueque();
		}
		
	}
	
	

	@Override
	public void onStart() {
		super.onStart();
		if(fromcamera)
			return;
		Resources res = getResources();
		categorias = res.getStringArray(R.array.array_categorias);
//		Activity a = getActivity();
		imagen = (ImageView) getView().findViewById(R.id.Preview);
		spinner = (Spinner) getView().findViewById(R.id.spinnerCategorias);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				getActivity(), R.array.array_categorias,
				android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
		spinnerMoneda = (Spinner) getActivity()
				.findViewById(R.id.spinnerMoneda);
		try{
		ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(
				getActivity(), R.array.array_monedas,
				android.R.layout.simple_spinner_item);
		adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerMoneda.setAdapter(adapter2);

		catAceptadas = (ListView) getActivity().findViewById(
				R.id.categoriasaceptadas);
		adapterCategorias = new AdaptarCategoria(getActivity(), categorias);
		catAceptadas.setAdapter(adapterCategorias);
		}catch(Exception e){
			modo = bundle.getString("modo");
		}
		// ************* si viene de editar seteo los campos. ****************+
		//bundle = getActivity().getIntent().getExtras();
		modo = getArguments().getString("modo");
		if (modo.equals("editar")) {
			setCampos();
		}
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		nick = MainActivity.user;
		super.onActivityCreated(savedInstanceState);
	}

	public void setCampos() {
		try {
			jsonActual = new JSONObject(getArguments().getString("trueque"));

			spinner.setId(Integer.parseInt(jsonActual.get("tipo").toString()));

			spinnerMoneda.setId(Integer.parseInt(jsonActual.get("moneda")
					.toString()));

			// tipo.setText(jsonActual.get("Tipo").toString(),TextView.BufferType.EDITABLE);

			valor = (EditText) getActivity().findViewById(R.id.editTextValor);
			valor.setText(jsonActual.get("valor").toString(),
					TextView.BufferType.EDITABLE);

			descripcion = (EditText) getActivity().findViewById(
					R.id.editTextDescripcion);
			descripcion.setText(jsonActual.get("descripcion").toString(),
					TextView.BufferType.EDITABLE);

			imagen = (ImageView) getActivity().findViewById(R.id.Preview);

			byte[] encodeByte = Base64.decode(jsonActual.getString("Imagen"),
					Base64.DEFAULT);
			Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0,
					encodeByte.length);
			imagen.setImageBitmap(bitmap);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void editarTrueque() throws JSONException {

		Trueque t = new Trueque();
		t.id = jsonActual.getInt(Constants.jsonidMongo);
		t.tipo = spinner.getSelectedItemPosition();
		t.moneda = spinnerMoneda.getSelectedItemPosition();
		t.valor = Integer.parseInt(valor.getText().toString());
		t.descripcion = descripcion.getText().toString();
		BitmapFactory.Options options = new BitmapFactory.Options();

		// downsizing image as it throws OutOfMemory Exception for larger
		// images
		options.inSampleSize = 8;
		String imagenchica;
		String imagenGrande;
		if (fileUri != null) {
			Bitmap bitmap = BitmapFactory
					.decodeFile(fileUri.getPath(), options);
			imagenchica = Camara.redimensionarImagenMaximo(bitmap, 250, 300);
			imagenGrande = Camara.redimensionarImagenMaximo(bitmap, 600, 800);

		} else {
			imagenchica = "";
			imagenGrande = "";
		}
		for (int i = 0; i < adapterCategorias.checkBoxState.length; i++) {
			if (adapterCategorias.checkBoxState[i]) {

				t.tipoOfertas.add(Integer.valueOf(i));
			}
		}
		t.nick = nick;
		t.imagenChica = imagenchica;
		t.id_imagenChica = jsonActual.getString(Constants.json_id_imagen_chica);
		t.imagenGrande = imagenGrande;
		t.id_imagenGrande = jsonActual
				.getString(Constants.json_id_imagen_grande);
		ActualizarComunicacion claseactualizar = new ActualizarComunicacion(
				getActivity());
		claseactualizar.execute(t);

	}

	public void agregarTrueque() {

		Trueque t = new Trueque();

		Spinner spinner = (Spinner) getActivity().findViewById(
				R.id.spinnerCategorias);
		t.tipo = spinner.getSelectedItemPosition();
		Spinner spinnerMoneda = (Spinner) getActivity().findViewById(
				R.id.spinnerMoneda);
		t.moneda = spinnerMoneda.getSelectedItemPosition();
		final EditText valor = (EditText) getActivity().findViewById(
				R.id.editTextValor);
		t.valor = Integer.parseInt(valor.getText().toString());
		final EditText descripcion = (EditText) getActivity().findViewById(
				R.id.editTextDescripcion);
		t.descripcion = descripcion.getText().toString();
		BitmapFactory.Options options = new BitmapFactory.Options();

		options.inSampleSize = 8;
		String imagenchica;
		String imagenGrande;
		if (fileUri != null) {
			Bitmap bitmap = BitmapFactory
					.decodeFile(fileUri.getPath(), options);
			imagenchica = Camara.redimensionarImagenMaximo(bitmap, 250, 188);
			imagenGrande = Camara.redimensionarImagenMaximo(bitmap, 720, 540);
		} else {
			imagenchica = "";
			imagenGrande = "";
		}

		t.imagenChica = imagenchica;
		t.imagenGrande = imagenGrande;

		t.nick = nick;

		for (int i = 0; i < adapterCategorias.checkBoxState.length; i++) {
			if (adapterCategorias.checkBoxState[i]) {

				t.tipoOfertas.add(Integer.valueOf(i));
			}
		}

		IngresarComunicacion claseInsertar = new IngresarComunicacion(
				getActivity());
		claseInsertar.execute(t);
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
		fromcamera = true;

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

	static class AdaptarCategoria extends ArrayAdapter<Object> {
		Activity context;
		ViewHolder viewHolder = null;
		boolean[] checkBoxState;
		String[] cat;

		static class ViewHolder {
			protected CheckBox checkbox;

		}

		public AdaptarCategoria(Activity c, String[] categorias) {
			super(c, R.layout.creartrueque, categorias);
			this.context = c;
			this.cat = categorias;
			checkBoxState = new boolean[categorias.length];

		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			LayoutInflater i = context.getLayoutInflater();
			View item = i.inflate(R.layout.item_categoria, null);

			viewHolder = new ViewHolder();
			CheckBox c = (CheckBox) item.findViewById(R.id.checkBox1);
			c.setText(cat[position]);
			viewHolder.checkbox = c;

			item.setTag(viewHolder);

			viewHolder.checkbox.setChecked(checkBoxState[position]);
			viewHolder.checkbox.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					if (((CheckBox) v).isChecked())
						checkBoxState[position] = true;
					else
						checkBoxState[position] = false;
				}
			});

			return item;
		}
	}

}
