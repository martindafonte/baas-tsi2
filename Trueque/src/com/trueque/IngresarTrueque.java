package com.trueque;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import modelo.Trueque;

import org.json.JSONException;
import org.json.JSONObject;

import com.trueque.VerTruequesActivity.adaptarElemento;

import baas.sdk.utils.Constants;
import rest.ActualizarComunicacion;
import rest.IngresarComunicacion;
import android.R.array;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ExpandableListActivity;
import android.content.Context;
import android.content.Intent;


import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera.ShutterCallback;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;



public class IngresarTrueque extends Activity {
	
	
	private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;
    private ImageView imagen;
    private Uri fileUri;
    
	
	int TAKE_PHOTO_CODE = 0;
	public static int count=0;
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
	public  String[] categorias;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.creartrueque);
		
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		
		// **********************************************
		//Carpeta para guardar imagen
        final String dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/Trueque/"; 
        File newdir = new File(dir); 
        newdir.mkdirs();
        
        imagen = (ImageView) findViewById(R.id.Preview);

      
      	//combo box de categoria y moneda.
      	Resources res = getResources();
		categorias = res.getStringArray(R.array.array_categorias);
      	
        spinner = (Spinner) findViewById(R.id.spinnerCategorias);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
        R.array.array_categorias, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
     
        spinnerMoneda = (Spinner) findViewById(R.id.spinnerMoneda);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
        R.array.array_monedas, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMoneda.setAdapter(adapter2);
        
        
        catAceptadas = (ListView) findViewById(R.id.categoriasaceptadas);   
        adapterCategorias = new AdaptarCategoria(this,categorias);
        catAceptadas.setAdapter(adapterCategorias);
        
        
     // ************* si viene de editar seteo los campos. ****************+
	     bundle = getIntent().getExtras();
	     modo = bundle.getString("modo"); 
	     if (modo.equals("editar")){
	    	 setCampos();
	     }
	}	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu); 
		MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.main, menu);
	    return true;
		
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items
	    switch (item.getItemId()) {
	    	case android.R.id.home:
	    		Intent i = new Intent(this, MainActivity.class );
	            startActivity(i);
	            return true;
	    	case R.id.itemaceptar:
	    		if (modo.equals("editar")){
	    			try {
						editarTrueque();
					} catch (JSONException e) {
						e.printStackTrace();
					}
	    		}
	    		else{
	    			agregarTrueque();
	    		}
	    		
	            return true;
	    	case R.id.itemcamara:
	    		captureImage();
	    		return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	public void setCampos(){
		try {
			jsonActual = new JSONObject(bundle.getString("trueque"));
			
			spinner.setId(Integer.parseInt(jsonActual.get("tipo").toString()));
			
			spinnerMoneda.setId(Integer.parseInt(jsonActual.get("moneda").toString()));
			
			//tipo.setText(jsonActual.get("Tipo").toString(),TextView.BufferType.EDITABLE);

			valor = (EditText)findViewById(R.id.editTextValor);
			valor.setText(jsonActual.get("valor").toString(),TextView.BufferType.EDITABLE);
			
			descripcion = (EditText)findViewById(R.id.editTextDescripcion);
			descripcion.setText(jsonActual.get("descripcion").toString(),TextView.BufferType.EDITABLE);
			
			imagen = (ImageView)findViewById(R.id.Preview);
			
			byte [] encodeByte=Base64.decode(bundle.getString("imagen"),Base64.DEFAULT);
	        Bitmap bitmap=BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
	        imagen.setImageBitmap(bitmap);	
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private void editarTrueque() throws JSONException {
		
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
         if (fileUri != null){
        	 Bitmap bitmap = BitmapFactory.decodeFile(fileUri.getPath(),options);      	 
        	 imagenchica = Camara.redimensionarImagenMaximo(bitmap, 250, 300);
             imagenGrande = Camara.redimensionarImagenMaximo(bitmap,600,800);
         
         }else{
        	 imagenchica = "";
        	 imagenGrande = "";
         }
         for (int i = 0; i < adapterCategorias.checkBoxState.length;i++){
 			if (adapterCategorias.checkBoxState[i]){
 				
 				t.tipoOfertas.add(Integer.valueOf(i));
 			}
 		}    
         t.imagenChica = imagenchica;
         t.id_imagenChica = jsonActual.getString(Constants.json_id_imagen_chica);
         t.imagenGrande = imagenGrande;
         t.id_imagenGrande = jsonActual.getString(Constants.json_id_imagen_grande);
         ActualizarComunicacion claseactualizar = new ActualizarComunicacion(this);
    	 claseactualizar.execute(t);
		
	}
	
	public void agregarTrueque(){
		 
		Trueque t = new Trueque();
		
		 Spinner spinner = (Spinner) findViewById(R.id.spinnerCategorias);
		 t.tipo = spinner.getSelectedItemPosition();
		 Spinner spinnerMoneda = (Spinner) findViewById(R.id.spinnerMoneda);
		 t.moneda = spinnerMoneda.getSelectedItemPosition();
		 final EditText valor = (EditText)findViewById(R.id.editTextValor);
		 t.valor = Integer.parseInt(valor.getText().toString());
		final EditText descripcion = (EditText)findViewById(R.id.editTextDescripcion);
		t.descripcion = descripcion.getText().toString();
		 BitmapFactory.Options options = new BitmapFactory.Options();
		
        options.inSampleSize = 8;
        String imagenchica;
        String imagenGrande;
        if (fileUri != null){
       	 	Bitmap bitmap = BitmapFactory.decodeFile(fileUri.getPath(),options);
       	 	imagenchica = Camara.redimensionarImagenMaximo(bitmap, 250, 188); 	 
       	 	imagenGrande = Camara.redimensionarImagenMaximo(bitmap,720,540);
        }else{
       	 imagenchica = "";
       	 imagenGrande = "";
        }
        
        t.imagenChica = imagenchica;
        t.imagenGrande = imagenGrande;
        SharedPreferences sharedPref = this.getSharedPreferences("claves", Context.MODE_PRIVATE);
		t.nick = sharedPref.getString(Constants.nickapp, null);
		
		for (int i = 0; i < adapterCategorias.checkBoxState.length;i++){
			if (adapterCategorias.checkBoxState[i]){
				
				t.tipoOfertas.add(Integer.valueOf(i));
			}
		}
		
	
		IngresarComunicacion claseInsertar = new IngresarComunicacion(this);
		claseInsertar.execute(t);
	 }
	

	
	
	/**
     * Checking device has camera hardware or not
     * */
    private boolean isDeviceSupportCamera() {
        if (getApplicationContext().getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA)) {
            return true;
        } else {
            return false;
        }
    }
    
	private void captureImage() {
		 if (isDeviceSupportCamera()){
	        //	imagen.setOnClickListener(new View.OnClickListener() {
	          //  	public void onClick(View v) {
	            		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
	            	    fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE); 
	            	    intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
	            	    startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);		
	            //	};
	            //});
	        }else{
	        	// no spoprta camara!
	        	 Toast.makeText(this, "No soporta Camara", Toast.LENGTH_SHORT).show();
	        }
	    
		
	    
	}
	
	public Uri getOutputMediaFileUri(int type) {
	    return Uri.fromFile(getOutputMediaFile(type));
	}
	
	private static File getOutputMediaFile(int type) {
		final String dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/Trueque/"; 
        File newdir = new File(dir); 
        newdir.mkdirs();
        count++;
      String file = dir+count+".jpg";
      File newfile = new File(file);
      try {
          newfile.createNewFile();
      } catch (IOException e) {}       

	    return newfile;
	}
	 	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    super.onActivityResult(requestCode, resultCode, data);
	    
	    if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
	        if (resultCode == RESULT_OK) {
	            previewCapturedImage();
	        } else if (resultCode == RESULT_CANCELED) {
	            Toast.makeText(getApplicationContext(),
	                    "User cancelled image capture", Toast.LENGTH_SHORT)
	                    .show();
	        } else {
	            Toast.makeText(getApplicationContext(),
	                    "Sorry! Failed to capture image", Toast.LENGTH_SHORT)
	                    .show();
	        }
	    }
	    
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
			super(c, R.layout.creartrueque,categorias);
			this.context = c;
			this.cat = categorias;
			checkBoxState= new boolean[categorias.length];
			
		}
	
		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			LayoutInflater i = context.getLayoutInflater();
			View item = i.inflate(R.layout.item_categoria, null);
			
			viewHolder = new ViewHolder();	 
			CheckBox c = (CheckBox)item.findViewById(R.id.checkBox1);
			c.setText(cat[position]);
            viewHolder.checkbox = c;
           
            item.setTag(viewHolder);
            
            viewHolder.checkbox.setChecked(checkBoxState[position]);
            viewHolder.checkbox.setOnClickListener(new View.OnClickListener() {     
		    public void onClick(View v) {
		     if(((CheckBox)v).isChecked())
		      checkBoxState[position]=true;
		     else
		      checkBoxState[position]=false;		       
		     }
		    });
			
			return item;
		}
	}
	
}
