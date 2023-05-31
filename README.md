# Reimplementación del Laboratorio 2 usando Spark

### Autor:

- Lautaro Augusto Rodríguez, lautaro.rodriguez@mi.unc.edu.ar

# Objetivo:

El objetivo de este proyecto es reimplementar el código de lectura de feeds y conteo de entidades nombradas del Laboratorio 2 usando Spark.

# Requisitos:

- Java 11
- Apache Spark 3.4.0

# ¿Cómo instalar Spark?

En los siguientes pasos se detalla cómo instalar Spark 3.4.0 e integrarlo con Java 11:

1. Descargar Spark 3.4.0

```shell
wget https://dlcdn.apache.org/spark/spark-3.4.0/spark-3.4.0-bin-hadoop3.tgz
```

2. Descomprimir Spark 3.4.0

```shell
tar -xvzf spark-3.4.0-bin-hadoop3.tgz
```

3. Entrar al directorio y mover el directorio `jars` a la carpeta `lib` de nuestro proyecto

```shell
cd spark-3.4.0-bin-hadoop3
mv jars {PATH_A_NUESTRO_PROYECTO}/lib
```

4. Debemos agregar un archivo `settings.json` en la carpeta de nuestro proyecto que indique la ubicación de las liberías referenciadas en nuestro proyecto, en particular, Spark:

```json
{
  "java.project.referencedLibraries": ["lib/*.jar"]
}
```

# Estructura de un programa en Spark

En este contexto, un programa en Spark típicamente tiene la siguiente estructura:

1. Imports.

   - El programa importa las librerías necesarias, tanto de Spark como de otras librerías que sean necesarias.

     En nuestro caso, importamos las librerías requeridas para seguir con el paso 2.

   ```java
   import org.apache.spark.SparkConf;
   import org.apache.spark.api.java.JavaSparkContext;
   ```

2. Configuración.

   - Se crea una instancia de `SparkConf` para configurar la aplicación Spark. En particular, se puede configurar el nombre de la aplicación, la cantidad de recursos que se le asignan a la misma, etc.

   - Establecemos el nombre de la aplicación y a continuación, el modo de ejecución. En este caso, se ejecuta en modo local en la máquina actual utilizando todos los núcleos disponibles.

   ```java
   SparkConf conf = new SparkConf().setAppName("MiAplicación").setMaster("local[*]");
   ```

   - Creamos una instancia de `JavaSparkContext` a partir de la configuración anterior. Esta instancia es la que se utiliza para interactuar con Spark, es responsable de la comunicación con el cluster de Spark y de coordinar la ejecución de las tareas.

   ```java
   JavaSparkContext sc = new JavaSparkContext(conf);

   ```

3. Carga de datos.

   - Se cargan los datos en Spark. Utilizamos RDDs para representar los datos, un RDD (Resilient Distributed Dataset) es la abstracción principal de datos en Apache Spark. Es una colección inmutable y distribuida de objetos que se pueden procesar en paralelo en un clúster.

4. Procesamiento.

   - Spark proporciona una amplia gama de operaciones de  
     **transformación** y **acción** para manipular y procesar los RDDs.

     Las **transformaciones** son operaciones que crean un nuevo RDD a partir de uno existente, como map, filter, reduceByKey, etc.

     Las **acciones** son operaciones que realizan cálculos y devuelven un resultado, como count, collect, saveAsTextFile, etc.

     Estas operaciones se pueden encadenar en secuencias para construir el flujo de procesamiento deseado.

   - Una vez definidas las transformaciones y acciones, se llama a una acción final que desencadena la ejecución del programa.

5. Salida.

   - Se llama al método `close` de la instancia de `JavaSparkContext` para cerrar la conexión con Spark y liberar los recursos asociados.

# Estructura de un programa de conteo de palabras en diferentes documentos en Spark

Utilizando los pasos anteriores, podemos definir la siguiente estructura para un programa de conteo de palabras en diferentes documentos
utilizando el modelo map-reduce en Spark:

1. Imports.

   ```java
   import sparkUtil.SparkUtil;
   import org.apache.spark.api.java.JavaSparkContext;
   import org.apache.spark.api.java.JavaRDD;
   import org.apache.spark.api.java.*;
   import scala.Tuple2;
   import java.util.Arrays;
   import java.util.List;
   ```

2. Configuración.

   ```java
   JavaSparkContext sc = SparkUtil.getSparkContext();
   ```

3. Carga de datos.

   - Se cargan los datos desde uno o más archivos de texto. En este caso, los que se encuentran en la carpeta `docs`.

     ```java
     JavaRDD<String> documents = sc.textFile("docs/*.txt");
     ```

