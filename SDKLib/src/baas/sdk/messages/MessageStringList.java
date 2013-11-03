package baas.sdk.messages;

import java.util.LinkedList;
import java.util.List;

public class MessageStringList extends Message {
	public List<String> lista;
	public MessageStringList(){
		lista = new LinkedList<String>();
	}
}
