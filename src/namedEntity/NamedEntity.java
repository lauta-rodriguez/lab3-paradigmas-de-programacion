package namedEntity;

import java.lang.reflect.InvocationTargetException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

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
	Topic topic;

	private String category = "Other";
	private String parentCategory = "Named Entity";

	// class frequency
	private static int totalFrequency = 0;
	// named entity instance frequency
	private int frequency = 0;

	// dictionary used to map a category to the corresponding subclass
	private static final Map<String, Class<? extends NamedEntity>> CATEGORY_CLASS_MAP = new HashMap<>();

	// dictionary used to get the total frequency of a category subclass
	private static final Map<String, Integer> SUBCLASS_FREQUENCY = new HashMap<>();

	public NamedEntity(String name, int frequency) {
		super();
		this.name = name;
		this.frequency = frequency;
		totalFrequency += frequency;
	}

	public String getName() {
		return name;
	}

	public Topic getTopic() {
		return topic;
	}

	public String getCategory() {
		return category;
	}

	protected String getParentCategory() {
		return this.parentCategory;
	}

	public static int getTotalFrequency() {
		return totalFrequency;
	}

	public int getFrequency() {
		return frequency;
	}

	public static int getTotalNamedEntities() {
		int sum = 0;
		for (String key : SUBCLASS_FREQUENCY.keySet()) {
			sum += SUBCLASS_FREQUENCY.get(key);
		}

		return sum;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setTopic(Topic topic) {
		this.topic = topic;
	}

	protected void setCategory(String category) {
		this.category = category;
	}

	protected void setParentCategory(String parentCategory) {
		this.parentCategory = parentCategory;
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

	// dictionary used to get the total frequency of a category subclass
	public static void loadSubclassFrequency() {
		SUBCLASS_FREQUENCY.put("Other", NamedEntity.getTotalFrequency());
		SUBCLASS_FREQUENCY.put("Lastname", Lastname.getTotalFrequency());
		SUBCLASS_FREQUENCY.put("Name", Name.getTotalFrequency());
		SUBCLASS_FREQUENCY.put("Title", Title.getTotalFrequency());
		SUBCLASS_FREQUENCY.put("Place", Place.getTotalFrequency());
		SUBCLASS_FREQUENCY.put("City", City.getTotalFrequency());
		SUBCLASS_FREQUENCY.put("Country", Country.getTotalFrequency());
		SUBCLASS_FREQUENCY.put("Address", Address.getTotalFrequency());
		SUBCLASS_FREQUENCY.put("Organization", Organization.getTotalFrequency());
		SUBCLASS_FREQUENCY.put("Product", Product.getTotalFrequency());
		SUBCLASS_FREQUENCY.put("Event", Event.getTotalFrequency());
		SUBCLASS_FREQUENCY.put("CDate", CDate.getTotalFrequency());
	}

	public static void prettyPrintFrequencies() {
		Map<String, Integer> sortedSubclassFrequency = SUBCLASS_FREQUENCY.entrySet()
				.stream()
				.sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) -> oldValue,
						LinkedHashMap::new));

		System.out.println("Total occurrences by category");
		for (Map.Entry<String, Integer> entry : sortedSubclassFrequency.entrySet()) {
			System.out.println("\t" + entry.getKey() + ": " + entry.getValue());
		}
		System.out.println("\n");

	}

	public void prettyPrint() {
		System.out.println("\"" + this.getName() + "\"" + " occurs in:\n"
				+ "\tCategory \"" + this.getCategory() + "\": " + this.getFrequency() + " of "
				+ SUBCLASS_FREQUENCY.get(this.getCategory()) + " times\n"
				+ "\tTopic    \"" + this.getTopic().getName() + "\": " + this.getTopic().getFrequency() + " of "
				+ Topic.getSubclassFrequency().get(this.getTopic().getCategory()) + " times\n");
	}

}
