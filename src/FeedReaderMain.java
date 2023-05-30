import java.util.ArrayList;
import java.util.List;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import httpRequest.httpRequester;
// import namedEntity.heuristic.StandfordHeuristic;
import feed.Article;
import feed.Feed;

import parser.GeneralParser;
import parser.RedditParser;
import parser.RssParser;
import parser.SubscriptionParser;
import subscription.SingleSubscription;
import subscription.Subscription;
import namedEntity.NamedEntity;
import namedEntity.heuristic.Heuristic;
import namedEntity.heuristic.QuickHeuristic;

public class FeedReaderMain {

	private static void printHelp() {
		System.out.println("Please, call this program in correct way: FeedReader [-ne]");
	}

	public static void main(String[] args) {

		if (args.length > 1 || (args.length == 1 && !args[0].equals("-ne"))) {
			printHelp();
			return;
		}

		System.out.println("************* FeedReader version 1.0 *************");
		GeneralParser<List<Article>> feedParser = null;

		SparkConf conf = new SparkConf().setAppName("FeedReader").setMaster("local");
		JavaSparkContext sc = new JavaSparkContext(conf);
		sc.setLogLevel("ERROR");

		/* Leer el archivo de suscription por defecto */
		Subscription subscription = new SubscriptionParser()
				.parse("config/subscriptions.json");

		List<Feed> fullFeedList = new ArrayList<Feed>();

		/* Llamar al httpRequester para obtener el feed del servidor */
		for (int i = 0; i < subscription.getLength(); i++) {
			SingleSubscription single = subscription.getSingleSubscription(i);
			String type = single.getUrlType();
			String rawUrl = single.getUrl();

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

				/* llamada al constructor de Feed */
				Feed feed = new Feed(url);
				feed.setParser(feedParser);
				fullFeedList.add(feed);
			}
		}

		JavaRDD<Feed> parallell = sc.parallelize(fullFeedList, fullFeedList.size());

		List<NamedEntity> collect = parallell
				.flatMap((Feed feed) -> {
					httpRequester requester = new httpRequester();
					GeneralParser<List<Article>> parser = feed.getParser();

					String data = requester.getFeed(feed.getSiteName(), parser.getParserType());
					List<Article> articleList = parser.parse(data);
					feed.setArticleList(articleList);
					// feed.prettyPrint();

					return articleList.iterator();
				})
				.flatMap((Article article) -> {
					Heuristic heuristic = new QuickHeuristic();
					article.computeNamedEntities(heuristic);
					return article.getNamedEntities().iterator();
				})
				.filter((NamedEntity namedEntity) -> !namedEntity.getCategory().equals("Other"))
				// .flatMap((String text) -> {
				// StandfordHeuristic sh = new StandfordHeuristic();
				// return sh.getEntities(text).iterator();
				// })
				// .filter((String text) -> !text.equals("O"));
				.collect();


		sc.close();
		sc.stop();

		List<NamedEntity> reducedList = new ArrayList<NamedEntity>();

		for (NamedEntity ne : collect) {
			NamedEntity reduced = null;
			for (NamedEntity ne2 : reducedList) {
				if (ne.getName().equals(ne2.getName())) {
					reduced = ne2;
					break;
				}
			}

			if (reduced == null) {
				reducedList.add(ne);
				continue;
			}

			// CROTO - VILLERO - HORRIBLE
			for (int i = 0; i < ne.getNEFrequency(); i++) {
				reduced.incrementNEFrequency();
			}
		}

		System.out.println("\n************* Named Entities *************\n");
		for (NamedEntity ne : reducedList) {
			ne.prettyPrint();
		}
	}

}
