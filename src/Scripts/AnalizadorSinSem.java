package Scripts;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.swing.JOptionPane;

//Esta clase ejecuta el funcionamiento del Analizador Sintáctico y Semántico
public class AnalizadorSinSem {
	//Constantes para calcular el desplazamiento según el tipo de variable
	private final static int desInt = 2; //Las variables enteras son 2
	private final static int desChars = 4; //Las cadenas son 4
	private final static int desBool = 2; //Los booleanos son 2

	//Variables necesarias para las tablas de símbolos
	public static itemTS T, Sa, X, H, L, E, Ea, R, Ra, U, Ua, V;
	public static ArrayList <ArrayList <itemTS>> tablasSimbolos;
	private static ArrayList <itemTS> TSG; 	//TSG: TSimbolos Global
	private static ArrayList <itemTS> TSL; 	//TSL: TSimbolos Local
	private static ArrayList <String> tituloFunciones = new ArrayList<String>();
	private static boolean condicional = false;

	//Variables que controlan las funciones (Tablas locales)
	private static itemTS functionActual;
	private static int idFunction = 1;
	private static boolean llamadaFuncion = false;
	
	//Variables auxiliares para comprobar las expresiones
	private static boolean EhayError = false;
	private static boolean UaSuma = false;
	private static boolean RaRel = false;
	private static boolean EaRel = false;
	private static ArrayList <String> LArgumentos = new ArrayList<String>();
	private static ArrayList <String> VaArgumentos = new ArrayList<String>();
	//Token que estamos analizando
	public static Token sgtetoken;
	
	//Programa principal: Representa P' e inicializa todas las variables correspondientes
	public static void AnalizadorSt(){
		//El analizador sintáctico es el Descendente Recursivo:
		escribirParse("Descendente");
		//Pedimos primer token
		sgtetoken = AnManager.pedirTokenAlex();
		//Tablas de símbolos
		TSG = new ArrayList<itemTS>();
		TSL = new ArrayList<itemTS>();
		tablasSimbolos = new ArrayList<ArrayList <itemTS>>();
		//Inicializamos variables necesarias para el analizador semántico
		T = Sa = X = H = L = E = Ea = R = Ra = U = Ua = V = new itemTS();
		//La primera tabla de símbolos será la global
		tablasSimbolos.add(0,TSG);
		//Llamamos al axioma
		P();
		//Actualizamos tabla de símbolos global
		tablasSimbolos.set(0,TSG);
		//Imprimimos todas las tablas
		imprimirTablas();
	}

	//Utilizamos una función por cada regla		
	public static void P(){
		if (sgtetoken == null){
			return;
		}else{
			switch(sgtetoken.tipoToken){
			case "var":
				escribirParse("1");
				B();
				P();
				return;
			case "if":
				escribirParse("1");
				B();
				P();
				return;
			case "ID":
				escribirParse("1");
				B();
				P();
				return;
			case "return":
				escribirParse("1");
				B();
				P();
				return;
			case "prompt":
				escribirParse("1");
				B();
				P();
				return;
			case "write":
				escribirParse("1");
				B();
				P();
				return;
			case "for":
				escribirParse("1");
				B();
				P();
				return;
			case "function":
				escribirParse("2");
				F();
				P();
				return;
			case "EOL": //Ignoramos saltos de linea
				sgtetoken = AnManager.pedirTokenAlex();
				P();
				return;
			case "EOF": //Ignoramos saltos de linea
				escribirParse("3");
				return;
			default:
				Errores.escribirError("Analizador sintáctico", "No se permite el token <"+sgtetoken.tipoToken+","+sgtetoken.attrToken+"> aquí", AnManager.lineasST);
				Errores.panicMode();
				P();
				break;
			}	

		}
	}

	public static void B(){
		if (sgtetoken == null){
			Errores.escribirError("Analizador sintáctico", "No hay mas tokens", AnManager.lineasST);
			return;
		}else{
			switch(sgtetoken.tipoToken){
			case "var":
				escribirParse("4");
				sgtetoken = AnManager.pedirTokenAlex();
				T();
				if(sgtetoken.tipoToken.equals("ID")){
					//Analizador semántico:
					if (functionActual == null){ //Si no hay local, buscamos en la global
						if (buscaTS(TSG, sgtetoken.attrToken) != null){
							Errores.escribirError("Analizador semántico", "La variable "+sgtetoken.attrToken+" ya se ha declarado anteriormente", AnManager.lineasST);
						}else{
							itemTS a = new itemTS();
							a.lexema = sgtetoken.attrToken;
							a.desplazamiento = calculoDesplazamiento(T.tipo);
							a.tipo = T.tipo;
							TSG.add(a);
						}
					}else{//Si hay local, buscamos en la tabla local
						if (buscaTS(TSL, sgtetoken.attrToken) != null){
							Errores.escribirError("Analizador semántico", "La variable "+sgtetoken.attrToken+" ya se ha declarado anteriormente", AnManager.lineasST);
						}else{
							itemTS a = new itemTS();
							a.lexema = sgtetoken.attrToken;
							a.desplazamiento = calculoDesplazamiento(T.tipo);
							a.tipo = T.tipo;
							TSL.add(a);
						}
					}
					sgtetoken = AnManager.pedirTokenAlex();

					if(sgtetoken.tipoToken.equals(";")){
						sgtetoken = AnManager.pedirTokenAlex();
					}else{
						Errores.escribirError("Analizador sintáctico", "Falta un ';' antes de recibir "+sgtetoken.tipoToken+" aquí", AnManager.lineasST);
						//Tratamiento de error:
						if (AnManager.comprobarSiguienteToken().tipoToken.equals(";")){ //Si el siguiente token es el que esperamos, continuamos normalmente
							sgtetoken = AnManager.pedirTokenAlex();
							sgtetoken = AnManager.pedirTokenAlex();
						}else{ //Si no es lo que esperamos, procedemos al PanicMode
							Errores.panicMode();
						}
					}
				}else{
					Errores.escribirError("Analizador sintáctico", "Debería haber un identificador", AnManager.lineasST);
					//Tratamiento de error:
					if (AnManager.comprobarSiguienteToken().tipoToken.equals(";")){ //Si el siguiente token es el que esperamos, continuamos normalmente
						sgtetoken = AnManager.pedirTokenAlex();
						sgtetoken = AnManager.pedirTokenAlex();
					}else{ //Si no es lo que esperamos, procedemos al PanicMode
						Errores.panicMode();
					}
				}

				return;
			case "if":
				escribirParse("5");
				sgtetoken = AnManager.pedirTokenAlex();
				if(sgtetoken.tipoToken.equals("(")){
					sgtetoken = AnManager.pedirTokenAlex();

					condicional = true; //Estamos en un condicional
					E();
					if (EhayError){	
						Errores.escribirError("Analizador semántico", "No es correcta la expresión", AnManager.lineasST);
						EhayError = false;
					}
					//Semántico
					if(!E.tipo.equals("Bool")){
						Errores.escribirError("Analizador semántico", "No es una expresión condicional lo que se encuentra en el if", AnManager.lineasST);
					}

					if(sgtetoken.tipoToken.equals(")")){
						sgtetoken = AnManager.pedirTokenAlex();
						S();
						condicional = false;
					}else{
						Errores.escribirError("Analizador sintáctico", "Falta un ')' antes de recibir el token <"+sgtetoken.tipoToken+","+sgtetoken.attrToken+"> aquí", AnManager.lineasST);
						//Tratamiento de error:
						Errores.panicMode();
					}	
				}else{
					Errores.escribirError("Analizador sintáctico", "Debería haber un paréntesis tras el 'if' antes de recibir el token <"+sgtetoken.tipoToken+","+sgtetoken.attrToken+"> aquí", AnManager.lineasST);
					//Tratamiento de error:
					Errores.panicMode();
				}
				return;
				//FIRST 'S'
			case "ID":
				escribirParse("6");
				S();
				return;
			case "return":
				escribirParse("6");
				S();
				return;
			case "write":
				escribirParse("6");
				S();
				return;
			case "prompt":
				escribirParse("6");
				S();
				return;
			case "for":
				escribirParse("7");
				sgtetoken = AnManager.pedirTokenAlex();
				if(sgtetoken.tipoToken.equals("(")){
					sgtetoken = AnManager.pedirTokenAlex();	
				}else{
					Errores.escribirError("Analizador sintáctico", "Debería haber un paréntesis tras el 'for' antes de recibir el token <"+sgtetoken.tipoToken+","+sgtetoken.attrToken+"> aquí", AnManager.lineasST);
					//Tratamiento de error:
					if (AnManager.comprobarSiguienteToken().tipoToken.equals("(")){ //Si el siguiente token es el que esperamos, continuamos normalmente
						sgtetoken = AnManager.pedirTokenAlex();
						sgtetoken = AnManager.pedirTokenAlex();
					}else{ //Si no es lo que esperamos, procedemos al PanicMode
						Errores.panicModeLlaves(0);
						return;
					}
				}
				I();
				if(sgtetoken.tipoToken.equals(";")){
					sgtetoken = AnManager.pedirTokenAlex();
				}else{
					Errores.escribirError("Analizador sintáctico", "Falta un ';' antes de recibir el token <"+sgtetoken.tipoToken+","+sgtetoken.attrToken+"> aquí", AnManager.lineasST);
					//Tratamiento de error:
					if (AnManager.comprobarSiguienteToken().tipoToken.equals(";")){ //Si el siguiente token es el que esperamos, continuamos normalmente
						sgtetoken = AnManager.pedirTokenAlex();
						sgtetoken = AnManager.pedirTokenAlex();
					}else{ //Si no es lo que esperamos, procedemos al PanicMode
						Errores.panicModeLlaves(0);
						return;
					}
				}
				E();
				if (EhayError){	
					Errores.escribirError("Analizador semántico", "No es correcta la expresión", AnManager.lineasST);
					EhayError = false;
				}
				//Analizador semántico
				if (!E.tipo.equals("Bool")){
					Errores.escribirError("Analizador semántico", "La expresión en el 'for' no es una condición", AnManager.lineasST);
				}

				if(sgtetoken.tipoToken.equals(";")){
					sgtetoken = AnManager.pedirTokenAlex();
				}else{
					Errores.escribirError("Analizador sintáctico", "Falta un ';' antes de recibir el token <"+sgtetoken.tipoToken+","+sgtetoken.attrToken+"> aquí", AnManager.lineasST);
					//Tratamiento de error:
					if (AnManager.comprobarSiguienteToken().tipoToken.equals(";")){ //Si el siguiente token es el que esperamos, continuamos normalmente
						sgtetoken = AnManager.pedirTokenAlex();
						sgtetoken = AnManager.pedirTokenAlex();
					}else{ //Si no es lo que esperamos, procedemos al PanicMode
						Errores.panicModeLlaves(0);
						return;
					}
				}
				D();
				if(sgtetoken.tipoToken.equals(")")){
					sgtetoken = AnManager.pedirTokenAlex();
				}else{
					Errores.escribirError("Analizador sintáctico", "Falta un ')' en el for, no se entiende el token "+sgtetoken.tipoToken+" aquí", AnManager.lineasST);
					//Tratamiento de error:
					if (AnManager.comprobarSiguienteToken().tipoToken.equals(")")){ //Si el siguiente token es el que esperamos, continuamos normalmente
						sgtetoken = AnManager.pedirTokenAlex();
						sgtetoken = AnManager.pedirTokenAlex();
					}else{ //Si no es lo que esperamos, procedemos al PanicMode
						Errores.panicModeLlaves(0);
						return;
					}
				}
				if (sgtetoken.tipoToken.equals("EOL")){ //Permitimos aqui un salto de línea
					sgtetoken = AnManager.pedirTokenAlex();
				}
				if(sgtetoken.tipoToken.equals("{")){
					sgtetoken = AnManager.pedirTokenAlex();
				}else{
					Errores.escribirError("Analizador sintáctico", "Falta un '{' antes de recibir el token <"+sgtetoken.tipoToken+","+sgtetoken.attrToken+"> aquí", AnManager.lineasST);
					//Tratamiento de error:
					if (AnManager.comprobarSiguienteToken().tipoToken.equals("{")){ //Si el siguiente token es el que esperamos, continuamos normalmente
						sgtetoken = AnManager.pedirTokenAlex();
						sgtetoken = AnManager.pedirTokenAlex();
					}else{ //Si no es lo que esperamos, procedemos al PanicMode
						Errores.panicModeLlaves(0);
						return;
					}
				}
				C();

				if(sgtetoken.tipoToken.equals("}")){
					sgtetoken = AnManager.pedirTokenAlex();
				}else{
					Errores.escribirError("Analizador sintáctico", "Falta un '}' para terminar el bucle, antes de recibir el token <"+sgtetoken.tipoToken+","+sgtetoken.attrToken+"> aquí", AnManager.lineasST);
					//Tratamiento de errores
					if (AnManager.comprobarSiguienteToken().tipoToken.equals("}")){ //Si el siguiente token es el que esperamos, continuamos normalmente
						sgtetoken = AnManager.pedirTokenAlex();
						sgtetoken = AnManager.pedirTokenAlex();
					}else{ //Si no es lo que esperamos, procedemos al PanicMode
						Errores.panicModeLlaves(1);
						return;
					}
				}
				return;
			default:
				Errores.escribirError("Analizador sintáctico", "No se permite el token <"+sgtetoken.tipoToken+","+sgtetoken.attrToken+"> aquí", AnManager.lineasST);
				break;
			}	

		}
	}

