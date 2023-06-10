import sparkUtil.SparkUtil;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.JavaRDD;

import org.apache.spark.api.java.*;
import scala.Tuple2;
import java.util.Arrays;
import java.util.List;

public class SparkWordCount {

    public static void main(String[] args) {
        JavaSparkContext sc = SparkUtil.getSparkContext("Spark Word Count");

        try {

            // Lectura de datos
            JavaRDD<String> documents = sc.textFile("docs/*.txt");

            // Map: Dividir cada documento en palabras
            JavaRDD<String> words = documents.flatMap(line -> Arrays.asList(line.split(" ")).iterator());

            // Map: Asignar el valor 1 a cada palabra
            JavaPairRDD<String, Integer> wordCounts = words.mapToPair(word -> new Tuple2<>(word, 1));

            // Reduce: Sumar los valores de cada palabra
            JavaPairRDD<String, Integer> reducedCounts = wordCounts.reduceByKey((count1, count2) -> count1 + count2);

            // Acción y resultados
            List<Tuple2<String, Integer>> results = reducedCounts.collect();
            for (Tuple2<String, Integer> tuple : results) {
                System.out.println(tuple._1() + ": " + tuple._2());
            }
        }

        finally {
            sc.close();
        }

    }
}
