package feed;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import word.Word;

import namedEntity.NamedEntity;
import namedEntity.classes.Organization.Organization;
import namedEntity.classes.Event.Event;
import namedEntity.classes.Person.Lastname;
import namedEntity.classes.Person.Name;
import namedEntity.classes.Person.Title;
import namedEntity.classes.Place.City;
import namedEntity.classes.Place.Address;
import namedEntity.classes.Place.Place;
import namedEntity.classes.Place.Country;
import namedEntity.classes.Product.Product;
import namedEntity.classes.CDate.CDate;
import namedEntity.heuristic.Heuristic;
import parser.RedditParser;
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
import java.io.Serializable;

/*Esta clase modela el contenido de un articulo (ie, un item en el caso del rss feed) */

public class Article implements Serializable {

	private static final int MAX_CHARS = 80;
	private String title;
	private String text;
	private Date publicationDate;
	private String link;

	private List<Word> wordList = new ArrayList<Word>();

	public Article(String title, String text, Date publicationDate, String link) {
		super();
		this.title = title;
		this.text = text;
		this.publicationDate = publicationDate;
		this.link = link;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Date getPublicationDate() {
		return publicationDate;
	}

	public List<Word> getWordList() {
		return wordList;
	}

	public void setPublicationDate(Date publicationDate) {
		this.publicationDate = publicationDate;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	@Override
	public String toString() {
		return "Article [title=" + title + ", text=" + text + ", publicationDate=" + publicationDate + ", link=" + link
				+ "]";
	}

	public Word getWord(String word) {
		for (Word w : wordList) {
			if (w.getWord().compareTo(word) == 0) {
				return w;
			}
		}
		return null;
	}

	public void computeSingleWords() {
		String text = this.getTitle() + " " + this.getText();

		// Reemplaza los siguientes caracteres .,;:()'\"!?&*‘'“\n por "" (empty string)
		text = text.replaceAll("[.,;:()\"!?&*‘“\n]", "");

		// Divide el texto en palabras
		String[] words = text.split("\\s+");

		// Filtra palabras vacias
		words = Arrays.stream(words)
				.filter(word -> !word.isEmpty())
				.toArray(String[]::new);

		// Filtra palabras que no son alfanumericas
		words = Arrays.stream(words)
				.filter(word -> word.matches("[a-zA-Z0-9]+"))
				.toArray(String[]::new);

		// Reconstruye el texto
		text = String.join(" ", words);

		// Agrega las palabras una por una a la lista de palabras
		// WordList es una lista con todas las palabras del articulo (una misma palabra
		// puede aparecer varias veces)
		for (String s : text.split(" ")) {
			// vamos a trabajar con palabras en minuscula
			s = s.toLowerCase();
			this.wordList.add(new Word(s, this.getLink()));
		}

	}

	public void prettyPrint() {
		// limita la descripcion a RedditParser.MAX_CHARS caracteres
		// considerando palabras completas
		String[] sentences = this.getText().split("\\.");
		String description = sentences[0];

		if (description.length() > Article.MAX_CHARS) {
			int lastSpaceIndex = description.lastIndexOf(' ', Article.MAX_CHARS);

			if (lastSpaceIndex == -1) {
				lastSpaceIndex = Article.MAX_CHARS;
			}

			description = description.substring(0, lastSpaceIndex);
		}

		if (sentences.length > 1) {
			String secondSentence = sentences[1];

			if (secondSentence.length() > Article.MAX_CHARS) {
				int lastSpaceIndex = secondSentence.lastIndexOf(' ', Article.MAX_CHARS);

				if (lastSpaceIndex == -1) {
					lastSpaceIndex = Article.MAX_CHARS;
				}

				secondSentence = secondSentence.substring(0, lastSpaceIndex);
			}

			description += ". " + secondSentence;
		}

		// Insert newlines every 94 characters in the description
		StringBuilder formattedDescription = new StringBuilder();
		int charCount = 0;
		for (int i = 0; i < description.length(); i++) {
			char currentChar = description.charAt(i);
			formattedDescription.append(currentChar);
			charCount++;

			if (charCount == 94) {
				formattedDescription.append("\n");
				charCount = 0;
			}
		}

		description = formattedDescription.toString() + "...";

		System.out.println(
				"**********************************************************************************************");
		System.out.println("Title: " + this.getTitle());
		System.out.println("Publication Date: " + this.getPublicationDate());
		System.out.println("Link: " + this.getLink());
		System.out.println("Text: " + this.getText());
		System.out.println("Word List: ");
		for (Word w : this.wordList) {
			System.out.println(w.getWord() + " " + w.getArticleLink());
		}
		System.out.println("\n");
		System.out.println(
				"**********************************************************************************************");
	}

	public static void main(String[] args) {
		Article a = new Article("This Historically Black University Created Its Own Tech Intern Pipeline",
				"A new program at Bowie Bowie State connects computing students directly with companies, bypassing an often harsh Silicon Valley vetting process",
				new Date(),
				"https://www.nytimes.com/2023/04/05/technology/bowie-hbcu-tech-intern-pipeline.html");

		a.computeSingleWords();
		a.prettyPrint();

	}

}