	public static void I(){
		if (sgtetoken == null){
			Errores.escribirError("Analizador sintáctico", "No hay mas tokens", AnManager.lineasST);
			return;
		}else{
			switch(sgtetoken.tipoToken){
			case "ID":
				escribirParse("8");

				//Analizador semántico:
				itemTS item;
				if (functionActual == null){
					if ((item = buscaTS(TSG, sgtetoken.attrToken)) == null){ //Si no existe la variable la consideramos entera y la metemos
						//item = insertaTS(TSG, sgtetoken.attrToken, "Entero");
						item = new itemTS();
						item.lexema = sgtetoken.attrToken;
						item.tipo = "Entero";
						item.desplazamiento = calculoDesplazamiento(item.tipo);
						TSG.add(item);
					}
				}else{
					if ((item = buscaTS(TSL, sgtetoken.attrToken)) == null && (item = buscaTS(TSG, sgtetoken.attrToken)) == null){ //Si no existe la variable la consideramos entera y la metemos
						//item = insertaTS(TSL, sgtetoken.attrToken, "Entero");
						item = new itemTS();
						item.lexema = sgtetoken.attrToken;
						item.tipo = "Entero";
						item.desplazamiento = calculoDesplazamiento(item.tipo);
						TSG.add(item);
					}
				}


				sgtetoken = AnManager.pedirTokenAlex();
				if(sgtetoken.tipoToken.equals("Asignación")){
					sgtetoken = AnManager.pedirTokenAlex();
				}else{
					Errores.escribirError("Analizador sintáctico", "Debería haber un '=', no se permite el token <"+sgtetoken.tipoToken+","+sgtetoken.attrToken+"> aquí", AnManager.lineasST);
					//Tratamiento de error:
					if (AnManager.comprobarSiguienteToken().tipoToken.equals("Asignación")){ //Si el siguiente token es el que esperamos, continuamos normalmente
						sgtetoken = AnManager.pedirTokenAlex();
						sgtetoken = AnManager.pedirTokenAlex();
					}else{ //Si no es lo que esperamos, procedemos al PanicMode
						//Errores.panicMode();
						return;
					}
				}
				E();
				if (EhayError){	
					Errores.escribirError("Analizador semántico", "No es correcta la expresión", AnManager.lineasST);
					EhayError = false;
				}
				if(!item.tipo.equals(E.tipo)){
					Errores.escribirError("Analizador semántico", "No se puede asignar un "+E.tipo+" a un "+item.tipo, AnManager.lineasST);
				}

				return;
			case "var":
				escribirParse("9");
				sgtetoken = AnManager.pedirTokenAlex();
				T();
				itemTS item1 = new itemTS();
				if(sgtetoken.tipoToken.equals("ID")){
					//Analizador semántico:
					if (functionActual == null){
						if ((item1 = buscaTS(TSG, sgtetoken.attrToken)) != null){
							Errores.escribirError("Analizador semántico", "La variable "+sgtetoken.attrToken+" ya se ha declarado anteriormente", AnManager.lineasST);
						}else{
							item1 = new itemTS();
							item1.lexema = sgtetoken.attrToken;
							item1.tipo = T.tipo;
							item1.desplazamiento = calculoDesplazamiento(item1.tipo);
							TSG.add(item1);
						}
					}else{
						if ((item1 = buscaTS(TSL, sgtetoken.attrToken)) != null){
							Errores.escribirError("Analizador semántico", "La variable "+sgtetoken.attrToken+" ya se ha declarado anteriormente", AnManager.lineasST);
						}else{
							item1 = new itemTS();
							item1.lexema = sgtetoken.attrToken;
							item1.tipo = T.tipo;
							item1.desplazamiento = calculoDesplazamiento(item1.tipo);
							TSL.add(item1);
						}
					}

					sgtetoken = AnManager.pedirTokenAlex();
				}else{
					Errores.escribirError("Analizador sintáctico", "Debería haber un identificador en vez del token <"+sgtetoken.tipoToken+","+sgtetoken.attrToken+"> aquí", AnManager.lineasST);
					//Tratamiento de error:
					if (AnManager.comprobarSiguienteToken().tipoToken.equals("ID")){ //Si el siguiente token es el que esperamos, continuamos normalmente
						sgtetoken = AnManager.pedirTokenAlex();
						sgtetoken = AnManager.pedirTokenAlex();
					}else{ //Si no es lo que esperamos, procedemos al PanicMode
						return;
					}
				}
				if(sgtetoken.tipoToken.equals("Asignación")){
					sgtetoken = AnManager.pedirTokenAlex();
				}else{
					Errores.escribirError("Analizador sintáctico", "Debería haber un '=' en vez del token <"+sgtetoken.tipoToken+","+sgtetoken.attrToken+"> aquí", AnManager.lineasST);
					//Tratamiento de error:
					if (AnManager.comprobarSiguienteToken().tipoToken.equals("Asignación")){ //Si el siguiente token es el que esperamos, continuamos normalmente
						sgtetoken = AnManager.pedirTokenAlex();
						sgtetoken = AnManager.pedirTokenAlex();
					}else{ //Si no es lo que esperamos, procedemos al PanicMode
						return;
					}
				}
				E();
				if (EhayError){	
					Errores.escribirError("Analizador semántico", "No es correcta la expresión", AnManager.lineasST);
					EhayError = false;
				}
				//Semántico
				if(!item1.tipo.equals(E.tipo)){
					Errores.escribirError("Analizador semántico", "No se puede asignar un "+E.tipo+" a un "+item1.tipo, AnManager.lineasST);
				}

				return;
				//Al existir lambda comprobamos follow de I
			case ";":
				//No hacemos nada
				escribirParse("10");
				return;
			default:
				Errores.escribirError("Analizador sintáctico", "No se permite el token <"+sgtetoken.tipoToken+","+sgtetoken.attrToken+"> aquí", AnManager.lineasST);
				break;
			}	
		}
	}

