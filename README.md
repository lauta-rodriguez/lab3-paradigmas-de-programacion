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

# Estructura de un programa de conteo de palabras en diferentes documentos en Spark

# ¿Cómo adaptar el código del Laboratorio 2 a la estructura del programa objetivo en Spark?

# ¿Cómo se integra una estructura orientada a objetos con la estructura funcional de map-reduce?
