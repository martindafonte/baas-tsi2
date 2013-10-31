package com.trueque;

import org.json.JSONException;
import org.json.JSONObject;
import android.os.Bundle;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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
		
	//	tv1=(TextView)findViewById(R.id.TituloTrueques);
        lv1 =(ListView)findViewById(R.id.listTrueques);  
        
        
        adapter = new adaptarElemento(this);
       //ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, MainActivity.trueques);
        lv1.setAdapter(adapter);
        
    
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.ver_trueques, menu);
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
				
				TextView Tipo = (TextView) item.findViewById(R.id.item_tipo);
				Tipo.setText(j.getString("Tipo"));
				
				TextView Valor = (TextView) item.findViewById(R.id.item_valor);
				Valor.setText(j.getString("Valor"));
				
				TextView Descripcion = (TextView) item.findViewById(R.id.item_descripcion);
				Descripcion.setText(j.getString("Descripcion"));
				
				return item;	
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			return null;
		}
		
	}

}
