package parser;

import feed.Article;
import java.util.List;
import java.util.ArrayList;

import java.util.Date;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

/* Esta clase se encarga de parsear los resultados del httpRequester en una lista de articulos
 * */
public class RedditParser extends GeneralParser<List<Article>> {

    private static final int MAX_CHARS = 80;

    /*
     * Este metodo genera articulos dado un post
     * Extrae los atributos: titulo, descripcion, fecha y link
     */
    private Article parseArticle(JSONObject postJson) throws JSONException {
        String title = postJson.getString("title");
        String text = postJson.getString("selftext");

        // limita la descripcion a RedditParser.MAX_CHARS caracteres
        // considerando palabras completas
        String[] sentences = text.split("\\.");
        String description = sentences[0];

        if (description.length() > RedditParser.MAX_CHARS) {
            int lastSpaceIndex = description.lastIndexOf(' ', RedditParser.MAX_CHARS);

            if (lastSpaceIndex == -1) {
                lastSpaceIndex = RedditParser.MAX_CHARS;
            }

            description = description.substring(0, lastSpaceIndex);
        }

        if (sentences.length > 1) {
            String secondSentence = sentences[1];

            if (secondSentence.length() > RedditParser.MAX_CHARS) {
                int lastSpaceIndex = secondSentence.lastIndexOf(' ', RedditParser.MAX_CHARS);

                if (lastSpaceIndex == -1) {
                    lastSpaceIndex = RedditParser.MAX_CHARS;
                }

                secondSentence = secondSentence.substring(0, lastSpaceIndex);
            }

            description += ". " + secondSentence;
        }

        description += "...";

        String link = "https://www.reddit.com" + postJson.getString("permalink");
        Date date = new Date(postJson.getLong("created_utc") * 1000);
        Article article = new Article(title, description, date, link);
        return article;
    }

    @Override
    public List<Article> parse(String data) {
        List<Article> articles = new ArrayList<Article>();
        try {
            JSONObject json = new JSONObject(data);
            JSONArray posts = json.getJSONObject("data").getJSONArray("children");
            for (int i = 0; i < posts.length(); i++) {
                JSONObject post = posts.getJSONObject(i).getJSONObject("data");
                // Ignora los posts fijados (sticky posts)
                if (post.getBoolean("stickied")) {
                    continue;
                }
                // Parsea el post y lo agrega a la lista de articulos
                Article article = parseArticle(post);
                articles.add(article);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            System.out.println(e);
        }
        return articles;
    }
}
