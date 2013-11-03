package com.trueque;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import org.json.JSONObject;

import rest.ActualizarComunicacion;
import rest.IngresarComunicacion;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class EditarTrueque extends Activity {
	
private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;
    private ImageView imagen;
    private Uri fileUri;
    
	
	int TAKE_PHOTO_CODE = 0;
	public static int count=0;
	Camera camera;
	JSONObject jsonActual;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_editar_trueque);
		
		Bundle bundle = getIntent().getExtras();
		
		try {
			jsonActual = new JSONObject(bundle.getString("trueque"));
			
			final EditText tipo =  (EditText)findViewById(R.id.EditTextTipo);
			tipo.setText(jsonActual.get("Tipo").toString(),TextView.BufferType.EDITABLE);

			final EditText valor = (EditText)findViewById(R.id.editTextValor);
			valor.setText(jsonActual.get("Valor").toString(),TextView.BufferType.EDITABLE);
			
			final EditText descripcion = (EditText)findViewById(R.id.editTextDescripcion);
			descripcion.setText(jsonActual.get("Descripcion").toString(),TextView.BufferType.EDITABLE);
			
			imagen = (ImageView)findViewById(R.id.Preview);
			
			byte [] encodeByte=Base64.decode(jsonActual.getString("imagen"),Base64.DEFAULT);
	        Bitmap bitmap=BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
	        imagen.setImageBitmap(bitmap);	
	        
	        imagen.setOnClickListener(new View.OnClickListener() {
	        	public void onClick(View v) {
	        		
	        		captureImage();
	        	
	        	};
	        });	
	        
	        
		}catch(Exception e){
			
		}
    
		
	}

	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items

		
	    switch (item.getItemId()) {
	    	case android.R.id.home:
	    		// Volver!
	    	case R.id.itemaceptar:
	    		actualizarJson();
	            //openSearch();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}



	private void actualizarJson() {
		// TODO Auto-generated method stub
		final EditText tipo =  (EditText)findViewById(R.id.EditTextTipo);
		String tipo_var = tipo.getText().toString();

		final EditText valor = (EditText)findViewById(R.id.editTextValor);
		String valor_var  = valor.getText().toString();
		
		final EditText descripcion = (EditText)findViewById(R.id.editTextDescripcion);
		String descripcion_var = descripcion.getText().toString();
		
		 BitmapFactory.Options options = new BitmapFactory.Options();
		 
         // downsizing image as it throws OutOfMemory Exception for larger
         // images
         options.inSampleSize = 8;

         Bitmap bitmap = BitmapFactory.decodeFile(fileUri.getPath(),
                 options);
         ByteArrayOutputStream baos = new ByteArrayOutputStream();
         bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
         String encodedImage = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
		
         try {
         // Actualizar
        	 ActualizarComunicacion claseactualizar = new ActualizarComunicacion(this);
        	 claseactualizar.execute( "trueque",tipo_var,valor_var,descripcion_var,encodedImage,jsonActual.getString("_id"));
         }catch(Exception e){
        	 
         }
	}
	


	// FOTO**************************************************************
	/**
     * Checking device has camera hardware or not
     * */
    private boolean isDeviceSupportCamera() {
        if (getApplicationContext().getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA)) {
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }
    
	private void captureImage() {
	    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
	 
	    fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
	 
	    intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
	 
	    // start the image capture Intent
	    startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
	}
	
	public Uri getOutputMediaFileUri(int type) {
	    return Uri.fromFile(getOutputMediaFile(type));
	}
	
	/*
	 * returning image / video
	 */
	private static File getOutputMediaFile(int type) {
	 
	    // External sdcard location
		
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
	            // successfully captured the image
	            // display it in image view
	            previewCapturedImage();
	        } else if (resultCode == RESULT_CANCELED) {
	            // user cancelled Image capture
	            Toast.makeText(getApplicationContext(),
	                    "User cancelled image capture", Toast.LENGTH_SHORT)
	                    .show();
	        } else {
	            // failed to capture image
	            Toast.makeText(getApplicationContext(),
	                    "Sorry! Failed to capture image", Toast.LENGTH_SHORT)
	                    .show();
	        }
	    }
	    
	}
	
	
	private void previewCapturedImage() {
        try {
         
            imagen.setVisibility(View.VISIBLE);
 
            // bimatp factory
            BitmapFactory.Options options = new BitmapFactory.Options();
 
            // downsizing image as it throws OutOfMemory Exception for larger
            // images
            options.inSampleSize = 8;
 
            final Bitmap bitmap = BitmapFactory.decodeFile(fileUri.getPath(),
                    options);
 
            imagen.setImageBitmap(bitmap);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

	
	
	
	
}
