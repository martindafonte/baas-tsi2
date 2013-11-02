package sdk;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

public class ObtenerJson {
	
	public String obtenerJson(int appid, int jsonid){
	
		try{
			HttpClient httpClient = new DefaultHttpClient();
			 
			String id = Integer.toString(jsonid);
			String app = Integer.toString(appid);
			 
			HttpGet del = new HttpGet("http://192.168.1.41:8080/WebUserManager/MongoServicios/" +app +"/" + id);
			 
			del.setHeader("content-type", "application/json");
			 
			
		    HttpResponse resp = httpClient.execute(del);
		    String hola = EntityUtils.toString(resp.getEntity());
		    
		    return hola; 
		    //String re= EntityUtils.toString(resp.getEntity());
		   
		    
		    
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	
	
	}
}
