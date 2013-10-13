package ejb;

import java.io.IOException;
import java.net.UnknownHostException;

import javax.ejb.Local;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

import org.json.JSONException;
import org.json.JSONObject;

import com.mongodb.util.JSON;

//@Path("Mongo")
@Local
public interface MongoLocal {
	//@GET
	public void Crear(String nombreDB, int clienteId) throws IOException;
	public void Eliminar(String nombreDB) throws UnknownHostException;
	public void IngresarJson(String nombreDB, JSONObject json, int clienteId) throws UnknownHostException;
	public JSONObject Json(String nombreDB, int clienteId) throws UnknownHostException, JSONException;
	public boolean ExisteCliente(String nombreDB, int clienteId) throws UnknownHostException;
	public void ActualizarJson(String nombreDB, JSONObject json, int clienteId) throws UnknownHostException;
	public void EliminarJson(String nombreDB, int clienteId) throws UnknownHostException;
}
