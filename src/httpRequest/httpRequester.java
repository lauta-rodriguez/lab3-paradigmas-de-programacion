package httpRequest;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/* Esta clase se encarga de realizar efectivamente el pedido de feed al servidor de noticias
 * Leer sobre como hacer una http request en java
 * https://www.baeldung.com/java-http-request
 * */

public class httpRequester {

	public String getFeed(String urlFeed, String urlType) {
		String feed = null;

		if (urlType.equals("rss")) {
			feed = getFeedRss(urlFeed);
		} else if (urlType.equals("reddit")) {
			feed = getFeedReddit(urlFeed);
		}

		return feed;
	}

	public String getFeedRss(String urlFeed) {
		String feedRssXml = "";

		try {
			URL url = new URL(urlFeed);
			URLConnection urlConnection = url.openConnection(); // creating a urlconnection object

			// Wrappea la urlconnection en un bufferedreader
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

			// Lee la respuesta del servidor y la guarda en un String
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				feedRssXml = feedRssXml + line + "\n";
			}
			bufferedReader.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e);
		}

		return feedRssXml;
	}

	public String getFeedReddit(String urlFeed) {
		String feedRedditJson = "";

		try {
			// Crea un objeto URL con el endpoint de la API de Reddit
			URL url = new URL(urlFeed);

			// Setea el user agent del request HTTP para evitar un error 429 (Too many
			// requests)
			URLConnection connection = url.openConnection();
			connection.setRequestProperty("User-Agent", "Mozilla/5.0");

			// Lee la respuesta del servidor y la guarda en un String
			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String line;
			while ((line = reader.readLine()) != null) {
				feedRedditJson += line;
			}
			reader.close();

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e);
		}

		return feedRedditJson;
	}

	public static void main(String[] args) {
		System.out.println("httpRequesterClass");
		httpRequester h = new httpRequester();
		String feedRssXml = h.getFeedRss("https://rss.nytimes.com/services/xml/rss/nyt/World.xml");
		System.out.println(feedRssXml);
	}

}
