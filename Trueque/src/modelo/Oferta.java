package modelo;

import baas.sdk.utils.Constants;

public class Oferta extends Transaccion {
	public String idTrueque;
	public Oferta() {
		super();
		TipoObjeto = Constants.tipoOferta;
		
	}	
}
