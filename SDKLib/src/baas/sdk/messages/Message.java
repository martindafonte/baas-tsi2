package baas.sdk.messages;

/**Clase que devuelve el resultado de una operaci�n */
public class Message {
	public Message(){};
	public Message(int p_codigo, String p_descripcion){
		codigo = p_codigo;
		descripcion = p_descripcion;
	}
	/**Esta funci�n recibe como parametro un codigo, y utiliza la funcion getDescription para obtener la descripcion
	 * */
	public Message(int p_codigo){
		
	}
	public int codigo;
	public String descripcion;
}
