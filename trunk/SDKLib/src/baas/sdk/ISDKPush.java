package baas.sdk;

import android.app.IntentService;
import baas.sdk.messages.Message;

public interface ISDKPush {

	abstract Message registrarseCanal(String canalId);

	abstract Message desregistrarseCanal(String canalId);
	
	
	
	
	/**Para poder recibir las notificaciones push se debe extender esta clase
	 * */
	public abstract class SDKIntentService extends IntentService {

		public SDKIntentService() {
	        super("SDKIntentService");
	    }
	}
}

