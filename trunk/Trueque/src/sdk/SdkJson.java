package sdk;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

public class SdkJson implements ISDKJson {
	
	private DefaultHttpClient l_httpClient;
	static String l_baseURL = "192.168.0.104";
	String ultimo;
	
	
	/* (non-Javadoc)
	 * @see sdk.ISDKJson#Ingresar(org.json.JSONObject, int)
	 */
	@Override
	public String Ingresar(JSONObject dato, int appid) throws ClientProtocolException, IOException {
		// TODO Auto-generated method stub
		try{	
			HttpClient httpClient = new DefaultHttpClient();
			 
		    HttpPost post = new HttpPost("http://" + l_baseURL +":8080/WebUserManager/MongoServicios/" + Integer.toString(appid));
		 
		    post.setHeader("content-type", "application/json");	
			StringEntity entity = new StringEntity(dato.toString());
	        post.setEntity(entity);
	        
	        HttpResponse resp = httpClient.execute(post);
	        String hola = EntityUtils.toString(resp.getEntity());	
	        
	        return hola;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}    
	}
	
	/* (non-Javadoc)
	 * @see sdk.ISDKJson#obtenerJson(int, int)
	 */
	@Override
	public String obtenerJson(int appid, int jsonid){

		
		try{
			HttpClient l_httpClient = new DefaultHttpClient();
			 
			String id = Integer.toString(jsonid);
			String app = Integer.toString(appid);
			 
			HttpGet del = new HttpGet("http://" + l_baseURL +":8080/WebUserManager/MongoServicios/" +app +"/" + id);
			 
			del.setHeader("content-type", "application/json");
			 
			
		    HttpResponse resp = l_httpClient.execute(del);
		    String hola = EntityUtils.toString(resp.getEntity());
		    
		    return hola; 
		    //String re= EntityUtils.toString(resp.getEntity());
		   
		    
		    
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	
	
	}

	/* (non-Javadoc)
	 * @see sdk.ISDKJson#obtenerLista(org.json.JSONObject, int, int, int)
	 */
	@Override
	public JSONArray obtenerLista(JSONObject filtro, int appid, int desde, int cant) throws ClientProtocolException, IOException {
		// TODO Auto-generated method stub
		try{
			
			
			
			String appidString = Integer.toString(appid);
			String desdeString = Integer.toString(desde);
			String cantString = Integer.toString(cant);
			
			HttpClient httpClient = new DefaultHttpClient();
			HttpPost post = new HttpPost("http://" + l_baseURL + ":8080/WebUserManager/MongoServicios/listaJson/"+appidString+"/" + desde + "/"+ cantString);
				 
			post.setHeader("content-type", "application/json");
			StringEntity entity = new StringEntity(filtro.toString());
	        post.setEntity(entity);
		   
			
			HttpResponse resp = httpClient.execute(post);
		   
			JSONObject respJSON = new JSONObject(EntityUtils.toString(resp.getEntity()));
			return new JSONArray(respJSON.get("json").toString());
		    
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}    
	}

	/* (non-Javadoc)
	 * @see sdk.ISDKJson#ActualizarJson(org.json.JSONObject, int, int)
	 */
	@Override
	public String ActualizarJson(JSONObject json, int id, int appid){
		try{
			HttpClient httpClient = new DefaultHttpClient();
			HttpPut put = new HttpPut("http://" + l_baseURL +":8080/WebUserManager/MongoServicios/"+Integer.toString(appid)+"/" +Integer.toString(id));
				 
			put.setHeader("content-type", "application/json");
			StringEntity entity = new StringEntity(json.toString());
	        put.setEntity(entity);
		   
			
			HttpResponse resp = httpClient.execute(put);
		   
		    return EntityUtils.toString(resp.getEntity());		  
		}
		catch (Exception e) {
			// TODO: handle exception
			return null;	
		}

	}

	/* (non-Javadoc)
	 * @see sdk.ISDKJson#EliminarJson(int, int)
	 */
	@Override
	public String EliminarJson(int id, int appid){
		
		try {
			HttpClient httpClient = new DefaultHttpClient();
			HttpDelete delete = new HttpDelete("http://" + l_baseURL +":8080/WebUserManager/MongoServicios/"+Integer.toString(appid)+"/"+Integer.toString(id));
				 
			delete.setHeader("content-type", "application/json");
	       	
			HttpResponse resp = httpClient.execute(delete);
		   
		    return EntityUtils.toString(resp.getEntity());	

		}catch(Exception e){
			return null;
		}
		
		
	}
}
