package global;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;

import lexico.AnalizadorLexico;

public class ControladorErrores {
	private static ArrayList<String> listaErrores =  new ArrayList<String>();

	public static void addError(String error){
		String output = "Cerca de la linea: " + AnalizadorLexico.lineaActual + " Descripcion: " + error;
		listaErrores.add(output);
	}

	public static void printErrors(){
		PrintWriter writer;
		try {
			writer = new PrintWriter("resources/errores.txt", "UTF-8");
			Iterator<String> it = listaErrores.iterator();
			String error;
			while(it.hasNext()){
				error = it.next();
				System.out.println("ERROR: " + error);
				// añadir a fichero
				writer.println("ERROR: " + error);
			}
			writer.close();
			System.out.println("Fichero errores.txt generado");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

}
