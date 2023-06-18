package word;
import java.io.Serializable;


public class Word implements Serializable{

    // word es la palabra con la que vamos a armar el indice invertido
    // campos: word, articleLink

    private String word;
    private String articleLink;

    // Constructor
    public Word(String word, String articleLink) {
        this.word = word;
        this.articleLink = articleLink;
    }

    // Getters y Setters
    public String getWord() {
        return word;
    }

    public String getArticleLink() {
        return articleLink;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public void setArticleLink(String articleLink) {
        this.articleLink = articleLink;
    }
}
