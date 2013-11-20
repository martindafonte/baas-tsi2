package baas.sdk;

import java.io.IOException;

import org.apache.http.impl.client.DefaultHttpClient;

import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import baas.sdk.messages.Message;
import baas.sdk.utils.Constants;
import baas.sdk.utils.exceptions.NotInitilizedException;

import com.example.android.network.sync.basicsyncadapter.accounts.GenericAccountService;
import com.example.android.network.sync.basicsyncadapter.provider.FeedProvider;
import com.google.android.gms.gcm.GoogleCloudMessaging;

public abstract class Factory {

	protected static long l_app_id = -1;
	private static SDKUser l_userSDK = null;
	private static SDKPush l_pushSDK = null;
	private static SDKJson l_jsonSDK = null;
	// persistir despues la cookie de sesion
	private static DefaultHttpClient l_httpClient;
	private static Context ctx;
	private static String l_regid;
	private static GoogleCloudMessaging l_gcm;
	private static boolean initiliazed = false;
	// Constants
	public static final String AUTHORITY = "baas.sdk.sync.provider";
	// An account type, in the form of a domain name
	public static final String ACCOUNT_TYPE = "example.com";
	// The account name
	public static final String ACCOUNT = "dummyaccount";

	// Instance fields

	protected static Context getContext() {
		return ctx;
	}

	protected static String getRegid() {
		return l_regid;
	}

	public static Message initialize(long p_app_id, Context p_ctx) {
		Message msg = null;
		l_app_id = 1;
		if (!initiliazed) {
			l_httpClient = new DefaultHttpClient();
			initiliazed = true;
		}
		ctx = p_ctx;
		SharedPreferences settings = p_ctx.getSharedPreferences(
				"baas.sdk.sync", Context.MODE_PRIVATE);
		
		if ((l_regid == "") || (l_regid == null)) {
			l_regid=settings.getString("regid", "");
			if ((l_regid == "") || (l_regid == null)) {
			try {
				registrarGCM();
				msg = new Message(Constants.Exito);
				SharedPreferences.Editor editor = settings.edit();
				editor.putString("regid", l_regid);
				editor.commit();
			} catch (IOException e) {
				msg = new Message(Constants.GCM_Problem_Registering);
				msg.descripcion += " Excepcion: " + e.getMessage();
			}
			}
		}
		if (msg == null) {
			msg = new Message(Constants.Exito);
		}
		ContentResolver.setSyncAutomatically(GenericAccountService.GetAccount(),FeedProvider.CONTENT_AUTHORITY, true);
		return msg;
	}

	public static ISDKUser getUserSDK() throws NotInitilizedException {
		if (l_app_id == -1) {
			throw new NotInitilizedException();
		}
		if (l_userSDK == null) {
			l_userSDK = new SDKUser(l_httpClient, l_app_id, ctx);
		}
		return (ISDKUser) l_userSDK;
	}

	public static ISDKPush getPushSDK() throws NotInitilizedException {
		if (l_app_id == -1) {
			throw new NotInitilizedException();
		}
		if (l_pushSDK == null) {
			l_pushSDK = new SDKPush(l_regid, l_app_id);
		}
		return (ISDKPush) l_pushSDK;
	}

	public static ISDKJson getJsonSDK() throws NotInitilizedException {
		if (l_app_id == -1) {
			throw new NotInitilizedException();
		}
		if (l_jsonSDK == null) {
			l_jsonSDK = new SDKJson(l_app_id, ctx);
		}
		return (ISDKJson) l_jsonSDK;
	}

	private static void registrarGCM() throws IOException {
		l_gcm = GoogleCloudMessaging.getInstance(ctx);
		l_regid = l_gcm.register(Constants.SENDER_ID);
	}
}
