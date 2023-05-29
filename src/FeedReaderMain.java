import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

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
import scala.Tuple2;
import subscription.SingleSubscription;
import subscription.Subscription;

public class FeedReaderMain {

	private static List<Feed> allFeeds = new ArrayList<Feed>();
	private static List<Article> allArticles = new ArrayList<Article>();
	private static List<NamedEntity> namedEntities = new ArrayList<NamedEntity>();

	private static void printHelp() {
		System.out.println("Please, call this program in correct way: FeedReader [-ne]");
	}

	private static boolean isWord(String str) {
		return str.matches("[a-zA-Z0-9]+") && !str.matches("[0-9]+");
	}

	private static String cleanString(String str) {
		return str.replaceAll("[.,;:()'\"!?&*‘'“\n\\s]", "");
	}

	public static void main(String[] args) {

		if (args.length > 1 || (args.length == 1 && !args[0].equals("-ne"))) {
			printHelp();
			return;
		}

		System.out.println("************* FeedReader version 1.0 *************");
		httpRequester requester = new httpRequester();

		/* Leer el archivo de suscription por defecto */
		Subscription subscription = new SubscriptionParser()
				.parse("config/subscriptions.json");

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
			}

			for (int j = 0; j < single.getUlrParamsSize(); j++) {
				String url = rawUrl.replace("%s", single.getUlrParams(j));
				String data = requester.getFeed(url, type);

				List<Article> articleList = feedParser.parse(data);

				/* llamada al constructor de Feed */
				Feed feed = new Feed(url);
				feed.setArticleList(articleList);

				// add this feed to the list of feeds
				allFeeds.add(feed);

				// add all articles to the list of articles
				allArticles.addAll(articleList);

			}
		}

		/* Si se llama al programa sin argumentos, se genera el Feed */
		if (args.length == 0) {

			for (Feed feed : allFeeds) {
				feed.prettyPrint();
			}

		} else { // args.length == 1 && args[0].equals("-ne")
			Heuristic heuristic = new QuickHeuristic();

			// Create a SparkContext
			JavaSparkContext sc = new JavaSparkContext("local[*]", "ArticleProcessing");

			// Load the articles into RDDs
			JavaRDD<Article> articles = sc.parallelize(allArticles);

			JavaRDD<Tuple2<String, Integer>> words = articles
					.flatMap(article -> Arrays.asList(article.getContent().split("\\s+")).iterator())
					.map(str -> cleanString(str))
					.filter(str -> isWord(str))
					.filter(word -> heuristic.isEntity(word))
					.mapToPair(word -> new Tuple2<>(word, 1))
					.reduceByKey((count1, count2) -> count1 + count2)
					.map(tuple -> new Tuple2<>(tuple._1(), tuple._2()));

			// collect all partial results
			List<Tuple2<String, Integer>> result = words.collect();

			// close spark context
			sc.close();

			// instantiate named entities
			for (Tuple2<String, Integer> tuple : result) {
				try {
					NamedEntity ne = NamedEntity.generateNamedEntity(tuple._1(), tuple._2());

					namedEntities.add(ne);

				} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
						| InvocationTargetException | NoSuchMethodException | SecurityException
						| ClassNotFoundException e) {
					e.printStackTrace();
				}

			}
		}
	}

}