	public static void D(){
		if (sgtetoken == null){
			Errores.escribirError("Analizador sintáctico", "No hay mas tokens", AnManager.lineasST);
			return;
		}else{
			switch(sgtetoken.tipoToken){
			case "Predecremento":
				escribirParse("11");
				sgtetoken = AnManager.pedirTokenAlex();
				if(sgtetoken.tipoToken.equals("ID")){
					itemTS a = new itemTS();
					//Analizador semántico:
					if (functionActual == null){
						if ((a = buscaTS(TSG, sgtetoken.attrToken)) == null){
							Errores.escribirError("Analizador semántico", "La variable "+sgtetoken.attrToken+" no se ha declarado anteriormente", AnManager.lineasST);
						}else{
							if(!a.tipo.equals("Entero")){
								Errores.escribirError("Analizador semántico", "La variable "+sgtetoken.attrToken+" no es de tipo 'Entero' por lo que no es posible hacer predecremento", AnManager.lineasST);
							}
						}
					}else{
						if ((a = buscaTS(TSL, sgtetoken.attrToken)) == null && (a = buscaTS(TSG, sgtetoken.attrToken)) == null){
							Errores.escribirError("Analizador semántico", "La variable "+sgtetoken.attrToken+" no se ha declarado anteriormente", AnManager.lineasST);
						}else{
							if(!a.tipo.equals("Entero")){
								Errores.escribirError("Analizador semántico", "La variable "+sgtetoken.attrToken+" no es de tipo 'Entero' por lo que no es posible hacer predecremento", AnManager.lineasST);
							}
						}
					}

					sgtetoken = AnManager.pedirTokenAlex();
				}else{
					Errores.escribirError("Analizador sintáctico", "Debería haber un identificador tras el predecremento", AnManager.lineasST);
				}
				return;
			case ")":
				//No hacemos nada
				escribirParse("12");
				return;
			default:
				Errores.escribirError("Analizador sintáctico", "No se permite el token <"+sgtetoken.tipoToken+","+sgtetoken.attrToken+"> aquí", AnManager.lineasST);
				break;
			}
		}
	}

	public static void T(){
		if (sgtetoken == null){
			Errores.escribirError("Analizador sintáctico", "No hay mas tokens", AnManager.lineasST);
			return;
		}else{
			switch(sgtetoken.tipoToken){
			case "int":
				escribirParse("13");

				//Analizador semántico
				T.tipo = "Entero";

				sgtetoken = AnManager.pedirTokenAlex();
				return;
			case "chars":
				escribirParse("14");

				//Analizador semántico
				T.tipo = "Chars";

				sgtetoken = AnManager.pedirTokenAlex();
				return;
			case "bool":
				escribirParse("15");

				//Analizador semántico
				T.tipo = "Bool";

				sgtetoken = AnManager.pedirTokenAlex();
				return;
			default:
				Errores.escribirError("Analizador sintáctico", "No se permite el token <"+sgtetoken.tipoToken+","+sgtetoken.attrToken+"> aquí, debería haber un 'Tipo'", AnManager.lineasST);
				break;
			}	
		}
	}

	public static void S(){
		if (sgtetoken == null){
			Errores.escribirError("Analizador sintáctico", "No hay mas tokens", AnManager.lineasST);
			return;
		}else{
			switch(sgtetoken.tipoToken){
			case "ID":
				escribirParse("16");

				//Analizador semántico:
				itemTS item = null;
				String lexema = sgtetoken.attrToken;

				//Vamos a comprobar si existe el id en alguna tabla, si no, lo consideramos entero y lo creamos
				if (functionActual == null){ //Tabla global
					if ((item = buscaTS(TSG, lexema)) == null){ //No está en la tabla global
						//La consideramos variable entera y la metemos en TSG
						item = new itemTS();
						item.tipo = "Entero";
						item.desplazamiento = calculoDesplazamiento("Entero");
						item.lexema = lexema;
						TSG.add(item);
					}
				}else{
					if (!lexema.equals(functionActual.lexema)&&(item = buscaTS(TSL, lexema)) == null && (item = buscaTS(TSG, lexema)) == null){ //No está en la tabla global
						//La consideramos variable entera y la metemos en TSG
						item = new itemTS();
						item.tipo = "Entero";
						item.desplazamiento = calculoDesplazamiento("Entero");
						item.lexema = lexema;
						TSG.add(item);
					}
				}


				sgtetoken = AnManager.pedirTokenAlex();
				Sa();
				if (Sa.tipo == null){ //Se ha llamado a una función
					if ((item = buscaTS(TSG, lexema)) == null){ //La función no existe
						if(functionActual != null && (item = functionActual).lexema.equals(lexema)){ //Puede ser una llamada recursiva
							int i=0;
							boolean aux = true;
							if (item.argumentos == LArgumentos.size()){
								while(i < item.argumentos){
									if (!TSL.get(i).tipo.equals(LArgumentos.get(i))){ //Vamos a la tabla de la función y comprobamos las variables correspondientes a los parámetros
										aux = false;
									}
									i++;
								}
								if(!aux){
									Errores.escribirError("Analizador semántico", "No se ha llamado a la función "+item.lexema+" con los argumentos correctos", AnManager.lineasST-1);	
								}
							}else{
								Errores.escribirError("Analizador semántico", "No se ha llamado a la función "+item.lexema+" con los argumentos correctos", AnManager.lineasST-1);	
							}
					}else{
						//La función no existe
						Errores.escribirError("Analizador semántico", "La función "+lexema+" no se ha declarado", AnManager.lineasST-1);
					}
						
					}else{//La función existe y hay que comprobar si se han utilizado los argumentos correctos
						int i=0;
						boolean aux = true;
						if (item.argumentos == LArgumentos.size()){
							while(i < item.argumentos){
								if (!tablasSimbolos.get(item.entID).get(i).tipo.equals(LArgumentos.get(i))){ //Vamos a la tabla de la función y comprobamos las variables correspondientes a los parámetros
									aux = false;
								}
								i++;
							}
							if(!aux){
								Errores.escribirError("Analizador semántico", "No se ha llamado a la función "+item.lexema+" con los argumentos correctos", AnManager.lineasST-1);	
							}
						}
					}
				}else{//Se ha asignado valor a un id
					if (!Sa.tipo.equals(item.tipo)){ //Si no es del mismo tipo, error
						Errores.escribirError("Analizador semántico", "No se puede asignar un valor de tipo "+Sa.tipo+" a la variable "+ lexema+" de tipo "+ item.tipo, AnManager.lineasST-1);
					}
				}
				LArgumentos.clear();
				return;

			case "return":
				escribirParse("17");
				sgtetoken = AnManager.pedirTokenAlex();
				X();
				//Analizador semántico
				if(functionActual != null){
					//Si ya Devuelto es false significa que ya se había ejecutado un return
					if (X.tipo.equals(functionActual.tipoDevuelto)){
						if (!condicional){ //Si no estamos en un if, tenemos en cuenta que ya se ha devuelto el valor
							functionActual.yaDevuelto = true;
						}

					}else{
						Errores.escribirError("Analizador semántico","La función "+ functionActual.lexema + " no puede devolver un "+X.tipo+", tiene que devolver un "+functionActual.tipoDevuelto, AnManager.lineasST);
						functionActual.yaDevuelto = true; //Devuelve algo aunque sea erroneo
					}
				}else{
					Errores.escribirError("Analizador semántico","No se puede ejecutar un return aqui ", AnManager.lineasST);	
				}


				if (sgtetoken.tipoToken.equals(";")){
					sgtetoken = AnManager.pedirTokenAlex();
				}else{
					Errores.escribirError("Analizador sintáctico", "Se esperaba ';' y se ha recibido el token <"+sgtetoken.tipoToken+","+sgtetoken.attrToken+"> aquí", AnManager.lineasST);
					Errores.panicMode();
				}
				return;

			case "prompt":
				escribirParse("19");
				sgtetoken = AnManager.pedirTokenAlex();
				if (sgtetoken.tipoToken.equals("(")){
					sgtetoken = AnManager.pedirTokenAlex();
				}else{
					Errores.escribirError("Analizador sintáctico", "Se esperaba '(' y se ha recibido el token <"+sgtetoken.tipoToken+","+sgtetoken.attrToken+"> aquí", AnManager.lineasST);
					//Tratamiento de error:
					if (AnManager.comprobarSiguienteToken().tipoToken.equals("ID")){ //Si el siguiente token es el que esperamos, continuamos normalmente
						sgtetoken = AnManager.pedirTokenAlex();
						sgtetoken = AnManager.pedirTokenAlex();
					}else{ //Si no es lo que esperamos, procedemos al PanicMode
						Errores.panicMode();
						return;
					}
				}


				if (sgtetoken.tipoToken.equals("ID")){
					itemTS a;
					//Analizador semántico:
					if (functionActual == null){
						if ((a = buscaTS(TSG, sgtetoken.attrToken)) != null){
							if(a.tipo.equals("Entero") || buscaTS(TSG, sgtetoken.attrToken).tipo.equals("Chars")){
								//Todo OK
							}else{
								Errores.escribirError("Analizador semántico", "La variable "+sgtetoken.attrToken+" no es de tipo 'Entero' o 'Chars'", AnManager.lineasST);
							}
						}else{ //Si no, la consideramos variable de tipo entero
							a = new itemTS();
							a.lexema = sgtetoken.attrToken;
							a.tipo = "Entero";
							a.desplazamiento = calculoDesplazamiento("Entero");
							TSG.add(a);
						}
					}else{
						if ((a = buscaTS(TSG, sgtetoken.attrToken)) == null && (a = buscaTS(TSL, sgtetoken.attrToken)) == null){
							//La consideramos entera y la insertamos en la tabla global
							a = new itemTS();
							a.lexema = sgtetoken.attrToken;
							a.tipo = "Entero";
							a.desplazamiento = calculoDesplazamiento("Entero");
							TSG.add(a);
						}else{  //Está en la tabla y tenemos que comprobar si es de tipo entero y chars
							if(a.tipo.equals("Entero") || a.tipo.equals("Chars")){
								//Todo OK
							}else{
								Errores.escribirError("Analizador semántico", "La variable "+sgtetoken.attrToken+" no es de tipo 'Entero' o 'Chars'", AnManager.lineasST);
							}
						}
					}

					sgtetoken = AnManager.pedirTokenAlex();
				}else{
					Errores.escribirError("Analizador sintáctico", "Se esperaba un identificador y se ha recibido el token <"+sgtetoken.tipoToken+","+sgtetoken.attrToken+"> aquí", AnManager.lineasST);
					//Tratamiento de error:
					if (AnManager.comprobarSiguienteToken().tipoToken.equals(")")){ //Si el siguiente token es el que esperamos, continuamos normalmente
						sgtetoken = AnManager.pedirTokenAlex();
						sgtetoken = AnManager.pedirTokenAlex();
					}else{ //Si no es lo que esperamos, procedemos al PanicMode
						Errores.panicMode();
						return;
					}
				}
				if (sgtetoken.tipoToken.equals(")")){
					sgtetoken = AnManager.pedirTokenAlex();
				}else{
					Errores.escribirError("Analizador sintáctico", "Se esperaba un ')' y se ha recibido el token <"+sgtetoken.tipoToken+","+sgtetoken.attrToken+"> aquí", AnManager.lineasST);
					//Tratamiento de error:
					if (AnManager.comprobarSiguienteToken().tipoToken.equals(";")){ //Si el siguiente token es el que esperamos, continuamos normalmente
						sgtetoken = AnManager.pedirTokenAlex();
						sgtetoken = AnManager.pedirTokenAlex();
					}else{ //Si no es lo que esperamos, procedemos al PanicMode
						Errores.panicMode();
						return;
					}
				}
				if (sgtetoken.tipoToken.equals(";")){
					sgtetoken = AnManager.pedirTokenAlex();
				}else{
					Errores.escribirError("Analizador sintáctico", "Se esperaba un ';' y se ha recibido el token <"+sgtetoken.tipoToken+","+sgtetoken.attrToken+"> aquí", AnManager.lineasST);
					Errores.panicMode();
				}
				return;
			case "write":
				escribirParse("18");
				sgtetoken = AnManager.pedirTokenAlex();
				if (sgtetoken.tipoToken.equals("(")){
					sgtetoken = AnManager.pedirTokenAlex();
				}else{
					Errores.escribirError("Analizador sintáctico", "Se esperaba '(' y se ha recibido el token <"+sgtetoken.tipoToken+","+sgtetoken.attrToken+"> aquí", AnManager.lineasST);
					Errores.panicMode();
				}
				E();
				if (EhayError){	
					Errores.escribirError("Analizador semántico", "No es correcta la expresión", AnManager.lineasST);
					EhayError = false;
				}
				if (sgtetoken.tipoToken.equals(")")){
					sgtetoken = AnManager.pedirTokenAlex();
				}else{
					Errores.escribirError("Analizador sintáctico", "Se esperaba un ')' y se ha recibido el token <"+sgtetoken.tipoToken+","+sgtetoken.attrToken+"> aquí", AnManager.lineasST);
					//Tratamiento de error:
					if (AnManager.comprobarSiguienteToken().tipoToken.equals(";")){ //Si el siguiente token es el que esperamos, continuamos normalmente
						sgtetoken = AnManager.pedirTokenAlex();
						sgtetoken = AnManager.pedirTokenAlex();
					}else{ //Si no es lo que esperamos, procedemos al PanicMode
						Errores.panicMode();
						return;
					}
				}
				if (sgtetoken.tipoToken.equals(";")){
					sgtetoken = AnManager.pedirTokenAlex();
				}else{
					Errores.escribirError("Analizador sintáctico", "Se esperaba un ';' y se ha recibido el token <"+sgtetoken.tipoToken+","+sgtetoken.attrToken+"> aquí", AnManager.lineasST);
					Errores.panicMode();
				}
				return;
			default:
				Errores.escribirError("Analizador sintáctico", "No se permite el token <"+sgtetoken.tipoToken+","+sgtetoken.attrToken+"> aquí", AnManager.lineasST);
				break;
			}	
		}
	}

