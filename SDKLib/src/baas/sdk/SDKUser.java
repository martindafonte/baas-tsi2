package baas.sdk;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import baas.sdk.messages.Message;
import baas.sdk.messages.MessageStringList;
import baas.sdk.messages.MessageUser;
import baas.sdk.utils.Constants;
import baas.sdk.utils.Http;
import baas.sdk.utils.User;

class SDKUser implements ISDKUser {

	private DefaultHttpClient l_httpClient;
	private long l_appid;
	private String l_baseURL;

	SDKUser(DefaultHttpClient p_httpclient, long p_app_id) {
		l_httpClient = p_httpclient;
		l_appid = p_app_id;
		l_baseURL = baas.sdk.utils.Constants.baseURL + "/Users";
	}

	@Override
	public Message login(String nick, String pass) {
		Message msg;
		try {
			HttpPost login = new HttpPost(l_baseURL);
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			nameValuePairs.add(new BasicNameValuePair("apid", String.valueOf(l_appid)));
			nameValuePairs.add(new BasicNameValuePair("nick", nick));
			nameValuePairs.add(new BasicNameValuePair("pass", pass));
			login.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			HttpResponse resp = l_httpClient.execute(login);
			JSONObject jObj = Http.obtenerJSONRespuesta(resp);
			if (jObj == null) {
				msg=new Message( Constants.User_Login_parsingException);
			} else {
				msg = new Message();
				msg.codigo = jObj.getInt("codigo");
				msg.descripcion = jObj.getString("descripcion");
			}
			return msg;
		} catch (Exception ex) {
			msg=new Message( Constants.User_login_exception);
			return msg;
		}
	}

	@Override
	public Message register(User user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Message logout() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MessageUser getUser(String nick) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MessageStringList getUserPermissions(String nick) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Message setUserRole(String nick, String role) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Message removeUserRole(String nick, String role) {
		// TODO Auto-generated method stub
		return null;
	}

}
