package global.token;

import lexico.AnalizadorLexico;

/* 
 * && / || / ! /
 * Implementados: ! /
 */
public class OpLog implements Token {
	private int codigo;

	public OpLog(int cod){
		this.codigo = cod;
	}

	@Override
	public String aString() {
		return "<OpLog," + codigo + ">";
	}

	@Override
	public String tipo() {
		String resultado = "";
		if(codigo==AnalizadorLexico.NEGACION)resultado="negacion";
		else{
			System.out.println("Error, se ha generado un token erróneo: OP.LOGICO + "+ codigo);
			return null;
		}
		return resultado;
	}
}
