package baas.sdk;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import baas.sdk.messages.Message;
import baas.sdk.messages.MessageStringList;
import baas.sdk.messages.MessageUser;
import baas.sdk.utils.Constants;
import baas.sdk.utils.Helper_Http;
import baas.sdk.utils.User;

class SDKUser implements ISDKUser {

	private DefaultHttpClient l_httpClient;
	private String l_appid;
	private String l_baseURL;
	private String logged_nick;
	private String l_regid;

	SDKUser(DefaultHttpClient p_httpclient, long p_app_id, String p_regid) {
		l_httpClient = p_httpclient;
		l_appid = String.valueOf(p_app_id);
		l_baseURL = baas.sdk.utils.Constants.baseURL + "/Users";
		l_regid = p_regid;
	}

	@Override
	public Message login(String nick, String pass) {
		Message msg;
		try {
			HttpPost login = new HttpPost(l_baseURL + "/" + l_appid+"/"+nick+"/login");
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
			
			nameValuePairs.add(new BasicNameValuePair(Constants.pass, pass));
			nameValuePairs.add(new BasicNameValuePair("regid", l_regid));
			login.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			HttpResponse resp = l_httpClient.execute(login);
			JSONObject jObj = Helper_Http.obtenerJSONRespuesta(resp);
			if (jObj == null) {
				msg = new Message(Constants.User_Login_parsingException);
			} else {
				msg = new Message();
				msg.codigo = Helper_Http.obtenerCodigo(jObj);
				msg.descripcion = Helper_Http.obtenerDescripcion(jObj);
			}
			return msg;
		} catch (Exception ex) {
			msg = new Message(Constants.User_exception);
			return msg;
		}
	}

	@Override
	public Message register(User user) {
		Message msg;
		try {
			HttpPost register = new HttpPost(l_baseURL + "/" + l_appid);
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(4);
			nameValuePairs
					.add(new BasicNameValuePair(Constants.nick, user.Nick));
			nameValuePairs.add(new BasicNameValuePair(Constants.nombre,
					user.Name));
			nameValuePairs.add(new BasicNameValuePair(Constants.pass,
					user.Password));
			nameValuePairs.add(new BasicNameValuePair(Constants.apellido,
					user.Surename));
			register.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			HttpResponse resp = l_httpClient.execute(register);
			JSONObject jObj = Helper_Http.obtenerJSONRespuesta(resp);
			if (jObj == null) {
				msg = new Message(Constants.User_Login_parsingException);
			} else {
				msg = new Message();
				msg.codigo = Helper_Http.obtenerCodigo(jObj);
				msg.descripcion = Helper_Http.obtenerDescripcion(jObj);
			}
			return msg;
		} catch (Exception ex) {
			msg = new Message(Constants.User_exception, ex.getMessage());
			return msg;
		}
	}

	@Override
	public Message logout() {
		Message msg = new Message();
		try {
			if ((logged_nick != null) || logged_nick.isEmpty()) {
				msg.codigo = Constants.User_no_logged_user;
				msg.descripcion = "No hay ningún usuario logeado";
				return msg;
			}
			HttpDelete register = new HttpDelete(l_baseURL + "/" + l_appid
					+ "/" + logged_nick + "/login");
			HttpResponse resp = l_httpClient.execute(register);
			JSONObject jObj = Helper_Http.obtenerJSONRespuesta(resp);
			if (jObj == null) {
				msg = new Message(Constants.User_Login_parsingException);
			} else {
				msg.codigo = Helper_Http.obtenerCodigo(jObj);
				msg.descripcion = Helper_Http.obtenerDescripcion(jObj);
			}
			logged_nick = null;
			l_httpClient = new DefaultHttpClient();
			return msg;
		} catch (Exception ex) {
			msg.codigo = Constants.User_exception;
			msg.descripcion = ex.getMessage();
			return msg;
		}
	}

