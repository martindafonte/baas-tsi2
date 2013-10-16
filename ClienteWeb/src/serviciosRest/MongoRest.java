package serviciosRest;


import java.net.UnknownHostException;
import javax.ejb.Stateless;
import org.json.JSONException;
import ejb.Mongo;
import ejb.MongoLocal;


@Stateless
public class MongoRest implements MongoPublico  {
	
	public String ObtenerJson(int ClienteId) throws UnknownHostException, JSONException {
		MongoLocal m = new Mongo();
		String json = m.Json("base", ClienteId);
		return json;
	}

	@Override
	public String IngresarJson(int ClienteId, String jsonString) throws UnknownHostException {
		// TODO Auto-generated method stub
		MongoLocal m = new Mongo();
		return m.IngresarJson("base", jsonString, ClienteId);

	}

	@Override
	public String EliminarJson(int ClienteId) throws UnknownHostException {
		// TODO Auto-generated method stub
		MongoLocal m = new Mongo();
		
		return m.EliminarJson("base", ClienteId);
	}

	@Override
	public String ActualizarJson(int ClienteId, String json)
			throws UnknownHostException {
		// TODO Auto-generated method stub
		MongoLocal m = new Mongo();
		
		return m.ActualizarJson("base", json, ClienteId);
	}
}