4. Procesamiento.

   - Se cargan los datos desde un conjunto de documentos y se almacenan en un RDD de tipo `String`, en donde cada elemento del RDD es una línea de un documento.

   - Se aplica la transformacion `flatMap` y se divide cada línea en palabras, estas se almacenan en un RDD de tipo `String`, en donde cada elemento del RDD es una palabra.

   - A continuación, se hace uso del modelo map-reduce:

     - Mediante la transformación `mapToPair`, se transforma cada palabra en un par clave-valor y se almacenan en un RDD de tipo `Par` en donde cada elemento del RDD es un par **(palabra, 1)**.

     - Luego, se aplica la transformación `reduceByKey` para sumar los valores de las palabras que son iguales (es decir, se suman los valores de las palabras repetidas como si tuvieramos un contador). El resultado es un RDD de tipo `Par` en donde cada elemento del RDD es un par **(palabra, cantidad_de_veces_que_aparece)**.

   ```java
   // Lectura de datos
   JavaRDD<String> documents = sc.textFile("docs/*.txt");


   // Map: Dividir cada documento en palabras
   JavaRDD<String> words = documents.flatMap(line -> Arrays.asList(line.split(" ")).iterator());

   // Map: Asignar el valor 1 a cada palabra
   JavaPairRDD<String, Integer> wordCounts = words.mapToPair(word -> new Tuple2<>(word, 1));

   // Reduce: Sumar los valores de cada palabra
   JavaPairRDD<String, Integer> reducedCounts = wordCounts.reduceByKey((count1, count2) -> count1 + count2);
   ```

5. Salida.

   - Finalmente, se llama a la acción `collect` para recopilar todos los elementos del RDD en una lista **results** y se imprimen los resultados.

   ```java
   // Acción y resultados
   List<Tuple2<String, Integer>> results = reducedCounts.collect();
   for (Tuple2<String, Integer> tuple : results) {
       System.out.println(tuple._1() + ": " + tuple._2());
   }

   sc.close();
   ```

# ¿Cómo adaptar el código del Laboratorio 2 a la estructura del programa objetivo en Spark?

Adaptar el código del Laboratorio 2 a la estructura del programa objetivo en Spark implica:

- Hacer serializables las clases que usan los objetos de spark:

  - Article
  - NamedEntity

    ¿Por qué? ¿Qué significa que una clase sea serializable?

    Una clase es serializable si implementa la interfaz `java.io.Serializable`. Esto significa que los objetos de esa clase pueden ser convertidos en una secuencia de bytes, que luego pueden ser guardados en un archivo, enviados a través de una red, etc. y luego reconstruidos en un objeto idéntico al original.

    En el contexto de Spark, esto es necesario porque los objetos de las clases `Article` y `NamedEntity` se envían a través de la red a los nodos del cluster para ser procesados. Por lo tanto, deben ser serializables para que puedan ser enviados a través de la red.

- Establecer **qué** se va a paralelizar con Spark:

  - El procesamiento de los artículos
  - La computación de las entidades nombradas
  - El conteo de las entidades nombradas

- Decidir **cómo** se va a distribuir el procesamiento de los artículos:

  - Para distribuir el procesamiento de los artículos en Spark podemos seguir el modelo map-reduce presentado en el programa de conteo de palabras con ciertas modificaciones:

    1. Se crea una instancia de la clase `QuickHeuristic`, que representa una heurística para el procesamiento de entidades nombradas.

    2. Se crea el contexto de `Spark` utilizando la configuración `SparkConf`. En este caso, se establece el nombre de la aplicación como "NER" y se utiliza el modo de ejecución local con setMaster("local[*]").

    3. Se paraleliza el procesamiento de los artículos mediante la función parallelize de JavaSparkContext. Los artículos se cargan en un JavaRDD<Article> articleRDD, que es un Resilient Distributed Dataset (RDD) de objetos de la clase Article.

    4. Comienza la sección donde se aplica el modelo **map-reduce** en el RDD articleRDD.

       a. `Map`: Se aplica la función map al RDD articleRDD. En cada artículo, se computan las entidades nombradas utilizando la heurística definida. Luego, se devuelve la lista de entidades nombradas del artículo.

       b. `FlatMap`: Se aplica la función flatMap al RDD namedEntityRDD. Esto transforma el RDD de listas de entidades nombradas en un RDD de entidades nombradas individuales. Las entidades nombradas pueden repetirse en este RDD resultante.

       c. `MapToPair`: Se aplica la función mapToPair al RDD flatNamedEntityRDD. Cada entidad nombrada se mapea a una tupla en formato clave-valor, donde la clave es el nombre de la entidad y el valor es 1. Esto se hace para preparar el RDD para el paso de reducción posterior.

       d. `ReduceByKey`: Se aplica la función reduceByKey al RDD namedEntityPairRDD. Se realiza una reducción por clave, es decir, se suman los valores asociados a cada clave (nombre de la entidad nombrada). Esto resulta en un RDD que contiene la frecuencia global de cada entidad nombrada.

    5. Se finaliza la sección de paralelización.

       a. `Collect`: Se utiliza la función collect en el RDD namedEntityFrequencyRDD para recolectar los resultados en el programa principal. Los resultados se almacenan en una lista de tuplas clave-valor, donde la clave es el nombre de la entidad nombrada y el valor es su frecuencia.

    6. Se imprime en la consola las entidades nombradas y sus frecuencias utilizando un bucle for sobre la lista namedEntityFrequencyList.

    7. Se cierra el contexto de Spark utilizando jsc.close().

