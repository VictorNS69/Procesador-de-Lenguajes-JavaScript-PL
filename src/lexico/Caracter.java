package lexico;

import java.util.regex.*;

public class Caracter {
	private Pattern letra = Pattern.compile("[a-zA-z]");
	private Pattern digito = Pattern.compile("[0-9]");
	private Pattern delimitador = Pattern.compile("["+" "+"\\t"+"\\f"+"]");
	
	public Caracter(){
		
	}
	
	public boolean esLetra(String caracter){
		Matcher isLetter = this.letra.matcher(caracter);
		return isLetter.matches();
	}
	
	public boolean esDigito(String caracter){
		Matcher isDigit = this.digito.matcher(caracter);
		return isDigit.matches();
	}
	
	public boolean esDelimitador(String caracter){
		Matcher isDelimiter = this.delimitador.matcher(caracter);
		return isDelimiter.matches();
	}
}