	public static void Sa(){
		if (sgtetoken == null){
			Errores.escribirError("Analizador sintáctico", "No hay mas tokens", AnManager.lineasST);
			return;
		}else{
			switch(sgtetoken.tipoToken){
			case "Asignación":
				escribirParse("20");
				sgtetoken = AnManager.pedirTokenAlex();
				E();
				if (EhayError){	
					Errores.escribirError("Analizador semántico", "No es correcta la expresión", AnManager.lineasST);
					EhayError = false;
				}
				//Analizador semántico
				Sa.tipo = E.tipo;


				if (sgtetoken.tipoToken.equals(";")){
					sgtetoken = AnManager.pedirTokenAlex();
				}else{
					Errores.escribirError("Analizador sintáctico", "Se esperaba ';'. Se ha recibido el token <"+sgtetoken.tipoToken+","+sgtetoken.attrToken+"> aquí", AnManager.lineasST);
					Errores.panicMode();
				}
				return;
			case "(":
				escribirParse("21");
				sgtetoken = AnManager.pedirTokenAlex();
				L();
				Sa.tipo = null;
				//Analizador semántico
				if (sgtetoken.tipoToken.equals(")")){
					sgtetoken = AnManager.pedirTokenAlex();
				}else{
					Errores.escribirError("Analizador sintáctico", "Se esperaba ')' y se ha recibido el token <"+sgtetoken.tipoToken+","+sgtetoken.attrToken+"> aquí", AnManager.lineasST);
					//Tratamiento de error:
					if (AnManager.comprobarSiguienteToken().tipoToken.equals(";")){ //Si el siguiente token es el que esperamos, continuamos normalmente
						sgtetoken = AnManager.pedirTokenAlex();
						sgtetoken = AnManager.pedirTokenAlex();
					}else{ //Si no es lo que esperamos, procedemos al PanicMode
						Errores.panicMode();
						return;
					}
				}
				if (sgtetoken.tipoToken.equals(";")){
					sgtetoken = AnManager.pedirTokenAlex();
				}else{
					Errores.escribirError("Analizador sintáctico", "Se esperaba ';'. Se ha recibido el token <"+sgtetoken.tipoToken+","+sgtetoken.attrToken+"> aquí", AnManager.lineasST);
					Errores.panicMode();
				}
				return;
			default:
				Errores.escribirError("Analizador sintáctico", "No se permite el token <"+sgtetoken.tipoToken+","+sgtetoken.attrToken+"> aquí", AnManager.lineasST);
				break;
			}
		}
	}

	public static void X(){
		if (sgtetoken == null){
			Errores.escribirError("Analizador sintáctico", "No hay mas tokens", AnManager.lineasST);
			return;
		}else{
			switch(sgtetoken.tipoToken){
			//First E
			case "ID":
				escribirParse("22");
				E();
				if (EhayError){	
					Errores.escribirError("Analizador semántico", "No es correcta la expresión", AnManager.lineasST);
					EhayError = false;
				}
				//Analizador semántico
				X.tipo = E.tipo;
				return;
			case "Entero":
				escribirParse("22");
				E();
				if (EhayError){	
					Errores.escribirError("Analizador semántico", "No es correcta la expresión", AnManager.lineasST);
					EhayError = false;
				}
				//Analizador semántico
				X.tipo = E.tipo;
				return;
			case "Cadena":
				escribirParse("22");
				E();
				if (EhayError){	
					Errores.escribirError("Analizador semántico", "No es correcta la expresión", AnManager.lineasST);
					EhayError = false;
				}
				//Analizador semántico
				X.tipo = E.tipo;
				return;
			case "Predecremento":
				escribirParse("22");
				E();
				if (EhayError){	
					Errores.escribirError("Analizador semántico", "No es correcta la expresión", AnManager.lineasST);
					EhayError = false;
				}
				//Analizador semántico
				X.tipo = E.tipo;
				return;
			case "true":
				escribirParse("22");
				E();
				if (EhayError){	
					Errores.escribirError("Analizador semántico", "No es correcta la expresión", AnManager.lineasST);
					EhayError = false;
				}
				//Analizador semántico
				X.tipo = E.tipo;
				return;
			case "false":
				escribirParse("22");
				E();
				if (EhayError){	
					Errores.escribirError("Analizador semántico", "No es correcta la expresión", AnManager.lineasST);
					EhayError = false;
				}
				//Analizador semántico
				X.tipo = E.tipo;
				return;
			case "(":
				escribirParse("22");
				E();
				if (EhayError){	
					Errores.escribirError("Analizador semántico", "No es correcta la expresión", AnManager.lineasST);
					EhayError = false;
				}
				//Analizador semántico
				X.tipo = E.tipo;
				return;
			case ";": //Follow
				escribirParse("23");
				//Analizador semántico
				X.tipo = "Void";
				return; 
			default:
				Errores.escribirError("Analizador sintáctico", "No se permite el token <"+sgtetoken.tipoToken+","+sgtetoken.attrToken+"> aquí", AnManager.lineasST);
				break;
			}	
		}
	}

