package baas.sdk.messages;

import java.util.LinkedList;
import java.util.List;

import org.json.JSONObject;

public class MessageJsonList extends Message {
	public List<JSONObject> resultList;
	
	public MessageJsonList(){
		resultList = new LinkedList<JSONObject>();
	}
}
