package sdk;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

public class IngresarJson {
	
	public String Ingresar(JSONObject dato, int appid) throws ClientProtocolException, IOException {
		// TODO Auto-generated method stub
		try{	
			HttpClient httpClient = new DefaultHttpClient();
			 
		    HttpPost post = new HttpPost("http://192.168.1.41:8080/WebUserManager/MongoServicios/" + Integer.toString(appid));
		 
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
}
