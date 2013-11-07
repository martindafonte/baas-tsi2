package com.trueque;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class VerOfertas extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ver_ofertas);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.ver_ofertas, menu);
		return true;
	}

}
