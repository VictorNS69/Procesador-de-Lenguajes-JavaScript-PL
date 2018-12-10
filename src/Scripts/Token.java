package Scripts;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import javax.swing.JOptionPane;

public class Token {
	//Atributos de la clase Token
	public String tipoToken;
	public String attrToken;
	public int lineaToken;
	
	//Constructor Token
	public Token(String tipo, String attr, int linea){
		this.tipoToken=tipo;
		this.attrToken=attr;
		this.lineaToken = linea;
	}
	
	//Clase acci�n: Representa las acciones que realiza el An.L�xico
	public static class Accion{
		public int accion;
		//Accion: leer 0
		//        No leer 1
		//        Leer y Concatenar 2
		//        Comprobar palRes 3
		//        GenToken Numero 4
		//        GenToken Cadena 5
		public int estado;
		public Accion(int accion, int estado){
			this.accion=accion;
			this.estado=estado;
		}
	}

	//Estoy en el estado 'estado' y leo 'character':
	public static Accion estado(int estado, char character){
		switch(estado){
		case 0:
			if(character == ' ' || character == '	' || character == '\b' || character == '\t' || character == '\n'){ //Espacio o tabulador
				return new Accion(0,0); //Leemos y vamos al estado 0
			}
			if(Character.toString(character).matches("[a-zA-Z]")){//Cualquier letra (id o Palres) 
				return new Accion(2,1); //Leemos y concatenamos y vamos al estado 11
			}
			if(Character.toString(character).matches("[0-9]")){//Numeros
				return new Accion(2,2); //Leemos y concatenamos y vamos al estado 9
			}
			if (character == '{' || character == '}' || character == '(' ||
					character == ')' ||character == ';' || character == ',') {
				return new Accion(1,3);
			}
			if (character == '/') {
				return new Accion(0,4);
			}
			if (character == '+') {
				return new Accion(0,7);
			}
			if (character == '!') {
				return new Accion (0,12);
			}
			if (character == '=') {
				return new Accion (1,11);
			}
			if (character == '"') {
				return new Accion (2,10);
			}
			//Si encontramos un caracter especial (retorno de carro) lo ignoramos
			if(character == '\r'){
				return new Accion(0,0); //Leemos y vamos al estado 0
			}
			if (character == '\0') {
				escribirToken("EOF", " ",AnManager.contadorLineas);
				//return new Accion(0,0); 
			}
			//El caracter no concuerda: Error
			Errores.escribirError("Analizador lexico","No se reconoce el caracter ( "+character+" )" , AnManager.contadorLineas );
			return new Accion(0,0); //Leemos car�cter err�neo y continuamos
			
		case 1:
			if(Character.toString(character).matches("[\\w\\d]")){//Numeros y letras
				return new Accion(2,1); //Leemos y concatenamos y vamos al estado 11
			}else{
				return new Accion(3,0); //No leemos, comprobamos palRes/id y vamos al estado 0
			}
		case 2:
			if(Character.toString(character).matches("[0-9]")){//Numeros
				return new Accion(2,2); //Leemos y concatenamos y vamos al estado 9
			}else{
				return new Accion(1,16); //No Leemos y vamos al estado 0
			}
		case 3:
			if (character == ';') {
				escribirToken("PuntoComa", " ",AnManager.contadorLineas);
				return new Accion (0,0);
			}
			if (character == ',') {
				escribirToken("Coma", " ",AnManager.contadorLineas);
				return new Accion (0,0);
			}
			if (character == '{') {
				escribirToken("al", " ",AnManager.contadorLineas);
				return new Accion (0,0);
			}
			if (character == '}') {
				escribirToken("cl", " ",AnManager.contadorLineas);
				return new Accion (0,0);
			}
			if (character == '(') {
				escribirToken("ap", " ",AnManager.contadorLineas);
				return new Accion (0,0);
			}
			if (character == ')') {
				escribirToken("cp", " ",AnManager.contadorLineas);
				return new Accion (0,0);
			}
		case 4:
			if(character == '/'){//Fin comentario
				return new Accion(1,5); //Leemos y vamos al estado 5. Ignoramos comentario
			}else{
				return new Accion(1,6); //Leemos y vamos al estado 5
			}
		case 5:
			if (character == '\n') {
//				escribirToken("EOL", "",AnManager.contadorLineas); //GenTokensalto de linea
				return new Accion (0,0);
			}
			//escribirToken("DobleBarra", ">", AnManager.contadorLineas);
			return new Accion(0,5);
		case 6:
			escribirToken("div", " ", AnManager.contadorLineas);
			return new Accion(1,0);
		case 7:
			if (character == '+') {
				return new Accion (1,9);
			}
			else {
				return new Accion (1,8);
			}
		case 8:
			escribirToken("suma", " ", AnManager.contadorLineas);
			return new Accion (1,0);
		case 9:
			escribirToken("Preincremento", " ", AnManager.contadorLineas);
			return new Accion (0,0);
		case 10:
			if (character == '"') {
				return new Accion (5,0);
				//return new Accion (2,15);
			}
			else {
				return new Accion(2,10); 
			}
		case 11:
			escribirToken("igual", "", AnManager.contadorLineas);
			return new Accion(0,0);
		case 12:
			if (character == '=') {
				return new Accion(1,13);
			}
			else {
				return new Accion(1,14);
			}
		case 13:
			escribirToken("distinto", "", AnManager.contadorLineas);
			return new Accion (0,0);
		case 14:
			escribirToken("negacion", "", AnManager.contadorLineas);
			return new Accion (1,0);
		case 15:
			return new Accion (5,0);//Generar token cadena
		case 16:
			return new Accion (4,0);//Generar token numero
		
		default:
			return new Accion (1,0);
		}
	}

	//Generamos el archivo Tokens
	public static void genArchivoTokens(){
		String path = AnManager.getPath() + File.separator +"Resultados Grupo81" + File.separator+ "Tokens.txt";
		File f = new File(path);
		f.getParentFile().mkdirs(); 
		try {
			f.delete(); //Eliminamos si existe algo antes
			f.createNewFile();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Fallo al crear archivo tokens");
		}
	}

	//Funci�n para escribir en el archivo Tokens
	public static void escribirToken(String tipo, String attr, int linea){
		PrintWriter pw = null;
		try {
			File file = new File(AnManager.getPath() + File.separator +"Resultados Grupo81" + File.separator+ "Tokens.txt");
			FileWriter fw = new FileWriter(file, true);
			pw = new PrintWriter(fw);
			pw.println("<"+tipo+","+attr+">");
			//A�adimos el token a la lista
			AnManager.listaTokens.add(new Token(tipo,attr,linea));
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (pw != null) {
				pw.close();
			}
		}
	}
}
