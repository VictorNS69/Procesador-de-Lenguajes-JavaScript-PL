package lexico;

import global.ControladorErrores;
import global.tabla.ControladorTS;
import global.token.Cadena;
import global.token.Entero;
import global.token.Identificador;
import global.token.OpArit;
import global.token.OpAsig;
import global.token.OpLog;
import global.token.PalRes;
import global.token.Simbolo;
import global.token.Token;
import global.tabla.*;
import global.token.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class AnalizadorLexico {
	private ArrayList<Token> tokensLeidos;
	private FileReader fileReader;
	private BufferedReader bufferReader;
	private String caracter;
	public static int lineaActual;
	public static int columnaActual;
	private boolean flagSL;
	private PrintWriter log;

	// Constantes

	// Aritmeticos
	public static final int SUMA = 1;
	public static final int DIVISION = 2;
	public static final int MASMAS = 3;

	// Asignacion
	public static final int IGUAL = 1;
	public static final int DISTINTO = 2;

	// Logicos
	public static final int NEGACION = 1;


	public AnalizadorLexico(String ficheroALeer){
		this.lineaActual = 0;
		this.columnaActual = 0;
		this.tokensLeidos = new ArrayList<Token>();
		this.caracter = "";
		this.flagSL = false;
		try {
			String filePath = new File("").getAbsolutePath();
			filePath = filePath.concat("/resources/" + ficheroALeer);
			System.out.println(filePath);
			this.fileReader = new FileReader(filePath);
			this.bufferReader = new BufferedReader(fileReader);
			// Se entra al automata (estado 0) con un caracter leido
			leerCaracter();
		} catch (FileNotFoundException e) {
			System.out.println("Error al leer el fichero");
			e.printStackTrace();
		}
		try {
			this.log = new PrintWriter("lexico/log_lexico.txt","UTF-8");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	public Token leerToken(){
		Token token = null;
		Caracter isChar = new Caracter(); //objeto para comprobar patrones
		String lexema = "";
		int valor = 0;
		int estado = 0;
		boolean leido = false;

		while(!leido){
			log.println("Lexema: "+lexema+"<");
			// Ejecutar automata
			if(estado != 17 || estado != 16 || estado != 3 
					|| estado != 5 || estado != 6 || estado != 8 
					|| estado != 9 || estado  != 13 || estado != 14
					|| estado != 11 || estado != 15){
				leerCaracter();
			}
			switch(estado){
			case 0:
				if (isChar.esDelimitador(caracter)){
					leerCaracter();
				}
				else if(isChar.esLetra(caracter)){
					estado = 1;
					// Acciones semanticas
					lexema += caracter;
				}
				else if(isChar.esDigito(caracter)){
					estado = 2;
					lexema += caracter;
				}
				else if(caracter.equals(";") || caracter.equals(",")
						|| caracter.equals("{") || caracter.equals("}")
						|| caracter.equals("(") || caracter.equals(")")){
					estado = 3;
					leerCaracter();
				}
				else if(caracter.equals("/")){
					estado = 4;
					leerCaracter();
				}
				else if(caracter.equals("+")){
					estado = 7;
					lexema += caracter;
					leerCaracter();
				}
				else if(caracter.equals("!")){
					estado = 12;
					lexema += caracter;
					leerCaracter();
				}
				else if(caracter.equals("=")){
					estado = 11;
				}			
				else if (caracter.equalsIgnoreCase("\"")){
					estado = 10;
				}
				else{
					// Error
					System.out.println("Símbolo inesperado en la línea " + lineaActual + ", columna " + columnaActual);
					log.println("Símbolo inesperado en línea " + lineaActual + ", columna " + columnaActual);
					leerCaracter();
					leido = true;
					return null;
				}
				break;
				
			case 1:
				if(isChar.esLetra(caracter)||isChar.esDigito(caracter)){
					lexema += caracter;
				}
				else{
					estado = 17;
				}
				break;
				
			case 2:
				if(isChar.esDigito(caracter)){
					lexema += caracter;
				}
				else {
					estado = 16;
				}
				break;
				
			case 3:
				token = new Simbolo (caracter);
				lexema = caracter;
				leido = true;
				log.println("Lexema: "+lexema+"<");
				break;
				
			case 4:
				if (caracter.equals("/")) {
					lexema += caracter;
					token = new Simbolo("//");
					leido = true;
					log.println("Lexema: "+lexema+"<");
				}
				else {
					estado = 6;
				}
				break;
				
			case 5:
				if(!caracter.equals("\"")){
					lexema += caracter;
				}
				else{
					estado = 6;
				}
				break;
				
			case 6:
				token = new Simbolo("/");
				leido = true;
				log.println("Lexema: "+lexema+"<");
				
			case 7:
				if(caracter.equals("+")){
					lexema += caracter;
					estado = 9;
				}
				else{
					estado = 8;
				}
				break;
				
			case 8:
				token = new OpArit(SUMA);
				leido = true;
				log.println("Lexema: "+lexema+"<");
				break;
				
			case 9:
				if(caracter.equals("+")){
					lexema += caracter;
					token = new OpArit(MASMAS);
					log.println("Lexema: "+lexema+"<");
				}
				else{
					token = new OpArit(SUMA);
					leido = true;
					log.println("Lexema: "+lexema+"<");
				}
				break;
				
			case 10:
				if(!caracter.equals("\"")){
					lexema += caracter;
					leerCaracter();
				}
				else{
					lexema += "\"";
					estado = 15;
				}
				break;
				
			case 11:
				token = new OpAsig(IGUAL);
				leido = true;
				log.println("Lexema: "+lexema+"<");
				break;
				
			case 12:
				if (caracter.equals("=")) {
					estado = 13;
				}
				else {
					estado = 14;
				}
				
			case 13:
				lexema += caracter;
				token = new OpAsig(DISTINTO);
				log.println("Lexema: "+lexema+"<");
				break;
				
			case 14:
				token = new OpLog(NEGACION);
				log.println("Lexema: "+lexema+"<");
				break;
				
			case 15:
				token = new Cadena(lexema);
				leido = true;
				log.println("Lexema: "+lexema+"<");
				break;
				
			case 16:
				valor = Integer.parseInt(lexema);
				token = new Entero(valor);
				leido = true;
				break;
				
			case 17:
				/* Si es palabra reservada se genera el token PReservada, lexema */
				if(ControladorTS.esReservada(lexema)){
					token = new PalRes(lexema);
				}
				/* Si es un id se busca en la TS y si no esta se añade
				 * y se genera el token Id, posTS
				 */
				else{
					int pos = ControladorTS.buscaIdTS(lexema);
					if(ControladorTS.getFlagDU()){ // DECLARACION
						if(pos==-1){ // identificador no declarado
							if(!ControladorTS.getFlagVF()){ // function
								pos = ControladorTS.insertaIdTS(lexema);
								ControladorTS.insertaTipoTS(lexema, "funcion");
								int desp = ControladorTS.getDesp();
								ControladorTS.insertaDespTS(lexema, Integer.toString(desp));
								ControladorTS.sumDesp(1);
								ControladorTS.setFuncion(lexema);
								token = new Identificador(ControladorTS.nombreTablaActual(),pos);
								ControladorTS.crearTS(lexema); // crear TS para la funcion
								ControladorTS.flagUso();
							}
							else{ // var o variable sin declarar
								pos = ControladorTS.insertaIdTS(lexema);
								int desp = ControladorTS.getDesp();
								ControladorTS.insertaDespTS(lexema, Integer.toString(desp));
								ControladorTS.sumDesp(4);
								token = new Identificador(ControladorTS.nombreTablaActual(),pos);
								ControladorTS.flagUso();
							}
						}
						else{ // identificador existe
							//ERROR: Identificador ya declarado
						}
					}
					else{//USO
						if(pos == -1){ // identificador no declarado
							pos = ControladorTS.insertaIdTS(lexema);
							int desp = ControladorTS.getDesp();
							ControladorTS.insertaDespTS(lexema, Integer.toString(desp));
							ControladorTS.sumDesp(4);
						}
						token = new Identificador(ControladorTS.nombreTablaActual(),pos);
					}
				}
				leido = true;
				break;

			default:
				// Error
				ControladorErrores.addError("Símbolo inesperado en la línea " + lineaActual + ", columna " + columnaActual);
				log.println("Símbolo inesperado en línea " + lineaActual + ", columna " + columnaActual);
				leerCaracter();
				leido = true;
				token = null;
			}
		}
		if(token != null){
			tokensLeidos.add(token);
			log.println("Token leido:" + token.aString());
		}
		return token;
	}

	/**
	 * Método que lee un carácter como un String
	 */
	private void leerCaracter(){
		int car = 0;
		char aux;
		String caracter = null;
		try {
			car = this.bufferReader.read();
			if(car == 13){
				this.flagSL = true;
				caracter = "flagSL";
			}
			else if (car == 10 && flagSL){
				caracter = "sl";
				flagSL = false;
			}
			else if(car != -1){
				aux = (char) car;
				caracter = Character.toString(aux);
			}
			else{
				caracter = "$";
				log.println("Token leido:"+ new Simbolo("$").aString());
				log.close();
			}
		} catch (IOException e) {
			System.out.println("Error al leer el fichero");
			e.printStackTrace();
		}
		this.columnaActual++;
		if(caracter.equals("sl")){
			lineaActual++;
			columnaActual = 0;
		}
		this.caracter = caracter;
	}

	public ArrayList<Token> getTokensLeidos() {
		return tokensLeidos;
	}

	public int getLineaActual() {
		return lineaActual;
	}

	public int getColumnaActual() {
		return columnaActual;
	}

	public String getCaracter() {
		return caracter;
	}

	public BufferedReader getBufferReader() {
		return bufferReader;
	}

	public FileReader getFileReader() {
		return fileReader;
	}
	public PrintWriter getLog() {
		return log;
	}

}
