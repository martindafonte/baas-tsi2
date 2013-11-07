package baas.sdk.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.json.JSONException;
import org.json.JSONObject;

public abstract class Helper_Http {
	public static JSONObject obtenerJSONRespuesta(HttpResponse p_response) {
		try {
			HttpEntity httpEntity = p_response.getEntity();
			InputStream is;
			is = httpEntity.getContent();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "iso-8859-1"), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();
			String json = sb.toString();
			JSONObject jObj = new JSONObject(json);
			return jObj;
		} catch (IllegalStateException e) {
			return null;
		} catch (IOException e) {
			return null;
		} catch (JSONException e) {
			return null;
		}
	}

	public static String obtenerDescripcion(JSONObject p_obj) {
		try {
			return p_obj.getString(Constants.descripcion);
		} catch (JSONException e) {
			return "";
		}
	}
	public static int obtenerJsonId(JSONObject p_obj) {
		try {
			return Integer.parseInt(p_obj.getString(Constants.jsonIdMensajeJson));
		} catch (JSONException e) {
			return -1;
		}
	}
	public static int obtenerCodigo(JSONObject p_obj) {
		try {
			return p_obj.getInt(Constants.codigo);
		} catch (JSONException e) {
			return Constants.No_Code_result;
		}
	}
}
