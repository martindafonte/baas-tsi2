package baas.sdk;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import baas.sdk.messages.Message;
import baas.sdk.utils.Constants;
import baas.sdk.utils.Helper_Http;

class SDKPush implements ISDKPush {

	private static String l_baseURL;
	private DefaultHttpClient l_httpClient;
	private static final int MAX_ATTEMPTS = 3;
	private static final Random random = new Random();
	private static String l_regId;
	private static String l_appId;

	SDKPush(String p_regId, long p_appId) {
		l_httpClient = new DefaultHttpClient();
		l_baseURL = Factory.baseURL+"/Push";
		l_regId = p_regId;
		l_appId = String.valueOf(p_appId);
	}

	@Override
	public Message registerInChanel(String p_canalId) {
		//TODO Controlar los errores
		sendRegistrationIdToBackend(p_canalId);
		return new Message(Constants.Exito);
	}

	@Override
	public Message unregisterFromChanel(String canalId) {
		Message mj = new Message();
		try {
			String serverUrl = l_baseURL + "/"+l_appId+"/"+canalId+"/User/"+l_regId;
			HttpDelete desregistrar = new HttpDelete(serverUrl);
			HttpResponse resp =l_httpClient.execute(desregistrar);
			JSONObject obj = Helper_Http.obtenerJSONRespuesta(resp);
			mj.codigo = Helper_Http.obtenerCodigo(obj);
			mj.descripcion = Helper_Http.obtenerDescripcion(obj);
		} catch (Exception e) {
			mj.codigo = Constants.JSON_Exception;
			mj.descripcion = e.getMessage();
		}
		return mj;
	}
	
	@Override
	public Message sendToUser(String nick, String message) {
		Message mj = new Message();
		try {
			String serverUrl = l_baseURL + "/"+l_appId+"/User/"+nick;
			HttpPost send = new HttpPost(serverUrl);
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
			nameValuePairs.add(new BasicNameValuePair(Constants.mensaje, message));
			send.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			HttpResponse resp =l_httpClient.execute(send);
			JSONObject obj = Helper_Http.obtenerJSONRespuesta(resp);
			mj.codigo = Helper_Http.obtenerCodigo(obj);
			mj.descripcion = Helper_Http.obtenerDescripcion(obj);
		} catch (Exception e) {
			mj.codigo = Constants.JSON_Exception;
			mj.descripcion = e.getMessage();
		}
		return mj;
	}

	@Override
	public Message sendToChanel(String message, String chanel) {
		Message mj = new Message();
		try {
			String serverUrl = l_baseURL + "/"+l_appId+"/"+chanel+"/Message";
			HttpPost send = new HttpPost(serverUrl);
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
			nameValuePairs.add(new BasicNameValuePair(Constants.mensaje, message));
			send.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			HttpResponse resp =l_httpClient.execute(send);
			JSONObject obj = Helper_Http.obtenerJSONRespuesta(resp);
			mj.codigo = Helper_Http.obtenerCodigo(obj);
			mj.descripcion = Helper_Http.obtenerDescripcion(obj);
		} catch (Exception e) {
			mj.codigo = Constants.JSON_Exception;
			mj.descripcion = e.getMessage();
		}
		return mj;
	}
	
	private Boolean sendRegistrationIdToBackend(String p_canalId) {
		String serverUrl = l_baseURL + "/"+l_appId+"/"+p_canalId+"/User";
		Map<String, String> params = new HashMap<String, String>();
		params.put(Constants.regId, l_regId);
		for (int i = 1; i <= MAX_ATTEMPTS; i++) {
			long backoff = random.nextInt(1000);
			try {
				post(serverUrl, params);
				return true;
			} catch (IOException e) {
				if (i == MAX_ATTEMPTS) {
					break;
				}
				try {
					Thread.sleep(backoff);
				} catch (InterruptedException e1) {
					// Interrumpieron el thread
					Thread.currentThread().interrupt();
					return false;
				}
			}
		}
		return false;
	}

	
	/**
	 * Issue a POST request to the server.
	 * 
	 * @param endpoint
	 *            POST address.
	 * @param params
	 *            request parameters.
	 * 
	 * @throws IOException
	 *             propagated from POST.
	 */
	private void post(String endpoint, Map<String, String> params)
			throws IOException {
		URL url;
		try {
			url = new URL(endpoint);
		} catch (MalformedURLException e) {
			throw new IllegalArgumentException("invalid url: " + endpoint);
		}
		StringBuilder bodyBuilder = new StringBuilder();
		Iterator<Entry<String, String>> iterator = params.entrySet().iterator();
		// constructs the POST body using the parameters
		while (iterator.hasNext()) {
			Entry<String, String> param = iterator.next();
			bodyBuilder.append(param.getKey()).append('=')
					.append(param.getValue());
			if (iterator.hasNext()) {
				bodyBuilder.append('&');
			}
		}
		String body = bodyBuilder.toString();
		byte[] bytes = body.getBytes();
		HttpURLConnection conn = null;
		try {
			conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setUseCaches(false);
			conn.setFixedLengthStreamingMode(bytes.length);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded;charset=UTF-8");
			// post the request
			OutputStream out = conn.getOutputStream();
			out.write(bytes);
			out.close();
			// handle the response
			int status = conn.getResponseCode();
			if (status != 200) {
				throw new IOException("Post failed with error code " + status);
			}
		} finally {
			if (conn != null) {
				conn.disconnect();
			}
		}
	}
}
