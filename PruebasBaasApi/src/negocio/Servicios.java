package negocio;



import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;


/**
 * Session Bean implementation class Servicios
 */
@Stateless
@LocalBean
public class Servicios implements ServiciosLocal {

    /**
     * Default constructor. 
     */
	public static final String baseURL ="http://localhost:8080/WebUserManager";
	public static final String app ="1";
    public Servicios() {
        
    }
    
	
	@Override
	public void login(String nick, String pass){
		HttpClient client = new DefaultHttpClient();
	    HttpPost post = new HttpPost(baseURL + "/Users/" + app + "/" + nick + "/login");
	    try {
	      List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
	      
	      nameValuePairs.add(new BasicNameValuePair("pass", pass));
	      nameValuePairs.add(new BasicNameValuePair("regId", ""));

	      post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
	      HttpResponse response = client.execute(post);
	      BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
	      String line = "";
	      while ((line = rd.readLine()) != null) {
	        System.out.println(line);
	      }
	    } catch (IOException e) {
	    }
	}
	
	@Override
	public void logout(String nick, String pass){
		HttpClient client = new DefaultHttpClient();
	    HttpDelete delete = new HttpDelete(baseURL + "/Users/" + app + "/" + nick + "//login");
	    try {
    	  HttpResponse response = client.execute(delete);
	      BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
	      String line = "";
	      while ((line = rd.readLine()) != null) {
	        System.out.println(line);
	      }
	    } catch (IOException e) {
	    }
	}

	@Override
	public boolean altaUsuario(String nick, String pass, String nombre, String apellido) {
			HttpClient client = new DefaultHttpClient();
		    HttpPost post = new HttpPost(baseURL + "/Users/" + app);
		    try {
		      List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
		      nameValuePairs.add(new BasicNameValuePair("nick", nick));
		      nameValuePairs.add(new BasicNameValuePair("pass", pass));
		      nameValuePairs.add(new BasicNameValuePair("nombre", nombre));
		      nameValuePairs.add(new BasicNameValuePair("apellido",apellido));

		      post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		 
		      HttpResponse response = client.execute(post);
		      BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		      String line = "";
		      while ((line = rd.readLine()) != null) {
		        System.out.println(line);
		      }
		      return true;

		    } catch (IOException e) {
		      return false;
		    }
	}
	
	@Override
	public void setearRolUsuario(String nick, String rol) {
		HttpClient client = new DefaultHttpClient();
	    HttpPost post = new HttpPost(baseURL + "/Users/" + app + "/" + nick + "/Permisos");
	    try {
	      List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
	      nameValuePairs.add(new BasicNameValuePair("rol", rol));
	      post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
	 
	      HttpResponse response = client.execute(post);
	      BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
	      String line = "";
	      while ((line = rd.readLine()) != null) {
	        System.out.println(line);
	      }

	    } catch (IOException e) {
	    }
		
	}
	
	@Override
	public void quitarRolUsuario(String nick, String rol) {
		HttpClient client = new DefaultHttpClient();
	    HttpDelete delete = new HttpDelete(baseURL + "/Users/" + app + "/" + nick + "/Permisos/" + rol );
	    try {
	      HttpResponse response = client.execute(delete);
	      BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
	      String line = "";
	      while ((line = rd.readLine()) != null) {
	        System.out.println(line);
	      }

	    } catch (IOException e) {
	    }
		
	}
	
	@Override
	public List<String> obtenerPermisosUsuario(String nick) throws JSONException {
		HttpClient client = new DefaultHttpClient();
		List<String> lp = new ArrayList<String>();
		HttpGet request = new HttpGet(baseURL + "/Users/" + app + "/" + nick + "/Permisos");
		try {
			HttpResponse response = client.execute(request);
		
			// Get the response
			BufferedReader rd = new BufferedReader (new InputStreamReader(response.getEntity().getContent()));
			StringBuilder sb = new StringBuilder();
			String line = "";
			while ((line = rd.readLine()) != null) {
				sb.append(line + "\n");
			} 
			String json = sb.toString();
			JSONObject jObj = new JSONObject(json);
			JSONArray permisos =jObj.getJSONArray("permisos");
			for(int i=0;i<permisos.length();i++){
				lp.add(permisos.getString(i));
			}
		} catch (IOException e) {
	    }
		return lp;
	}
	
}
