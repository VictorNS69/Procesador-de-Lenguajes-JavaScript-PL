package Scripts;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import Scripts.Token.Accion;


public class AnManager {
	public static String pathDir;
	//Contador de lineas para los errores (L�xico y Sint�ctico/Sem�ntico)
	public static int contadorLineas=1;
	public static int lineasST = 1;
	//Lista de tokens para el analizador sint�ctico
	public static ArrayList <Token> listaTokens = new ArrayList<Token>();
	public static int posicionLista = 0;

	//Variable que servir� para detectar si ha habido errores o no
	public static boolean sinErrores = true;
	
	public static void setPath(String path){
		pathDir = path.replace("\\", "/");
	}
	
	public static Token pedirTokenAlex(){
		if (posicionLista < listaTokens.size()){
			if (listaTokens.get(posicionLista).tipoToken.equals("EOL")){
				lineasST++;//Sumamos linea
			}
			return listaTokens.get(posicionLista++);
		}else{
			return null;
		}
	}
	public static Token comprobarSiguienteToken(){
		if (posicionLista < listaTokens.size()){
			return listaTokens.get(posicionLista);
		}else{
			return null;
		}
	}
	public static String getPath(){
		return pathDir;
	}

	//Proceso principal
	public static void pPrincipal(BufferedReader file) throws IOException{
		//Generacion de archivos
		genArchivoGramatica();
		rellenarGramatica();
		Token.genArchivoTokens();
		Errores.genArchivoErrores();
		AnalizadorSinSem.genArchivoParse();
		AnalizadorSinSem.genArchivoTS();
		//Proceso de lectura de tokens
		int state = 0;
		Accion accion;
		String concatenacion = "";
		int c;
		while((c = file.read()) != -1) {
			char character = (char) c; //Leemos un caracter
			accion = Token.estado(state, character);
			state = accion.estado;
			if(character == '\n') contadorLineas++; //Incrementamos contador lineas si encontramos un salto de linea
			//Accion: leer 0
			//        No leer 1, es decir, volvemos a ejecutar con otro estado
			//        Leer y Concatenar 2
			//        Comprobar palRes 3
			//        GenToken Numero 4
			//        GenToken Cadena 5
			while(accion.accion != 0){
				if (accion.accion == 1){
					accion = Token.estado(state, character); 
					state = accion.estado;
					continue;
				}
				if (accion.accion == 2){
					concatenacion = concatenacion + character;
					break;
				}
				if (accion.accion == 3){
					if(concatenacion.matches("\\b(?:true|var|int|if|false|while|function|bool|string|return|print|prompt)\\b")){
						Token.escribirToken(concatenacion, "",contadorLineas); //Palabra reservada
					}else{
						Token.escribirToken("ID", concatenacion,contadorLineas); //Generamos ID
					}
					concatenacion=""; //Reset
					//Leemos caracter actual:
					accion = Token.estado(state, character); 
					state = accion.estado;
					continue;
				}
				if (accion.accion == 4){
					//Pasamos el string numero a int
					int n = Integer.parseInt(concatenacion);
					if (n<=32767){
						Token.escribirToken("Entero", concatenacion,contadorLineas); //Numero
					}else{
						Errores.escribirError("Analizador lexico","El numero "+n+" sobrepasa el valor permitido" , AnManager.contadorLineas );
					}
					concatenacion = "";
					//Leemos caracter actual:
					accion = Token.estado(state, character); 
					state = accion.estado;
					continue;
				}
				if (accion.accion == 5){
					Token.escribirToken("Cadena", concatenacion+"\"",contadorLineas); //Cadena
					concatenacion=""; //Reset
					break;
				}
			}
		}
		Token.escribirToken("EOF", " ",contadorLineas); //Fin de fichero
		
		//Una vez termina el analizador l�xico y generamos los tokens, ejecutamos analizador Sint�ctico/Sem�ntico
		AnalizadorSinSem.AnalizadorSt();
		//Si no hay errores escribimos: "Sin errores"
		if (sinErrores){
			Errores.escribirOK();
		}
		//Fin
	}
	
	
	//Genera el archivo Gram�tica.txt en la carpeta destino
	public static void genArchivoGramatica(){
		String path = AnManager.getPath() + File.separator +"Resultados Grupo81" + File.separator+ "Gramatica.txt";
		File f = new File(path);
		f.getParentFile().mkdirs(); 
		try {
			f.delete(); //Eliminamos si existe algo antes
			f.createNewFile();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Fallo al crear archivo Gramatica");
		}
	}


	//Gram�tica para Vast
	public static void rellenarGramatica(){
		PrintWriter pw = null;
		try {
			File file = new File(AnManager.getPath() + File.separator +"Resultados Grupo81" + File.separator+ "Gramatica.txt");
			FileWriter fw = new FileWriter(file, true);
			pw = new PrintWriter(fw);
			pw.println("");
			pw.println("Terminales = { var id ; if =  {  } (  ) ! != +  ++ , function int string bool true false return print prompt entero cadena while eof }");
			pw.println("");
			pw.println("NoTerminales = { P B T S Sa X C F H A K L Q R Ra U Ua V Va }");
			pw.println("");
			pw.println("Axioma = P");
			pw.println("");
			pw.println("Producciones = {");
			pw.println("P -> B P");
			pw.println("P -> F P");
			pw.println("P -> eof");
			pw.println("B -> var T id ;");
			pw.println("B -> if ( R ) S");
			pw.println("B -> while ( R ) { C }");
			pw.println("B -> S");
			pw.println("T -> int");
			pw.println("T -> string");
			pw.println("T -> bool");
			pw.println("S -> id Sa");
			pw.println("S -> return X ;");
			pw.println("S -> print ( R ) ; ");
			pw.println("S -> prompt ( id ) ;");
			pw.println("Sa -> = R ; ");
			pw.println("Sa -> ( L ) ;");
			pw.println("X -> R");
			pw.println("X -> lambda");
			pw.println("C -> B C");
			pw.println("C -> lambda");
			pw.println("C -> eol"); // puede dar error
			pw.println("F -> function H id ( A ) { C }");
			pw.println("H -> T");
			pw.println("H -> lambda");
			pw.println("A -> T id K");
			pw.println("A -> lambda");
			pw.println("K -> , T id K");
			pw.println("K -> lambda");
			pw.println("L -> R Q");
			pw.println("L -> lambda");
			pw.println("Q -> , R Q");
			pw.println("Q -> lambda");
			pw.println("R -> U Ra");
			pw.println("Ra -> = R");
			pw.println("Ra -> lambda");
			pw.println("U -> V Ua");
			pw.println("Ua -> + U");
			pw.println("Ua -> lambda");
			pw.println("V -> id Va");
			pw.println("V -> entero");
			pw.println("V -> cadena");
			pw.println("V -> ++ id");
			pw.println("V -> true");
			pw.println("V -> false");
			pw.println("V -> ( R )");
			pw.println("V -> !");
			pw.println("V -> //"); //puede dar error
			pw.println("Va -> ( L )");
			pw.println("Va -> lambda");
			pw.println("}");
			pw.println("");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (pw != null) {
				pw.close();
			}
		}
	}
}
