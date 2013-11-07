package modelo;

import java.util.LinkedList;

import baas.sdk.utils.Constants;

public class Trueque extends Transaccion {
	public LinkedList<Integer> tipoOfertas;
	
	public Trueque() {
		super();
		TipoObjeto = Constants.tipoTrueque;
		tipoOfertas = new LinkedList<Integer>();
	}	
}