	@Override
	public MessageUser getUser(String nick) {
		MessageUser msg = new MessageUser();
		try {
			HttpGet obtener = new HttpGet(l_baseURL + "/" + l_appid + "/"
					+ nick);
			HttpResponse resp = l_httpClient.execute(obtener);
			JSONObject jObj = Helper_Http.obtenerJSONRespuesta(resp);
			if (jObj == null) {
				msg.codigo=Constants.User_Login_parsingException;
			} else {
				msg.codigo = Helper_Http.obtenerCodigo(jObj);
				msg.descripcion = Helper_Http.obtenerDescripcion(jObj);
				User u = new User();
				u.Name =jObj.getString(Constants.nombre);
				u.Surename =jObj.getString(Constants.apellido);
				u.Nick =jObj.getString(Constants.nick);
				u.ChanelId =jObj.getString(Constants.canalcodigo);
				msg.user = u;
			}
			return msg;
		} catch (Exception ex) {
			msg.codigo = Constants.User_exception;
			msg.descripcion = ex.getMessage();
			return msg;
		}
	}

	@Override
	public MessageStringList getUserPermissions(String nick) {
		MessageStringList msg = new MessageStringList();
		try {
			HttpGet obtener = new HttpGet(l_baseURL + "/" + l_appid + "/"
					+ nick+"/Permisos");
			HttpResponse resp = l_httpClient.execute(obtener);
			JSONObject jObj = Helper_Http.obtenerJSONRespuesta(resp);
			if (jObj == null) {
				msg.codigo=Constants.User_Login_parsingException;
			} else {
				msg.codigo = Helper_Http.obtenerCodigo(jObj);
				msg.descripcion = Helper_Http.obtenerDescripcion(jObj);
				JSONArray permisos =jObj.getJSONArray(Constants.permisos);
				for(int i=0;i<permisos.length();i++){
					msg.lista.add(permisos.getString(i));
				}
			}
		} catch (Exception ex) {
			msg.codigo = Constants.User_exception;
			msg.descripcion = ex.getMessage();
		}
		return msg;
	}

	@Override
	public Message setUserRole(String nick, String role) {
		Message msg;
		try {
			HttpPost set = new HttpPost(l_baseURL+"/"+l_appid+"/"+nick+"/Permisos");
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
			nameValuePairs.add(new BasicNameValuePair(Constants.rol, role));
			set.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			HttpResponse resp = l_httpClient.execute(set);
			JSONObject jObj = Helper_Http.obtenerJSONRespuesta(resp);
			if (jObj == null) {
				msg = new Message(Constants.User_Login_parsingException);
			} else {
				msg = new Message();
				msg.codigo = Helper_Http.obtenerCodigo(jObj);
				msg.descripcion = Helper_Http.obtenerDescripcion(jObj);
			}
			return msg;
		} catch (Exception ex) {
			msg = new Message(Constants.User_exception);
			return msg;
		}
	}

	@Override
	public Message removeUserRole(String nick, String role) {
		Message msg;
		try {
			HttpDelete set = new HttpDelete(l_baseURL+"/"+l_appid+"/"+nick+"/Permisos/"+role);
			HttpResponse resp = l_httpClient.execute(set);
			JSONObject jObj = Helper_Http.obtenerJSONRespuesta(resp);
			if (jObj == null) {
				msg = new Message(Constants.User_Login_parsingException);
			} else {
				msg = new Message();
				msg.codigo = Helper_Http.obtenerCodigo(jObj);
				msg.descripcion = Helper_Http.obtenerDescripcion(jObj);
			}
			return msg;
		} catch (Exception ex) {
			msg = new Message(Constants.User_exception);
			return msg;
		}
	}

	@Override
	public boolean isloggedIn(String nick) throws Exception {
		if(isNetworkAvailable()){
				HttpGet obtener = new HttpGet(l_baseURL + "/" + l_appid + "/"
						+ nick+"/login");
				HttpResponse resp = l_httpClient.execute(obtener);
				JSONObject jObj = Helper_Http.obtenerJSONRespuesta(resp);
				if (jObj == null) {
					throw new Exception("Error"+Constants.User_Login_parsingException);
				} else {
					if(Helper_Http.obtenerCodigo(jObj)==0){
						return true;
					}else{
						return false;
					}
				}
		}else{
			return((logged_nick != null) &&(!logged_nick.isEmpty()));
		}
	}
	
	private boolean isNetworkAvailable() {
	    ConnectivityManager connectivityManager 
	          = (ConnectivityManager) Factory.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
	    return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}
}
