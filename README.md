# Entregas individuales

Los trabajos individuales se encuentran en las branches con los nombres de cada uno de los integrantes del grupo:

- Bordon Gonzalo: branch `bordon-gonzalo`
- Kurtz Lara: branch `kurtz-lara`
- Rodríguez Lautaro: branch `rodriguez-lautaro`

# Cómo correr el projecto

## Requisitos:

- Java 11
  ```
  sudo apt install openjdk-11-jdk
  ```
- Apache Spark 3.4.0

## ¿Cómo instalar Spark?

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
4. Debemos agregar un archivo `settings.json` en la carpeta de nuestro proyecto que indique la ubicación de las liberí­as referenciadas en nuestro proyecto, en particular, Spark:
   ```json
   {
     "java.project.referencedLibraries": ["lib/*.jar"]
   }
   ```

## Comando para correr el projecto

- Compilación:

  ```shell
  javac -cp lib/jars/*:src -d bin -proc:none src/FeedReaderMain.java
  ```

- Ejecución:

  Si se desea imprimir los artículos, entonces se debe correr el proyecto sin ninguna opción.

  Si se corre con la opción `-s` entonces se puede consultar al índice invertido por documentos que contengan una cierta entidad nombrada:

  ```shell
  cd bin && java -cp .:../lib/jars/* FeedReaderMain -s
  ```

  Se obtendrá la siguiente sesión interactiva, de la cual se podrá salir escribiendo `exit`:

  ```
    Search for: meta

      Articles containing "meta"
      https://www.nytimes.com/2023/06/16/technology/meta-virtual-reality-headset-children-safety.html - 12

    Search for: exit
  ```

- Limpiar el directorio de binarios:

  ```shell
  cd .. && rm -r bin/
  ```

# Laboratorio 3: Entrega grupal

## Parte 1: Puesta en común de implementaciones individuales

A continuación se presentan las ventajas y desventajas de cada implementación individual:

### Lara Kurtz:

Ventajas: Es menos verboso al utilizar notación de punto en la estructura map-reduce.
Está muy bien documentado, lo cual facilita la lectura y posterior modificación del código.

Desventajas: primero evalúa si una palabra es una entidad nombrada y trabaja sobre esa lista. Una vez identificadas las entidades nombradas, las computa. Aunque nos parece una buena idea, para la segunda parte de este laboratorio nos sirve computar las entidades nombradas a medida que las vamos parseando, esto para más adelante enlazarlas con los artículos en los que aparecen.

### Gonzalo Bordón:

Ventajas: paraleliza la obtención de feeds y logra el mejor tiempo de ejecución.

Desventajas: el código se vuelve más complejo y difícil de leer si queremos extenderlo.

### Lautaro Rodríguez:

Ventajas: usa una lista global de artículos, lo cual facilita el procesamiento de los artículos en paralelo, así como elimina la necesidad de instanciar la clase Feed por cada suscripción.

Desventajas: la implementación es simple y no se enfoca en la eficiencia, sino en resolver el problema de forma clara y comprensible.

## ¿Con qué implementación nos quedamos?

Nos quedamos con la implementación de Lautaro, principalmente porque se adecua a las necesidades de la segunda parte del laboratorio sin necesidad de modificar mucho el código.

- Utiliza un feed global al cual se le asigna una lista compuesta por todos los artículos parseados de todos los feeds (del archivo de suscripciones).
- Computa las entidades nombradas en la estructura map-reduce.

## Parte 2: Índice invertido

### Objetivo

Implementamos una funcionalidad que permite recuperar artículos por palabra clave. Para lograr esto, utilizamos un índice invertido. Un índice invertido es una forma de estructurar la información que va a ser recuperada en la búsqueda. En este caso, el índice invertido es un diccionario que tiene como clave las **entidades nombradas** que identificamos y clasificamos en la primera parte del laboratorio, y como valor una lista de los documentos en los que aparece esa entidad nombrada, ordenados de mayor a menor frecuencia.

### ¿Cómo se adaptó la implementación elegida para recuperar documentos por palabra clave?

1. Modificamos la clase **NamedEntity** para que tenga un atributo `articleLink`, que es el enlace al artículo del cual se extrajo la entidad nombrada. Esta información nos sirve para poder contar la frecuencia de aparición de la entidad nombrada en cierto artículo.

2. Implementamos los métodos setter y getter para el atributo `articleLink`.

3. Modificamos la clase **Article**, ya que en ella se computan las entidades nombradas que aparecen en el artículo. Ahora, también asociamos a cada entidad nombrada el enlace al artículo del cual fue extraída mediante el setter de la clase **NamedEntity**.

4. Actualizamos la estructura map-reduce para obtener un RDD de tuplas (articleLink, (namedEntity, frequency)).

5. A partir del RDD obtenido en el paso anterior, creamos un diccionario `Index` que contiene la información necesaria para recuperar los artículos por palabra clave.

A continuación se muestra la salida de la ejecución del programa y la búsqueda de la palabra clave "Sam" en el índice invertido:

La salida indica que se ha encontrado la palabra clave "Sam" en 1 artículo, y que ha aparecido 8 veces en el mismo.

```shell
Articles containing "Sam"
https://www.nytimes.com/2023/06/07/technology/sam-altman-ai-regulations.html - 8
```
