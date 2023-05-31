## Cómo correr el programa

Ejemplo de ejecución:

```java
************* FeedReader version 1.0 *************

Found 1299 occurrences of named entities

Total occurrences by category
	Other: 1116
	Organization: 57
	Place: 45
	Name: 30
	Lastname: 24
	Title: 10
	Country: 8
	City: 5
	CDate: 3
	Event: 1
	Address: 0
	Product: 0

Total occurrences by topic
	Other: 767
	Politics: 103
	National: 63
	International: 40
	Sports: 30
	Tennis: 4
	Futbol: 0
	Music: 0
	Basket: 0
	F1: 0
	Cine: 0

"OpenAI" occurs in:
	Category "Organization": 3 of 57 times
	Topic    "International": 3 of 40 times

"NBA" occurs in:
	Category "Other": 4 of 1116 times
	Topic    "Sports": 4 of 30 times

"President" occurs in:
	Category "Title": 2 of 10 times
	Topic    "National": 2 of 63 times
```

## ¿Cómo se instala Spark en una computadora personal?

En lugar de instalar Spark a nivel del sistema, opté por agregar las librerías de Spark al proyecto. De esta manera, las librerías pueden ser utilizadas en el código sin necesidad de configurar variables de entorno.

Asumiendo que JDK ya está instalado, seguir los siguientes pasos:

1. Descargar las librerías de Spark desde el sitio de [Apache Spark](<[https://spark.apache.org/downloads.html](https://spark.apache.org/downloads.html)>).
2. Descomprimir el directorio obtenido: `spark-3.4.0-bin-hadoop3` .
3. Extraer los archivos `.jar` del directorio `spark-3.4.0-bin-hadoop3/jars` y colocarlos en el directorio `lib/spark` del projecto.
4. Configurar el `build path` de la IDE para incluir los `.jar` en las dependencias del programa. Para lograr esto en Visual Studio Code incorporar la siguiente línea al archivo `setting.json`:

   ```json
   {
     "java.project.referencedLibraries": ["lib/*.jar"]
   }
   ```

## Estructura de un programa en Spark

En general, un programa en Spark se estructura como un conjunto de operaciones a realizar sobre Resilient Distributed Datasets (RDDs). Estos programas se dividen en conjuntos de tareas a ser realizadas por nodos en el clúster.

Un RDD (Resilient Distributed Dataset) es una estructura de datos en Spark que representa una colección inmutable y distribuida de objetos. Los RDDs son tolerantes a fallos y permiten realizar operaciones en paralelo.

A continuación, ejemplificamos la estructura de un programa en Spark con el siguiente código, que cuenta la cantidad de ocurrencias de cada palabra en un conjunto de documentos:

```java
package tests;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import scala.Tuple2;

import java.util.Arrays;
import java.util.List;

// from chatgpt
public class exampleSpark {
    public static void main(String[] args) {
        // Create a SparkContext
        JavaSparkContext sc = new JavaSparkContext("local[*]", "WordCountSparkApp");

        // Load the input text file
        JavaRDD<String> lines = sc.textFile("tests/input/*.txt");

        // Perform word count using MapReduce model
        JavaRDD<Tuple2<String, Integer>> wordCounts = lines
                .flatMap(line -> Arrays.asList(line.split(" ")).iterator())
                .mapToPair(word -> new Tuple2<>(word, 1))
                .reduceByKey((count1, count2) -> count1 + count2)
                .map(tuple -> new Tuple2<>(tuple._1(), tuple._2()));

        // collect the partial results from the workers
        List<Tuple2<String, Integer>> wordCountList = wordCounts.collect();

        // print the results
        for (Tuple2<String, Integer> wordCount : wordCountList) {
            System.out.println(wordCount._1() + ": " + wordCount._2());
        }

        // Stop the SparkContext
        sc.stop();
        // close the spark context
        sc.close();
    }
}
```

Podemos identificar las siguientes partes:

1. **Creación de un `SparkContext`** para inicializar el framework de Spark y establecer la comunicación con el cluster.

En este caso, se crea un `JavaSparkContext` con el argumento `"local[*]"` que indica que se utilizarán todos los procesadores disponibles en la máquina local. El segundo argumento define un nombre para el programa, que se mostrará en la salida estándar de Spark.

```java
// Create a SparkContext
        JavaSparkContext sc = new JavaSparkContext("local[*]", "WordCountSparkApp");
```

2. **Carga de datos** en RDDs para su procesamiento. Esto permite que los datos sean distribuidos y procesados en paralelo.

En este caso, se cargan todos los archivos de texto específicados en un RDD llamado `lines`.

```java
 // Load the input text file
        JavaRDD<String> lines = sc.textFile("tests/input/*.txt");
```

3. **Definición de transformaciones** sobre los RDDs. Las transformaciones son operaciones que se aplican a los RDDs para realizar cálculos o preparar los datos para el procesamiento.

En este caso se realizan varias transformaciones, como `flatMap`, `mapToPair` y `reduceByKey`, que en conjunto obtienen para cada palabra una tupla de la forma `(<word>,<word_frequency>)`.

```java
// Perform word count using MapReduce model
        JavaRDD<Tuple2<String, Integer>> wordCounts = lines
                .flatMap(line -> Arrays.asList(line.split(" ")).iterator())
                .mapToPair(word -> new Tuple2<>(word, 1))
                .reduceByKey((count1, count2) -> count1 + count2)
                .map(tuple -> new Tuple2<>(tuple._1(), tuple._2()));
```

4. **Definición de acciones**. Las acciones son operaciones que disparan la ejecución del programa de Spark y computan los resultados.

En este caso, se utiliza la acción `collect` para recolectar los resultados parciales de los trabajadores y se imprime el resultado final.

```java
        // collect the partial results from the workers
        List<Tuple2<String, Integer>> wordCountList = wordCounts.collect();
```

5. **Cierre y terminación del Sparkcontext** para terminar todos los trabajos y liberar los recursos utilizados.

```java
        // Stop the SparkContext
        sc.stop();
        // close the spark context
        sc.close();
```
