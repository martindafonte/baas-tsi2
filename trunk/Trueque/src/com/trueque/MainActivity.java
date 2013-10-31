package com.trueque;

import org.json.JSONException;
import org.json.JSONObject;
import rest.ListarComunicacion;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	public static  String trueques[];
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	public void cambiar(View view){
		Intent i = new Intent(this, IngresarTrueque.class );
        startActivity(i);
	}
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.item1:
            Toast.makeText(this, "Baas TSI2 214 /n Version 1.0",
                    Toast.LENGTH_SHORT).show();
            break;
        case R.id.item2:
            finish();
        }
        return true;
    }
	
	public void verTrueques(View view) throws JSONException{
		ListarComunicacion j = new ListarComunicacion(this);
	    j.execute("trueque");
	}
}
