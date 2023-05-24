package sparkUtil;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;

public class SparkUtil {

    public static JavaSparkContext getSparkContext(String AppName) {
        SparkConf conf = new SparkConf().setAppName(AppName).setMaster("local[*]");
        JavaSparkContext sc = new JavaSparkContext(conf);
        return sc;
    }
}
