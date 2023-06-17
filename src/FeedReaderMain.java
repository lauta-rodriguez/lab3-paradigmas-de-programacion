import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import feed.Article;
import feed.Feed;
import httpRequest.httpRequester;
import namedEntity.NamedEntity;
import namedEntity.classes.CDate.CDate;
import namedEntity.classes.Event.Event;
import namedEntity.classes.Organization.Organization;
import namedEntity.classes.Person.Lastname;
import namedEntity.classes.Person.Name;
import namedEntity.classes.Person.Person;
import namedEntity.classes.Person.Title;
import namedEntity.classes.Place.Address;
import namedEntity.classes.Place.City;
import namedEntity.classes.Place.Country;
import namedEntity.classes.Place.Place;
import namedEntity.classes.Product.Product;
import namedEntity.heuristic.Heuristic;
import namedEntity.heuristic.QuickHeuristic;
import parser.GeneralParser;
import parser.RedditParser;
import parser.RssParser;
import parser.SubscriptionParser;
import scala.Tuple2;
import subscription.SingleSubscription;
import subscription.Subscription;
import topic.Topic;
import topic.Culture.Cine;
import topic.Culture.Culture;
import topic.Culture.Music;
import topic.Politics.International;
import topic.Politics.National;
import topic.Politics.Politics;
import topic.Sports.Basket;
import topic.Sports.F1;
import topic.Sports.Futbol;
import topic.Sports.Sports;
import topic.Sports.Tennis;

public class FeedReaderMain {

    // articleList global para computar las entidades nombradas y unificar los feeds
    // en uno solo
    private static List<Article> globalArticleList = null;
    // lista de nombres de los sitios de los cuales se obtuvieron los feeds,
    // se le pasa como parametro al generador de feeds
    private static List<String> siteNames = new ArrayList<String>();

    // hashmap de categorias y sus frecuencias
    private static HashMap<String, Integer> categoriesAndFrequencies = new HashMap<String, Integer>();
    // hashmap de topics y sus frecuencias
    private static HashMap<String, Integer> topicsAndFrequencies = new HashMap<String, Integer>();

    // diccionario que mapea cada named entity a una lista de la fecuencia con
    // la que aparece en cada articulo
    private static final Map<String, List<Tuple2<Integer, String>>> INDEX = new HashMap<>();

    // método que agrega elementos al diccionario:
    // esto lo único que hace es un put de la key, que es la named entity, y la
    // lista
    private static void addToIndex(String namedEntity, Tuple2<Integer, String> tuple) {
        // agregar la tupla a la lista correspondiente a la key
        if (INDEX.containsKey(namedEntity)) {
            INDEX.get(namedEntity).add(tuple);
        } else {
            List<Tuple2<Integer, String>> newList = new ArrayList<>();
            newList.add(tuple);
            INDEX.put(namedEntity, newList);
        }
    }

    // para buscar una named entity, solo tenemos que hacer dictionary.key(<named
    // entity>)

    // agrega todas las categorias al hashmap y en el segundo miembro, se llama a la
    // funcion getFrequency() de cada clase
    private static void getCategoriesAndFrequencies() {

        // se agregan las categorias y sus frecuencias al hashmap
        categoriesAndFrequencies.put("CDate", CDate.getFrequency());
        categoriesAndFrequencies.put("Event", Event.getFrequency());
        categoriesAndFrequencies.put("Organization", Organization.getFrequency());
        categoriesAndFrequencies.put("Lastname", Lastname.getFrequency());
        categoriesAndFrequencies.put("Name", Name.getFrequency());
        categoriesAndFrequencies.put("Person", Person.getFrequency());
        categoriesAndFrequencies.put("Title", Title.getFrequency());
        categoriesAndFrequencies.put("Address", Address.getFrequency());
        categoriesAndFrequencies.put("City", City.getFrequency());
        categoriesAndFrequencies.put("Country", Country.getFrequency());
        categoriesAndFrequencies.put("Place", Place.getFrequency());
        categoriesAndFrequencies.put("Product", Product.getFrequency());
        categoriesAndFrequencies.put("Other [Category]", NamedEntity.getFrequency());
    }

    // agrega todos los topics al hashmap y en el segundo miembro, se llama a la
    // funcion getFrequency() de cada clase
    private static void getTopicsAndFrequencies() {
        // se agregan los topics y sus frecuencias al hashmap
        topicsAndFrequencies.put("Cine", Cine.getFrequency());
        topicsAndFrequencies.put("Culture", Culture.getFrequency());
        topicsAndFrequencies.put("Music", Music.getFrequency());

        topicsAndFrequencies.put("International", International.getFrequency());
        topicsAndFrequencies.put("National", National.getFrequency());
        topicsAndFrequencies.put("Politics", Politics.getFrequency());

        topicsAndFrequencies.put("Basket", Basket.getFrequency());
        topicsAndFrequencies.put("F1", F1.getFrequency());
        topicsAndFrequencies.put("Futbol", Futbol.getFrequency());
        topicsAndFrequencies.put("Sports", Sports.getFrequency());
        topicsAndFrequencies.put("Tennis", Tennis.getFrequency());

        topicsAndFrequencies.put("Other [Topic]", Topic.getFrequency());
    }

