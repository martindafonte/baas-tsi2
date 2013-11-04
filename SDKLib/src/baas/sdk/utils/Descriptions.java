package baas.sdk.utils;

public abstract class Descriptions {

	public static String getDescription(int p_codigo){
		switch (p_codigo) {
		case Constants.Exito:
			return "Ok";
	//UserSDK 100-199
		case Constants.User_exception:
			return "Ocurrio una excepcion al hacer login";
		case Constants.User_Login_parsingException:
			return "Ocurrio una excepcion al intentar parsear la respuesta";
	//Factory 200-299
		case Constants.GCM_Problem_Registering:
			return "Ocurrio un problema al registrar en GCM";
		default:
			return "";
		}
	}
}
