package modelo;

import baas.sdk.utils.Constants;

public class Oferta extends Transaccion {
	public String nickTrueque;
	public Oferta() {
		super();
		TipoObjeto = Constants.tipoOferta;
		
	}	
}