    // imprime las categorias y topics y sus frecuencias
    private static void printCTFrequencies() {
        // Obtener las categorias y frecuencias
        getCategoriesAndFrequencies();
        // Obtener los topics y frecuencias
        getTopicsAndFrequencies();

        // Ordenar las categorias y frecuencias por frecuencia, en orden descendente
        Map<String, Integer> sortedCategoriesbyFrequency = categoriesAndFrequencies.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1,
                        java.util.LinkedHashMap::new));

        // Ordenar los topics y frecuencias por frecuencia, en orden descendente
        Map<String, Integer> sortedTopicsbyFrequency = topicsAndFrequencies.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1,
                        java.util.LinkedHashMap::new));

        // Impresion de las categorias y topics y sus frecuencias (!= 0)
        System.out.println("\nCategories and their frequencies: ");
        for (Map.Entry<String, Integer> entry : sortedCategoriesbyFrequency.entrySet()) {
            if (entry.getValue() != 0) {
                System.out.println(entry.getKey() + " - " + entry.getValue());
            }
        }

        System.out.println("\nTopics and their frequencies: ");
        for (Map.Entry<String, Integer> entry : sortedTopicsbyFrequency.entrySet()) {
            if (entry.getValue() != 0) {
                System.out.println(entry.getKey() + " - " + entry.getValue());
            }
        }
    }

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

        // los argumentos que se le pueden pasar al programa son:
        // 0 argumentos: generar feed
        // 1 argumento: generar indice invertido y buscar argumento en el indice
        if (args.length > 1) {
            System.out.println("Error: too many arguments");
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
            Heuristic heuristic = new QuickHeuristic();

            // se crea el contexto de spark
            SparkConf conf = new SparkConf().setAppName("NER").setMaster("local[*]");
            JavaSparkContext jsc = new JavaSparkContext(conf);

            // se paraleliza el procesamiento de los articulos y se los carga en un
            // "JavaRDD<Article> articleRDD"
            JavaRDD<Article> articleRDD = jsc.parallelize(globalArticleList, globalArticleList.size());

            // --- begin: paralelizacion hasta reduce || collect ---

            // # map
            // a cada articulo de articleRDD se le computa las ne y por cada articulo vamos
            // a tener una List<NamedEntity>, resultando en un
            JavaRDD<List<NamedEntity>> namedEntityRDD = articleRDD.map(article -> {
                article.computeNamedEntities(heuristic);
                return article.getNamedEntityList();
            });

            // # flatMap
            // obtengo un RDD con todas las named entities individuales, por lo que van a
            // haber ne repetidas
            JavaRDD<NamedEntity> flatNamedEntityRDD = namedEntityRDD
                    .flatMap(namedEntityList -> namedEntityList.iterator());

            // # mapToPair
            // se mapea cada ne a una tupla <articleLink, <ne, 1>>
            JavaPairRDD<String, Tuple2<NamedEntity, Integer>> articleLinkNamedEntityRDD = flatNamedEntityRDD
                    .mapToPair(namedEntity -> new Tuple2<String, Tuple2<NamedEntity, Integer>>(
                            namedEntity.getArticleLink(),
                            new Tuple2<NamedEntity, Integer>(namedEntity, 1)));

            // # reduceByKey
            // se reduce por clave, es decir, por articleLink, y se obtiene un RDD con
            // <articleLink, <ne, frequency>>
            JavaPairRDD<String, Tuple2<NamedEntity, Integer>> articleLinkNamedEntityFrequencyRDD = articleLinkNamedEntityRDD
                    .reduceByKey(
                            (tuple1, tuple2) -> new Tuple2<NamedEntity, Integer>(tuple1._1, tuple1._2 + tuple2._2));

            // iterar sobre el RDD y agregar cada named entity al diccionario
            // se itera sobre cada tupla <articleLink, <ne, frequency>>
            // se obtiene la named entity y se la agrega al diccionario
            articleLinkNamedEntityFrequencyRDD.foreach(tuple -> {
                NamedEntity namedEntity = tuple._2._1;
                String namedEntityName = namedEntity.getName();
                Tuple2<Integer, String> tuple2 = new Tuple2<Integer, String>(tuple._2._2, namedEntity.getArticleLink());
                addToIndex(namedEntityName, tuple2);
            });

            // ordenar las listas de tuplas <frequency, articleLink> por frecuencia de mayor
            // a menor
            // se itera sobre cada lista de tuplas <frequency, articleLink> y se la ordena
            for (Map.Entry<String, List<Tuple2<Integer, String>>> entry : INDEX.entrySet()) {
                List<Tuple2<Integer, String>> list = entry.getValue();
                list.sort(new Comparator<Tuple2<Integer, String>>() {
                    @Override
                    public int compare(Tuple2<Integer, String> tuple1, Tuple2<Integer, String> tuple2) {
                        return tuple2._1.compareTo(tuple1._1);
                    }
                });
            }

            // imprime el diccionario de named entities, para cada valor de la key, se
            // imprime la lista de tuplas
            // <frequency, articleLink>
            // System.out.println("\nNamed Entities and their frequencies: ");
            // for (Map.Entry<String, List<Tuple2<Integer, String>>> entry :
            // INDEX.entrySet()) {
            // System.out.println(entry.getKey() + " - " + entry.getValue());
            // }

            // buscamos la palabra pasado por parametro en el diccionario, si existe,
            // se imprime la lista de tuplas <frequency, articleLink> asociada a la key
            // si no existe, se imprime un mensaje de error
            String keyword = args[0].toLowerCase();
            if (INDEX.containsKey(keyword)) {
                System.out.println("\nArticles containing \"" + keyword + "\"");
                List<Tuple2<Integer, String>> list = INDEX.get(keyword);
                for (Tuple2<Integer, String> tuple : list) {
                    System.out.println(tuple._2 + " - " + tuple._1);
                }
            } else {
                System.out.println("\nError: keyword \"" + keyword + "\" not found");
            }

            // se cierra el contexto
            jsc.close();

            // se imprime las Categorias y Topics y sus Frecuencias
            // printCTFrequencies();
        }

    }
}
