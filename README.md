# Practica Procesadores de Lenguajes curso 2018/2019
Diseño y construcción de un Analizador de una versión del lenguaje JavaScript llamado JavaScript-PL.

## Autores
- [Víctor Nieves Sánchez](https://twitter.com/VictorNS69)
- Alejandro Carmona Ayllón
- Miguel Moreno Mardones

## Ejecutar
Para ejecutar, escribir en un terminal:
```
java -jar Analizador.jar
```
O ejecutar en eclipse o similar el archivo [InterfazFile.java](/src/Scripts/InterfazFile.java)

## Nota
Este proyecto no funciona correctamente. Tiene varios fallos y bugs, pero la base de funcionamiento está, y la función básica del problema se resuelve para una gran cantidad de casos de nuestro lenguaje.

## Documentos
En el directorio [_/doc_](/doc) se encuentra la [memoria de la práctica](/doc/Memoria-PDL.pdf).

También como se menciona en la memoria, se encuentra disponible la [tabla LL(1)](/doc/TablaLL1.xlsx).

## Lenguaje JavaScript-PL

- **Comentarios**
Comentario de línea: Los comentarios comienzan por los caracteres // y finalizan al acabar la línea. Este tipo de comentario sólo ocupa una línea y puede ir colocado en cualquier parte del código:
```
	// Comentario de línea
```
- **Constantes**
El lenguaje dispone de varios tipos de constantes:

 - **Enteras**
   Para representar las constantes enteras se utilizan los dígitos decimales. Por ejemplo: 159.

   Los números enteros se tienen que poder representar con 2 bytes (con signo), por lo que el máximo entero será el 32767.

 - **Cadenas de Caracteres**
   Las constantes cadena van encerradas entre comillas dobles ("Hola, mundo"). Se utiliza internamente el carácter nulo (cuyo código ASCII es 0) como carácter de fin de cadena. Puede aparecer cualquier carácter imprimible en la cadena.

- **Lógicas**
Negación (!).

- **Operadores**
	 - **Aritmeticos**
   La suma (+).
 	- **De relación**
   Distinto (!=).
 	- **De incremento y decremento**
   Pre-autoincremento (++ como prefijo).
 	- **De asignación** 
   Igual (=).

- **Identificadores**
Los nombres de identificadores están formados por cualquier cantidad de letras,dígitos y subrayados (\_), siendo el primero siempre una letra. Ejemplos: a, a3, A3, Sueldo\_de\_Trabajador, z\_9\_9\_\_...

Como ya se ha dicho, el lenguaje es dependiente de minúsculas o mayúsculas, por lo que los nombres a3 y A3 referencian a identificadores distintos.

- **Declaraciones**
El lenguaje JavaScript-PL no exige declaración de las variables que se utilicen. En el caso de que se use un nombre de variable que no ha sido declarado previamente, se considera que dicha variable es global y entera.

Para realizar una declaración de una variable, se coloca la palabra var seguida del tipo y del nombre de la variable. Se puede poner una lista de variables separadas por comas:
```
	var Tipo var0;
	var Tipo var1, var2, var3;	// se declaran las tres variables
```

Pueden realizarse declaraciones en cualquier lugar de un bloque de una función; en este caso, la variable será visible desde ese punto hasta el final de la función. También pueden realizarse declaraciones fuera de las funciones en cualquier parte del código (variables globales), siendo solo visibles desde ese punto hasta el final del fichero.

Opcionalmente, puede inicializarse una variable en la misma instrucción de la declaración, colocando detrás del nombre de la variable el operador de asignación (=) seguido de una expresión.
	
```
	var Tipo var4 = expresión4, var5 = expresión5;
```

Si una variable no se inicializa cuando se declara se realiza una inicialización por omisión basándose en su tipo: 0 si es entera, falso si es lógica y la cadena vacía ("") si es cadena.

El ámbito de una variable será global si no se ha declarado o si se declara fuera de cualquier función, y será local si se declara dentro del cuerpo de una función. No se admite la redeclaración del mismo identificador en un mismo ámbito.

- **Tipos de Datos**
El lenguaje dispone de distintos tipos de datos básicos.

Se deben considerar sólo los siguientes tipos de datos básicos: **entero**, **lógico** y **cadena**.

El tipo entero se refiere a un entero que debe representarse con un tamaño de 16 bits. Se representa con la palabra int.

El tipo lógico permite representar valores lógicos. Las expresiones relacionales y lógicas devuelven un valor lógico. Se representa con la palabra bool.

El tipo cadena permite representar secuencias de caracteres. Se representa con la palabra string.

El lenguaje no tiene conversiones automáticas entre tipos.

Ejemplos:
```
	var int i = 11;    // variable entera
	var string st;     // variable cadena 
	var bool b;        // variable lógica
	var int c = 66+i;  // variable entera
	b = i != c + 1;    // i y c+1 son enteros; b valdrá verdadero
	c = c + i;         // i y c son enteras; c valdrá 88
	i = b + i;         // Error: no se puede sumar un lógico con un entero
	b = ! i;           // Error: el operador de negación solo puede aplicarse a lógicos
```

- **Instrucciones de Entrada/Salida**
La sentencia print (expresión) evalúa la expresión e imprime el resultado por pantalla. Por ejemplo:
```
	a= 50; print(a * 2 + 16); /* imprime: 116 */
```

La expresión puede ser también una cadena o un lógico. Por ejemplo:
```
	a= 'Adiós';
	print('Hola'); print(a); /* imprime HolaAdiós */
```
Se admite poner varias expresiones en print separadas por comas (implementación opcional):
```
	print ("El factorial de ", num, " es ", fact(num), ".\n");
```
La sentencia prompt (var) lee un número o una cadena del teclado y lo almacena en la variable var, que tiene que ser, respectivamente, de tipo entero o cadena. Por ejemplo:
```
	var int a;
	var string c;
	prompt (a); // lee un número
	print(a * a); /* imprime el cuadrado del número leído */
	print("Pon tu nombre");
	prompt (c); // lee una cadena
	print("Hola, ", c); /* imprime las cadenas */
```
- **Sentencias**
De todo el grupo de sentencias del lenguaje JavaScript, se han seleccionado para ser implementadas las que aparecen a continuación:

- **Sentencias de Asignación**
Existe una sentencia de asignación en JavaScript-PL, que se construye mediante el símbolo de asignación = . Su sintaxis general es la siguiente: identificador, igual y expresión. Esta sentencia asigna al identificador el resultado de evaluar la expresión:
```
	i= 8 + 6;
```

Como ya se ha indicado, no hay conversiones entre tipos, por lo que tanto el identificador como la expresión han de ser del mismo tipo.
```
		var int i = 123;	// i es una variable entera
		var string cad;
		print (i);   // imprime el valor entero 123
		cad= 'hola';
		print (cad); // imprime el valor cadena "hola"
		i = i > 88;  // Error: no se puede asignar un lógico a un entero
```

- **Sentencia de Llamada a una Función**
Esta sentencia permite invocar la ejecución de una función que debe estar previamente definida .

La llamada a una función se realiza mediante el nombre de la función seguido de los argumentos actuales (separados por comas) entre paréntesis (si no tiene argumentos, hay que poner los paréntesis vacíos). Los argumentos pueden ser cualquier expresión:
```
		p1 (5);        /* llamada a una función con un argumento entero */ 
		p2 ();         /* llamada sin argumentos a una función */ 
		p3 (b, i - 8); /* llamada con dos argumentos a una función */
```
Los parámetros actuales en la llamada tienen que coincidir en número y tipo con los parámetros formales de la declaración de la función.

Si una función devuelve un valor, podrá incluirse una llamada a dicha función dentro de cualquier expresión. Si la llamada se realiza como una sentencia (no se realiza en una expresión), se invocará a la función pero el valor devuelto se perderá:
```
		b= fun1 (9); /* llamada a una función con un argumento entero */ 
		c= b + fun2 (b, fun3() - 8); /* llamada con dos argumentos a una función, siendo fun3, una llamada a otra función sin argumentos */
		fun2 (5, c); /* el valor devuelto por fun2 se pierde */
```
 - **Sentencia de Retorno de una Función**
JavaScript-PL dispone de la sentencia return para finalizar la ejecución de una función y volver al punto desde el que fue llamada. Si no se desea que una función devuelva un valor, ésta terminará cuando se ejecute la instrucción return (sin expresión) o al llegar al final del cuerpo de la función. Si se desea que la función devuelva algún dato, deberá incluirse una expresión en la sentencia return. Si se indica, el tipo de la expresión retornada deberá coincidir con el tipo de la función. Si no se incluye una expresión, la función debe haber sido declarada sin tipo.
```
		function int SumaAlCuadrado (int a, int b)
		{
		  j= a + b;
		  return j * j;
		  /* La función finaliza y devuelve el valor entero de la expresión */
		}
		function pro (int x)
		{
		  x= SumaAlCuadrado (x - 1, x);
		   /* x contendrá el valor devuelto por la función: (x+x-1)^2 */
		  if (x > (194/2)) return; /* finaliza la ejecución si se ejecuta */
		  print (SumaAlCuadrado (x, x));
		} /* finaliza la ejecución si antes no se ejecutó el return */
```
 - **Sentencia Condicional simple**
Selecciona la ejecución de una sentencia, dependiendo del valor correspondiente de una condición de tipo lógico:
```	
	if (condición) sentencia
```
Si la condición lógica se evalúa como cierta se ejecuta la sentencia que puede ser cualquier sentencia simple del lenguaje, es decir, asignación, operación de entrada/salida, llamada a función o retorno (también break o sentencias de auto-incremento o auto-decremento para los grupos que tengan dichas opciones); en caso contrario, se finaliza su ejecución:
```
		if (a > b) c= b;
		if (fin) print("adiós");
```
 - **Sentencia Repetitiva while**
Esta sentencia permite repetir la ejecución de unas sentencias basándose en el resultado de una expresión lógica. La sintaxis es:
```
		while (condición) 
		{
		   sentencias
		}
```
Se evalúa la condición lógica y, si resulta ser cierta, se ejecutan las sentencias (que será un bloque de sentencias encerradas entre llaves). Este proceso se repite hasta que la condición sea falsa:
```
		while (n <= 10) 
		{
		    n= n + 1;
		    print (n);
		} /* mientras que n sea menor o igual que 10... */
```
- **Funciones**
Es necesario definir cada función antes de poder utilizarla. La definición de una función se realiza indicando la palabra function, el tipo de retorno (si la función devuelve algo), el nombre y, entre paréntesis, los argumentos (si existen) con sus tipos. Tras esta cabecera va un bloque (delimitado por llaves) con el cuerpo de la función:
```
	function [Tipo] nombre (lista de argumentos)
	{
	   sentencias
	}
```
La lista de argumentos (que puede estar vacía y, en este caso, se ponen los paréntesis vacíos) consta del tipo y del nombre de cada parámetro formal). Si hay más de un argumento, éstos se separan por comas. Los argumentos se pasan siempre por valor.

