package com.trueque;

import baas.sdk.Factory;

import com.example.android.network.sync.basicsyncadapter.SyncUtils;
import com.example.android.network.sync.basicsyncadapter.accounts.GenericAccountService;
import com.example.android.network.sync.basicsyncadapter.provider.FeedProvider;
import baas.sdk.Factory;
import baas.sdk.messages.Message;
import baas.sdk.utils.Constants;
import baas.sdk.utils.exceptions.NotInitilizedException;
import rest.CrearUsuario;
import rest.ListarComunicacion;
import android.accounts.Account;
import android.app.ActionBar;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SyncStatusObserver;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;


public class MainActivity extends Activity implements OnItemClickListener  {
	
	public static Context c;
	public static  String trueques[];
	private DrawerLayout mDrawer;
	private ListView mDrawerOptions;
	private String[] values; 
	private String nick;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		c = this;
		
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
	
		// Buscar nick
		SharedPreferences sharedPref = this.getSharedPreferences("claves", Context.MODE_PRIVATE);
		nick = sharedPref.getString(Constants.nickapp, null);
		try {
			if ((nick != null) ) {
				// preguntar si esta logueado contra el servidor.
				values = new String[3];
				values[0] = nick;
				values[1] = "Mis trueques";
				values[2] = "Cerrar sesion";
			}else{
				values = new String[2];
				values[0] = "Iniciar Sesion";
				values[1] = "Alta Usuario";
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 mDrawerOptions = (ListView) findViewById(R.id.left_drawer);
	     mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
	     mDrawerOptions.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, values));
	     mDrawerOptions.setOnItemClickListener(this);
	     SharedPreferences settings = getApplicationContext().getSharedPreferences("baas.sdk.sync", Context.MODE_PRIVATE);
		 SharedPreferences.Editor editor = settings.edit();
	      editor.putString("key1", "value1");
	      editor.commit();
	      Factory.initialize(0, getApplicationContext());
	     SyncUtils.CreateSyncAccount(this);
	     
	}
	
	
	        
	    
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu); 
		MenuInflater inflater = getMenuInflater();
	    
		
		inflater.inflate(R.menu.main_activity_actions, menu);
	    if (nick == null){
	    	menu.removeItem(R.id.itemAgregar);
	    }	
	    return true;
	    	
		
	}
	
	@Override
	public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
	    if (values[i].equals("Iniciar Sesion")){
	    	Intent in = new Intent(this, Login.class);
	        startActivity(in);
	    }else if(values[i].equals("Alta Usuario")){
	    	Intent in = new Intent(this, AltaUsuario.class);
	        startActivity(in);
	    }else if(values[i].equals("My perfil")){
	    	
	    }else if(values[i].equals("Mis trueques")){
	    	verTrueques(nick);
	    }else if(values[i].equals("Cerrar sesion")){
	    	cerrarSesion();
	    }
	}
	
	private void cerrarSesion() {
		CerrarSesion c = new CerrarSesion(this);
		c.execute();
		
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items
	    switch (item.getItemId()) {
	    	case android.R.id.home:
	    		if (mDrawer.isDrawerOpen(mDrawerOptions)){
	                mDrawer.closeDrawers();
	            }else{
	                mDrawer.openDrawer(mDrawerOptions);
	            }
	    	      return true;
	    
	    	case R.id.listar:
	            //openSearch();
				verTrueques(null);
	            return true;
	        case R.id.itemAgregar:
	            //openSettings();
	        	cambiar();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	public void cambiar(){
		Intent i = new Intent(this, IngresarTrueque.class);
		i.putExtra("modo", "insertar");
        startActivity(i);
	}

	public void verTrueques(String n) {
		ListarComunicacion j = new ListarComunicacion(this,n);
	    j.execute("trueque");
	}
	
	
	 /**
     * Crfate a new anonymous SyncStatusObserver. It's attached to the app's ContentResolver in
     * onResume(), and removed in onPause(). If status changes, it sets the state of the Refresh
     * button. If a sync is active or pending, the Refresh button is replaced by an indeterminate
     * ProgressBar; otherwise, the button itself is displayed.
     */
    private SyncStatusObserver mSyncStatusObserver = new SyncStatusObserver() {
        /** Callback invoked with the sync adapter status changes. */
        @Override
        public void onStatusChanged(int which) {
            Runnable r =new Runnable() {
                /**
                 * The SyncAdapter runs on a background thread. To update the UI, onStatusChanged()
                 * runs on the UI thread.
                 */
                @Override
                public void run() {
                    // Create a handle to the account that was created by
                    // SyncService.CreateSyncAccount(). This will be used to query the system to
                    // see how the sync status has changed.
                    Account account = GenericAccountService.GetAccount();
                    if (account == null) {
                        // GetAccount() returned an invalid value. This shouldn't happen, but
                        // we'll set the status to "not refreshing".
//                        setRefreshActionButtonState(false);
                        return;
                    }

                    // Test the ContentResolver to see if the sync adapter is active or pending.
                    // Set the state of the refresh button accordingly.
                    boolean syncActive = ContentResolver.isSyncActive(
                            account, FeedProvider.CONTENT_AUTHORITY);
                    boolean syncPending = ContentResolver.isSyncPending(
                            account, FeedProvider.CONTENT_AUTHORITY);
//                    setRefreshActionButtonState(syncActive || syncPending);
                }
            };
            r.run();
        }
    };
}


class CerrarSesion extends AsyncTask<String, String, String>{
	
	private Context c;
	
	
	public CerrarSesion(Context context){
		c = context;
		Factory.initialize(0, c);
	}
	
	@Override
	protected String doInBackground(String... params) {
		try {
			
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	protected void onPostExecute(String result) {
		
		super.onPostExecute(result);
		Intent i = new Intent(this.c.getApplicationContext(),MainActivity.class);
		this.c.startActivity(i);
	}
	
	
}

