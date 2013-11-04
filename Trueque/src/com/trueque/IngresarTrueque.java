package com.trueque;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import rest.IngresarComunicacion;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;


import android.content.pm.PackageManager;
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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
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
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.creartrueque);
		
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		
		// **********************************************
		//here,we are making a folder named picFolder to store pics taken by the camera using this application
        final String dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/Trueque/"; 
        File newdir = new File(dir); 
        newdir.mkdirs();

        imagen = (ImageView) findViewById(R.id.Preview);
        
        if (isDeviceSupportCamera()){
        	imagen.setOnClickListener(new View.OnClickListener() {
            	public void onClick(View v) {
            		
            		captureImage();
            	
            	};
            });
        }else{
        	// no spoprta camara!
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
	    	case R.id.itemaceptar:
	    		agregarTrueque();
	            return true;
	    	case R.id.itemcamara:
	    		captureImage();
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	
	
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
            
    //        Camara c = new Camara();
     //       c.Bitmap(bitmap, imagen);
            
            imagen.setImageBitmap(bitmap);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }


	
	public void agregarTrueque(){
		 
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
         String encodedImage;
         if (bitmap != null){
        	 ByteArrayOutputStream baos = new ByteArrayOutputStream();
             bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
             encodedImage = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
         }else{
        	 encodedImage = "";
         }
         
//         ByteArrayOutputStream baos = new ByteArrayOutputStream();
//         bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
//         String encodedImage = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
		
		IngresarComunicacion claseInsertar = new IngresarComunicacion(this);
		claseInsertar.execute( "trueque",tipo_var,valor_var,descripcion_var,encodedImage);
		
		//setContentView(R.layout.activity_main);
	 }
	
}
