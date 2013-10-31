package baas.sdk.utils;
/**Clase que contiene la definición de las constantes que se pueden devolver*/
public abstract class Constants {
	public static final String baseURL="http://192.168.0.104:8080/WebUserManager";
	public static final String SENDER_ID = "920697992611";
	
	
	//Constantes nombres de parametros
	public static final String appId ="apid";
	public static final String canal ="chanel";
	public static final String regId ="id";
	public static final String jsonId ="jsonid";
	
	//Constantes de error en la SDK
	public static final int Exito = 0;
	
	
	//UserSDK 100-199
	public static final int User_login_exception = 100;
	public static final int User_Login_parsingException = 101;
	
	//Factory 200-299
	public static final int GCM_Problem_Registering = 200;
	
}