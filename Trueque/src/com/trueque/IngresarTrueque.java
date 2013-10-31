package com.trueque;

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
import android.util.Log;
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
	
	private static final String TAG = "CameraDemo";
	Camera camera;
	
	Button botonImagen;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.creartrueque);
		
		
		// **********************************************
		//here,we are making a folder named picFolder to store pics taken by the camera using this application
        final String dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/Trueque/"; 
        File newdir = new File(dir); 
        newdir.mkdirs();

        imagen = (ImageView) findViewById(R.id.Preview);
        Button botonImagen = (Button) findViewById(R.id.btnCamara);
        botonImagen.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {
        	
        	// capture picture
            captureImage();
        	
        	
         //    here,counter will be incremented each time,and the picture taken by camera will be stored as 1.jpg,2.jpg and likewise.
//            count++;
//            String file = dir+count+".jpg";
//            File newfile = new File(file);
//            try {
//                newfile.createNewFile();
//            } catch (IOException e) {}       
//
//            Uri outputFileUri = Uri.fromFile(newfile);
//
//            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); 
//            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
//
//            startActivityForResult(cameraIntent, TAKE_PHOTO_CODE);
        }
    });
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
 
            imagen.setImageBitmap(bitmap);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }


	
	public void agregarTrueque(View boton){
		 
		final EditText tipo =  (EditText)findViewById(R.id.EditTextTipo);
		String tipo_var = tipo.getText().toString();

		final EditText valor = (EditText)findViewById(R.id.editTextValor);
		String valor_var  = valor.getText().toString();
		
		final EditText descripcion = (EditText)findViewById(R.id.editTextDescripcion);
		String descripcion_var = descripcion.getText().toString();
		String a = imagen.toString();
		
		IngresarComunicacion claseInsertar = new IngresarComunicacion(this);
		claseInsertar.execute( "trueque",tipo_var,valor_var,descripcion_var,a);
		
		//setContentView(R.layout.activity_main);
	 }
	
}
