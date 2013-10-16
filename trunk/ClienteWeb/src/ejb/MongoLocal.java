package ejb;

import java.io.IOException;
import java.net.UnknownHostException;
import javax.ejb.Local;
import org.json.JSONException;

@Local
public interface MongoLocal {
	
	public void Crear(String nombreDB, int clienteId) throws IOException;
	public void Eliminar(String nombreDB) throws UnknownHostException;
	public String IngresarJson(String nombreDB, String json, int clienteId) throws UnknownHostException;
	public String Json(String nombreDB, int clienteId) throws UnknownHostException, JSONException;
	public boolean ExisteCliente(String nombreDB, int clienteId) throws UnknownHostException;
	public String ActualizarJson(String nombreDB, String json, int clienteId) throws UnknownHostException;
	public String EliminarJson(String nombreDB, int clienteId) throws UnknownHostException;
}
