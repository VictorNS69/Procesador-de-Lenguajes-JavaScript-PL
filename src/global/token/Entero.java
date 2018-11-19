package global.token;

public class Entero implements Token {
	private int valor;
	
	public Entero(int valor){
		this.valor = valor;
	}
	@Override
	public String aString() {
		return "<Entero," + valor + ">";
	}
	@Override
	public String tipo() {
		return "num";
	}

}
