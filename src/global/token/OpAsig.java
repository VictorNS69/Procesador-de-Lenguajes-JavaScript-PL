package global.token;

import lexico.AnalizadorLexico;
 
public class OpAsig implements Token {
	private int codigo;

	public OpAsig(int cod){
		this.codigo = cod;
	}

	@Override
	public String aString() {
		return "<OpAsig," + codigo + ">";
	}

	@Override
	public String tipo() {
		String resultado = "";
		if(codigo==AnalizadorLexico.IGUAL)resultado="igual";
		else if(codigo==AnalizadorLexico.DISTINTO)resultado="distinto";
		else{
			System.out.println("Error, se ha generado un token erróneo: OP.ASIGNACION + "+ codigo);
			return null;
		}
		return resultado;
	}

}