	public static void C(){
		if (sgtetoken == null){
			Errores.escribirError("Analizador sintáctico", "No hay mas tokens", AnManager.lineasST);
			return;
		}else{
			switch(sgtetoken.tipoToken){
			//First B
			case "var":
				escribirParse("24");
				//Analizador semántico
				//Vamos a comprobar si ya se ha ejecutado return en la funcion (en el caso de que estemos)
				if (functionActual != null && functionActual.yaDevuelto){
					Errores.escribirError("Analizador semántico", "Lo que haya tras el return no se ejecutará", AnManager.lineasST);
				}
				B();
				C();
				return;
			case "if":
				escribirParse("24");
				//Analizador semántico
				//Vamos a comprobar si ya se ha ejecutado return en la funcion (en el caso de que estemos)
				if (functionActual != null && functionActual.yaDevuelto){
					Errores.escribirError("Analizador semántico", "Lo que haya tras el return no se ejecutará", AnManager.lineasST);
				}
				B();
				C();
				return;
			case "ID":
				escribirParse("24");
				//Analizador semántico
				//Vamos a comprobar si ya se ha ejecutado return en la funcion (en el caso de que estemos)
				if (functionActual != null && functionActual.yaDevuelto){
					Errores.escribirError("Analizador semántico", "Lo que haya tras el return no se ejecutará", AnManager.lineasST);
				}
				B();
				C();
				return;
			case "return":
				escribirParse("24");
				//Analizador semántico
				//Vamos a comprobar si ya se ha ejecutado return en la funcion (en el caso de que estemos)
				if (functionActual != null && functionActual.yaDevuelto){
					Errores.escribirError("Analizador semántico", "Lo que haya tras el return no se ejecutará", AnManager.lineasST);
				}
				B();
				C();
				return;
			case "write":
				escribirParse("24");
				//Analizador semántico
				//Vamos a comprobar si ya se ha ejecutado return en la funcion (en el caso de que estemos)
				if (functionActual != null && functionActual.yaDevuelto){
					Errores.escribirError("Analizador semántico", "Lo que haya tras el return no se ejecutará", AnManager.lineasST);
				}
				B();
				C();
				return;
			case "prompt":
				escribirParse("24");
				//Analizador semántico
				//Vamos a comprobar si ya se ha ejecutado return en la funcion (en el caso de que estemos)
				if (functionActual != null && functionActual.yaDevuelto){
					Errores.escribirError("Analizador semántico", "Lo que haya tras el return no se ejecutará", AnManager.lineasST);
				}
				B();
				C();
				return;
			case "for":
				escribirParse("24");
				//Analizador semántico
				//Vamos a comprobar si ya se ha ejecutado return en la funcion (en el caso de que estemos)
				if (functionActual != null && functionActual.yaDevuelto){
					Errores.escribirError("Analizador semántico", "Lo que haya tras el return no se ejecutará", AnManager.lineasST);
				}
				B();
				C();
				return;
			case "EOL":	//Tenemos en cuenta aquí el salto de línea, pero no lo meteremos en el árbol
				sgtetoken = AnManager.pedirTokenAlex();
				C();
				return;
				//follow C
			case "}":
				escribirParse("25");
				return;
			default:
				Errores.escribirError("Analizador sintáctico", "No se permite el token <"+sgtetoken.tipoToken+","+sgtetoken.attrToken+"> aquí", AnManager.lineasST);
				//En el caso de que haya funcion anidada
				if (sgtetoken.tipoToken.equals("function")){
					Errores.panicModeLlaves(0);
				}
				
				break;
			}	
		}
	}

	public static void F(){
		if (sgtetoken == null){
			Errores.escribirError("Analizador sintáctico", "No hay mas tokens", AnManager.lineasST);
			return;
		}else{
			if (sgtetoken.tipoToken.equals("function")){

				sgtetoken = AnManager.pedirTokenAlex();
				escribirParse("26");
				H();
				if (sgtetoken.tipoToken.equals("ID")){
					//Analizador semántico
					if ((functionActual = buscaTS(TSG, sgtetoken.attrToken)) != null){
						Errores.escribirError("Analizador semántico", "La función "+sgtetoken.attrToken+" ya se ha declarado anteriormente", AnManager.lineasST);
						//Seguiremos analizando normalmente pero no la añadiremos a la tabla final
						functionActual = new itemTS();
						functionActual.tipo = "Null";
						functionActual.tipoDevuelto = H.tipo;
						functionActual.lexema = sgtetoken.attrToken;
						functionActual.argumentos = 0;
						TSL.clear();
						functionActual.yaDevuelto = false;
					}else{
						functionActual = new itemTS();
						functionActual.tipo = "Función";
						functionActual.tipoDevuelto = H.tipo;
						functionActual.lexema = sgtetoken.attrToken;
						tituloFunciones.add(sgtetoken.attrToken);
						functionActual.argumentos = 0;
						TSL.clear();
						functionActual.yaDevuelto = false;
					}


					sgtetoken = AnManager.pedirTokenAlex();
				}else{
					Errores.escribirError("Analizador sintáctico", "Se esperaba un identificador en 'function'. Se ha recibido el token <"+sgtetoken.tipoToken+","+sgtetoken.attrToken+"> aquí", AnManager.lineasST);
					//Tratamiento de error:
					if (AnManager.comprobarSiguienteToken().tipoToken.equals("(")){ //Si el siguiente token es el que esperamos, continuamos normalmente
						sgtetoken = AnManager.pedirTokenAlex();
						sgtetoken = AnManager.pedirTokenAlex();
					}else{ //Si no es lo que esperamos, procedemos al PanicMode
						Errores.panicModeLlaves(0);;
						return;
					}
				}
				if (sgtetoken.tipoToken.equals("(")){
					sgtetoken = AnManager.pedirTokenAlex();
				}else{
					Errores.escribirError("Analizador sintáctico", "Se esperaba '('. Se ha recibido el token <"+sgtetoken.tipoToken+","+sgtetoken.attrToken+"> aquí", AnManager.lineasST);
					//Tratamiento de error:
						Errores.panicModeLlaves(0);;
						return;
					
				}
				A();
				//Ya se han añadido los argumentos a la correspondiente tabla e itemTS
				if (sgtetoken.tipoToken.equals(")")){
					sgtetoken = AnManager.pedirTokenAlex();
				}else{
					Errores.escribirError("Analizador sintáctico", "Se esperaba ')'. Se ha recibido el token <"+sgtetoken.tipoToken+","+sgtetoken.attrToken+"> aquí", AnManager.lineasST);
					//Tratamiento de error:
					if (AnManager.comprobarSiguienteToken().tipoToken.equals("EOL") || AnManager.comprobarSiguienteToken().tipoToken.equals("{")){ //Si el siguiente token es el que esperamos, continuamos normalmente
						sgtetoken = AnManager.pedirTokenAlex();
						sgtetoken = AnManager.pedirTokenAlex();
					}else{ //Si no es lo que esperamos, procedemos al PanicMode
						Errores.panicModeLlaves(0);
						return;
					}
				}
				if (sgtetoken.tipoToken.equals("EOL")){ //Permitimos aqui un salto de línea
					sgtetoken = AnManager.pedirTokenAlex();
				}
				if (sgtetoken.tipoToken.equals("{")){
					sgtetoken = AnManager.pedirTokenAlex();
				}else{
					Errores.escribirError("Analizador sintáctico", "Se esperaba '{'. Se ha recibido el token <"+sgtetoken.tipoToken+","+sgtetoken.attrToken+"> aquí", AnManager.lineasST);
						Errores.panicModeLlaves(1);
						return;
				}
				C();
				if (sgtetoken.tipoToken.equals("EOL")){ //Permitimos aqui un salto de línea (En el caso de que haya habido error)
					sgtetoken = AnManager.pedirTokenAlex();
				}
				if(!functionActual.yaDevuelto && !functionActual.tipoDevuelto.equals("Void")){
					Errores.escribirError("Analizador semántico", "No devuelve nada la función "+functionActual.lexema, AnManager.lineasST);
				}
				//El valor devuelto ya estaría comprobado
				//Insertamos todos los datos de la funcion en la tabla de símbolos
				//Si es error no la guardamos
				if (!functionActual.tipo.equals("Null")){
				ArrayList<itemTS> copy = new ArrayList<itemTS>(TSL);
				tablasSimbolos.add(idFunction,copy);
				int i = 0;
				while (i<tablasSimbolos.size()){
					i++;
				}
				functionActual.entID = idFunction;
				idFunction++;
				TSG.add(functionActual); //Añadimos a la tabla global
				}
				//Reiniciamos variables
				TSL.clear();
				functionActual = null;


				if (sgtetoken.tipoToken.equals("}")){
					sgtetoken = AnManager.pedirTokenAlex();
				}else{
					Errores.escribirError("Analizador sintáctico", "Se esperaba '}'. Se ha recibido el token <"+sgtetoken.tipoToken+","+sgtetoken.attrToken+"> aquí", AnManager.lineasST);
					return;
				}	
			}else{
				Errores.escribirError("Analizador sintáctico", "Se esperaba ';'. Se ha recibido el token <"+sgtetoken.tipoToken+","+sgtetoken.attrToken+"> aquí", AnManager.lineasST);
				Errores.panicMode();
				return;
			}
		}
	}

	public static void H(){
		if (sgtetoken == null){
			Errores.escribirError("Analizador sintáctico", "No hay mas tokens", AnManager.lineasST);
			return;
		}else{
			//First T
			switch(sgtetoken.tipoToken){
			case "int":
				escribirParse("27");
				T();

				//Analizador semántico
				H.tipo = "Entero";

				return;
			case "chars":
				escribirParse("27");
				T();

				//Analizador semántico
				H.tipo = "Chars";

				return;
			case "bool":
				escribirParse("27");
				T();

				//Analizador semántico
				H.tipo = "Bool";

				return;
				//Follow H
			case "ID":
				escribirParse("28");

				//Analizador semántico
				H.tipo = "Void";

				return;
			default:
				Errores.escribirError("Analizador sintáctico", "Se debe poner un 'Tipo' o un 'Identificador' tras 'function'", AnManager.lineasST);
				break;
			}	
		}
	}

