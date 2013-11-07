package baas.sdk;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import baas.sdk.messages.MessageJsonList;
import baas.sdk.utils.Constants;
import baas.sdk.utils.Helper_Http;

public class Query {
	private JSONObject consulta;
	private int posicion;
	private int cantPorPagina;
	
	private DefaultHttpClient l_httpClient;
	private String l_appid;
	static String l_baseURL;
	
	
	public Query(JSONObject consulta, int cantPorPagina, String l_appid, String l_baseURL) {
		this.consulta = consulta;
		this.posicion = 0;
		this.cantPorPagina = cantPorPagina;
		l_httpClient = new DefaultHttpClient();
	}
	
	public boolean hasNext(){
		return true;
	}
	
	public MessageJsonList  firstPage(){
		MessageJsonList mj = getJsonList(consulta, 0, cantPorPagina);
		try{
			JSONArray lista = mj.resultList;
			JSONObject j = lista.getJSONObject(lista.length()-1);
			posicion = 0 + cantPorPagina;
			return mj;
		}catch(Exception e)
		{
			return mj;
		}
		
	};
	
	public MessageJsonList nextPage(){
		
		
		MessageJsonList mj = getJsonList(consulta, posicion, cantPorPagina);
		try{
			JSONArray lista = mj.resultList;
			JSONObject j = lista.getJSONObject(lista.length()-1);
			posicion += cantPorPagina; 	
			return mj;
		}catch(Exception e)
		{
			return mj;
		}
		
	};
	
	public MessageJsonList  lastPage(){
		MessageJsonList mj;
		if (posicion == 0){
			mj = new MessageJsonList();
			mj.codigo = Constants.Json_Error_paginado;
			mj.descripcion = "No hay pagina anterior";
			mj.resultList = null;
			return mj;
		}
			
		mj = getJsonList(consulta, posicion, cantPorPagina);
		try{
			JSONArray lista = mj.resultList;
			JSONObject j = lista.getJSONObject(lista.length()-1);
			posicion -= cantPorPagina;
			 
			return mj;
		}catch(Exception e)
		{
			return mj;
		}
	} 
	
	// ************* LLamar al servicio *************
	
	
	public MessageJsonList getJsonList(JSONObject querry, int from, int cant) {
		MessageJsonList mj = new MessageJsonList();
		try {
			String appidString = l_appid;
			String desdeString = Integer.toString(from);
			String cantString = Integer.toString(cant);
			HttpPost post = new HttpPost(l_baseURL +"/" + appidString+ "/listaJson/"
					  + desdeString + "/" + cantString);
			post.setHeader("content-type", "application/json");
			StringEntity entity = new StringEntity(querry.toString());
			post.setEntity(entity);
			HttpResponse resp = l_httpClient.execute(post);
			JSONObject obj = Helper_Http.obtenerJSONRespuesta(resp);
			mj.codigo = Helper_Http.obtenerCodigo(obj);
			mj.descripcion = Helper_Http.obtenerDescripcion(obj);
			mj.resultList = new JSONArray(obj.get(Constants.json).toString());
		} catch (Exception e) {
			mj.codigo = Constants.JSON_Exception;
			mj.descripcion = e.getMessage();
		}
		return mj;
	}
	
	//**** termino *****
	
	
}
