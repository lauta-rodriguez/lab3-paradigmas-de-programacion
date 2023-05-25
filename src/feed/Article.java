package feed;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import namedEntity.NamedEntity;
import namedEntity.classes.CDate.CDate;
import namedEntity.classes.Event.Event;
import namedEntity.classes.Organization.Organization;
import namedEntity.classes.Person.Lastname;
import namedEntity.classes.Person.Name;
import namedEntity.classes.Person.Title;
import namedEntity.classes.Place.Address;
import namedEntity.classes.Place.City;
import namedEntity.classes.Place.Country;
import namedEntity.classes.Place.Place;
import namedEntity.classes.Product.Product;
import namedEntity.heuristic.Heuristic;
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

/*Esta clase modela el contenido de un articulo (ie, un item en el caso del rss feed) */

public class Article implements Serializable {
	private String title;
	private String text;
	private Date publicationDate;
	private String link;

	private List<NamedEntity> namedEntityList = new ArrayList<NamedEntity>();

	public Article(String title, String text, Date publicationDate, String link) {
		super();
		this.title = title;
		this.text = text;
		this.publicationDate = publicationDate;
		this.link = link;
	}

	private static final Map<String, Class<? extends NamedEntity>> CATEGORY_CLASS_MAP = new HashMap<>();

	static {
		CATEGORY_CLASS_MAP.put("Lastname", Lastname.class);
		CATEGORY_CLASS_MAP.put("Name", Name.class);
		CATEGORY_CLASS_MAP.put("Title", Title.class);
		CATEGORY_CLASS_MAP.put("Place", Place.class);
		CATEGORY_CLASS_MAP.put("City", City.class);
		CATEGORY_CLASS_MAP.put("Country", Country.class);
		CATEGORY_CLASS_MAP.put("Address", Address.class);
		CATEGORY_CLASS_MAP.put("Organization", Organization.class);
		CATEGORY_CLASS_MAP.put("Product", Product.class);
		CATEGORY_CLASS_MAP.put("Event", Event.class);
		CATEGORY_CLASS_MAP.put("CDate", CDate.class);
	}

	private static final Map<String, Class<? extends Topic>> TOPIC_CLASS_MAP = new HashMap<>();

	static {
		TOPIC_CLASS_MAP.put("Culture", Culture.class);
		TOPIC_CLASS_MAP.put("Cine", Cine.class);
		TOPIC_CLASS_MAP.put("Music", Music.class);
		TOPIC_CLASS_MAP.put("Politics", Politics.class);
		TOPIC_CLASS_MAP.put("International", International.class);
		TOPIC_CLASS_MAP.put("National", National.class);
		TOPIC_CLASS_MAP.put("Sports", Sports.class);
		TOPIC_CLASS_MAP.put("Futbol", Futbol.class);
		TOPIC_CLASS_MAP.put("Basket", Basket.class);
		TOPIC_CLASS_MAP.put("Tennis", Tennis.class);
		TOPIC_CLASS_MAP.put("F1", F1.class);
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

	public String getContent() {
		return this.title + " " + this.text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Date getPublicationDate() {
		return publicationDate;
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

	public NamedEntity getNamedEntity(String namedEntity) {
		for (NamedEntity n : namedEntityList) {
			if (n.getName().compareTo(namedEntity) == 0) {
				return n;
			}
		}
		return null;
	}

	private NamedEntity generateNamedEntity(String namedEntity, String category)
			throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException, ClassNotFoundException {

		Class<? extends NamedEntity> action = CATEGORY_CLASS_MAP.getOrDefault(category, NamedEntity.class);
		NamedEntity ne = action.getDeclaredConstructor(String.class)
				.newInstance(namedEntity);

		return ne;
	}

	private Topic generateTopic(String topic)
			throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException, ClassNotFoundException {

		Class<? extends Topic> action = TOPIC_CLASS_MAP.getOrDefault(topic, Topic.class);
		Topic t = action.getDeclaredConstructor(String.class).newInstance(topic);

		return t;
	}

	public void computeNamedEntities(Heuristic h)
			throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, ClassNotFoundException {
		String text = this.getTitle() + " " + this.getText();

		String charsToRemove = ".,;:()'\"!?&*\n";
		for (char c : charsToRemove.toCharArray()) {
			text = text.replace(String.valueOf(c), "");
		}

		for (String s : text.split(" ")) {
			if (h.isEntity(s)) {
				// ver si la entidad nombrada ya se encuentra en las entidades
				// de este articulo
				NamedEntity ne = this.getNamedEntity(s);

				// si no esta, se genera la entidad nombrada y el topic correspondiente
				if (ne == null) {
					ne = this.generateNamedEntity(s, h.getCategory(s));

					Topic t = this.generateTopic(h.getTopic(s));
					ne.setTopic(t);

					this.namedEntityList.add(ne);

				} else { // si esta, incrementa su contador de ocurrencias
					ne.incrementFrequency();
					ne.getTopic().incrementFrequency();
				}

				ne.prettyPrint();
			}
		}
	}

	public void prettyPrint() {
		System.out
				.println(
						"**********************************************************************************************");
		System.out.println("Title: " + this.getTitle());
		System.out.println("Publication Date: " + this.getPublicationDate());
		System.out.println("Link: " + this.getLink());
		System.out.println("Text: " + this.getText());
		System.out
				.println(
						"**********************************************************************************************");

	}

	public static void main(String[] args) {
		Article a = new Article("This Historically Black University Created Its Own Tech Intern Pipeline",
				"A new program at Bowie State connects computing students directly with companies, bypassing an often harsh Silicon Valley vetting process",
				new Date(),
				"https://www.nytimes.com/2023/04/05/technology/bowie-hbcu-tech-intern-pipeline.html");

		a.prettyPrint();
	}

}