	public static void A(){
		if (sgtetoken == null){
			Errores.escribirError("Analizador sintáctico", "No hay mas tokens", AnManager.lineasST);
			return;
		}else{
			switch(sgtetoken.tipoToken){
			//First T
			case "int":
				escribirParse("29");
				T();

				//Analizador semántico
				if (sgtetoken.tipoToken.equals("ID")){
					//También tenemos que añadir los parámetros a la TS
					itemTS a= new itemTS();
					a.lexema = sgtetoken.attrToken;
					a.tipo = T.tipo;
					a.desplazamiento = calculoDesplazamiento(T.tipo);
					functionActual.argumentos = functionActual.argumentos + 1;
					TSL.add(a);
					sgtetoken = AnManager.pedirTokenAlex();
				}else{
					Errores.escribirError("Analizador sintáctico", "Se esperaba un identificador. Se ha recibido el token <"+sgtetoken.tipoToken+","+sgtetoken.attrToken+"> aquí", AnManager.lineasST);
					return;
				}	
				K();
				return;
			case "chars":
				escribirParse("29");
				T();

				//Analizador semántico
				if (sgtetoken.tipoToken.equals("ID")){
					//También tenemos que añadir los parámetros a la TS
					itemTS a= new itemTS();
					a.lexema = sgtetoken.attrToken;
					a.tipo = T.tipo;
					a.desplazamiento = calculoDesplazamiento(T.tipo);
					functionActual.argumentos = functionActual.argumentos + 1;
					TSL.add(a);

					sgtetoken = AnManager.pedirTokenAlex();
				}else{
					Errores.escribirError("Analizador sintáctico", "Se esperaba un identificador. Se ha recibido el token <"+sgtetoken.tipoToken+","+sgtetoken.attrToken+"> aquí", AnManager.lineasST);
				return;
				}	
				K();
				return;
			case "bool":
				escribirParse("29");
				T();

				//Analizador semántico
				if (sgtetoken.tipoToken.equals("ID")){
					//También tenemos que añadir los parámetros a la TS
					itemTS a= new itemTS();
					a.lexema = sgtetoken.attrToken;
					a.tipo = T.tipo;
					a.desplazamiento = calculoDesplazamiento(T.tipo);
					functionActual.argumentos = functionActual.argumentos + 1;
					TSL.add(a);
					sgtetoken = AnManager.pedirTokenAlex();
				}else{
					Errores.escribirError("Analizador sintáctico", "Se esperaba un identificador. Se ha recibido el token <"+sgtetoken.tipoToken+","+sgtetoken.attrToken+"> aquí", AnManager.lineasST);
				return;
				}	
				K();
				return;
				//Follow A
			case ")":
				escribirParse("30");
				return;
			default:
				Errores.escribirError("Analizador sintáctico", "Falta el 'Tipo' del parámetro que recibe la función", AnManager.lineasST);
				if (sgtetoken.tipoToken.equals("ID")){ //Se ha olvidado el tipo.
					sgtetoken = AnManager.pedirTokenAlex();
				}	
				break;
			}	
		}
	}

	public static void K(){
		if (sgtetoken == null){
			Errores.escribirError("Analizador sintáctico", "No hay mas tokens", AnManager.lineasST);
			return;
		}else{
			switch(sgtetoken.tipoToken){
			case "Coma":
				escribirParse("31");
				sgtetoken = AnManager.pedirTokenAlex();
				T();

				//Analizador semántico
				if (sgtetoken.tipoToken.equals("ID")){
					//añadir los parámetros a la TS
					itemTS a= new itemTS();
					a.lexema = sgtetoken.attrToken;
					a.tipo = T.tipo;
					a.desplazamiento = calculoDesplazamiento(T.tipo);
					functionActual.argumentos = functionActual.argumentos + 1;
					TSL.add(a);
					sgtetoken = AnManager.pedirTokenAlex();
					K();
				}else{
					Errores.escribirError("Analizador sintáctico", "Se esperaba un identificador. Se ha recibido el token <"+sgtetoken.tipoToken+","+sgtetoken.attrToken+"> aquí", AnManager.lineasST);
				return;
				}	
				return;
				//Follow K
			case ")":
				escribirParse("32");
				return;
			default:
				break;
			}	
		}
	}

	public static void L(){
		if (sgtetoken == null){
			Errores.escribirError("Analizador sintáctico", "No hay mas tokens", AnManager.lineasST);
			return;
		}else{
			switch(sgtetoken.tipoToken){
			//First E
			case "ID":
				escribirParse("33");
				E();
				LArgumentos.clear();
				LArgumentos.add(E.tipo);
				Q();
				if (EhayError){	
					Errores.escribirError("Analizador semántico", "No es correcta la expresión", AnManager.lineasST);
					EhayError = false;
				}
				return;
			case "Entero":
				escribirParse("33");
				E();
				LArgumentos.clear();
				LArgumentos.add(E.tipo);
				Q();
				if (EhayError){	
					Errores.escribirError("Analizador semántico", "No es correcta la expresión", AnManager.lineasST);
					EhayError = false;
				}
				return;
			case "Cadena":
				escribirParse("33");
				E();
				LArgumentos.clear();
				LArgumentos.add(E.tipo);
				Q();
				if (EhayError){	
					Errores.escribirError("Analizador semántico", "No es correcta la expresión", AnManager.lineasST);
					EhayError = false;
				}
				return;
			case "Predecremento":
				escribirParse("33");
				E();
				LArgumentos.clear();
				LArgumentos.add(E.tipo);
				Q();
				if (EhayError){	
					Errores.escribirError("Analizador semántico", "No es correcta la expresión", AnManager.lineasST);
					EhayError = false;
				}
				return;
			case "true":
				escribirParse("33");
				E();
				LArgumentos.clear();
				LArgumentos.add(E.tipo);
				Q();
				if (EhayError){	
					Errores.escribirError("Analizador semántico", "No es correcta la expresión", AnManager.lineasST);
					EhayError = false;
				}
				return;
			case "false":
				escribirParse("33");
				E();
				LArgumentos.clear();
				LArgumentos.add(E.tipo);
				Q();
				if (EhayError){	
					Errores.escribirError("Analizador semántico", "No es correcta la expresión", AnManager.lineasST);
					EhayError = false;
				}
				return;
			case "(":
				escribirParse("33");
				E();
				LArgumentos.clear();
				LArgumentos.add(E.tipo);
				Q();
				if (EhayError){	
					Errores.escribirError("Analizador semántico", "No es correcta la expresión", AnManager.lineasST);
					EhayError = false;
				}
				return;
				//Follow L
			case ")":
				escribirParse("34");
				return;
			default:
				break;
			}	
		}
	}
	public static void Q(){
		if (sgtetoken == null){
			Errores.escribirError("Analizador sintáctico", "No hay mas tokens", AnManager.lineasST);
			return;
		}else{
			switch(sgtetoken.tipoToken){
			case "Coma":
				escribirParse("35");
				sgtetoken = AnManager.pedirTokenAlex();
				E();
				LArgumentos.add(E.tipo);
				Q();
				return;
				//Follow Q
			case ")":
				escribirParse("36");
				return;
			default:
				break;
			}	
		}
	}

	public static void E(){
		if (sgtetoken == null){
			Errores.escribirError("Analizador sintáctico", "No hay mas tokens", AnManager.lineasST);
			return;
		}else{
			switch(sgtetoken.tipoToken){
			//First R
			case "ID":
				escribirParse("37");
				R();
				String tipo = R.tipo;
				E.tipo = tipo;
				Ea();
				if (EaRel){
					if (!(R.tipo.equals("Bool") && tipo.equals("Bool")) ){
						EhayError = true;
					}
					E.tipo = "Bool";
				}
				EaRel = false;

				if (EhayError){
					EhayError = false;
					Errores.escribirError("Analizador semántico", "No es correcta la expresión", AnManager.lineasST);
				}

				return;
			case "Entero":
				escribirParse("37");
				R();
				String tipo1 = R.tipo;
				E.tipo = tipo1;
				Ea();
				if (EaRel){
					if (!(R.tipo.equals("Bool") && tipo1.equals("Bool")) ){
						EhayError = true;
					}
					E.tipo = "Bool";
				}
				EaRel = false;
				return;
			case "Cadena":
				escribirParse("37");
				R();
				String tipo2 = R.tipo;
				E.tipo = tipo2;
				Ea();
				if (EaRel){
					if (!(R.tipo.equals("Bool") && tipo2.equals("Bool")) ){
						EhayError = true;
					}
					E.tipo = "Bool";
				}
				EaRel = false;
				return;
			case "Predecremento":
				escribirParse("37");
				R();
				String tipo3 = R.tipo;
				E.tipo = tipo3;
				Ea();
				if (EaRel){
					if (!(R.tipo.equals("Bool") && tipo3.equals("Bool")) ){
						EhayError = true;
					}
					E.tipo = "Bool";
				}
				EaRel = false;
				return;
			case "true":
				escribirParse("37");
				R();
				String tipo4 = R.tipo;
				E.tipo = tipo4;
				Ea();
				if (EaRel){
					if (!(R.tipo.equals("Bool") && tipo4.equals("Bool")) ){
						EhayError = true;
					}
					E.tipo = "Bool";
				}
				EaRel = false;
				return;
			case "false":
				escribirParse("37");
				R();
				String tipo5 = R.tipo;
				E.tipo = tipo5;
				Ea();
				if (EaRel){
					if (!(R.tipo.equals("Bool") && tipo5.equals("Bool")) ){
						EhayError = true;
					}
					E.tipo = "Bool";
				}
				EaRel = false;
				return;
			case "(":
				escribirParse("37");
				R();
				String tipo6 = R.tipo;
				E.tipo = tipo6;
				Ea();
				if (EaRel){
					if (!(R.tipo.equals("Bool") && tipo6.equals("Bool")) ){
						EhayError = true;
					}
					E.tipo = "Bool";
				}
				EaRel = false;
				return;
			default:
				break;
			}	
		}
	}

