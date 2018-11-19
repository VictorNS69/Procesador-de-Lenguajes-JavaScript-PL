package global.token;

public class Identificador implements Token {
	private int posicion;
	private String tabla;
	
	public Identificador(String tabla, int pos){
		this.posicion = pos;
		this.tabla = tabla;
	}

	@Override
	public String aString() {
		return "<Id," + tabla + "(" + posicion + ")" + ">";
	}

	@Override
	public String tipo() {
		return "id";
	}
	
	public int getPos(){
		return posicion;
	}
	
	public String getTabla(){
		return tabla;
	}

}
