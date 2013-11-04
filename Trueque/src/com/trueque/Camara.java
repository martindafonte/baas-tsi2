package com.trueque;

import com.trueque.R.id;

import android.os.Bundle;
import android.provider.MediaStore;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Menu;
import android.widget.ImageView;

public class Camara extends Activity {
	
	private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
	ImageView imagen;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_camara);
		
		imagen =(ImageView) findViewById(id.imagen);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.camara, menu);
		return true;
	}
	
	public void sacarfoto(){
		Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
	    startActivityForResult(takePictureIntent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
	
	    handleSmallCameraPhoto(takePictureIntent);
	}
	
	private void handleSmallCameraPhoto(Intent intent) {
	    Bundle extras = intent.getExtras();
	    Bitmap  mImageBitmap = (Bitmap) extras.get("data");
	    imagen.setImageBitmap(mImageBitmap);
	}
	
	public Bitmap Bitmap (String mCurrentPhotoPath , ImageView imagen) {
	    // Get the dimensions of the View
	    int targetW = imagen.getWidth();
	    int targetH = imagen.getHeight();
	  
	    // Get the dimensions of the bitmap
	    BitmapFactory.Options bmOptions = new BitmapFactory.Options();
	    bmOptions.inJustDecodeBounds = true;
	  //  BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
	    int photoW = bmOptions.outWidth;
	    int photoH = bmOptions.outHeight;
	  
	    // Determine how much to scale down the image
	    int scaleFactor = Math.min(photoW/targetW, photoH/targetH);
	  
	    // Decode the image file into a Bitmap sized to fill the View
	    bmOptions.inJustDecodeBounds = false;
	    bmOptions.inSampleSize = scaleFactor;
	    bmOptions.inPurgeable = true;
	  
	 
	   Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
	   return bitmap;
	    //imagen.setImageBitmap(bitmap);
	}
}