	public static void Ea(){
		if (sgtetoken == null){
			Errores.escribirError("Analizador sintáctico", "No hay mas tokens", AnManager.lineasST);
			return;
		}else{
			switch(sgtetoken.tipoToken){
			case "OpLog":
				escribirParse("38");
				sgtetoken = AnManager.pedirTokenAlex();
				E();
				EaRel = true;
				if (!R.tipo.equals("Bool")){
					EhayError = true;
				}
				return;
				//Follow Ea
			case ";":
				escribirParse("39");
				return;
			case ")":
				escribirParse("39");
				return;
			case "Coma":
				escribirParse("39");
				return;
			default:
				break;
			}	
		}
	}

	public static void R(){
		if (sgtetoken == null){
			Errores.escribirError("Analizador sintáctico", "No hay mas tokens", AnManager.lineasST);
			return;
		}else{
			switch(sgtetoken.tipoToken){
			//First U
			case "ID":
				escribirParse("40");
				U();
				String tipo = U.tipo;
				R.tipo = tipo;
				Ra();
				if (RaRel){
					if (!(U.tipo.equals("Entero") && tipo.equals("Entero")) ){
						EhayError = true;
					}
					R.tipo = "Bool";
				}
				RaRel = false;

				return;
				//Follow Ea
			case "Entero":
				escribirParse("40");
				U();
				String tipo1 = U.tipo;
				R.tipo = tipo1;
				Ra();
				if (RaRel){
					if (!(U.tipo.equals("Entero") && tipo1.equals("Entero")) ){
						EhayError = true;
					}
					R.tipo = "Bool";
				}
				RaRel = false;

				return;
			case "Cadena":
				escribirParse("40");
				U();
				String tipo2 = U.tipo;
				R.tipo = tipo2;
				Ra();
				if (RaRel){
					if (!(U.tipo.equals("Entero") && tipo2.equals("Entero")) ){
						EhayError = true;
					}
					R.tipo = "Bool";
				}
				RaRel = false;

				return;
			case "Predecremento":
				escribirParse("40");
				U();
				String tipo3 = U.tipo;
				R.tipo = tipo3;
				Ra();
				if (RaRel){
					if (!(U.tipo.equals("Entero") && tipo3.equals("Entero")) ){
						EhayError = true;
					}
					R.tipo = "Bool";
				}
				RaRel = false;

				return;
			case "true":
				escribirParse("40");
				U();
				String tipo4 = U.tipo;
				R.tipo = tipo4;
				Ra();
				if (RaRel){
					if (!(U.tipo.equals("Entero") && tipo4.equals("Entero")) ){
						EhayError = true;
					}
					R.tipo = "Bool";
				}
				RaRel = false;

				return;
			case "false":
				escribirParse("40");
				U();
				String tipo5 = U.tipo;
				R.tipo = tipo5;
				Ra();
				if (RaRel){
					if (!(U.tipo.equals("Entero") && tipo5.equals("Entero")) ){
						EhayError = true;
					}
					R.tipo = "Bool";
				}
				RaRel = false;

				return;
			case "(":
				escribirParse("40");
				U();
				String tipo6 = U.tipo;
				R.tipo = tipo6;
				Ra();
				if (RaRel){
					if (!(U.tipo.equals("Entero") && tipo6.equals("Entero")) ){
						EhayError = true;
					}
					R.tipo = "Bool";
				}
				RaRel = false;

				return;
			default:
				break;
			}	
		}
	}

	public static void Ra(){
		if (sgtetoken == null){
			Errores.escribirError("Analizador sintáctico", "No hay mas tokens", AnManager.lineasST);
			return;
		}else{
			switch(sgtetoken.tipoToken){
			case "OpRelac":
				escribirParse("41");
				sgtetoken = AnManager.pedirTokenAlex();	
				R();
				RaRel = true;
				if (!U.tipo.equals("Entero")){
					EhayError = true;
				}
				return;
				//Follow Ra
			case "OpLog":
				escribirParse("42");
				return;
			case ";":
				escribirParse("42");
				return;
			case ")":
				escribirParse("42");
				return;
			case "Coma":
				escribirParse("42");
				return;
			default:
				break;
			}	

		}
	}
	public static void U(){
		if (sgtetoken == null){
			Errores.escribirError("Analizador sintáctico", "No hay mas tokens", AnManager.lineasST);
			return;
		}else{
			switch(sgtetoken.tipoToken){
			//First V
			case "ID":
				escribirParse("43");
				V();
				String tipo = V.tipo;
				U.tipo = tipo;
				Ua();
				if (UaSuma){
					if (!(Ua.tipo.equals("Entero") && tipo.equals("Entero")) ){
						EhayError = true;
					}
					U.tipo = "Entero";
				}
				UaSuma = false;
				return;
			case "Entero":
				escribirParse("43");
				V();
				String tipo1 = V.tipo;
				U.tipo = tipo1;
				Ua();
				if (UaSuma){
					if (!(Ua.tipo.equals("Entero") && tipo1.equals("Entero")) ){
						EhayError = true;
					}
					U.tipo = "Entero";
				}
				UaSuma = false;
				return;
			case "Cadena":
				escribirParse("43");
				V();
				String tipo2 = V.tipo;
				U.tipo = tipo2;
				Ua();
				if (UaSuma){
					if (!(Ua.tipo.equals("Entero") && tipo2.equals("Entero")) ){
						EhayError = true;
					}
					U.tipo = "Entero";
				}
				UaSuma = false;
				return;
			case "Predecremento":
				escribirParse("43");
				V();
				String tipo3 = V.tipo;
				U.tipo = tipo3;
				Ua();
				if (UaSuma){
					if (!(Ua.tipo.equals("Entero") && tipo3.equals("Entero")) ){
						EhayError = true;
					}
					U.tipo = "Entero";
				}
				UaSuma = false;
				return;
			case "true":
				escribirParse("43");
				V();
				String tipo4 = V.tipo;
				U.tipo = tipo4;
				Ua();
				if (UaSuma){
					if (!(Ua.tipo.equals("Entero") && tipo4.equals("Entero")) ){
						EhayError = true;
					}
					U.tipo = "Entero";
				}
				UaSuma = false;
				return;
			case "false":
				escribirParse("43");
				V();
				String tipo5 = V.tipo;
				U.tipo = tipo5;
				Ua();
				if (UaSuma){
					if (!(Ua.tipo.equals("Entero") && tipo5.equals("Entero")) ){
						EhayError = true;
					}
					U.tipo = "Entero";
				}
				UaSuma = false;
				return;
			case "(":
				escribirParse("43");
				V();
				String tipo6 = V.tipo;
				U.tipo = tipo6;
				Ua();
				if (UaSuma){
					if (!(Ua.tipo.equals("Entero") && tipo6.equals("Entero")) ){
						EhayError = true;
					}
					U.tipo = "Entero";
				}
				UaSuma = false;				
				return;
			default:
				break;
			}	
		}
	}

	public static void Ua(){
		if (sgtetoken == null){
			Errores.escribirError("Analizador sintáctico", "No hay mas tokens", AnManager.lineasST);
			return;
		}else{
			switch(sgtetoken.tipoToken){
			case "OpArit":
				escribirParse("44");
				sgtetoken = AnManager.pedirTokenAlex();
				U();
				UaSuma = true;
				//Analizador semántico
				if (!V.tipo.equals("Entero")){
					EhayError = true;
				}
				Ua.tipo = "Entero";
				return;
				//Follow Ua
			case "OpRelac":
				escribirParse("45");
				return;
			case "OpLog":
				escribirParse("45");
				return;
			case ";":
				escribirParse("45");
				return;
			case ")":
				escribirParse("45");
				return;
			case "Coma":
				escribirParse("45");
				return;
			default:
				break;
			}	
		}
	}

