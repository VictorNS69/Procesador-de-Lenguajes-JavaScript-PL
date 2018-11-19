package global.tabla;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

public class TablaSimbolos {
	private String nombreTabla;
	private int n_entradas = 1;
	private int desp;
	private HashMap<String,Integer> indice;
	private HashMap<Integer,EntradaTS> entradas;
	
	public TablaSimbolos(String nombre){
		this.nombreTabla = nombre;
		this.desp = 0;
		this.indice = new HashMap<String, Integer>();
		this.entradas = new HashMap<Integer, EntradaTS>();
	}
    
    public int buscarTS(String lexema){
        int pos = -1;
    	if(!indice.isEmpty()){
    		if(indice.containsKey(lexema)){
    			pos = indice.get(lexema);
    		}
    	}
        return pos;
    }
    
    public String buscaLexemaTS(int pos){
        String lex = null;
        if(!entradas.isEmpty()){
            if(entradas.containsKey(pos)){
                lex = entradas.get(pos).getLexema();
            }
        }
        return lex;
    }
    
    public String buscaTipoTS(String lexema){
    	String tipo = "-";
    	if(!indice.isEmpty() && !entradas.isEmpty()){
    		if(indice.containsKey(lexema)){
    			if(entradas.containsKey(indice.get(lexema))){
    				tipo = entradas.get(indice.get(lexema)).getTipo();
    			}
    		}
    	}
    	return tipo;
    }

    public String buscaDespTS(String lexema){
        String desp = "-";
        if(!indice.isEmpty() && !entradas.isEmpty()){
    		if(indice.containsKey(lexema)){
    			if(entradas.containsKey(indice.get(lexema))){
    				desp = entradas.get(indice.get(lexema)).getDesp();
    			}
    		}
    	}
        return desp;
    }

    public int buscaNumParamTS(String lexema){
        int nparam = 0;
        if(!indice.isEmpty() && !entradas.isEmpty()){
    		if(indice.containsKey(lexema)){
    			if(entradas.containsKey(indice.get(lexema))){
    				nparam = entradas.get(indice.get(lexema)).getNumParam();
    			}
    		}
    	}
        return nparam;
    }

    public String buscaTipoDevTS(String lexema){
        String tipoDev = "-";
        if(!indice.isEmpty() && !entradas.isEmpty()){
    		if(indice.containsKey(lexema)){
    			if(entradas.containsKey(indice.get(lexema))){
    				tipoDev = entradas.get(indice.get(lexema)).getTipoDev();
    			}
    		}
    	}
        return tipoDev;
    }

    public int insertarTS(String lexema){
        int pos = -1;
        if(!indice.containsKey(lexema)){
        	indice.put(lexema, n_entradas);
        	entradas.put(n_entradas, new EntradaTS(n_entradas));
        	entradas.get(n_entradas).setLexema(lexema);
        	pos = n_entradas;
            n_entradas++;
        }
        return pos; 
    }
    
    public boolean insertaTipoTS(String lexema, String tipo){
    	boolean res = false;
    	if(!indice.isEmpty() && !entradas.isEmpty()){
    		if(indice.containsKey(lexema)){
    			if(entradas.containsKey(indice.get(lexema))){
    				entradas.get(indice.get(lexema)).setTipo(tipo);
        			res = true;
    			}
    		}
    	}
    	return res;
    }
    
    public boolean insertaDespTS(String lexema, String desp){
    	boolean res = false;
    	if(!indice.isEmpty() && !entradas.isEmpty()){
    		if(indice.containsKey(lexema)){
    			if(entradas.containsKey(indice.get(lexema))){
    				entradas.get(indice.get(lexema)).setDesp(desp);
        			res = true;
    			}
    		}
    	}
    	return res;
    }
    
    public boolean insertaNumParamTS(String lexema, int nparam){
    	boolean res = false;
    	if(!indice.isEmpty() && !entradas.isEmpty()){
    		if(indice.containsKey(lexema)){
    			if(entradas.containsKey(indice.get(lexema))){
    				entradas.get(indice.get(lexema)).setNumParam(nparam);
        			res = true;
    			}
    		}
    	}
    	return res;
    }
    
    public boolean insertaTipoDevTS(String lexema, String tipoDev){
    	boolean res = false;
    	if(!indice.isEmpty() && !entradas.isEmpty()){
    		if(indice.containsKey(lexema)){
    			if(entradas.containsKey(indice.get(lexema))){
    				entradas.get(indice.get(lexema)).setTipoDev(tipoDev);
        			res = true;
    			}
    		}
    	}
    	return res;
    }
    
    public int getDesp(){
    	return desp;
    }
    
    public void sumDesp(int x){
    	this.desp += x;
    }

	public String getNombreTabla() {
		return nombreTabla;
	}
	
	public void printTabla(boolean append){
		PrintWriter writer;
		try {
			writer = new PrintWriter(new BufferedWriter(new FileWriter("resources\\tabla_simbolos.txt", append)));
			writer.println("Tabla de símbolos: " +this.nombreTabla);
			writer.println("---------------------------------------------------------------------------------------------------------");
			writer.println(String.format("%15s | %15s | %15s | %15s | %15s | %15s", "POS", "LEXEMA", "TIPO", "DESP", "NPARAM", "TIPODEV"));
			writer.println("---------------------------------------------------------------------------------------------------------");
			Iterator<Entry<Integer, EntradaTS>> it = entradas.entrySet().iterator();
			while(it.hasNext()){
				Entry<Integer,EntradaTS> entrada = it.next();
				writer.println(entrada.getValue().aString());
			}
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
