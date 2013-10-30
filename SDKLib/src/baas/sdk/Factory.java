package baas.sdk;

import org.apache.http.impl.client.DefaultHttpClient;

public abstract class Factory {

	protected static long l_app_id;	
	private static SDKUser l_userSDK=null;
	private static DefaultHttpClient l_httpClient;
	
	
	public static void initialize(long p_app_id){
		l_app_id=p_app_id;
		l_httpClient = new DefaultHttpClient();
	}
	
	public static ISDKUser getUserSDK() {
		if(l_userSDK==null){
			l_userSDK = new SDKUser(l_httpClient,l_app_id);
		}
		return (ISDKUser) l_userSDK;
	}

}
