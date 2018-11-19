package global.token;

public class PalRes implements Token {
	private String codigo;
	
	public PalRes(String cod){
		this.codigo = cod;
	}

	@Override
	public String aString() {
		return "<PalRes," + codigo + ">";
	}

	@Override
	public String tipo() {
		return codigo;
	}
}
