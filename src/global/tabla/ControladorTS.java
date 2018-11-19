package global.tabla;

import java.util.ArrayList;
import java.util.Stack;

public class ControladorTS {
    
    private static Stack<TablaSimbolos> pilaTablas = new Stack<TablaSimbolos>();
    private static Stack<TablaSimbolos> pilaLocales = new Stack<TablaSimbolos>();
    private static ArrayList<String> tablaReservadas = new ArrayList<String>(){{
        add("bool");
        add("function");
        add("int");
        add("print");
        add("return");
        add("if");
        add("string");
        add("prompt");
        add("while");
        add("true");
        add("flase");
        add("var");
    }};
    private static boolean flagDU = false;//True = Declaracion, False = Uso
    private static boolean flagVF = false;//True = Var, False = Function
    private static String funcionActual;
    
    public static void setFuncion(String funcion){
    	funcionActual = funcion;
    }
    
    public static String getFuncion(){
    	return funcionActual;
    }
    
    public static boolean getFlagDU(){
    	return flagDU;
    }
    
    public static void flagDeclaracion(){
    	flagDU = true;
    }
    public static void flagUso(){
    	flagDU = false;
    }
    
    public static boolean getFlagVF(){
    	return flagVF;
    }
    
    public static void flagVar(){
    	flagVF = true;
    }
    public static void flagFunction(){
    	flagVF = false;
    }
    
    
    public static boolean esReservada(String palabra){
        return tablaReservadas.contains(palabra);
    }
    
    public static void crearTS(String nombre){
        pilaTablas.push(new TablaSimbolos(nombre));
    }
    
    public static void eliminarTS(){
        pilaLocales.push(pilaTablas.pop());
    }
    
    public static String nombreTablaActual(){
    	return pilaTablas.peek().getNombreTabla();
    }
    
    public static int buscaIdTS(String id){
    	int pos = -1;
    	boolean found = false;
    	if(!pilaTablas.isEmpty()){
    		for(int i=0; i < pilaTablas.size() && !found; i++){
    			if(pilaTablas.get(i).buscarTS(id) > 0){
    				pos = pilaTablas.get(i).buscarTS(id);
    				found = true;
    			}
    		}
    	}
    	return pos;
    }

    public static String buscaTipoTS(String id){
    	String tipo = "-";
    	boolean found = false;
    	if(!pilaTablas.isEmpty()){
    		for(int i=0; i < pilaTablas.size() && !found; i++){
    			tipo = pilaTablas.get(i).buscaTipoTS(id);
    			if(!tipo.equals("-")){
    				found = true;
    			}
    		}
    	}
    	return tipo;
    }
    
    public static String buscaDespTS(String id){
    	String desp = "-";
    	boolean found = false;
    	if(!pilaTablas.isEmpty()){
    		for(int i=0; i < pilaTablas.size() && !found; i++){
    			desp = pilaTablas.get(i).buscaDespTS(id);
    			if(!desp.equals("-")){
    				found = true;
    			}
    		}
    	}
    	return desp;
    }
    
    public static int insertaIdTS(String id){
    	if(!pilaTablas.isEmpty()){
    		return pilaTablas.peek().insertarTS(id);
    	}
    	else{
    		return -1;
    	}
    }
    

    public static boolean insertaTipoTS(String id, String tipo){
    	boolean insertado = false;
    	if(!pilaTablas.isEmpty()){
    		if(!pilaTablas.isEmpty()){
    			for(int i=0; i < pilaTablas.size() && !insertado; i++){
    				insertado = pilaTablas.get(i).insertaTipoTS(id, tipo);
    			}
    		}
    	}
    	return insertado;
    }

	public static Stack<TablaSimbolos> getPilaTablas() {
		return pilaTablas;
	}

	public static Stack<TablaSimbolos> getPilaLocales() {
		return pilaLocales;
	}

	public static String getLexema(int pos, String nombreTabla){
		String resultado = "";
		boolean encontrado = false;
		TablaSimbolos tabla;
		for(int i=0; i < pilaTablas.size() && !encontrado; i++){
			tabla = pilaTablas.get(i);
			if(tabla.getNombreTabla().equals(nombreTabla)){
				resultado = tabla.buscaLexemaTS(pos);
				encontrado = true;
			}
		}
		return resultado;
	}
    
	public static void printTablas(){
		pilaTablas.peek().printTabla(false);
		for(int i=pilaLocales.size(); i > 0 ; i--){
			pilaLocales.get(i-1).printTabla(true);
		}
	}
    
	public static String buscaTipoDevTS(String lexema){
		return pilaTablas.peek().buscaTipoDevTS(lexema);
	}
	
	public static void insertaTipoDevTS(String lexema, String tipo){
		pilaTablas.peek().insertaTipoDevTS(lexema, tipo);
	}
    
	
	public static int getDesp(){
		return pilaTablas.peek().getDesp();
	}
	
	public static void sumDesp(int x){
		pilaTablas.peek().sumDesp(x);
	}
	
	public static void insertaDespTS(String lexema, String desp){
		pilaTablas.peek().insertaDespTS(lexema, desp);
	}
}
