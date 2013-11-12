package com.trueque;

import modelo.Oferta;

import org.json.JSONException;

import rest.IngresarComunicacion;
import baas.sdk.utils.Constants;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

public class CrearOferta extends Activity {
	private Spinner spinner;
	private Spinner spinnerMoneda; 
	String id;
	String nickPropietario;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.crear_oferta);
		
		Bundle bundle = getIntent().getExtras();
		id = bundle.getString("nick_trueque");
		nickPropietario = bundle.getString("nicktrueque");
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
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.crear_oferta, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    	case android.R.id.home:
	    		Intent i = new Intent(this, MainActivity.class );
	            startActivity(i);
	            return true;
	    	case R.id.aceptarOferta:
	    		agregarOferta();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}

	private void agregarOferta() {
		Oferta o = new Oferta();
		Spinner spinner = (Spinner) findViewById(R.id.spinnerCategorias);
		o.tipo = spinner.getSelectedItemPosition();
		Spinner spinnerMoneda = (Spinner) findViewById(R.id.spinnerMoneda);
		o.moneda = spinnerMoneda.getSelectedItemPosition();
		final EditText valor = (EditText)findViewById(R.id.editTextValor);
		o.valor = Integer.parseInt(valor.getText().toString());
		final EditText descripcion = (EditText)findViewById(R.id.editTextDescripcion);
		o.descripcion = descripcion.getText().toString();
		BitmapFactory.Options options = new BitmapFactory.Options();
		
//        options.inSampleSize = 8;
//        String imagenchica;
//        String imagenGrande;
//        if (fileUri != null){
//       	 	Bitmap bitmap = BitmapFactory.decodeFile(fileUri.getPath(),options);
//       	 	imagenchica = Camara.redimensionarImagenMaximo(bitmap, 53, 53); 	 
//       	 	imagenGrande = Camara.redimensionarImagenMaximo(bitmap,580,285);
//        }else{
//       	 imagenchica = "";
//       	 imagenGrande = "";
//        }
//        
//        t.imagenChica = imagenchica;
//        t.imagenGrande = imagenGrande;
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
		SharedPreferences sharedPref = this.getSharedPreferences("claves", Context.MODE_PRIVATE);
		o.nick = sharedPref.getString(Constants.nickapp, null);
		o.nickDestinatario = nickPropietario;
		o.idTrueque = id;
		IngresarComunicacion claseInsertar = new IngresarComunicacion(this);
		claseInsertar.execute(o);
		
	}

}
