import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;
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
import namedEntity.Counter;
import namedEntity.Generator;
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
					feed.prettyPrint();

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

		Dictionary<String, NamedEntity> singleEntities = new Hashtable<>();
		Dictionary<String, Dictionary<String, List<NamedEntity>>> compoundEntities = new Hashtable<>();

		for (NamedEntity ne : collect) {
			String key = ne.getCategory() + " - " + ne.getTopic().getCategory();
			NamedEntity reduced = singleEntities.get(ne.getName());

			if (reduced == null) {
				singleEntities.put(ne.getName(), ne);
				Counter.increment(key, ne.getNEFrequency());
				Dictionary<String, List<NamedEntity>> sEntities = compoundEntities.get(ne.getCategory());
				
				if (sEntities == null) {
					Dictionary<String, List<NamedEntity>> newDict = new Hashtable<>();
					List<NamedEntity> newList = new ArrayList<>();
					newList.add(ne);
					newDict.put(ne.getTopic().getCategory(), newList);
					compoundEntities.put(ne.getCategory(), newDict);
				} else {
					List<NamedEntity> newList = sEntities.get(ne.getTopic().getCategory());

					if (newList == null) {
						newList = new ArrayList<>();
						sEntities.put(ne.getTopic().getCategory(), newList);
					}

					newList.add(ne);
				}

				continue;
			}

			Counter.increment(key, ne.getNEFrequency());
			// CROTO - VILLERO - HORRIBLE
			for (int i = 0; i < ne.getNEFrequency(); i++) {
				reduced.incrementNEFrequency();
			}
		}

		System.out.println("\n************* Named Entities *************\n");

		System.out.println("Named Entity: " + NamedEntity.getFrequency());

		Enumeration<String> k = compoundEntities.keys();
		while (k.hasMoreElements()) {
			String key = k.nextElement();
			Dictionary<String, List<NamedEntity>> nestedDict = compoundEntities.get(key);

			Enumeration<String> k2 = nestedDict.keys();
			while (k2.hasMoreElements()) {
				String key2 = k2.nextElement();
				List<NamedEntity> list = nestedDict.get(key2);

				int count = 0;
				try {
					count = Integer.parseInt(Generator.getNamedEntity(key).getDeclaredMethod("getFrequency").invoke(null).toString());
				} catch (Exception e) {
				}
	
				System.out.println("\t" + key + ": " + count);
				System.out.println("\t\t" + key2 + ": " + Counter.get(key + " - " + key2));

				list.forEach((NamedEntity ne) -> {
					System.out.println("\t\t\t" + ne.getName() + ": " + ne.getNEFrequency());
				});
			}
		}
	}

}
