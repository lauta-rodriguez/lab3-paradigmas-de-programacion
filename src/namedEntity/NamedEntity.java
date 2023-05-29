package namedEntity;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import namedEntity.classes.CDate.CDate;
import namedEntity.classes.Event.Event;
import namedEntity.classes.Organization.Organization;
import namedEntity.classes.Person.*;
import namedEntity.classes.Place.*;
import namedEntity.classes.Product.Product;
import namedEntity.heuristic.Heuristic;
import topic.Topic;

/*Esta clase modela la nocion de entidad nombrada*/

public class NamedEntity {
	String name;
	private String category = "Other";
	private String parentCategory = "Named Entity";

	private static int totalFrequency = 0;

	Topic topic;

	// dictionary used to map a category to the corresponding subclass
	private static final Map<String, Class<? extends NamedEntity>> CATEGORY_CLASS_MAP = new HashMap<>();

	public NamedEntity(String name) {
		super();
		this.name = name;
		totalFrequency++;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCategory() {
		return category;
	}

	protected void setCategory(String category) {
		this.category = category;
	}

	public static int getTotalFrequency() {
		return totalFrequency;
	}

	public void incrementFrequency() {
		totalFrequency++;
	}

	public Topic getTopic() {
		return topic;
	}

	public void setTopic(Topic topic) {
		this.topic = topic;
	}

	protected void setParentCategory(String parentCategory) {
		this.parentCategory = parentCategory;
	}

	protected String getParentCategory() {
		return this.parentCategory;
	}

	public static NamedEntity generateNamedEntity(String namedEntity, int frequency)
			throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException, ClassNotFoundException {

		String category = Heuristic.getCategory(namedEntity);

		Class<? extends NamedEntity> ne_action = CATEGORY_CLASS_MAP.getOrDefault(category,
				NamedEntity.class);
		NamedEntity ne = ne_action.getDeclaredConstructor(String.class, int.class)
				.newInstance(namedEntity, frequency);

		Topic t = Topic.generateTopic(namedEntity, frequency);

		ne.setTopic(t);

		return ne;
	}

	// dictionary used to map a category to the corresponding subclass
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

	@Override
	public String toString() {
		return "ObjectNamedEntity [name=" + name + ", totalFrequency=" + totalFrequency + "]";
	}

	public String StringifyObject() {
		return ("[" + this.getName() + ": (" + this.getCategory() + ", " + getTotalFrequency() + ") ");
	}

	public void prettyPrint() {
		System.out.println(this.StringifyObject() + " " + this.getTopic().StringifyObject());
	}

}
