import java.util.List;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.SparkSession;

import feed.Article;
import feed.Feed;
import httpRequest.httpRequester;
import namedEntity.NamedEntity;
import namedEntity.heuristic.Heuristic;
import namedEntity.heuristic.QuickHeuristic;
import parser.GeneralParser;
import parser.RedditParser;
import parser.RssParser;
import parser.SubscriptionParser;
import subscription.SingleSubscription;
import subscription.Subscription;

public class SparkFeedReaderMain {

    private static void printHelp() {
        System.out.println("Please, call this program in correct way: FeedReader [-ne]");
    }

    public static void main(String[] args) {

        args = new String[] { "-ne" };

        if (args.length > 1 || (args.length == 1 && !args[0].equals("-ne"))) {
            printHelp();
            return;
        }

        System.out.println("************* FeedReader version 1.0 *************");
        httpRequester requester = new httpRequester();

        // Initialize SparkSession
        SparkSession spark = SparkSession.builder()
                .appName("FeedReader")
                .master("local[*]")
                .getOrCreate();

        // Create JavaSparkContext from SparkSession
        JavaSparkContext jsc = new JavaSparkContext(spark.sparkContext());

        /* Leer el archivo de suscription por defecto */
        Subscription subscription = new SubscriptionParser()
                .parse("config/subscriptions.json");

        /* Si se llama al programa sin argumentos, se genera el Feed */
        if (args.length == 0) {

            /* Llamar al httpRequester para obtener el feed del servidor */
            for (int i = 0; i < subscription.getLength(); i++) {
                SingleSubscription single = subscription.getSingleSubscription(i);
                String type = single.getUrlType();
                String rawUrl = single.getUrl();
                GeneralParser<List<Article>> feedParser = null;

                /*
                 * llamada al Parser especifico para extrar los datos necesarios por la
                 * aplicacion
                 */
                if (type.equals("rss")) {
                    feedParser = new RssParser();
                } else if (type.equals("reddit")) {
                    feedParser = new RedditParser();
                } else {
                    System.out.println("Error: type of feed not supported");
                    continue;
                }

                for (int j = 0; j < single.getUlrParamsSize(); j++) {
                    String url = rawUrl.replace("%s", single.getUlrParams(j));
                    String data = requester.getFeed(url, type);

                    List<Article> articleList = feedParser.parse(data);

                    /* llamada al constructor de Feed */
                    Feed feed = new Feed(url);
                    feed.setArticleList(articleList);

                    /*
                     * llamada al prettyPrint del Feed para ver los articulos del feed en forma
                     * legible y amigable para el usuario
                     */
                    feed.prettyPrint();
                }
            }

        }
        /*
         * Si se llama al programa con el argumento -ne
         * se genera el Feed y se computan las entidades nombradas
         */
        else { // args.length == 1
            /* Llamar al httpRequester para obtener el feed del servidor */
            for (int i = 0; i < subscription.getLength(); i++) {
                SingleSubscription single = subscription.getSingleSubscription(i);
                String type = single.getUrlType();
                String rawUrl = single.getUrl();

                GeneralParser<List<Article>> feedParser = null;
                Heuristic heuristic = new QuickHeuristic();

                /*
                 * llamada al Parser especifico para extrar los datos necesarios por la
                 * aplicacion
                 */
                if (type.equals("rss")) {
                    feedParser = new RssParser();
                } else if (type.equals("reddit")) {
                    feedParser = new RedditParser();
                } else {
                    System.out.println("Error: type of feed not supported");
                    continue;
                }

                for (int j = 0; j < single.getUlrParamsSize(); j++) {
                    String url = rawUrl.replace("%s", single.getUlrParams(j));
                    String data = requester.getFeed(url, type);

                    List<Article> articleList = feedParser.parse(data);

                    // Convert the list of articles into an RDD
                    JavaRDD<Article> articleRDD = jsc.parallelize(articleList, articleList.size());

                    // Apply a transformation to each article in the RDD to compute its named
                    // entities
                    JavaRDD<Article> namedEntityRDD = articleRDD.map(article -> {
                        article.computeNamedEntities(heuristic);
                        return article;
                    });

                    // Print the named entities for each article
                    namedEntityRDD.foreach(article -> {
                        System.out.println("Article: " + article.getTitle());
                        System.out.println("Named entities: ");
                        for (NamedEntity ne : article.getNamedEntityList()) {
                            System.out
                                    .println("\t\t" + ne.getName() + " - " + ne.getCategory() + " - "
                                            + ne.getTopic().getName());
                        }
                    });

                }
            }
        }

        // Stop the SparkSession
        spark.stop();
    }
}
