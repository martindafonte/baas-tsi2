package beans;

import java.io.IOException;

import javax.ejb.EJB;
import org.json.JSONException;
import org.json.JSONObject;
import ejb.MongoLocal;

public class Login   {
	
	/**
	 * 
	 */
	
	private String nombre;
	private String apellido;
	
	@EJB
	private MongoLocal m;

	public Login() {
	
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellido() {
		return apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}
	
	public String ingresarDatos() throws IOException, JSONException{
	
		m.Crear("base",0);
	//	m.Emliminar("soynueva");
	//	JSON json = new JSON();
		JSONObject json = new JSONObject();
		json.put("Nombre", "Juan");
		json.put("Apellido", "Rodriguez");
		json.put("Edad", "23");
		
		//m.IngresarJson("base", json, 8);
	//	m.IngresarJson("base", json, 8);
		
//		JSONObject json2 = m.Json("base", 8);
//		System.out.println("voy a imprimir el json ***************************** \n\n");
//		System.out.println(json2.toString());
//		System.out.println(" ***************************** \n\n");
//		JSONObject json3 = m.Json("base", 90);
		//m.IngresarJson("base", json, 8);
		
		JSONObject json6 = new JSONObject();
		json6.put("Nombre", "Ana");
		json6.put("Apellido", "Rodriguez");
		json6.put("Edad", "23");
		//m.IngresarJson("base", json6, 1);
		//m.ActualizarJson("base", json6, 8);
		
		//m.EliminarJson("base", 8);
		//m.EliminarJson("base", 8);
		return "index.jsp";
	}
	
}
