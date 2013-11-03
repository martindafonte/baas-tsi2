package baas.sdk;

import android.app.IntentService;
import baas.sdk.messages.Message;

public interface ISDKPush {

	abstract Message registerInChanel(String canalId);

	abstract Message unregisterFromChanel(String canalId);
	
	abstract Message sendToUser(String nick, String message);	
	
	abstract Message sendToChanel(String message, String chanel);
	
	/**Para poder recibir las notificaciones push se debe extender esta clase
	 * */
	public abstract class SDKIntentService extends IntentService {

		public SDKIntentService() {
	        super("SDKIntentService");
	    }
	}
}

