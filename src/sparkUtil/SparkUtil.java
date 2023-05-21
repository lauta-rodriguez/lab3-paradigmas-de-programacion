package sparkUtil;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;

public class SparkUtil {

    private static final SparkConf conf = new SparkConf().setAppName("FRSpark").setMaster("local[*]");
    private static final JavaSparkContext sc = new JavaSparkContext(conf);

    public static JavaSparkContext getSparkContext() {
        return sc;
    }
}
