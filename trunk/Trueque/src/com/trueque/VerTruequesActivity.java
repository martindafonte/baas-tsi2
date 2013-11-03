package com.trueque;

import org.json.JSONException;
import org.json.JSONObject;

import rest.ObterJsonComunicacion;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class VerTruequesActivity extends Activity {
	
	//private TextView tv1;
	private ListView lv1;
	private adaptarElemento adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ver_trueques);
		

        lv1 =(ListView)findViewById(R.id.list);  
        adapter = new adaptarElemento(this);
    
        lv1.setAdapter(adapter);
        
        lv1.setOnItemClickListener(new OnItemClickListener() {
        	
            public void onItemClick(AdapterView<?> myAdapter, View myView, int myItemInt, long mylng) {
              String selectedFromList =(String) (lv1.getItemAtPosition(myItemInt));
              try {
				JSONObject json = new JSONObject(selectedFromList);
				String id = json.getString("_id");
				
				Intent i = new Intent(VerTruequesActivity.this , VerTruequeActivity.class);
		        i.putExtra("id", id);
		        startActivity(i);		        
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
              
            }                 
      });
    
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.ver_trueques, menu);
		return true;
	}
	
	
	// ********* ADAPTAR ITEM ***************
	public class adaptarElemento extends ArrayAdapter<Object>{
		Activity context;
		
		public adaptarElemento(Activity c) {
			// TODO Auto-generated constructor stub
			super(c, R.layout.ver_trueques,MainActivity.trueques);
			this.context = c;
	
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
		
			LayoutInflater i = context.getLayoutInflater();
			View item = i.inflate(R.layout.item_trueque, null);
			
			
			JSONObject j;
			try {
				j = new JSONObject(MainActivity.trueques[position]);
				
				TextView Tipo = (TextView) item.findViewById(R.id.title);
				Tipo.setText(j.getString("Tipo"));
				
				//TextView Valor = (TextView) item.findViewById(R.id.secondLine);
				//Valor.setText(j.getString("Valor"));
				
				TextView Descripcion = (TextView) item.findViewById(R.id.artist);
				Descripcion.setText(j.getString("Descripcion"));
				
				ImageView imagen = (ImageView) item.findViewById(R.id.list_image);
				
				byte [] encodeByte=Base64.decode(j.getString("imagen"),Base64.DEFAULT);
  		        Bitmap bitmap=BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
				imagen.setImageBitmap(bitmap);
  		        
				return item;	
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			return null;
		}
		
	}

}
