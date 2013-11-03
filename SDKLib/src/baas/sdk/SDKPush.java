package baas.sdk;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import baas.sdk.messages.Message;
import baas.sdk.utils.Constants;

class SDKPush implements ISDKPush {

	private static String baseURL;
	private static final int MAX_ATTEMPTS = 3;
	private static final Random random = new Random();
	private static String l_regId;
	private static String l_appId;

	SDKPush(String p_regId, long p_appId) {
		baseURL = Constants.baseURL;
		l_regId = p_regId;
		l_appId = String.valueOf(p_appId);
	}

	@Override
	public Message registrarseCanal(String p_canalId) {
		//TODO Controlar los errores
		sendRegistrationIdToBackend(p_canalId);
		return new Message(Constants.Exito);
	}

	@Override
	public Message desregistrarseCanal(String canalId) {
		//TODO Controlar los errores
			deleteRegistrationIdToBackend(canalId);
		return new Message(Constants.Exito);
	}

	private Boolean sendRegistrationIdToBackend(String p_canalId) {
		String serverUrl = baseURL + "/register";
		Map<String, String> params = new HashMap<String, String>();
		params.put(Constants.appId,l_appId );
		params.put(Constants.canal, p_canalId);
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

	private boolean deleteRegistrationIdToBackend(String p_canalId) {
		String serverUrl = baseURL + "/unregister";
		Map<String, String> params = new HashMap<String, String>();
		params.put(Constants.appId,l_appId );
		params.put(Constants.canal, p_canalId);
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
