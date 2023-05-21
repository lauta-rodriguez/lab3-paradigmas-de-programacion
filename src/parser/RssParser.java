package parser;

import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import feed.Article;

/* Esta clase implementa el parser de feed de tipo rss (xml)
 * https://www.tutorialspoint.com/java_xml/java_dom_parse_document.htm 
 * */
public class RssParser extends GeneralParser<List<Article>> {

  /* Parsea la fecha en el formato específico de los feeds RSS */
  private static Date parseDate(String pubDate) {
    try {
      SimpleDateFormat dateFormatter = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.ENGLISH);
      return dateFormatter.parse(pubDate);
    } catch (Exception e) {
      System.out.println("Error: formato de fecha invalido");
      return null;
    }
  }

  /* Obtiene el contenido de un nodo dado su nombre */
  private static String getNodeContent(Element element, String nodeName) {
    return element.getElementsByTagName(nodeName).item(0).getTextContent();
  }

  /* Dado un post genera articulos */
  private static Article parseArticle(Node post) {

    if (!(post instanceof Node)) {
      throw new IllegalArgumentException("El post no es un Node");
    }

    Element itemElement = (Element) post;
    String title = getNodeContent(itemElement, "title");
    String description = getNodeContent(itemElement, "description");
    String link = getNodeContent(itemElement, "link");
    String pubDate = getNodeContent(itemElement, "pubDate");
    Date date = parseDate(pubDate);

    return new Article(title, description, date, link);
  }

  @Override
  public List<Article> parse(String data) {

    // Inicializa la lista de articulos
    List<Article> articleList = new ArrayList<Article>();

    // Crea un documento vacio
    Document xml = null;

    // Crea un DocumentBuilderFactory para parsear el xml
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

    try {
      // Crea un DocumentBuilder
      DocumentBuilder builder = factory.newDocumentBuilder();

      // Genera el documento xml a partir de la data
      xml = builder.parse(new InputSource(new StringReader(data)));

      NodeList items = xml.getElementsByTagName("item");
      for (int i = 0; i < items.getLength(); i++) {
        Node item = items.item(i);
        if (item.getNodeType() == Node.ELEMENT_NODE && item.getNodeName().equals("item")) {

          // Parsea el post y lo agrega a la lista de articulos
          Article article = parseArticle(item);
          articleList.add(article);
        }
      }

    } catch (Exception e) {
      e.printStackTrace();
      System.out.println(e);
    }

    // Devuelve la lista de articulos
    return articleList;
  }
}