Las funciones pueden recibir como parámetros cualquiera de los tipos básicos del lenguaje (entero, lógico o cadena).

Las funciones pueden devolver un valor de uno de los tipos básicos del lenguaje (int, bool o string). El tipo de retorno de la función se determina según el tipo que aparezca en su declaración. Si se omite el tipo en la declaración, se entiende que la función no devolverá ningún valor. En caso de que las instrucciones return de una función tengan expresiones de un tipo distinto al declarado, será un error.

JavaScript-PL admite recursividad. Todos los grupos de trabajo han de considerarla en su implementación. Cualquier función puede ser recursiva, es decir, puede llamarse a sí misma.

El lenguaje JavaScript-PL no permite la definición de funciones anidadas. Esto implica que dentro de una función no se puede definir otra función.

Dentro de una función se tiene acceso a las variables locales, a sus argumentos y a las variables globales. Si en una función se declara una variable local o un argumento con el mismo nombre que una variable global, ésta última no es accesible desde dicha función.
```
	var int x  // global
	function int factorial (int x) 
	   /* se define la función recursiva con un parámetro, 
	      que oculta a la variable global de igual nombre */
	{
	  if (x > 1)
	  {
	    return x * factorial (x - 1);
	  }
	  else
	  {
	    return 1;
	  }
	}	// la función devuelve un entero
	
	function bool Suma (int aux, int fin)
	  /* se define la función Suma que recibe 
	     dos enteros por valor */
	  /* usa la variable global x */ 
	{
	    for (x= 1; x < fin; x= x + 2)
	    {
	      aux += factorial (aux-1);
	    }
	    return aux > 10000;
	}	// la función devuelve un lógico
	
	function Imprime (int a)
	{
	    print (a);
	    return;	// esta instrucción se podría omitir
	}	// la función no devuelve nada
	Imprime (factorial (Suma (5, 3)));	// se llama a las tres funciones
```
