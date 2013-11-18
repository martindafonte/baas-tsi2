/*
 * Copyright 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.trueque;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import baas.sdk.Factory;
import baas.sdk.messages.Message;
import baas.sdk.utils.Constants;
import baas.sdk.utils.exceptions.NotInitilizedException;

public class MainActivity extends Activity {
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;
	public static Context c;
	public static String trueques[];
	// private CharSequence mDrawerTitle;
	private CharSequence mTitle;
	private String[] l_drawerItemList;
	public static String user;
	public final static int op_home = 1;
	public final static int op_user = 0;
	public final static int op_misTrueques = 2;
	public final static int op_cerrarSesion = 3;
	public final static int op_iniciarSesion = 0;
	public final static int op_altaUsuario = 2;
	public final static int op_altaTrueque = 4;
	public final static int op_altaoferta = 5;
	public int vistaActual = -1;
	private IngresarTrueque fIngresarTrueque;
	Bundle args; 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		c = this;
		args = new Bundle();
		mTitle = getTitle();
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.left_drawer);
		user = obtenerUsuarioLogeado();
		// user = "q";
		if (user != null) {
			l_drawerItemList = new String[4];
			l_drawerItemList[0] = user;
			l_drawerItemList[1] = getString(R.string.Home);
			l_drawerItemList[2] = getString(R.string.MisTrueques);
			l_drawerItemList[3] = getString(R.string.CerrarSesion);
		} else {
			l_drawerItemList = new String[3];
			l_drawerItemList[0] = getString(R.string.IniciarSesion);
			l_drawerItemList[1] = getString(R.string.Home);
			l_drawerItemList[2] = getString(R.string.AltaUsuario);
		}
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
				GravityCompat.START);
		mDrawerList.setAdapter(new ArrayAdapter<String>(this,
				R.layout.drawer_list_item, l_drawerItemList));
		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

		// enable ActionBar app icon to behave as action to toggle nav drawer
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

		// ActionBarDrawerToggle ties together the the proper interactions
		// between the sliding drawer and the action bar app icon
		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.ic_drawer, R.string.drawer_open,
				R.string.drawer_close) {
			public void onDrawerClosed(View view) {
				getActionBar().setTitle(mTitle);
				invalidateOptionsMenu(); // creates call to
											// onPrepareOptionsMenu()
			}

			public void onDrawerOpened(View drawerView) {
				invalidateOptionsMenu(); // creates call to
											// onPrepareOptionsMenu()
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		if (savedInstanceState == null) {
			selectItem(1);
		}
		// SyncUtils.CreateSyncAccount(this);
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	/* Called whenever we call invalidateOptionsMenu() */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// If the nav drawer is open, hide action items related to the content
		// view
		boolean vAceptar = false, vCamara = false, vListar = false, vAgregar = false;

		boolean visible = !mDrawerLayout.isDrawerOpen(mDrawerList);
		if (user == null) {
			switch (vistaActual) {
			case op_iniciarSesion:
				break;
			case op_altaUsuario:
				break;
			case op_home:
				vListar  = true;
				break;
			}
		} else {
			switch (vistaActual) {
			case op_home:
				vListar = vAgregar = true;
				break;
			case op_cerrarSesion:
				break;
			case op_misTrueques:
				vListar = vAgregar = true;
				break;
			case op_user:
				break;
			case op_altaTrueque:
				vCamara = vAceptar = true;
				break;
			case op_altaoferta:
				vCamara = vAceptar = true;
				break;
			
			}
		}
		menu.findItem(R.id.itemaceptar).setVisible(visible && vAceptar);
		menu.findItem(R.id.itemcamara).setVisible(visible && vCamara);
		menu.findItem(R.id.listar).setVisible(visible && vListar);
		menu.findItem(R.id.itemAgregar).setVisible(visible && vAgregar);
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// The action bar home/up action should open or close the drawer.
		// ActionBarDrawerToggle will take care of this.
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		// Handle action buttons
		switch (item.getItemId()) {
		case R.id.listar:
			selectItem(op_home);
			return true;
		case R.id.itemAgregar:
			Fragment f = fIngresarTrueque = new IngresarTrueque();
			Bundle args = new Bundle();
			args.putString("modo", "insertar");
			f.setArguments(args);
			FragmentManager fragmentManager = getFragmentManager();
			fragmentManager.beginTransaction().replace(R.id.content_frame, f)
					.commit();
			setTitle("Ingresar Trueque");
			vistaActual = op_altaTrueque;
			invalidateOptionsMenu();
			return true;
		case R.id.itemaceptar:
			fIngresarTrueque.Aceptar();
			return true;
		case R.id.itemcamara:
			fIngresarTrueque.captureImage();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@SuppressWarnings("unused")
	private void cambiarFragment(Fragment f, String title) {
		// args.putInt(PlanetFragment.ARG_PLANET_NUMBER, position);
		// fragment.setArguments(args);
		FragmentManager fragmentManager = getFragmentManager();
		fragmentManager.beginTransaction().replace(R.id.content_frame, f)
				.commit();
		setTitle(title);
	}

	/* The click listner for ListView in the navigation drawer */
	private class DrawerItemClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			selectItem(position);
		}
	}

	private void selectItem(int position) {
		// update the main content by replacing fragments
		Fragment fragment = null;
		if (user == null) {
			switch (position) {
			case op_iniciarSesion:
				fragment = new Login();
				break;
			case op_altaUsuario:
				fragment = new AltaUsuario();
				break;
			case op_home:
				args.putString("idUsuario", null);
				fragment = new VerTruequesActivity();
				break;
			}
		} else {
			switch (position) {
			case op_home:
				args.putString("idUsuario", null);
				fragment = new VerTruequesActivity();
				break;
			case op_cerrarSesion:
				CerrarSesion c = new CerrarSesion(this);
				c.execute();
				break;
			case op_misTrueques:
				args.putString("idUsuario", user);
				fragment = new VerTruequesActivity();
				break;
			case op_user:
				break;
			}
		}
		vistaActual = position;
		invalidateOptionsMenu();
		// Bundle args = new Bundle();
		// args.putInt(PlanetFragment.ARG_PLANET_NUMBER, position);
		// fragment.setArguments(args);
		fragment.setArguments(args);
		FragmentManager fragmentManager = getFragmentManager();
		fragmentManager.beginTransaction()
				.replace(R.id.content_frame, fragment).commit();

		// update selected item and title, then close the drawer
		mDrawerList.setItemChecked(position, true);
		setTitle(l_drawerItemList[position]);
		mDrawerLayout.closeDrawer(mDrawerList);
	}

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getActionBar().setTitle(mTitle);
	}

	public String obtenerUsuarioLogeado() {
		// // Buscar nick
		// SharedPreferences sharedPref = this.getSharedPreferences("claves",
		// Context.MODE_PRIVATE);
		// return sharedPref.getString(Constants.nickapp, null);
		// TODO remove comments
		return null;
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggls
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	public void logeado(String nick) {
		user = nick;
		selectItem(1);
		if (user != null) {
			l_drawerItemList = new String[4];
			l_drawerItemList[0] = user;
			l_drawerItemList[1] = getString(R.string.Home);
			l_drawerItemList[2] = getString(R.string.MisTrueques);
			l_drawerItemList[3] = getString(R.string.CerrarSesion);
			mDrawerList.setAdapter(new ArrayAdapter<String>(this,
					R.layout.drawer_list_item, l_drawerItemList));
		}
	}
	
	class CerrarSesion extends AsyncTask<String, String, String>{
		
		private Context c;
		
		
		public CerrarSesion(Context context){
			c = context;
			
		}
		
		@Override
		protected String doInBackground(String... params) {
			try {
				Factory.initialize(1, c);
				Message m = Factory.getUserSDK().logout();
				if (m.codigo == Constants.Exito){
					SharedPreferences sharedPref = c.getSharedPreferences("claves", Context.MODE_PRIVATE);
					SharedPreferences.Editor editor = sharedPref.edit();
					editor.putString(Constants.nickapp, null);
					editor.commit();
				}else{
					SharedPreferences sharedPref = c.getSharedPreferences("claves", Context.MODE_PRIVATE);
					SharedPreferences.Editor editor = sharedPref.edit();
					editor.putString(Constants.nickapp, null);
					editor.commit();
				}
			} catch (NotInitilizedException e) {
			}
			
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			user = null;
			selectItem(1);
			if (user == null) {
				l_drawerItemList = new String[3];
				l_drawerItemList[0] = getString(R.string.IniciarSesion);
				l_drawerItemList[1] = getString(R.string.Home);
				l_drawerItemList[2] = getString(R.string.AltaUsuario);
				mDrawerList.setAdapter(new ArrayAdapter<String>(getApplicationContext(),
						R.layout.drawer_list_item, l_drawerItemList));
			}
		}
		
		
	}
}