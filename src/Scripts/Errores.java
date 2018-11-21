package Scripts;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.JOptionPane;

public class Errores {

	public static void genArchivoErrores(){
		String path = AnManager.getPath() + File.separator +"Resultados Grupo144" + File.separator+ "Errores.txt";
		File f = new File(path);
		f.getParentFile().mkdirs(); 
		try {
			f.delete(); //Eliminamos si existe algo antes
			f.createNewFile();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Fallo al crear archivo errores");
		}
	}

	public static void escribirError(String analizador, String def, int line){
		AnManager.sinErrores = false;
		PrintWriter pw = null;
		try {
			File file = new File(AnManager.getPath() + File.separator +"Resultados Grupo81" + File.separator+ "Errores.txt");
			FileWriter fw = new FileWriter(file, true);
			pw = new PrintWriter(fw);
			pw.println("(linea "+line+") ->Error en "+analizador+": " + def);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (pw != null) {
				pw.close();
			}
		}
	}


	public static void escribirOK(){
		PrintWriter pw = null;
		try {
			File file = new File(AnManager.getPath() + File.separator +"Resultados Grupo81" + File.separator+ "Errores.txt");
			FileWriter fw = new FileWriter(file, true);
			pw = new PrintWriter(fw);
			pw.println("*No se han detectado errores en el c�digo*");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (pw != null) {
				pw.close();
			}
		}
	}

	//Esta funci�n se encarga de realizar la recuperaci�n de errores "Panicmode", buscamos una salida segura, como un ; o un EOL para recuperarse del error y seguir analizando
	public static void panicModeLlaves(int llavesabiertas){ //Saltamos todo hasta encontrar el final de }
		int aux= llavesabiertas; //Contamos posibles llaves
		while(true){
			Token token = AnManager.pedirTokenAlex();
			if (token.tipoToken.equals("{")){
				aux++;
			}
			if (token.tipoToken.equals("}")){
				aux--;
				if (aux <= 0){ //No hay llaves que cerrar pendientes
					AnalizadorSinSem.sgtetoken =  AnManager.pedirTokenAlex();
				}
				return;
			}
			if (token.tipoToken.equals("EOF")){
				AnalizadorSinSem.sgtetoken = token;
				return;
			}
			
			if (token.tipoToken.equals("function")){ //estamos en function
				AnalizadorSinSem.sgtetoken = token;
				return;
			}
		}
	}

	public static void panicMode(){ //Sin escape espec�fico, buscamos 'EOL', ';' o 'EOF'
		if (AnalizadorSinSem.sgtetoken.tipoToken.equals(";") || AnalizadorSinSem.sgtetoken.tipoToken.equals("EOL") || AnalizadorSinSem.sgtetoken.tipoToken.equals("EOF")){
			return;
		}
		while(true){
			Token token = AnManager.pedirTokenAlex();
			if (token.tipoToken.equals(";")){
				AnalizadorSinSem.sgtetoken =  AnManager.pedirTokenAlex();
				return;
			}
			if (token.tipoToken.equals("EOL")){
				AnalizadorSinSem.sgtetoken = token;
				return;
			}
			if (token.tipoToken.equals("EOF")){
				AnalizadorSinSem.sgtetoken = token;
				return;
			}
		}
	}
}
