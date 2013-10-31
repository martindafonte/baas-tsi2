package baas.sdk;

import java.io.IOException;

import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Context;
import baas.sdk.messages.Message;
import baas.sdk.utils.Constants;
import baas.sdk.utils.exceptions.NotInitilizedException;

import com.google.android.gms.gcm.GoogleCloudMessaging;

public abstract class Factory {

	protected static long l_app_id=-1;
	private static SDKUser l_userSDK = null;
	private static SDKPush l_pushSDK = null;
	private static DefaultHttpClient l_httpClient;
	private static Context ctx;
	private static String l_regid;
	private static GoogleCloudMessaging l_gcm;

	public static Message initialize(long p_app_id, Context p_ctx) {
		Message msg;
		l_app_id = p_app_id;
		l_httpClient = new DefaultHttpClient();
		ctx = p_ctx;
		try {
			registrarGCM();
			msg= new Message(Constants.Exito);
		} catch (IOException e) {
			msg=new Message(Constants.GCM_Problem_Registering);
			msg.descripcion+= " Excepcion: "+e.getMessage();
		}
		return msg;
	}

	public static ISDKUser getUserSDK() throws NotInitilizedException {
		if(l_app_id==-1){
			throw new NotInitilizedException();
		}
		if (l_userSDK == null) {
			l_userSDK = new SDKUser(l_httpClient, l_app_id);
		}
		return (ISDKUser) l_userSDK;
	}
	
	public static ISDKPush getPushSDK() throws NotInitilizedException {
		if(l_app_id==-1){
			throw new NotInitilizedException();
		}
		if (l_pushSDK == null) {
			l_pushSDK = new SDKPush(l_regid, l_app_id);
		}
		return (ISDKPush) l_pushSDK;
	}

	private static void registrarGCM() throws IOException {
		l_gcm = GoogleCloudMessaging.getInstance(ctx);
		l_regid = l_gcm.register(Constants.SENDER_ID);
	}
}
