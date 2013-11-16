package baas.sdk.utils;
/**Clase que contiene la definici�n de las constantes que se pueden devolver*/
public abstract class Constants {
	public static final String baseURL="http://192.168.1.102:8080/WebUserManager";
	public static final String SENDER_ID = "920697992611";
	
	
	//Constantes nombres de parametros
	public static final String appId ="apid";
	public static final String canal ="chanel";
	public static final String regId ="id";
	public static final String jsonId ="jsonid";
	public static final String codigo = "codigo";
	public static final String descripcion = "descripcion";
	public static final String json = "json";
	public static final String jsonidMongo = "_id";
	public static final String jsonTipoMongo = "TipoObjeto";
	public static final String json_imagen_chica = "ImagenChica";
	public static final String json_id_imagen_chica = "id_imagenChica";
	public static final String json_imagen_grande = "ImagenGrande";
	public static final String json_id_imagen_grande = "id_imagenGrande";
	public static final String jsonIdMensajeJson ="jsonId";
	public static final String jsonIdUsuario = "UsuarioId";
	public static final String tipoTrueque = "trueque";
	public static final String tipoOferta = "oferta";
	public static final String mensaje = "msg";
	public static final String nick = "nick";
	public static final String pass = "pass";
	public static final String nombre = "nombre";
	public static final String apellido = "apellido";
	public static final String canalcodigo = "codigoCanal";
	public static final String permisos = "permisos";
	public static final String rol = "rol";
	public static final String nickapp = "nick";
	
	//ValoresBase de Datos
	public static final String update = "UD";
	public static final String create = "CR";
	
	//Constantes de error en la SDK
	public static final int Exito = 0;
	public static final int No_Code_result = 10001;
	
	
	//UserSDK 10100-10199
	public static final int User_exception = 10100;
	public static final int User_Login_parsingException = 10101;
	public static final int User_no_logged_user = 10102;
	
	//Factory 10200-10299
	public static final int GCM_Problem_Registering = 10200;
	
	//JSONSDK 10300-10399
	public static final int JSON_Exception = 10300;
	public static final int Json_Error_paginado = 10301;
	
	public static final int JSON_Error_Saving_BD = 10302;
	
}