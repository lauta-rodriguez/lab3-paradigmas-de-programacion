import java.util.List;

import httpRequest.httpRequester;

import feed.Article;
import feed.Feed;
import feed.GlobalFeed;

import parser.GeneralParser;
import parser.RedditParser;
import parser.RssParser;
import parser.SubscriptionParser;

import subscription.SingleSubscription;
import subscription.Subscription;

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
		httpRequester requester = new httpRequester();

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
			GlobalFeed global = new GlobalFeed();
			Heuristic heuristic = new QuickHeuristic();

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

					global.appendArticleList(articleList);

					/* llamada al constructor de Feed */
					Feed feed = new Feed(url);
					feed.setArticleList(articleList);

					/*
					 * Llamar a la heuristica para que compute las entidades nombradas de cada
					 * articulos del feed
					 */
					for (int k = 0; k < articleList.size(); k++) {
						try {
							/* Imprime las entidades nombradas*/
							System.out
								.println("**********************************************************************************************");
							System.out.println("Article: " + articleList.get(k).getTitle());
							articleList.get(k).computeNamedEntities(heuristic);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					continue;
				}
			}
		}
	}

}
