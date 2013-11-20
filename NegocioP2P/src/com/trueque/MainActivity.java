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
import org.json.JSONException;
import org.json.JSONObject;

import rest.EliminarComunicacion;
import org.json.JSONException;
import org.json.JSONObject;

import rest.EliminarComunicacion;

import com.google.gson.JsonObject;
import com.example.android.network.sync.basicsyncadapter.SyncUtils;
import com.trueque.BaseFragment.ChangeFragment;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
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

public class MainActivity extends Activity implements ChangeFragment{
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;
	public static Context c;
	public static String trueques[];
	public int indice = -1;
	public static String ofertas[];
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
	public final static int op_verofertas = 6;
	public final static int op_veroferta = 7;
	public final int op_verTrueque = 7;
	public final int op_editarTrueque = 8;
	public boolean op_miTrueque = false;
	
	public int vistaActual = -1;
	private IngresarTrueque fIngresarTrueque;
	private CrearOferta fIngresarOferta;
	private VerOfertas fVerOfertas;
	private VerTruequesActivity fVerTrueques = null;
	private Fragment actual = null;
	FragmentManager fragmentManager;
	public String imagenGrande;
	Fragment f;
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
		SyncUtils.CreateSyncAccount(this);
	}

	@Override
	protected void onStart() {
		super.onStart();
//		Intent in = getIntent();
		//TODO ver si me llega un intent que hacer con el
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
		boolean vAceptar = false, vCamara = false, vListar = false, vAgregar = false, vEditar = false, vBorrar = false ;

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
				return true;
			case op_altaTrueque:
				vCamara = vAceptar = true;
				break;
			case op_altaoferta:
				vCamara = vAceptar = true;
				break;
			case op_verTrueque:
				if (op_miTrueque) {
					vEditar = vBorrar = true;
				}
				break;
			case op_editarTrueque:
				vCamara = vAceptar = true;
				break;
			}
		}
		menu.findItem(R.id.itemaceptar).setVisible(visible && vAceptar);
		menu.findItem(R.id.itemcamara).setVisible(visible && vCamara);
		menu.findItem(R.id.listar).setVisible(visible && vListar);
		menu.findItem(R.id.itemAgregar).setVisible(visible && vAgregar);
		menu.findItem(R.id.itemeborrar).setVisible(visible && vBorrar);
		menu.findItem(R.id.itemeditar).setVisible(visible && vEditar);
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
			if (vistaActual == op_altaTrueque){
				fIngresarTrueque.Aceptar();
			}else if(vistaActual == op_altaoferta){
				fIngresarOferta.agregarOferta();
			}else if (vistaActual == op_editarTrueque){
				try {
					fIngresarTrueque.editarTrueque();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return true;
		case R.id.itemcamara:
			fIngresarTrueque.captureImage();
			return true;
		case R.id.itemeborrar:
    		 try {
    			 EliminarComunicacion eliminar = new EliminarComunicacion(this);
				JSONObject j = new JSONObject(trueques[indice]);
				 eliminar.execute(j);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		case R.id.itemeditar:
			f = fIngresarTrueque = new IngresarTrueque();
			args = new Bundle();
			args.putString("modo", "editar");
			args.putString("trueque", trueques[indice]);
			args.putString("imagen", imagenGrande);
			f.setArguments(args);
			fragmentManager = getFragmentManager();
			fragmentManager.beginTransaction().replace(R.id.content_frame, f)
					.commit();
			setTitle("Editar Trueque");
			vistaActual = op_editarTrueque;
			invalidateOptionsMenu();
   		
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
//		Bundle args = new Bundle();
		Fragment fragment = null;
		boolean noCambia = false;
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
				if(fVerTrueques == null)
					fVerTrueques= new VerTruequesActivity();
				fragment =fVerTrueques;
				break;
			}
		} else {
			switch (position) {
			case op_home:
				args.putString("idUsuario", null);
				if(fVerTrueques == null)
					fVerTrueques=new VerTruequesActivity();
				fragment = fVerTrueques;
				break;
			case op_cerrarSesion:
				CerrarSesion c = new CerrarSesion(this);
				c.execute();
				if(fVerTrueques == null)
					fVerTrueques=new VerTruequesActivity();
				fragment = fVerTrueques;
				break;
			case op_misTrueques:
				args.putString("idUsuario", user);
				fragment = new VerTruequesActivity();
				break;
			case op_user:
				break;
			}
		}
		if(!fragment.equals(actual)){
			vistaActual = position;
			invalidateOptionsMenu();
		// 	args.putInt(PlanetFragment.ARG_PLANET_NUMBER, position);
		// fragment.setArguments(args);
			fragment.setArguments(args);
			FragmentManager fragmentManager = getFragmentManager();
			fragmentManager.beginTransaction()
				.replace(R.id.content_frame, fragment).commit();

		// update selected item and title, then close the drawer
			mDrawerList.setItemChecked(position, true);
			setTitle(l_drawerItemList[position]);
		}
			actual = fragment;
			mDrawerLayout.closeDrawer(mDrawerList);
	}

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getActionBar().setTitle(mTitle);
	}

	public String obtenerUsuarioLogeado() {
		// // Buscar nick
		 SharedPreferences sharedPref = this.getSharedPreferences("claves",
		 Context.MODE_PRIVATE);
		 return sharedPref.getString(Constants.nickapp, null);

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

	@Override
	public void changeFragment(int pantalla, Fragment f) {
		
		vistaActual = pantalla;
		switch (vistaActual) {
		case op_altaoferta:
			fIngresarOferta =(CrearOferta) f;
			break;

		case op_verofertas:
			fVerOfertas = (VerOfertas) f;
			break;
		}
	}

	@Override
	public boolean onNavigateUp() {
		if (vistaActual == op_home){
			return super.onNavigateUp();
		}else{
			selectItem(op_home);
			return true;
		}
	}

	@Override
	public boolean onNavigateUpFromChild(Activity child) {
		if (vistaActual == op_home){
			return super.onNavigateUp();
		}else{
			selectItem(op_home);
			return true;
		}
	}
	
	
}