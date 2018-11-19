package global.tabla;

public class EntradaTS {
	private int pos;
	private String lexema;
	private String tipo;//entero/logico, cadena, funcion
	private String desp;
	private int numParam;
	private String tipoDev;
	
	public EntradaTS(int pos){
		this.pos = pos;
		this.lexema = "-";
		this.tipo = "-";
		this.desp = "-";
		this.numParam = 0;
		this.tipoDev = "-";
	}
	public String aString(){
		return  String.format("%15s | %15s | %15s | %15s | %15s | %15s", pos, lexema, tipo, desp, numParam, tipoDev);
	}

	public int getPos() {
		return pos;
	}
	
	public void setPos(int pos) {
		this.pos = pos;
	}
	
	public String getLexema() {
		return lexema;
	}
	
	public void setLexema(String lexema) {
		this.lexema = lexema;
	}
	
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	
	public String getDesp() {
		return desp;
	}
	
	public void setDesp(String desp) {
		this.desp = desp;
	}
	
	public int getNumParam() {
		return numParam;
	}
	
	public void setNumParam(int numParam) {
		this.numParam = numParam;
	}
	
	public String getTipoDev() {
		return tipoDev;
	}
	
	public void setTipoDev(String tipoDev) {
		this.tipoDev = tipoDev;
	}
}
