package baas.sdk.utils;

/**Clase que contiene la definición de las constantes que se pueden devolver*/
public abstract class Constants {
	public static final String baseURL="http://192.168.0.104:8080/WebUserManager";
	
	//Constantes de error en la SDK
	
	//UserSDK 100-199
	public static final int User_login_exception = 100;
	public static final int User_Login_parsingException = 101;
}