# Integración de Inteligencias Artificiales a la producción y documentación de software

Para este proyecto, utilicé principalmente ChatGPT, una IA basada en el modelo GPT-3.5, para recibir asistencia en la generación de este README y comprender la consigna. Trabajar con ChatGPT fue un proceso iterativo en el que tuve que probar diferentes configuraciones y alimentar a la IA con módulos completos del Laboratorio 2 para que pudiera comprender mejor la consigna. A continuación, se muestra un ejemplo de una interacción con ChatGPT:

![chatgpt-1](/imagenes/chatgpt-1.png)

![chatgpt-2](/imagenes/chatgpt-2.png)
![chatgpt-3](/imagenes/chatgpt-3.png)
![chatgpt-4](/imagenes/chatgpt-4.png)

Sin embargo, también es importante tener en cuenta que la IA no reemplaza la comprensión humana. Aunque ChatGPT proporcionó información útil, fue necesario revisar y ajustar el contenido generado para garantizar su precisión y coherencia.

En cierto punto, ChatGPT dejó de ser útil debido a la complejidad de la consigna, pero me marcó el camino a seguir. Para continuar con el proyecto, utilicé Bard, una IA que pertenece a Google y que, hasta la fecha, solo tiene soporte en inglés. Al tener acceso a internet, Bard fue capaz de generar **código** más útil que ChatGPT, proporcionándome enlaces a los repositorios de GitHub de los proyectos que utilizó para generar el código. A continuación, se muestra un ejemplo de una interacción con Bard:

![bard-1](/imagenes/bard-1.png)
![bard-2](/imagenes/bard-2.png)
![bard-3](/imagenes/bard-3.png)

Las ventajas de utilizar más de una IA son poder alimentar a una con la salida de la otra y poder salir de un estado "bloqueado" que eventualmente se alcanza al trabajar con una sola IA. Sin embargo, las desventajas son que Bard no guarda los chats para revisarlos en el futuro, por lo que es necesario guardarlos manualmente, y que no tiene soporte en español. Además, el modelo gratuito ofrecido por ChatGPT es bastante limitado y no siempre agiliza el proceso de producción de software.

Con respecto a la utilización de Copilot, una IA que pertenece a GitHub y que tiene soporte en español, cuyo fin es generar código, hago uso de la misma para cuestiones que no requieren elaborar un prompt muy complejo. Por ejemplo, la utilizo para traducir pseudocódigo a código en Java, o para resolver implementaciones de algoritmos que no requieren de una estructura de datos compleja.

El caso de uso más complejo que tuve con Copilot fue el de presentar de manera ordenada las frecuencias de las categorías y topicos de las entidades nombradas. Para ello, escribo comentarios detallados en el código para que Copilot pueda comprender el objetivo de la función como se puede ver en el siguiente snippet:

(![copilot-usage](/imagenes/copilot.png))

# ¿Cómo se integra una estructura orientada a objetos con la estructura funcional de map-reduce?

En la programación funcional, las funciones son puras, los datos son inmutables y se controlan los efectos secundarios. Además, se utiliza la composición de funciones y la recursividad para expresar algoritmos de manera concisa. Por lo tanto, es un paradigma muy adecuado para el procesamiento de datos de manera concurrente.

En la programación orientada a objetos, los datos y el comportamiento se encapsulan en objetos. Los objetos se comunican entre sí mediante el envío de mensajes, lo que permite la reutilización de código y la abstracción de los detalles de implementación. Estas características son muy útiles para el desarrollo de aplicaciones complejas.

Una forma de integrar los dos paradigmas es utilizando una biblioteca que proporcione un puente entre la programación funcional y la programación orientada a objetos. Una de esas bibliotecas es Spark, que se basa en el modelo de programación MapReduce para procesar grandes volúmenes de datos distribuidos.

Este modelo divide los datos en fragmentos más pequeños y los procesa en paralelo en varios nodos de un clúster. MapReduce se combina con los conceptos de la programación orientada a objetos y funcional en Spark, ya que las operaciones de map y reduce son fundamentales en el procesamiento de los RDD (Resilient Distributed Datasets).

En un programa en Spark, la **programación orientada a objetos** se utiliza para definir las **estructuras de datos y los objetos** y los objetos, mientras que la **programación funcional** se utiliza para **describir las transformaciones y las operaciones** que se aplican a los datos distribuidos.

Al combinar los dos paradigmas, se pueden aprovechar las ventajas de ambos. Mediante la programación funcional expresar algoritmos de manera concisa y procesar datos de manera eficiente, y mediante la programación orientada a objetos reutilizar código y abstraer los detalles de implementación.
