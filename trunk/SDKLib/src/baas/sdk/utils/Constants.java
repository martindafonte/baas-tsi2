package baas.sdk.utils;
/**Clase que contiene la definición de las constantes que se pueden devolver*/
public abstract class Constants {
	public static final String baseURL="http://192.168.0.100:8080/WebUserManager";
	public static final String SENDER_ID = "920697992611";
	
	
	//Constantes nombres de parametros
	public static final String appId ="apid";
	public static final String canal ="chanel";
	public static final String regId ="id";
	public static final String jsonId ="jsonid";
	public static final String codigo = "codigo";
	public static final String descripcion = "descripcion";
	public static final String json = "json";
	
	//Constantes de error en la SDK
	public static final int Exito = 0;
	public static final int No_Code_result = 10001;
	
	
	//UserSDK 10100-10199
	public static final int User_login_exception = 10100;
	public static final int User_Login_parsingException = 10101;
	
	//Factory 10200-10299
	public static final int GCM_Problem_Registering = 10200;
	
	//JSONSDK 10300-10399
	public static final int JSON_Exception = 10300;
	
}