	public static void V(){
		if (sgtetoken == null){
			Errores.escribirError("Analizador sintáctico", "No hay mas tokens", AnManager.lineasST);
			return;
		}else{
			switch(sgtetoken.tipoToken){
			case "ID":
				escribirParse("46");
				String lexema = sgtetoken.attrToken;
				sgtetoken = AnManager.pedirTokenAlex();
				Va();
				//Analizador semántico
				itemTS a;
				if (llamadaFuncion){ //Se ha llamado a una función
					int i=0;
					boolean aux = true;
					ArrayList<itemTS> copia;
					if ((a = buscaTS(TSG, lexema)) == null){ //No existe la función
						//Comprobamos si se está haciendo una llamada recursiva:
						if(functionActual != null && (a = functionActual).lexema.equals(lexema)){
							V.tipo = a.tipoDevuelto;
							copia = new ArrayList<itemTS>(TSL);
							if (VaArgumentos.size() == a.argumentos){
								while(i < VaArgumentos.size()){						
									if (!VaArgumentos.get(i).equals(copia.get(i).tipo)){
										aux = false;
									}
									i++;
								}
								if(!aux){
									Errores.escribirError("Analizador semántico", "No se ha llamado a la función "+lexema+" con los argumentos correctos", AnManager.lineasST);	
								}
							}else{
								Errores.escribirError("Analizador semántico", "No se ha llamado a la función "+lexema+" con los argumentos correctos", AnManager.lineasST);	
							}
							copia.clear();
						}else{
							Errores.escribirError("Analizador semántico", "La función "+lexema+" no se ha declarado", AnManager.lineasST);
						}
					}else{ //Existe la función
						V.tipo = a.tipoDevuelto;
						copia = new ArrayList<itemTS>(tablasSimbolos.get(a.entID));
						if (VaArgumentos.size() == a.argumentos){
							while(i < VaArgumentos.size()){						
								if (!VaArgumentos.get(i).equals(copia.get(i).tipo)){
									aux = false;
								}
								i++;
							}
							if(!aux){
								Errores.escribirError("Analizador semántico", "No se ha llamado a la función "+lexema+" con los argumentos correctos", AnManager.lineasST);	
							}
						}else{
							Errores.escribirError("Analizador semántico", "No se ha llamado a la función "+lexema+" con los argumentos correctos", AnManager.lineasST);	
						}
						copia.clear();
					}
					llamadaFuncion = false;
				}else{ //No se ha llamado a una función, se ha llamado una variable.
					if (functionActual == null){ //Si no hay local, buscamos en la global
						if ((a = buscaTS(TSG, lexema)) == null){
							Errores.escribirError("Analizador semántico", "La variable "+lexema+" no se ha declarado anteriormente", AnManager.lineasST);
						}else{
							V.tipo = a.tipo;
						}
					}else{//Si hay local, buscamos en la tabla local
						if ((a = buscaTS(TSL, lexema)) != null || (a = buscaTS(TSG, lexema)) != null){
							V.tipo = a.tipo;
						}else{
							Errores.escribirError("Analizador semántico", "La variable "+lexema+" no se ha declarado anteriormente", AnManager.lineasST);
						}
					}
				}
				return;
			case "Entero":
				escribirParse("47");
				V.tipo = "Entero";
				sgtetoken = AnManager.pedirTokenAlex();
				return;
			case "Cadena":
				escribirParse("48");
				V.tipo = "Chars";
				sgtetoken = AnManager.pedirTokenAlex();
				return;
			case "Predecremento":
				escribirParse("49");
				V.tipo = "Entero";
				sgtetoken = AnManager.pedirTokenAlex();
				if (sgtetoken.tipoToken.equals("ID")){
					//Analizador semántico:
					itemTS a1 = new itemTS();
					if (functionActual == null){ //Si no hay local, buscamos en la global
						if ((a1=buscaTS(TSG, sgtetoken.attrToken)) == null){

						}else{
							if (!a1.tipo.equals("Entero")){
								Errores.escribirError("Analizador semántico", "La variable "+sgtetoken.attrToken+" debe ser de tipo 'Entero'", AnManager.lineasST);
							}
						}
					}else{//Si hay local, buscamos en la tabla local
						if ((a1=buscaTS(TSL, sgtetoken.attrToken)) != null || (a1=buscaTS(TSG, sgtetoken.attrToken)) != null){
							if (!a1.tipo.equals("Entero")){
								Errores.escribirError("Analizador semántico", "La variable "+sgtetoken.attrToken+" debe ser de tipo 'Entero'", AnManager.lineasST);
							}
						}else{
							Errores.escribirError("Analizador semántico", "La variable "+sgtetoken.attrToken+" no se ha declarado anteriormente", AnManager.lineasST);
						}
					}

					sgtetoken = AnManager.pedirTokenAlex();
				}else{
					Errores.escribirError("Analizador sintáctico", "Se esperaba un identificador. Se ha recibido el token <"+sgtetoken.tipoToken+","+sgtetoken.attrToken+"> aquí", AnManager.lineasST);
					return;
				}	
				return;
			case "true":
				escribirParse("50");
				V.tipo = "Bool";
				sgtetoken = AnManager.pedirTokenAlex();
				return;
			case "false":
				escribirParse("51");
				V.tipo = "Bool";
				sgtetoken = AnManager.pedirTokenAlex();
				return;
			case "(":
				escribirParse("52");
				sgtetoken = AnManager.pedirTokenAlex();
				E();
				V.tipo = E.tipo;
				if (sgtetoken.tipoToken.equals(")")){
					sgtetoken = AnManager.pedirTokenAlex();
				}else{
					Errores.escribirError("Analizador sintáctico", "Falta el cierre de un paréntesis", AnManager.lineasST);
				}	
				return;
			default:
				break;
			}	
		}
	}

	public static void Va(){
		if (sgtetoken == null){
			Errores.escribirError("Analizador sintáctico", "No hay mas tokens", AnManager.lineasST);
			return;
		}else{
			switch(sgtetoken.tipoToken){
			case "(":
				escribirParse("54");
				sgtetoken = AnManager.pedirTokenAlex();
				L();
				//Analizador semántico
				VaArgumentos = LArgumentos;
				llamadaFuncion=true;
				if (sgtetoken.tipoToken.equals(")")){
					sgtetoken = AnManager.pedirTokenAlex();
				}else{
					Errores.escribirError("Analizador sintáctico", "Falta el cierre de un paréntesis", AnManager.lineasST);
				}	
				return;
				//Follow Va
			case "OpRelac":
				escribirParse("53");
				llamadaFuncion=false;
				return;
			case "OpLog":
				escribirParse("53");
				llamadaFuncion=false;
				return;
			case "OpArit":
				escribirParse("53");
				llamadaFuncion=false;
				return;
			case ";":
				escribirParse("53");
				llamadaFuncion=false;
				return;
			case ")":
				escribirParse("53");
				llamadaFuncion=false;
				return;
			case "Coma":
				escribirParse("53");
				llamadaFuncion=false;
				return;
			default:
				break;
			}	
		}
	}
	//-----Funciones del analizador sintáctico--------------------
	//Generamos el archivo "Parse"
	public static void genArchivoParse(){
		String path = AnManager.getPath() + File.separator +"Resultados Grupo144" + File.separator+ "Parse.txt";
		File f = new File(path);
		f.getParentFile().mkdirs(); 
		try {
			f.delete(); //Eliminamos si existe algo antes
			f.createNewFile();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Fallo al crear archivo parse");
		}
	}
	//Función para escribir en el archivo "Parse"
	public static void escribirParse(String dato){
		PrintWriter pw = null;
		try {
			File file = new File(AnManager.getPath() + File.separator +"Resultados Grupo144" + File.separator+ "Parse.txt");
			FileWriter fw = new FileWriter(file, true);
			pw = new PrintWriter(fw);
			pw.print(dato + " ");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (pw != null) {
				pw.close();
			}
		}
	}


	//-----Funciones del analizador semántico--------------------
	//Funcion que comprueba si existe ya en la tabla de símbolos
	public static itemTS buscaTS(ArrayList <itemTS> TS, String lex){
		int i = 0;
		if (TS != null){
			while(i < TS.size()){
				if (TS.get(i).lexema.equals(lex)){
					return TS.get(i);
				}
				i++;
			}
		}
		return null;
	}

	//Calcula el desplazamiento dependiendo del tipo
	private static int calculoDesplazamiento(String tipo){
		if (tipo.equals("Entero")){
			return desInt;
		}
		if (tipo.equals("Bool")){
			return desBool;
		}
		if (tipo.equals("Chars")){
			return desChars;
		}
		return -1;
	}
	//Generamos el archivo "Tabla de Símbolos"
	public static void genArchivoTS(){
		String path = AnManager.getPath() + File.separator +"Resultados Grupo144" + File.separator+ "Tabla de Símbolos.txt";
		File f = new File(path);
		f.getParentFile().mkdirs(); 
		try {
			f.delete(); //Eliminamos si existe algo antes
			f.createNewFile();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Fallo al crear archivo Tabla de Símbolos");
		}
	}
	//Función que imprime todas las tablas de símbolos generadas
	public static void imprimirTablas(){
		PrintWriter pw = null;
		try {
			File file = new File(AnManager.getPath() + File.separator +"Resultados Grupo144" + File.separator+ "Tabla de Símbolos.txt");
			FileWriter fw = new FileWriter(file, true);
			pw = new PrintWriter(fw);

			int i = 0;
			int desplazamiento = 0;
			pw.println("TABLA GLOBAL #1 :"); //Tabla global
			pw.println(""); //Espacio
			while(i<tablasSimbolos.get(0).size()){
				if (tablasSimbolos.get(0).get(i).tipo.equals("Función")){ //Si es una función:
					pw.println("  * LEXEMA: '"+tablasSimbolos.get(0).get(i).lexema+"' (Función)");
					pw.println("	+ TipoDevuelto: '"+tablasSimbolos.get(0).get(i).tipoDevuelto+"'");
					pw.println("	+ NºParámetros: '"+tablasSimbolos.get(0).get(i).argumentos+"'");
					pw.println("");
				}else{
					pw.println("  * LEXEMA: '"+tablasSimbolos.get(0).get(i).lexema+"'");
					pw.println("	+ Tipo: '"+tablasSimbolos.get(0).get(i).tipo+"'");
					pw.println("	+ Desplazamiento: '"+desplazamiento+"'");
					desplazamiento = desplazamiento + tablasSimbolos.get(0).get(i).desplazamiento;
					pw.println("");
				}
				i++;
			}
			pw.println("-----------------------------------------------------------------------"); 
			//Ahora introducimos las demás tablas

			int j = 0;
			int x;
			int argumentos;
			while(j<tituloFunciones.size()){
				x = 0;
				pw.println("TABLA DE LA FUNCIÓN "+tituloFunciones.get(j)+" #"+(j+2)+" :"); //Tabla local
				pw.println(""); //Espacio
				desplazamiento = 0;
				argumentos = 0;
				while(x < tablasSimbolos.get(j+1).size()){
					if (argumentos != buscaTS(TSG, tituloFunciones.get(j)).argumentos){
						pw.println("  * LEXEMA: '"+tablasSimbolos.get(j+1).get(x).lexema+"' (parámetro de función)");
						argumentos++;
					}else{
						pw.println("  * LEXEMA: '"+tablasSimbolos.get(j+1).get(x).lexema+"'");
					}
					pw.println("	+ Tipo: '"+tablasSimbolos.get(j+1).get(x).tipo+"'");
					pw.println("	+ Desplazamiento: '"+desplazamiento+"'");
					desplazamiento = desplazamiento + tablasSimbolos.get(j+1).get(x).desplazamiento;
					pw.println("");
					x++;
				}
				j++;
				pw.println("-----------------------------------------------------------------------"); 
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (pw != null) {
				pw.close();
			}
		}
	}
}
