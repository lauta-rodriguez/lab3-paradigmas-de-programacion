import java.util.ArrayList;
import java.util.List;
import feed.Article;
import feed.Feed;
import httpRequest.httpRequester;
import parser.GeneralParser;
import parser.RedditParser;
import parser.RssParser;
import parser.SubscriptionParser;
import subscription.SingleSubscription;
import subscription.Subscription;

public class FeedReaderMain {

    // articleList global para computar las entidades nombradas y unificar los feeds
    // en uno solo
    private static List<Article> globalArticleList = null;
    // lista de nombres de los sitios de los cuales se obtuvieron los feeds,
    // se le pasa como parametro al generador de feeds
    private static List<String> siteNames = new ArrayList<String>();

    private static void printHelp() {
        System.out.println("Please, call this program in correct way: FeedReader [-ne]");
    }

    private static String extractSiteName(String url) {
        String siteName = url.substring(url.indexOf("//") + 2, url.indexOf("/", url.indexOf("//") + 2));

        // extrae la parte de la url que contiene el nombre del sitio
        // es decir, de www.google.com.ar obtiene google
        siteName = siteName.substring(siteName.indexOf(".") + 1, siteName.lastIndexOf("."));

        if (!siteNames.contains(siteName)) {
            siteNames.add(siteName);
        }
        return siteName;
    }

    public static void main(String[] args) {

        if (args.length > 1 || (args.length == 1 && !args[0].equals("-ne"))) {
            printHelp();
            return;
        }

        System.out.println("************* FeedReader version 1.0 *************");
        httpRequester requester = new httpRequester();

        // codigo comun a las ejecuciones con y sin parametros

        // obtengo la lista de suscripciones
        Subscription subscription = new SubscriptionParser().parse("config/subscriptions.json");

        // se obtienen las single subscriptions para poder extraer informacion sobre
        // cada una de ellas
        List<SingleSubscription> singleSubscriptions = subscription.getSubscriptionsList();

        // se itera sobre cada una de las single subscriptions y se extrae
        // single.getUrlType()
        // single.getUrl()
        // luego se asigna el parser correspondiente segun el tipo de url

        for (int i = 0; i < singleSubscriptions.size(); i++) {

            // informacion para poder construir el parser
            SingleSubscription single = singleSubscriptions.get(i);
            String urlType = single.getUrlType();
            String url = single.getUrl();

            // parentesis para extraer el nombre del sitio de la url
            // y si aun no se agrego a la lista de nombres de sitios, se agrega
            String siteName = extractSiteName(url);
            if (!siteNames.contains(siteName)) {
                siteNames.add(siteName);
            }

            GeneralParser<List<Article>> feedParser = null;

            // se construye el parser correspondiente
            if (urlType.equals("rss")) {
                feedParser = new RssParser();
            } else if (urlType.equals("reddit")) {
                feedParser = new RedditParser();
            } else {
                System.out.println("Error: type of feed not supported");
                continue;
            }

            // se itera sobre cada uno de los parametros de la url
            // por cada parametro se obtiene el feed, es decir, la lista de articulos
            // la lista de articulos se agrega a la lista global
            for (int j = 0; j < single.getUlrParamsSize(); j++) {
                String urlParam = single.getUlrParams(j);
                String urlParamFeed = String.format(url, urlParam);
                String data = requester.getFeed(urlParamFeed, urlType);

                // se parsea la informacion obtenida, se obtiene una lista de articulos
                // inmediateamente se agrega a la lista global
                if (globalArticleList == null) {
                    globalArticleList = feedParser.parse(data);
                } else {
                    globalArticleList.addAll(feedParser.parse(data));
                }

            }
        }

        // una vez llegado a este punto, la lista global contiene todos los articulos de
        // todas las suscripciones
        if (globalArticleList.isEmpty()) {
            System.out.println("Error: no articles found");
            return;
        }

        // dependiendo de si se pasa el parametro -ne o no, se genera el feed o se
        // computan las entidades nombradas
        if (args.length == 0) {

            // genero un string con los nombres de los sitios separados por comas
            String siteNamesString = "";
            for (int i = 0; i < siteNames.size(); i++) {
                siteNamesString += siteNames.get(i);
                if (i != siteNames.size() - 1) {
                    siteNamesString += ",";
                }
            }

            // se genera el feed
            Feed feed = new Feed(siteNamesString);
            feed.setArticleList(globalArticleList);
            // se imprime los sitios de los cuales se obtuvieron los feeds
            System.out.println("Feed from: " + siteNamesString);
            // se imprime el feed
            feed.prettyPrint();
        } else {
            // empieza el codigo para computar las entidades nombradas
            ;
        }

    }
}