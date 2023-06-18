import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import feed.Article;
import feed.Feed;
import httpRequest.httpRequester;
import parser.GeneralParser;
import parser.RedditParser;
import parser.RssParser;
import parser.SubscriptionParser;
import scala.Tuple2;
import subscription.SingleSubscription;
import subscription.Subscription;
import word.Word;

public class FeedReaderMain {

    // articleList global para computar las entidades nombradas y unificar los feeds
    // en uno solo
    private static List<Article> globalArticleList = null;
    // lista de nombres de los sitios de los cuales se obtuvieron los feeds,
    // se le pasa como parametro al generador de feeds
    private static List<String> siteNames = new ArrayList<String>();

    // diccionario que mapea cada word a una lista de la frecuencia con
    // la que aparece en cada articulo
    private static final Map<String, List<Tuple2<Integer, String>>> INDEX = new HashMap<>();

    // método que agrega elementos al diccionario:
    // esto lo único que hace es un put de la key, que es la word, y la
    // lista
    private static void addToIndex(String word, Tuple2<Integer, String> tuple) {
        // agregar la tupla a la lista correspondiente a la key
        if (INDEX.containsKey(word)) {
            INDEX.get(word).add(tuple);
        } else {
            List<Tuple2<Integer, String>> newList = new ArrayList<>();
            newList.add(tuple);
            INDEX.put(word, newList);
        }
    }

    private static void printHelp() {
        System.out.println("Please, call this program in correct way: FeedReader [-s | -word]");
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

        args = new String[] { "-s" };
        // los argumentos que se le pueden pasar al programa son:
        // 0 argumentos: generar feed
        // 1 argumento: generar indice invertido y buscar argumento en el indice
        if (args.length > 1 || ((args.length == 1) && !args[0].equals("-s"))) {
            System.out.println("Error: bad arguments");
            printHelp();
            return;
        }

        System.out.println("************* FeedReader version 1.0 *************");
        httpRequester requester = new httpRequester();

        // codigo comun a las ejecuciones con y sin parametros

        // obtengo la lista de suscripciones
        Subscription subscription = new SubscriptionParser().parse("./config/subscriptions.json");

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
            for (int j = 0; j < single.getUrlParamsSize(); j++) {
                String urlParam = single.getUrlParams(j);
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

        // dependiendo de si se pasa el parametro -word o no, se genera el feed o se
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
        } else { // program was called with the -s flag

            // se crea el contexto de spark
            SparkConf conf = new SparkConf().setAppName("NER").setMaster("local[*]");
            JavaSparkContext jsc = new JavaSparkContext(conf);

            // se paraleliza el procesamiento de los articulos y se los carga en un
            // "JavaRDD<Article> articleRDD"
            JavaRDD<Article> articleRDD = jsc.parallelize(globalArticleList, globalArticleList.size());

            // --- begin: paralelizacion hasta reduce || collect ---

            // # map
            // a cada articulo de articleRDD se le computa las word y por cada articulo
            // vamos
            // a tener una List<Word>, resultando en un
            JavaRDD<List<Word>> wordRDD = articleRDD.map(article -> {
                article.computeSingleWords();
                return article.getWordList();
            });

            // # flatMap
            // obtengo un RDD con todas las words individuales, por lo que van a
            // haber word repetidas
            JavaRDD<Word> flatWordRDD = wordRDD
                    .flatMap(wordList -> wordList.iterator());

            // # mapToPair
            // se mapea cada word a una tupla <articleLink, <word, 1>>
            JavaPairRDD<String, Tuple2<Word, Integer>> articleLinkWordRDD = flatWordRDD
                    .mapToPair(word -> new Tuple2<String, Tuple2<Word, Integer>>(
                            word.getArticleLink(),
                            new Tuple2<Word, Integer>(word, 1)));

            // # groupByKey
            // agrupo por clave, es decir, por articleLink, y se obtiene un RDD con
            // <articleLink, <word, 1>>
            JavaPairRDD<String, Iterable<Tuple2<Word, Integer>>> articleLinkWordGroupedRDD = articleLinkWordRDD
                    .groupByKey();

            // # mapValues
            // mapea cada grupo a una tupla que contiene el "articleLink" y un map de word
            // counts
            JavaPairRDD<String, Map<String, Integer>> wordCountRDD = articleLinkWordGroupedRDD
                    .mapValues(wordList -> {
                        Map<String, Integer> wordCountMap = new HashMap<>();
                        wordList.forEach(w -> {
                            String word = w._1.getWord();
                            if (wordCountMap.containsKey(word)) {
                                wordCountMap.put(word, wordCountMap.get(word) + 1);
                            } else {
                                wordCountMap.put(word, 1);
                            }
                        });
                        return wordCountMap;
                    });

            // # collect
            List<Tuple2<String, Map<String, Integer>>> wordCountList = wordCountRDD.collect();

            // --- end: paralelizacion hasta reduce || collect ---

            // filtra entries duplicadas
            List<Tuple2<String, Map<String, Integer>>> filteredWordCountList = wordCountList.stream().filter(tuple -> {
                return tuple._2.size() > 1;
            }).collect(ArrayList::new, ArrayList::add, ArrayList::addAll);

            // agrego los elementos al indice invertido
            for (Tuple2<String, Map<String, Integer>> tuple : filteredWordCountList) {
                String articleLink = tuple._1;
                Map<String, Integer> wordCountMap = tuple._2;

                for (Map.Entry<String, Integer> entry : wordCountMap.entrySet()) {
                    String word = entry.getKey();
                    Integer frequency = entry.getValue();
                    addToIndex(word, new Tuple2<Integer, String>(frequency, articleLink));
                }
            }

            // ordeno el indice invertido por frecuencia de mayor a menor
            for (Map.Entry<String, List<Tuple2<Integer, String>>> entry : INDEX.entrySet()) {
                entry.getValue().sort(new Comparator<Tuple2<Integer, String>>() {
                    @Override
                    public int compare(Tuple2<Integer, String> o1, Tuple2<Integer, String> o2) {
                        return o2._1.compareTo(o1._1);
                    }
                });
            }

            // imprime el diccionario de words, para cada valor de la key, se
            // imprime la lista de tuplas
            // <frequency, articleLink>
            // System.out.println("\n Words and their frequencies: ");
            // for (Map.Entry<String, List<Tuple2<Integer, String>>> entry :
            // INDEX.entrySet()) {
            // System.out.println(entry.getKey() + " -> " + entry.getValue());
            // }

            // crea un scanner para poder buscar palabras en el diccionario
            Scanner scanner = new Scanner(System.in);

            // buscamos la palabra pasado por parametro en el diccionario, si existe,
            // se imprime la lista de tuplas <frequency, articleLink> asociada a la key
            // si no existe, se imprime un mensaje de error
            while (true) {
                System.out.print("\nSearch for: ");
                String keyword = scanner.nextLine().toLowerCase();

                if (keyword.equals("exit")) {
                    break;
                }

                if (INDEX.containsKey(keyword)) {
                    System.out.println("\nArticles containing \"" + keyword + "\"");
                    List<Tuple2<Integer, String>> list = INDEX.get(keyword);
                    for (Tuple2<Integer, String> tuple : list) {
                        System.out.println(tuple._2 + " - " + tuple._1);
                    }
                } else {
                    System.out.println("\nError: keyword \"" + keyword + "\" not found");
                }
            }

            // se cierra el scanner
            scanner.close();

            // se cierra el contexto
            jsc.close();
        }

    }
}