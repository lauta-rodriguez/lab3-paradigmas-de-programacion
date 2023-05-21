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

# ¿Cómo adaptar el código del Laboratorio 2 a la estructura del programa objetivo en Spark?

# ¿Cómo se integra una estructura orientada a objetos con la estructura funcional de map-reduce?
